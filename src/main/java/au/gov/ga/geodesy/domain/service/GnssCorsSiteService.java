package au.gov.ga.geodesy.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.domain.model.Event;
import au.gov.ga.geodesy.domain.model.EventSubscriber;
import au.gov.ga.geodesy.domain.model.GnssCorsSite;
import au.gov.ga.geodesy.domain.model.GnssCorsSiteRepository;
import au.gov.ga.geodesy.domain.model.GnssReceiver;
import au.gov.ga.geodesy.domain.model.GnssReceiverConfiguration;
import au.gov.ga.geodesy.domain.model.GnssReceiverRepository;
import au.gov.ga.geodesy.domain.model.Setup;
import au.gov.ga.geodesy.domain.model.SiteLogUploaded;
import au.gov.ga.geodesy.igssitelog.domain.model.EffectiveDates;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLogRepository;

@Component
public class GnssCorsSiteService implements EventSubscriber<SiteLogUploaded> {

    private static final Logger log = LoggerFactory.getLogger(GnssCorsSiteService.class);

    @Autowired
    private IgsSiteLogRepository siteLogs;

    @Autowired
    private GnssReceiverRepository receivers;

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

        for (au.gov.ga.geodesy.igssitelog.domain.model.GnssReceiver r : siteLog.getGnssReceivers()) {
            EffectiveDates period = new EffectiveDates(r.getDateInstalled(), r.getDateRemoved());
            Setup s = new Setup("GNSS CORS Setup", period);
            gnssSite.getSetups().add(s);

            GnssReceiver receiver = receivers.findOne(r.getReceiverType(), r.getSerialNumber());
            if (receiver == null) {
                receiver = new GnssReceiver(r.getSerialNumber(), r.getReceiverType());
                receivers.saveAndFlush(receiver);
            }
            GnssReceiverConfiguration receiverConfig = new GnssReceiverConfiguration(receiver.getId(), r.getDateInstalled());
            receiverConfig.setSatelliteSystem(r.getSatelliteSystem());
            receiverConfig.setFirmwareVersion(r.getFirmwareVersion());
            receiverConfig.setElevetionCutoffSetting(r.getElevationCutoffSetting());
            receiverConfig.setTemperatureStabilization(r.getTemperatureStabilization());
            receiverConfig.setNotes(r.getNotes());
            s.getEquipmentConfigurations().add(receiverConfig);
        }
        gnssSites.save(gnssSite);
        siteLogUploaded.handled();
    }
}
