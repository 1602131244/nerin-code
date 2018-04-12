package com.nerin.nims.opt.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
import javax.servlet.*;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Inject
    private Environment env;

    @Inject
    private NerinProperties nerinProperties;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);

        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            log.info("Web application startup in production mode");
        }
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            log.info("Web application startup in development mode");
        }
        log.info("Web application fully configured");
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("rar", "application/x-rar-compressed;charset=utf-8");
        configurableEmbeddedServletContainer.setMimeMappings(mappings);
    }

//    @Bean
//    @ConditionalOnProperty(name = "nerin.cors.allowed-origins")
//    public CorsFilter corsFilter() {
//        log.debug("Registering CORS filter");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = nerinProperties.getCors();
//        source.registerCorsConfiguration("/api/**", config);
//        source.registerCorsConfiguration("/v2/api-docs", config);
//        source.registerCorsConfiguration("/oauth/**", config);
//        return new CorsFilter(source);
//    }

}
