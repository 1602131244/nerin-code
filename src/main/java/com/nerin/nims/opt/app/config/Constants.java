package com.nerin.nims.opt.app.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    // Spring profile for development and production, see http://jhipster.github.io/profiles/
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used to disable swagger
    public static final String SPRING_PROFILE_NO_SWAGGER = "no-swagger";
    // Spring profile used to disable running liquibase
    public static final String SPRING_PROFILE_NO_LIQUIBASE = "no-liquibase";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String LOGIN_TYPE_FORM = "form";
    public static final String LOGIN_TYPE_AJAX = "ajax";

    public static final String DB_PREFIX = "cux";

    private Constants() {
    }
}
