package seedu.address.model.alias;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.exceptions.RecursiveAliasException;
import seedu.address.logic.commands.GenericCommandWord;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.alias.AddAliasCommand;
import seedu.address.logic.commands.alias.DeleteAliasCommand;
import seedu.address.logic.commands.alias.ListAliasesCommand;
import seedu.address.logic.commands.budget.AddBudgetCommand;
import seedu.address.logic.commands.budget.SwitchBudgetCommand;
import seedu.address.logic.commands.event.AddEventCommand;
import seedu.address.logic.commands.event.DeleteEventCommand;
import seedu.address.logic.commands.event.EditEventCommand;
import seedu.address.logic.commands.event.ListEventsCommand;
import seedu.address.logic.commands.expense.AddExpenseCommand;
import seedu.address.logic.commands.expense.DeleteExpenseCommand;
import seedu.address.logic.commands.expense.EditExpenseCommand;
import seedu.address.logic.commands.expense.FindExpenseCommand;
import seedu.address.logic.commands.expense.ListExpensesCommand;
import seedu.address.logic.commands.general.ClearCommand;
import seedu.address.logic.commands.general.ExitCommand;
import seedu.address.logic.commands.general.HelpCommand;
import seedu.address.logic.commands.ui.ViewPanelCommand;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * A Serializable class that represents the user's {@code Alias} settings.
 * Guarantees: fields are present, immutable.
 */
public class AliasMappings implements Serializable {

    private static final String NON_MATCHING_KEY = "Alias' key must be the Alias' name.";
    private static final String RESERVED_NAME = "Alias cannot use a name reserved by a built-in command.";
    private static final String NULL_VALUE = "Either Alias or key is null.";
    private static final String INVALID_ALIAS = "Alias mappings contains an invalid Alias.";

    private static final List<String> RESERVED_COMMAND_WORDS = List.of(
            // generic commands
            GenericCommandWord.ADD,
            GenericCommandWord.DELETE,
            GenericCommandWord.LIST,
            GenericCommandWord.CLEAR,
            GenericCommandWord.EDIT,
            // event
            AddEventCommand.COMMAND_WORD,
            ListEventsCommand.COMMAND_WORD,
            DeleteEventCommand.COMMAND_WORD,
            EditEventCommand.COMMAND_WORD,
            //expense
            AddExpenseCommand.COMMAND_WORD,
            DeleteExpenseCommand.COMMAND_WORD,
            ListExpensesCommand.COMMAND_WORD,
            FindExpenseCommand.COMMAND_WORD,
            EditExpenseCommand.COMMAND_WORD,
            // budget
            AddBudgetCommand.COMMAND_WORD,
            SwitchBudgetCommand.COMMAND_WORD,
            // alias
            AddAliasCommand.COMMAND_WORD,
            ListAliasesCommand.COMMAND_WORD,
            DeleteAliasCommand.COMMAND_WORD,
            // general
            HelpCommand.COMMAND_WORD,
            ExitCommand.COMMAND_WORD,
            UndoCommand.COMMAND_WORD,
            RedoCommand.COMMAND_WORD,
            ClearCommand.COMMAND_WORD,
            // ui command
            ViewPanelCommand.COMMAND_WORD
    );

    private Map<String, Alias> aliasNameToAliasMap;

    // Constructors
    public AliasMappings() {
        this.aliasNameToAliasMap = new HashMap<>();
    }

    private AliasMappings(AliasMappings aliasMappings) {
        requireNonNull(aliasMappings);
        this.aliasNameToAliasMap = new HashMap<>(aliasMappings.aliasNameToAliasMap);
    }

    public Alias getAlias(String aliasName) {
        return aliasNameToAliasMap.get(aliasName);
    }

    public List<Alias> getAliases() {
        return List.copyOf(aliasNameToAliasMap.values());
    }

    /**
     * Removes an alias with the specified name if it exists
     * @param name
     * @return True if it existed and was deleted, false otherwise
     */
    public boolean removeAlias(String name) {
        if (!aliasWithNameExists(name)) {
            return false;
        } else {
            aliasNameToAliasMap.remove(name);
            return true;
        }
    }

    /**
     * Returns an {@code AliasMappings} with an added {@code Alias}.
     */
    public AliasMappings addAlias(Alias alias) throws RecursiveAliasException {
        if (aliasCommandWordIsAlias(alias)) {
            throw new RecursiveAliasException(alias);
        }

        AliasMappings aliasMappings = new AliasMappings(this);
        aliasMappings.aliasNameToAliasMap.put(alias.getAliasName(), alias);
        return aliasMappings;
    }

    /**
     * Returns true if an {@code Alias} is mapped to the given {@code String aliasName}, and false otherwise.
     * @param aliasName The alias name to check if it has a mapped {@code Alias}.
     * @return true if an {@code Alias} is mapped to the given {@code String aliasName}, and false otherwise.
     */
    public boolean aliasWithNameExists(String aliasName) {
        return aliasNameToAliasMap.containsKey(aliasName);
    }

    /**
     * Returns true if an alias' name is a reserved command word and false otherwise.
     * @param alias The alias which needs to be checked.
     * @return true if an alias name is a reserved command word and false otherwise.
     */
    public boolean aliasUsesReservedName(Alias alias) {
        String aliasName = alias.getAliasName();
        return RESERVED_COMMAND_WORDS.contains(aliasName);
    }

    /**
     * Returns true if the Alias' command word references another Alias' alias name.
     */
    public boolean aliasCommandWordIsAlias(Alias alias) {
        String commandWord = alias.getCommandWord();
        return aliasWithNameExists(commandWord);
    }

    /**
     * Checks if there are invalid Aliases in the AliasMappings.
     * @throws IllegalValueException if there are invalid Aliases.
     * @throws RecursiveAliasException if there are Aliases which cause recursion.
     */
    public void validate() throws IllegalValueException, RecursiveAliasException {
        for (Map.Entry<String, Alias> entry : aliasNameToAliasMap.entrySet()) {
            Alias a = entry.getValue();
            String aliasName = entry.getKey();
            try {
                ParserUtil.parseAlias(a.getAliasName(), a.getInput());
            } catch (ParseException e) {
                throw new IllegalValueException(INVALID_ALIAS);
            }

            if (aliasName == null) {
                throw new IllegalValueException(NULL_VALUE);
            }

            // alias' key is not alias name
            if (!aliasName.equals(a.getAliasName())) {
                throw new IllegalValueException(NON_MATCHING_KEY);
            }
            // alias name is reserved
            if (aliasUsesReservedName(a)) {
                throw new IllegalValueException(RESERVED_NAME);
            }
        }
        // ensure non recursive
        checkIfRecursive();
    }

    /**
     * Checks if there is an alias in the mappings that may cause recursion when it's input is executed.
     * @throws RecursiveAliasException if it may cause recursion.
     */
    private void checkIfRecursive() throws RecursiveAliasException {
        // ensure that all aliases that are chained do not chain to themselves.
        for (Alias a : aliasNameToAliasMap.values()) {
            Set<String> visited = new HashSet<>();
            String currentAlias = a.getAliasName();
            String nextCommand = a.getCommandWord();
            // while this alias chains to another alias
            while (aliasNameToAliasMap.containsKey(nextCommand)) {
                // if the chain returns to a visited alias
                if (visited.contains(currentAlias)) {
                    throw new RecursiveAliasException(aliasNameToAliasMap.get(currentAlias));
                }
                visited.add(currentAlias);
                currentAlias = nextCommand;
                nextCommand = aliasNameToAliasMap.get(nextCommand).getCommandWord();
            }
        }
    }

    @Override
    public int hashCode() {
        return aliasNameToAliasMap.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AliasMappings)) {
            return false;
        }

        AliasMappings other = (AliasMappings) obj;

        // contains the same keys, for the keys it contains, it maps to the same inputs
        if (!aliasNameToAliasMap.keySet().equals(other.aliasNameToAliasMap.keySet())) {
            return false;
        }
        return aliasNameToAliasMap
                .keySet()
                .stream()
                .allMatch(key -> aliasNameToAliasMap.get(key).equals(other.aliasNameToAliasMap.get(key)));
    }

}
