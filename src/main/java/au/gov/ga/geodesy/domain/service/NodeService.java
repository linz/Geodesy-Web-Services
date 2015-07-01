package au.gov.ga.geodesy.domain.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.ComparatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.gov.ga.geodesy.domain.model.Equipment;
import au.gov.ga.geodesy.domain.model.EquipmentInUse;
import au.gov.ga.geodesy.domain.model.EquipmentRepository;
import au.gov.ga.geodesy.domain.model.Event;
import au.gov.ga.geodesy.domain.model.EventPublisher;
import au.gov.ga.geodesy.domain.model.EventRepository;
import au.gov.ga.geodesy.domain.model.EventSubscriber;
import au.gov.ga.geodesy.domain.model.GnssAntenna;
import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.GnssReceiver;
import au.gov.ga.geodesy.domain.model.Node;
import au.gov.ga.geodesy.domain.model.NodeRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SiteUpdated;
import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;


@Component
@Transactional("geodesyTransactionManager")
public class NodeService implements EventSubscriber<SiteUpdated> {

    /* private static final Logger log = LoggerFactory.getLogger(NodeService.class); */

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private EventRepository events;

    @Autowired
    private GnssCorsSiteRepository gnssCorsSites;

    @Autowired
    private NodeRepository nodes;

    @Autowired
    private EquipmentRepository equipment;

    @PostConstruct
    private void subcribe() {
        eventPublisher.subscribe(this);
    }

    public boolean canHandle(Event e) {
        return e != null && (e instanceof SiteUpdated);
    }

    public void handle(SiteUpdated siteUpdated) {
        GnssCorsSite site = gnssCorsSites.findByFourCharacterId(siteUpdated.getFourCharacterId());
        EffectiveDates nodePeriod = null;

        for (Setup setup : site.getSetups()) {

            if (nodePeriod != null) {
                Date nodeEffectiveTo = nodePeriod.getTo();
                if (nodeEffectiveTo == null) {
                    break;
                }
                if (nodeEffectiveTo.compareTo(setup.getEffectivePeriod().getFrom()) == 1) {
                    continue;
                }
            }
            EquipmentInUse receiverInUse = getReceiverInUse(setup);
            EquipmentInUse antennaInUse = getAntennaInUse(setup);

            if (receiverInUse != null && antennaInUse != null) {
                nodePeriod = lcd(receiverInUse.getPeriod(), antennaInUse.getPeriod());
                nodes.save(new Node(site.getId(), nodePeriod, setup.getId()));
            }
        }
        eventPublisher.handled(siteUpdated);
    }

    @SuppressWarnings("unchecked")
    private EffectiveDates lcd(EffectiveDates e, EffectiveDates f) {
        Comparator<Date> fromC = ComparatorUtils.nullLowComparator(ComparatorUtils.NATURAL_COMPARATOR);
        Comparator<Date> toC   = ComparatorUtils.nullHighComparator(ComparatorUtils.NATURAL_COMPARATOR);
        return new EffectiveDates(max(e.getFrom(), f.getFrom(), fromC), min(e.getTo(), f.getTo(), toC));
    }

    private <T> T min(T a, T b, Comparator<T> c) {
        return c.compare(a, b) < 1 ? a : b;
    }

    private <T> T max(T a, T b, Comparator<T> c) {
        return c.compare(a, b) > -1 ? a : b;
    }

    private <T extends Equipment> EquipmentInUse getOneEquipmentInUse(Setup setup, Class<T> equipmentClass) {
        Collection<EquipmentInUse> inUse = getEquipmentInUse(setup, equipmentClass);
        if (inUse.isEmpty()) {
            return null;
        } else if (inUse.size() == 1) {
            return inUse.iterator().next();
        } else {
            throw new RuntimeException("Multiple " + equipmentClass.getSimpleName() + " in use!");
        }
    }

    private EquipmentInUse getReceiverInUse(Setup setup) {
        return getOneEquipmentInUse(setup, GnssReceiver.class);
    }

    private EquipmentInUse getAntennaInUse(Setup setup) {
        return getOneEquipmentInUse(setup, GnssAntenna.class);
    }

    private <T extends Equipment> Collection<EquipmentInUse> getEquipmentInUse(Setup setup, final Class<T> equipmentClass) {
        return Collections2.filter(setup.getEquipmentInUse(), new Predicate<EquipmentInUse>() {
            public boolean apply(EquipmentInUse equipmentInUse) {
                Equipment e = equipment.findOne(equipmentInUse.getEquipmentId());
                return equipmentClass.isAssignableFrom(e.getClass());
            }
        });
    }
}

