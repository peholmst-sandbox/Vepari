package net.pkhapps.vepari.server.security;

/**
 * Class listing the user permissions.
 */
public final class Permissions {

    public static final String RECEIVE_SMS = "PERM_RECEIVE_SMS";
    public static final String RECEIVE_PHONE_CALL = "PERM_RECEIVE_PHONE_CALL";
    public static final String INVALIDATE_ACCESS_TOKEN = "PERM_INVALIDATE_ACCESS_TOKEN";
    private Permissions() {
    }
}
