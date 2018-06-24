package net.pkhapps.vepari.server.security;

/**
 * Class listing the user permissions.
 */
public final class Permissions {

    public static final String PREFIX = "PERM_";
    public static final String RECEIVE_SMS = PREFIX + "RECEIVE_SMS";
    public static final String RECEIVE_PHONE_CALL = PREFIX + "RECEIVE_PHONE_CALL";
    public static final String RECEIVE_ALERT = PREFIX + "RECEIVE_ALERT";
    public static final String RECEIVE_ALERT_FULL_DETAILS = RECEIVE_ALERT + "_FULL_DETAILS";
    public static final String INVALIDATE_ACCESS_TOKEN = PREFIX + "INVALIDATE_ACCESS_TOKEN";

    public static final String RUN_AS_SYSTEM = "RUN_AS_SYSTEM";

    private Permissions() {
    }
}
