package cm.smith.android.smsresponder.model;

/**
 * Created by anthony on 2016-05-11.
 */
public enum Role {
    /*
        NOTE:
        Sadly RealmDB doesn't support saving ENUM values, so I'm storing roles as
        an string to get around that :(
     */

    GUEST("GUEST"),
    MEMBER("MEMBER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String role) {
        this.value = role;
    }

    public String getValue() {
        return value;
    }

    public static Role fromString(String text) throws IllegalArgumentException {
        if (text != null) {
            for (Role r : Role.values()) {
                if (text.equalsIgnoreCase(r.value)) {
                    return r;
                }
            }
        }
        throw new IllegalArgumentException("No role with text " + text + " found");
    }
}
