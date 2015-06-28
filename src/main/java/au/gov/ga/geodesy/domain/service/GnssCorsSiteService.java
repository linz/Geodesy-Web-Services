package au.gov.ga.geodesy.domain.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.ComparatorUtils;
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

    @Autowired
    private EventPublisher eventPublisher;

    @PostConstruct
    private void subcribe() {
        eventPublisher.subscribe(this);
    }

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
        SortedMap<Date, List<EquipmentLogItem>> logItemsByDate = new TreeMap<>(dateComparator);

        for (EquipmentLogItem logItem : siteLog.getEquipmentLogItems()) {
            EffectiveDates dates = logItem.getEffectiveDates();

            List<EquipmentLogItem> logItems = logItemsByDate.get(dates.getFrom());
            if (logItems == null) {
                logItems = new ArrayList<>();
                logItemsByDate.put(dates.getFrom(), logItems);
            }
            logItems.add(logItem);
            logItems = logItemsByDate.get(dates.getTo());
            if (logItems == null) {
                logItemsByDate.put(dates.getTo(), new ArrayList<EquipmentLogItem>());
            }
        }

        SortedMap<Date, Setup> setups = new TreeMap<>(dateComparator);

        Set<Date> datesOfChange = logItemsByDate.keySet();

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
        for (EquipmentLogItem logItem : siteLog.getEquipmentLogItems()) {
            addEquipment(logItem, setups);
        }
        return new ArrayList<Setup>(setups.values());
    }

    private void addEquipment(EquipmentLogItem logItem, SortedMap<Date, Setup> setups) {
        @SuppressWarnings("unchecked")
        Comparator<Date> comparator = ComparatorUtils.nullLowComparator(ComparatorUtils.NATURAL_COMPARATOR);

        Date equipmentFrom = logItem.getEffectiveDates().getFrom();
        Date equipmentTo = logItem.getEffectiveDates().getTo();

        Iterator<Date> setupFromIt = setups.keySet().iterator();
        while (setupFromIt.hasNext()) {
            Date setupFrom = setupFromIt.next();
            if (setupFrom.equals(equipmentFrom)) {
                addEquipment(logItem, setups.get(setupFrom));
                break;
            }
        }
        while (setupFromIt.hasNext()) {
            Date setupFrom = setupFromIt.next();
            Setup setup = setups.get(setupFrom);
            Date setupTo = setup.getEffectivePeriod().getTo();
            if (comparator.compare(equipmentTo, setupTo) < 1) {
                addEquipment(logItem, setup);
            } else {
                break;
            }
        }
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

    private Pair<Equipment, EquipmentConfiguration> getEquipment(EquipmentLogItem logItem) {
        Equipment e = null;

        if (logItem instanceof GnssReceiverLogItem) {
            e = getEquipment(GnssReceiver.class, logItem);

        } else if (logItem instanceof GnssAntennaLogItem) {
            e = getEquipment(GnssAntenna.class, logItem);

        } else if (logItem instanceof HumiditySensorLogItem) {
            HumiditySensor h = getEquipment(HumiditySensor.class, logItem);
            h.setAspiration(h.getAspiration());
            e = h;
        }
        return Pair.of(e, null);
    }


    private void addEquipment(EquipmentLogItem logItem, Setup s) {
        EffectiveDates period = logItem.getEffectiveDates();

        Equipment equipment = getEquipment(logItem).getLeft();
        /* EquipmentConfiguration config = null; */

        if (logItem instanceof GnssReceiverLogItem) {
            GnssReceiverLogItem r = (GnssReceiverLogItem) logItem;

            GnssReceiverConfiguration config = (GnssReceiverConfiguration) configurations.findOne(equipment.getId(), period.getFrom());
            if (config == null) {
                 config = new GnssReceiverConfiguration(equipment.getId(), period.getFrom());
                 configurations.saveAndFlush(config);
            }
            config.setSatelliteSystem(r.getSatelliteSystem());
            config.setFirmwareVersion(r.getFirmwareVersion());
            config.setElevetionCutoffSetting(r.getElevationCutoffSetting());
            config.setTemperatureStabilization(r.getTemperatureStabilization());
            config.setNotes(r.getNotes());

            s.getEquipmentInUse().add(new EquipmentInUse(equipment.getId(), config.getId(), period));

        } else if (logItem instanceof GnssAntennaLogItem) {
            GnssAntennaLogItem a = (GnssAntennaLogItem) logItem;

            GnssAntennaConfiguration config = (GnssAntennaConfiguration) configurations.findOne(equipment.getId(), period.getFrom());
            if (config == null) {
                 config = new GnssAntennaConfiguration(equipment.getId(), period.getFrom());
                 configurations.saveAndFlush(config);
            }
            config.setAlignmentFromTrueNorth(a.getAlignmentFromTrueNorth());

            s.getEquipmentInUse().add(new EquipmentInUse(equipment.getId(), config.getId(), period));
        } else if (logItem instanceof HumiditySensorLogItem) {
            HumiditySensorLogItem h = (HumiditySensorLogItem) logItem;

            HumiditySensorConfiguration config = (HumiditySensorConfiguration) configurations.findOne(equipment.getId(), period.getFrom());
            if (config == null) {
                 config = new HumiditySensorConfiguration(equipment.getId(), period.getFrom());
                 configurations.saveAndFlush(config);
            }
            config.setHeightDiffToAntenna(h.getHeightDiffToAntenna());
            s.getEquipmentInUse().add(new EquipmentInUse(equipment.getId(), config.getId(), period));
        }
        /* s.getEquipmentInUse().add(new EquipmentInUse(equipment.getId(), config.getId(), period)); */
    }
}

