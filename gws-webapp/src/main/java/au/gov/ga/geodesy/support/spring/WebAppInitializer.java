package au.gov.ga.geodesy.support.spring;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public static class CorsFilter implements Filter {

        @Override
        public void init(FilterConfig c) {}

        @Override
        public void destroy() {}

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {

            HttpServletResponse rsp = (HttpServletResponse) response;

            rsp.setHeader("Access-Control-Allow-Origin", "*");
            rsp.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE, PATCH");
            rsp.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, authorization");
            rsp.setHeader("Access-Control-Max-Age", "3600");

            chain.doFilter(request, response);
        }
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { WebAppConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] { 
             new CorsFilter(),
        };
    }
}
