package au.gov.ga.geodesy.domain.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.EquipmentInUse;
import au.gov.ga.geodesy.domain.model.CorsSite;
import au.gov.ga.geodesy.domain.model.CorsSiteRepository;
import au.gov.ga.geodesy.domain.model.Monument;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SetupRepository;
import au.gov.ga.geodesy.domain.model.equipment.Equipment;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentConfiguration;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentConfigurationRepository;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentFactory;
import au.gov.ga.geodesy.domain.model.equipment.EquipmentRepository;
import au.gov.ga.geodesy.domain.model.event.Event;
import au.gov.ga.geodesy.domain.model.event.EventPublisher;
import au.gov.ga.geodesy.domain.model.event.EventSubscriber;
import au.gov.ga.geodesy.domain.model.event.SiteLogReceived;
import au.gov.ga.geodesy.domain.model.event.SiteUpdated;
import au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLogRepository;
import au.gov.ga.geodesy.domain.model.sitelog.EffectiveDates;
import au.gov.ga.geodesy.domain.model.sitelog.EquipmentLogItem;
import au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog;
import au.gov.ga.geodesy.domain.model.sitelog.SiteIdentification;

@Component
@Transactional("geodesyTransactionManager")
public class CorsSiteService implements EventSubscriber<SiteLogReceived> {

    private static final Logger log = LoggerFactory.getLogger(CorsSiteService.class);
    private static final String gnssCorsSetupName = "GNSS CORS Setup";

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentConfigurationRepository configurationRepository;

    @Autowired
    private CorsSiteRepository gnssSites;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private SetupRepository setups;

    @Autowired
    private EquipmentFactory equipmentFactory;

    @PostConstruct
    private void subscribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event e) {
        return e != null && (e instanceof SiteLogReceived);
    }

    public void handle(SiteLogReceived siteLogUploaded) {
        log.info("Notified of " + siteLogUploaded);

        String fourCharacterId = siteLogUploaded.getFourCharacterId();
        IgsSiteLog siteLog = siteLogs.findByFourCharacterId(fourCharacterId);

        CorsSite gnssSite = gnssSites.findByFourCharacterId(fourCharacterId);
        if (gnssSite == null) {
            gnssSite = new CorsSite(fourCharacterId);
        }
        gnssSite.setName(siteLog.getSiteIdentification().getSiteName());

        SiteIdentification siteId = siteLog.getSiteIdentification();
        gnssSite.setDomesNumber(siteId.getIersDOMESNumber());
        gnssSite.setDateInstalled(siteId.getDateInstalled());

        Monument monument = new Monument();
        monument.setDescription(siteId.getMonumentDescription());
        monument.setHeight(siteId.getHeightOfMonument());
        monument.setFoundation(siteId.getMonumentFoundation());
        monument.setMarkerDescription(siteId.getMarkerDescription());
        gnssSite.setMonument(monument);

        gnssSites.saveAndFlush(gnssSite);

        List<Setup> newSetups = getSetups(siteLog);
        for (Setup s : newSetups) {
            s.setSiteId(gnssSite.getId());
        }
        List<Setup> oldSetups = setups.findBySiteId(gnssSite.getId());
        @SuppressWarnings("unchecked")
        List<Setup> commonSetups = ListUtils.intersection(oldSetups, newSetups);
        newSetups.removeAll(commonSetups);
        oldSetups.removeAll(commonSetups);

        oldSetups.forEach(s -> {
                s.invalidate();
                log.info("Invalidated site : " + s.getId());
            });

        setups.save(oldSetups);
        setups.save(newSetups);

        eventPublisher.handled(siteLogUploaded);
        eventPublisher.publish(new SiteUpdated(fourCharacterId));

        log.info("Saving site: " + fourCharacterId);
    }

    private List<Setup> getSetups(IgsSiteLog siteLog) {
        @SuppressWarnings("unchecked")
        Comparator<Date> fromC = ComparatorUtils.nullLowComparator(ComparatorUtils.NATURAL_COMPARATOR);
        SortedSet<Date> datesOfChange = new TreeSet<>(fromC);

        for (EquipmentLogItem logItem : siteLog.getEquipmentLogItems()) {
            EffectiveDates dates = logItem.getEffectiveDates();
            if (dates == null) {
                datesOfChange.add(null);
            } else {
                datesOfChange.add(dates.getFrom());
                if (dates.getTo() != null) {
                    datesOfChange.add(dates.getTo());
                }
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

        int i = 0;
        int j = setups.size();

        if (period != null) {
            equipmentFrom = period.getFrom();
            equipmentTo = period.getTo();
            if (equipmentFrom != null && equipmentFrom.equals(equipmentTo)) {
                // equipment log entries with empty periods are corrections
                return;
            }
            /* while (!setups.get(i).getEffectivePeriod().getFrom().equals(equipmentFrom)) { */
            /*     i++; */
            /* } */
            while (true) {
                EffectiveDates setupPeriod = setups.get(i).getEffectivePeriod();
                if (setupPeriod != null && setupPeriod.getFrom() != null
                        && setupPeriod.getFrom().equals(equipmentFrom)) {
                    break;
                }
                i++;
            }
            j = i;
            if (equipmentTo == null) {
                j = setups.size();
            } else {
                Date setupTo = null;
                do {
                    setupTo = setups.get(j++).getEffectivePeriod().getTo();
                } while (setupTo != null && !setupTo.equals(equipmentTo));
            }
        }
        for (int k = i; k < j; k++) {
            addEquipment(logItem, setups.get(k));
        }
    }

    private void addEquipment(EquipmentLogItem logItem, Setup s) {
        Pair<? extends Equipment, ? extends EquipmentConfiguration> ec = equipmentFactory.create(logItem);
        Equipment equipment = ec.getLeft();
        EquipmentConfiguration config = ec.getRight();
        equipmentRepository.save(equipment);
        configurationRepository.saveAndFlush(config); // TODO: must flush to get the id, is there another way?
        EquipmentInUse inUse = new EquipmentInUse(equipment.getId(), config.getId(), logItem.getEffectiveDates());
        s.getEquipmentInUse().add(inUse);
    }
}

