package com.pkulaw.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lind
 * @date 2022/9/21 14:53
 * @since 1.0.0
 */
@Configuration
public class AuthConfig {

	@Bean
	@RefreshScope
	public AuthProperties authProperties() {
		return new AuthProperties();
	}

}
