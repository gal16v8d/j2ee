package com.gsdd.j2ee.rest.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SwaggerJaxrsConfiguration extends HttpServlet {

    private static final long serialVersionUID = -4839403819889621126L;
    private static final String[] SCHEMES = new String[] { "http" };

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        toBeanConfig();
    }

    private BeanConfig toBeanConfig() {
        try {
            BeanConfig beanConfig = new BeanConfig();
            beanConfig.setVersion("1.0.001");
            beanConfig.setSchemes(SCHEMES);
            beanConfig.setHost("localhost:8080");
            beanConfig.setBasePath("/j2ee");
            beanConfig.setResourcePackage("com.gsdd.j2ee.rest");
            beanConfig.setScan(true);
            return beanConfig;
        } catch (Exception e) {
            log.error("Couldn't load bean config, so no swagger config generated", e);
            return null;
        }
    }
}