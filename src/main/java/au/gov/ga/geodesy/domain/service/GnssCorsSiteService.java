package au.gov.ga.geodesy.domain.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.ComparatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.EquipmentConfigurationRepository;
import au.gov.ga.geodesy.domain.model.EquipmentInUse;
import au.gov.ga.geodesy.domain.model.EquipmentRepository;
import au.gov.ga.geodesy.domain.model.Event;
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
import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;
import au.gov.ga.geodesy.igssitelog.domain.model.Equipment;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;

@Component
public class GnssCorsSiteService implements EventSubscriber<SiteLogUploaded> {

    private static final Logger log = LoggerFactory.getLogger(GnssCorsSiteService.class);

    @SuppressWarnings("unchecked")
    private static final Comparator<Date> dateComparator = ComparatorUtils.nullHighComparator(ComparatorUtils.NATURAL_COMPARATOR);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentConfigurationRepository configurations;

    @Autowired
    private GnssCorsSiteRepository gnssSites;

    public boolean canHandle(Event e) {
        return e != null && (e instanceof SiteLogUploaded);
    }

    public void handle(SiteLogUploaded siteLogUploaded) {
        log.info("Notified of " + siteLogUploaded);

        String siteId = siteLogUploaded.getFourCharacterId();
        IgsSiteLog siteLog = siteLogs.findByFourCharacterId(siteId);
        GnssCorsSite gnssSite = gnssSites.findByFourCharacterId(siteId);

        if (gnssSite == null) {
            gnssSite = new GnssCorsSite(siteId);
        }
        gnssSite.setName(siteLog.getSiteIdentification().getSiteName());
        gnssSite.setDescription(siteLog.getSiteIdentification().getMonumentDescription());

        gnssSite.getSetups().clear();

        gnssSite.getSetups().addAll(getSetups(siteLog));

        gnssSites.save(gnssSite);
        siteLogUploaded.handled();
    }

    private List<Setup> getSetups(IgsSiteLog siteLog) {
        SortedMap<Date, List<Equipment>> equipment =
            new TreeMap<Date, List<Equipment>>(dateComparator);

        for (Equipment e : siteLog.getEquipment()) {
            

            EffectiveDates dates = e.getEffectiveDates();

            List<Equipment> es = equipment.get(dates.getFrom());
            if (es == null) {
                es = new ArrayList<Equipment>();
                equipment.put(dates.getFrom(), es);
            }
            es.add(e);
            es = equipment.get(dates.getTo());
            if (es == null) {
                equipment.put(dates.getTo(), new ArrayList<Equipment>());
            }
        }

        SortedMap<Date, Setup> setups = new TreeMap<Date, Setup>(dateComparator);

        Set<Date> datesOfChange = equipment.keySet();

        if (datesOfChange.size() > 0) {
            if (datesOfChange.size() == 1) {
                Date d = datesOfChange.iterator().next();
                setups.put(d, new Setup("GNSS Cors Setup", new EffectiveDates(d, null)));
            } else {
                Iterator<Date> i = datesOfChange.iterator();
                Iterator<Date> j = datesOfChange.iterator();
                j.next();
                Setup s = null;
                do {
                    Date from = i.next();
                    Date to = j.next();
                    s = new Setup("Gnss Core Setup", new EffectiveDates(from, to));
                    setups.put(from, s);
                }
                while (j.hasNext());
                EffectiveDates lastPeriod = s.getEffectivePeriod();
                if (lastPeriod.getTo() != null) {
                    s = new Setup("Gnss Core Setup", new EffectiveDates(lastPeriod.getTo(), null));
                    setups.put(s.getEffectivePeriod().getFrom(), s);
                }
            }
        }
        for (Equipment e : siteLog.getEquipment()) {
            addEquipment(e, setups);
        }
        return new ArrayList<Setup>(setups.values());
    }


    private void addEquipment(Equipment e, SortedMap<Date, Setup> setups) {
        @SuppressWarnings("unchecked")
        Comparator<Date> comparator = ComparatorUtils.nullLowComparator(ComparatorUtils.NATURAL_COMPARATOR);

        Date equipmentFrom = e.getEffectiveDates().getFrom();
        Date equipmentTo = e.getEffectiveDates().getTo();

        Iterator<Date> setupFromIt = setups.keySet().iterator();
        while (setupFromIt.hasNext()) {
            Date setupFrom = setupFromIt.next();
            if (setupFrom.equals(equipmentFrom)) {
                addEquipment(e, setups.get(setupFrom));
                break;
            }
        }
        while (setupFromIt.hasNext()) {
            Date setupFrom = setupFromIt.next();
            Setup setup = setups.get(setupFrom);
            Date setupTo = setup.getEffectivePeriod().getTo();
            if (comparator.compare(equipmentTo, setupTo) < 1) {
                addEquipment(e, setup);
            } else {
                break;
            }
        }
    }

    private void addEquipment(Equipment e, Setup s) {
        EffectiveDates period = e.getEffectiveDates();

        if (e instanceof au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiver) {
            au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiver r = (au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiver) e;

            GnssReceiver receiver = equipmentRepository.findOne(GnssReceiver.class, r.getReceiverType(), r.getSerialNumber());
            if (receiver == null) {
                receiver = new GnssReceiver(r.getReceiverType(), r.getSerialNumber());
                equipmentRepository.saveAndFlush(receiver);
            }
            GnssReceiverConfiguration config = (GnssReceiverConfiguration) configurations.findOne(receiver.getId(), period.getFrom());
            if (config == null) {
                 config = new GnssReceiverConfiguration(receiver.getId(), r.getDateInstalled());
                 configurations.saveAndFlush(config);
            }
            config.setSatelliteSystem(r.getSatelliteSystem());
            config.setFirmwareVersion(r.getFirmwareVersion());
            config.setElevetionCutoffSetting(r.getElevationCutoffSetting());
            config.setTemperatureStabilization(r.getTemperatureStabilization());
            config.setNotes(r.getNotes());

            s.getEquipmentInUse().add(new EquipmentInUse(receiver.getId(), config.getId(), period));

        } else if (e instanceof au.gov.ga.geodesy.igssitelog.domain.model.GnssAntenna) {
            au.gov.ga.geodesy.igssitelog.domain.model.GnssAntenna a = (au.gov.ga.geodesy.igssitelog.domain.model.GnssAntenna) e;

            GnssAntenna antenna = equipmentRepository.findOne(GnssAntenna.class, a.getAntennaType(), a.getSerialNumber());
            if (antenna == null) {
                antenna = new GnssAntenna(a.getAntennaType(), a.getSerialNumber());
                equipmentRepository.saveAndFlush(antenna);
            }
            GnssAntennaConfiguration config = (GnssAntennaConfiguration) configurations.findOne(antenna.getId(), period.getFrom());
            if (config == null) {
                 config = new GnssAntennaConfiguration(antenna.getId(), period.getFrom());
                 configurations.saveAndFlush(config);
            }
            config.setAlignmentFromTrueNorth(a.getAlignmentFromTrueNorth());

            s.getEquipmentInUse().add(new EquipmentInUse(antenna.getId(), config.getId(), period));
        } else if (e instanceof au.gov.ga.geodesy.igssitelog.domain.model.HumiditySensor) {
            au.gov.ga.geodesy.igssitelog.domain.model.HumiditySensor h = (au.gov.ga.geodesy.igssitelog.domain.model.HumiditySensor) e;

            HumiditySensor humiditySensor = equipmentRepository.findOne(HumiditySensor.class, h.getType(), h.getSerialNumber());
            if (humiditySensor == null) {
                humiditySensor = new HumiditySensor(h.getType(), h.getSerialNumber());
                equipmentRepository.saveAndFlush(humiditySensor);
            }
            HumiditySensorConfiguration config = (HumiditySensorConfiguration) configurations.findOne(humiditySensor.getId(), period.getFrom());
            if (config == null) {
                 config = new HumiditySensorConfiguration(humiditySensor.getId(), period.getFrom());
                 configurations.saveAndFlush(config);
            }
            config.setHeightDiffToAntenna(h.getHeightDiffToAntenna());
            s.getEquipmentInUse().add(new EquipmentInUse(humiditySensor.getId(), config.getId(), period));
        }
    }
}

