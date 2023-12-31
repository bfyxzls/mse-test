package com.pkulaw.permission;

import com.pkulaw.config.UserContextHolder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lind
 * @date 2023/12/4 14:50
 * @since 1.0.0
 */
@Data
public class LoginUser {
    private static final Logger logger = LoggerFactory.getLogger(LoginUser.class);
    private String username;
    private String roles;
    private String code;
    private String uuid;

    public boolean validPermission(String... permissions) {
        if (roles != null) {
            for (String role : roles.split(",")) {
                for (String permission : permissions) {
                    if (role.equals(permission)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
