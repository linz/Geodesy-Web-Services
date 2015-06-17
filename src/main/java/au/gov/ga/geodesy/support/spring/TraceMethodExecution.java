package au.gov.ga.geodesy.support.spring;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.interceptor.SimpleTraceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceMethodExecution {

    /* @SuppressWarnings("unused") */
    /* private static final Logger log = LoggerFactory.getLogger(TraceMethodExcution.class); */

    @Bean
    public SimpleTraceInterceptor simpleTraceInterceptor() {
        SimpleTraceInterceptor tracer = new SimpleTraceInterceptor();
        tracer.setUseDynamicLogger(true);
        return tracer;
    }

    @Bean
    public ProxyFactory proxyFactory() {
        ProxyFactory factory = new ProxyFactory();
        factory.setInterfaces(Intercepted.class);
        factory.addAdvice(simpleTraceInterceptor());
        return factory;
    }

    public static interface Intercepted {
    }
}
