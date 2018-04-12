package com.nerin.nims.opt;

import com.nerin.nims.opt.app.config.Constants;
import com.nerin.nims.opt.app.config.NerinProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.SimpleCommandLinePropertySource;

@SpringBootApplication
@ServletComponentScan
@EnableConfigurationProperties({ NerinProperties.class, LiquibaseProperties.class })
public class NimsOptApplication {

	public static void main(String[] args) {
		SpringApplication.run(NimsOptApplication.class, args);
	}

//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer(){
//		return new EmbeddedServletContainerCustomizer() {
//			@Override
//			public void customize(ConfigurableEmbeddedServletContainer container) {
//				container.setSessionTimeout(216000);//单位为S
//			}
//		};
//	}

	/**
	 * If no profile has been configured, set by default the "dev" profile.
	 */
	private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
		if (!source.containsProperty("spring.profiles.active") &&
				!System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {

			app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
		}
	}
}

