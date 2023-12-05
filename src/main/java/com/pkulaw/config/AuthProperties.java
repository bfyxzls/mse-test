package com.pkulaw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置-热更新
 *
 * @author lind
 * @date 2022/9/21 13:42
 * @since 1.0.0
 */
@ConfigurationProperties("auth")
@Data
public class AuthProperties {
    private String title;
    private String version;
}
