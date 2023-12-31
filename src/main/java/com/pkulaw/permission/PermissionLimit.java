package com.pkulaw.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限限制
 *
 * @author xuxueli 2015-12-12 18:29:02
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionLimit {

    /**
     * 要求管理员权限
     *
     * @return
     */
    boolean adminuser() default false;

    /**
     * 操作权限列表，由开发者定义，如read,modify_product,delete_all,read_news
     *
     * @return
     */
    String[] permissions() default "";

}
