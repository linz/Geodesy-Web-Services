package au.gov.ga.geodesy.domain.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.Equipment;
import au.gov.ga.geodesy.domain.model.EquipmentConfiguration;
import au.gov.ga.geodesy.domain.model.EquipmentConfigurationRepository;
import au.gov.ga.geodesy.domain.model.EquipmentInUse;
import au.gov.ga.geodesy.domain.model.EquipmentRepository;
import au.gov.ga.geodesy.domain.model.Event;
import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.EventRepository;
import au.gov.ga.geodesy.domain.model.EventSubscriber;
import au.gov.ga.geodesy.domain.model.GnssAntenna;
import au.gov.ga.geodesy.domain.model.GnssAntennaConfiguration;
import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.GnssReceiver;
import au.gov.ga.geodesy.domain.model.GnssReceiverConfiguration;
import au.gov.ga.geodesy.domain.model.HumiditySensor;
import au.gov.ga.geodesy.domain.model.HumiditySensorConfiguration;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SiteLogUploaded;
import au.gov.ga.geodesy.domain.model.SiteUpdated;
import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;
import au.gov.ga.geodesy.igssitelog.domain.model.EquipmentLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssAntennaLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiverLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.HumiditySensorLogItem;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;

@Component
@Transactional("geodesyTransactionManager")
public class GnssCorsSiteService implements EventSubscriber<SiteLogUploaded> {

    private static final Logger log = LoggerFactory.getLogger(GnssCorsSiteService.class);
    private static final String gnssCorsSetupName = "GNSS CORS Setup";

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentConfigurationRepository configurationRepository;

    @Autowired
    private GnssCorsSiteRepository gnssSites;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private EventRepository events;

    @PostConstruct
    private void subcribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event e) {
        return e != null && (e instanceof SiteLogUploaded);
    }

    public void handle(SiteLogUploaded siteLogUploaded) {
        log.info("Notified of " + siteLogUploaded);

        String fourCharacterId = siteLogUploaded.getFourCharacterId();
        IgsSiteLog siteLog = siteLogs.findByFourCharacterId(fourCharacterId);

        GnssCorsSite gnssSite = gnssSites.findByFourCharacterId(fourCharacterId);
        if (gnssSite == null) {
            gnssSite = new GnssCorsSite(fourCharacterId);
        }
        gnssSite.setName(siteLog.getSiteIdentification().getSiteName());
        gnssSite.setDescription(siteLog.getSiteIdentification().getMonumentDescription());
        gnssSite.getSetups().clear();
        gnssSite.getSetups().addAll(getSetups(siteLog));
        gnssSites.save(gnssSite);
        eventPublisher.handled(siteLogUploaded);
        eventPublisher.publish(new SiteUpdated(fourCharacterId));
    }

    private List<Setup> getSetups(IgsSiteLog siteLog) {
        SortedSet<Date> datesOfChange = new TreeSet<>();

        for (EquipmentLogItem logItem : siteLog.getEquipmentLogItems()) {

            EffectiveDates dates = logItem.getEffectiveDates();
            if (dates == null) {
                dates = new EffectiveDates(new Date(0L), null);
            }
            datesOfChange.add(dates.getFrom());
            if (dates.getTo() != null) {
                datesOfChange.add(dates.getTo());
            }
        }
        List<Setup> setups = new ArrayList<>();

        if (datesOfChange.size() > 0) {
            if (datesOfChange.size() == 1) {
                Date d = datesOfChange.iterator().next();
                setups.add(new Setup(gnssCorsSetupName, new EffectiveDates(d, null)));
            } else {
                Iterator<Date> i = datesOfChange.iterator();
                Iterator<Date> j = datesOfChange.iterator();
                j.next();
                Setup s = null;
                do {
                    Date from = i.next();
                    Date to = j.next();
                    s = new Setup(gnssCorsSetupName, new EffectiveDates(from, to));
                    setups.add(s);
                }
                while (j.hasNext());
                EffectiveDates lastPeriod = s.getEffectivePeriod();
                if (lastPeriod.getTo() != null) {
                    s = new Setup(gnssCorsSetupName, new EffectiveDates(lastPeriod.getTo(), null));
                    setups.add(s);
                }
            }
        }
        for (EquipmentLogItem logItem : siteLog.getEquipmentLogItems()) {
            addEquipment(logItem, setups);
        }
        return setups;
    }

    private void addEquipment(EquipmentLogItem logItem, List<Setup> setups) {
        EffectiveDates period = logItem.getEffectiveDates();
        Date equipmentFrom = null;
        Date equipmentTo = null;
        if (period != null) {
            equipmentFrom = logItem.getEffectiveDates().getFrom();
            equipmentTo = logItem.getEffectiveDates().getTo();
        } else {
            equipmentFrom = new Date(0L);
        }
        if (equipmentFrom.equals(equipmentTo)) {
            // equipment log entries with empty periods are corrections
            return;
        }
        int i = 0;
        while (!setups.get(i).getEffectivePeriod().getFrom().equals(equipmentFrom)) {
            i++;
        }
        int j = i;
        if (equipmentTo == null) {
            j = setups.size();
        } else {
            while (!setups.get(j++).getEffectivePeriod().getTo().equals(equipmentTo));
        }
        for (int k = i; k < j; k++) {
            addEquipment(logItem, setups.get(k));
        }
    }

    private void addEquipment(EquipmentLogItem logItem, Setup s) {
        Pair<Equipment, EquipmentConfiguration> ec = getEquipmentAndConfiguration(logItem);
        Equipment equipment = ec.getLeft();
        EquipmentConfiguration config = ec.getRight();
        EquipmentInUse inUse = new EquipmentInUse(equipment.getId(), config.getId(), logItem.getEffectiveDates());
        s.getEquipmentInUse().add(inUse);
    }

    private Pair<Equipment, EquipmentConfiguration> getEquipmentAndConfiguration(EquipmentLogItem logItem) {
        Equipment equipment = null;
        EquipmentConfiguration configuration = null;

        if (logItem instanceof GnssReceiverLogItem) {
            GnssReceiverLogItem receiverLogItem = (GnssReceiverLogItem) logItem;
            GnssReceiver equip = getEquipment(GnssReceiver.class, logItem);
            GnssReceiverConfiguration config = getConfiguration(GnssReceiverConfiguration.class, equip.getId(), logItem);
            config.setSatelliteSystem(receiverLogItem.getSatelliteSystem());
            config.setFirmwareVersion(receiverLogItem.getFirmwareVersion());
            config.setElevetionCutoffSetting(receiverLogItem.getElevationCutoffSetting());
            config.setTemperatureStabilization(receiverLogItem.getTemperatureStabilization());
            config.setNotes(receiverLogItem.getNotes());
            equipment = equip;
            configuration = config;

        } else if (logItem instanceof GnssAntennaLogItem) {
            GnssAntennaLogItem antennaLogItem = (GnssAntennaLogItem) logItem;
            GnssAntenna equip = getEquipment(GnssAntenna.class, logItem);
            GnssAntennaConfiguration config = getConfiguration(GnssAntennaConfiguration.class, equip.getId(), logItem);
            config.setAlignmentFromTrueNorth(antennaLogItem.getAlignmentFromTrueNorth());
            equipment = equip;
            configuration = config;

        } else if (logItem instanceof HumiditySensorLogItem) {
            HumiditySensorLogItem humiditySensorLogItem = (HumiditySensorLogItem) logItem;
            HumiditySensor equip = getEquipment(HumiditySensor.class, logItem);
            HumiditySensorConfiguration config = getConfiguration(HumiditySensorConfiguration.class, equip.getId(), logItem);
            equip.setAspiration(humiditySensorLogItem.getAspiration());
            config.setHeightDiffToAntenna(humiditySensorLogItem.getHeightDiffToAntenna());
            equipment = equip;
            configuration = config;
        }
        equipmentRepository.save(equipment);
        configurationRepository.save(configuration);
        return Pair.of(equipment, configuration);
    }

    private <T extends Equipment> T getEquipment(Class<T> equipmentClass, EquipmentLogItem logItem) {
        T e = equipmentRepository.findOne(equipmentClass, logItem.getType(), logItem.getSerialNumber());
        if (e == null) {
            try {
                e = equipmentClass.getConstructor(String.class, String.class).newInstance(logItem.getType(), logItem.getSerialNumber());
                equipmentRepository.saveAndFlush(e);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        return e;
    }

    private <T extends EquipmentConfiguration> T getConfiguration(Class<T> configClass, Integer equipId, EquipmentLogItem logItem) {
        EffectiveDates period = logItem.getEffectiveDates();
        Date effectiveFrom = null;
        if (period != null) {
            effectiveFrom = period.getFrom();
        } else {
            effectiveFrom = new Date(0L);
        }
        T c = configurationRepository.findOne(configClass, equipId, effectiveFrom);
        if (c == null) {
            try {
                c = configClass.getConstructor(Integer.class, Date.class).newInstance(equipId, effectiveFrom);
                configurationRepository.saveAndFlush(c);
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
        return c;
    }
}

