package au.gov.ga.geodesy.support.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import au.gov.ga.geodesy.domain.model.EventSynchronousPublisher;
import au.gov.ga.geodesy.domain.service.GnssCorsSiteService;

@Configuration
public class GeodesyTestConfig extends GeodesyCommonConfig {

    @Autowired
    private GnssCorsSiteService siteService;

    @Bean
    public EventSynchronousPublisher eventPublisher() {
        return new EventSynchronousPublisher();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ConfigureEventSubscribers());
    }

    public class ConfigureEventSubscribers extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest req, HttpServletResponse rsp,
                Object handler) throws Exception {

            eventPublisher().subscribe(siteService);
            return true;
        }

        @Override
        public void postHandle(HttpServletRequest req, HttpServletResponse rsp,
                Object handler, ModelAndView modelAndView) {
            
            eventPublisher().clearSubscribers();
        }
    }
}
