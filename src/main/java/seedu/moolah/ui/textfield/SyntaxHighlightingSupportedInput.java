package seedu.moolah.ui.textfield;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import seedu.moolah.logic.parser.Prefix;

/**
 * Represents a supported input for syntax highlighting.
 */
public class SyntaxHighlightingSupportedInput {

    static final String PLACEHOLDER_REGEX = "(?<placeholder><[^>]+>)";
    // param1 commandword, param2 concat list of prefixes
    private static final String INPUT_PATTERN_TEMPLATE = "(?<COMMAND>%s)|" + PLACEHOLDER_REGEX + "|%s(?<arg>\\S+)";

    private String command;
    private List<Prefix> prefixes;
    private Pattern pattern;

    public SyntaxHighlightingSupportedInput(String command, List<Prefix> requiredPrefixes,
                                            List<Prefix> optionalPrefixes) {
        this.command = command;
        this.prefixes = new ArrayList<>(requiredPrefixes);
        this.prefixes.addAll(optionalPrefixes);
        this.pattern = compileCommandPattern(command, prefixes);
    }

    public String getCommand() {
        return command;
    }

    public String getDescription(String pre) {
        Prefix pref = getPrefix(pre);
        if (pref != null) {
            return pref.getDescriptionOfArgument();
        } else {
            return "";
        }
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Prefix getPrefix(String pre) {
        return prefixes.stream().filter(x -> x.toString().equals(pre)).findFirst().orElse(null);
    }

    public int getPrefixCount() {
        return prefixes.size();
    }

    public List<Prefix> getPrefixes() {
        return new ArrayList<>(prefixes);
    }


    /**
     * Compile pattern for a command input syntax used for matching during syntax highlighting.
     * @param commandWord The command word of the command.
     * @param prefixes The list of prefixes of the command.
     * @return The compiled pattern.
     */
    private Pattern compileCommandPattern(String commandWord, List<Prefix> prefixes) {
        StringBuilder prefixesPatterns = new StringBuilder();
        int count = 0;
        for (Prefix prefix : prefixes) {
            count++;
            prefixesPatterns.append(String.format("(?<prefix%s> %s)|", count, prefix.getPrefix()));
        }

        return Pattern.compile(String.format(INPUT_PATTERN_TEMPLATE, commandWord, prefixesPatterns.toString()));
    }
}
