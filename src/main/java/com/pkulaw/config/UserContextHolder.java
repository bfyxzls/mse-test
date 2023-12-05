package com.pkulaw.config;

import com.pkulaw.permission.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lind
 * @date 2023/12/4 15:00
 * @since 1.0.0
 */
public class UserContextHolder {
    private static final Logger logger = LoggerFactory.getLogger(UserContextHolder.class);
    private static final ThreadLocal<LoginUser> userThreadLocal = new ThreadLocal<>();

    public static void set(LoginUser user) {
        userThreadLocal.set(user);
    }

    public static LoginUser get() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }

}
