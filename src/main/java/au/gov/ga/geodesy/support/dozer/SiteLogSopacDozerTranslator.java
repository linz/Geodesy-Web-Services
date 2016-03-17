package au.gov.ga.geodesy.support.dozer;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.stereotype.Component;

import au.gov.ga.geodesy.igssitelog.domain.model.IgsSiteLog;
import au.gov.ga.geodesy.port.adapter.sopac.SiteLogSopacTranslator;

@Component
public class SiteLogSopacDozerTranslator implements SiteLogSopacTranslator {

    private DozerBeanMapper mapper;

    public SiteLogSopacDozerTranslator() {
        mapper = new DozerBeanMapper();
        mapper.addMapping(new BeanMappingBuilder() {
            protected void configure() {
                mapping(au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog.class, IgsSiteLog.class);
            }
        });
    }

    public au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog fromDTO(IgsSiteLog siteLogSopac) {
        return (au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog)
            mapper.map(siteLogSopac, au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog.class);
    }

    public IgsSiteLog toDTO(au.gov.ga.geodesy.domain.model.sitelog.IgsSiteLog siteLog) {
        return (IgsSiteLog) mapper.map(siteLog, IgsSiteLog.class);
    }
}
