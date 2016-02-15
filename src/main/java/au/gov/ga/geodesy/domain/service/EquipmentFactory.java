package au.gov.ga.geodesy.domain.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.Clock;
import au.gov.ga.geodesy.domain.model.ClockConfiguration;
import au.gov.ga.geodesy.domain.model.Equipment;
import au.gov.ga.geodesy.domain.model.EquipmentConfiguration;
import au.gov.ga.geodesy.domain.model.EquipmentConfigurationRepository;
import au.gov.ga.geodesy.domain.model.EquipmentRepository;
import au.gov.ga.geodesy.domain.model.GnssAntenna;
import au.gov.ga.geodesy.domain.model.GnssAntennaConfiguration;
import au.gov.ga.geodesy.domain.model.GnssReceiver;
import au.gov.ga.geodesy.domain.model.GnssReceiverConfiguration;
import au.gov.ga.geodesy.domain.model.HumiditySensor;
import au.gov.ga.geodesy.domain.model.HumiditySensorConfiguration;
import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;
import au.gov.ga.geodesy.igssitelog.domain.model.EquipmentLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.FrequencyStandardLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssAntennaLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiverLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.HumiditySensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.LogItemVisitor;
import au.gov.ga.geodesy.igssitelog.domain.model.PressureSensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.TemperatureSensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.WaterVaporSensorLogItem;

@Component
public class EquipmentFactory {
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(EquipmentFactory.class);

    @Autowired
    private EquipmentRepository equipment;

    @Autowired
    private EquipmentConfigurationRepository configurations;

    private EquipmentCreator creator = new EquipmentCreator();

    public Pair<? extends Equipment, ? extends EquipmentConfiguration> create(EquipmentLogItem logItem) {
        return logItem.accept(creator);
    }

    private class EquipmentCreator
            implements LogItemVisitor<Pair<? extends Equipment, ? extends EquipmentConfiguration>> {

        public Pair<? extends Equipment, ? extends EquipmentConfiguration> visit(GnssReceiverLogItem logItem) {
            GnssReceiver receiver = getEquipment(GnssReceiver.class, logItem);

            GnssReceiverConfiguration config = getConfiguration(GnssReceiverConfiguration.class,
                    receiver.getId(), logItem);

            config.setSatelliteSystem(logItem.getSatelliteSystem());
            config.setFirmwareVersion(logItem.getFirmwareVersion());
            config.setElevetionCutoffSetting(logItem.getElevationCutoffSetting());
            config.setTemperatureStabilization(logItem.getTemperatureStabilization());
            config.setNotes(logItem.getNotes());
            return Pair.of(receiver, config);
        }

        public Pair<? extends Equipment, ? extends EquipmentConfiguration> visit(GnssAntennaLogItem logItem) {
            GnssAntenna antenna = getEquipment(GnssAntenna.class, logItem);

            GnssAntennaConfiguration config =
                getConfiguration(GnssAntennaConfiguration.class, antenna.getId(), logItem);

            config.setAlignmentFromTrueNorth(logItem.getAlignmentFromTrueNorth());
            return Pair.of(antenna, config);
        }

        public Pair<? extends Equipment, ? extends EquipmentConfiguration> visit(HumiditySensorLogItem logItem) {
            HumiditySensor sensor = getEquipment(HumiditySensor.class, logItem);

            HumiditySensorConfiguration config =
                getConfiguration(HumiditySensorConfiguration.class, sensor.getId(), logItem);

            sensor.setAspiration(logItem.getAspiration());
            config.setHeightDiffToAntenna(logItem.getHeightDiffToAntenna());
            return Pair.of(sensor, config);
        }

        public Pair<? extends Equipment, ? extends EquipmentConfiguration> visit(FrequencyStandardLogItem logItem) {
            final Clock newClock = new Clock(logItem.getType());
            Optional<Clock> existingClock = equipment.findByEquipmentType(Clock.class)
                .stream().
                filter(c -> c.equals(newClock))
                .findFirst();

            Clock clock = existingClock.orElseGet(() -> {
                equipment.saveAndFlush(newClock);
                return newClock;
            });
            ClockConfiguration config = getConfiguration(ClockConfiguration.class, clock.getId(), logItem);
            config.setInputFrequency(logItem.getInputFrequency());
            return Pair.of(clock, config);
        }

        public Pair<Equipment, EquipmentConfiguration> visit(WaterVaporSensorLogItem logItem) {
            throw new UnsupportedOperationException();
        }

        public Pair<Equipment, EquipmentConfiguration> visit(TemperatureSensorLogItem logItem) {
            throw new UnsupportedOperationException();
        }

        public Pair<Equipment, EquipmentConfiguration> visit(PressureSensorLogItem logItem) {
            throw new UnsupportedOperationException();
        }

        private <T extends Equipment> T getEquipment(Class<T> equipmentClass, EquipmentLogItem logItem) {
            T e = equipment.findOne(equipmentClass, logItem.getType(), logItem.getSerialNumber());
            if (e == null) {
                try {
                    e = equipmentClass.getConstructor(String.class, String.class)
                            .newInstance(logItem.getType(), logItem.getSerialNumber());
                    // TODO: We shouldn't save here, but we need the id. Is there another way?
                    equipment.saveAndFlush(e);
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException
                        | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return e;
        }

        private <T extends EquipmentConfiguration> T getConfiguration(
                Class<T> configClass,
                Integer equipId,
                EquipmentLogItem logItem) {

            EffectiveDates period = logItem.getEffectiveDates();
            Date effectiveFrom = period == null ? null : period.getFrom();
            T c = null;
            if (effectiveFrom != null) {
                c = configurations.findOne(configClass, equipId, effectiveFrom);
            }
            if (c == null) {
                try {
                    return configClass.getConstructor(Integer.class, Date.class).newInstance(equipId, effectiveFrom);
                } catch (IllegalAccessException | InstantiationException
                        | NoSuchMethodException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
            return c;
        }
    }
}

