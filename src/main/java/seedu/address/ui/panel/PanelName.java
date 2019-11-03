package seedu.address.ui.panel;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.regex.Pattern;

/**
 * Represents a Panel Name which is used in the GUI.
 */
public class PanelName {
    public static final PanelName CURRENT = new PanelName("Current Page");

    public static final String MESSAGE_NAME_FORMAT = "\"%s\" is not a valid panel name. \n"
            + "Panel names can only have alphanumeric characters with whitespaces in between.";

    private static final String NAME_VALIDATION_REGEX = "\\p{Alnum}+(\\s+\\p{Alnum}+)*";

    private final String panelName;

    public PanelName(String panelName) {
        requireNonNull(panelName);
        String trimmed = panelName.trim();
        checkArgument(isValidPanelName(trimmed), String.format(MESSAGE_NAME_FORMAT, panelName));
        this.panelName = trimmed;
    }

    public static boolean isValidPanelName(String panelName) {
        return Pattern.compile(NAME_VALIDATION_REGEX).matcher(panelName).matches();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PanelName)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        PanelName otherPanelName = (PanelName) obj;
        return otherPanelName.panelName.equalsIgnoreCase(panelName);
    }

    @Override
    public int hashCode() {
        return panelName.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return panelName.toUpperCase() + " panel";
    }
}
