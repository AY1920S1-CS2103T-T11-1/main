package seedu.moolah.testutil;

import static seedu.moolah.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.moolah.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.moolah.logic.parser.CliSyntax.PREFIX_PRICE;

import seedu.moolah.logic.commands.alias.AddAliasCommand;
import seedu.moolah.logic.commands.event.AddEventCommand;
import seedu.moolah.logic.commands.expense.AddExpenseCommand;
import seedu.moolah.logic.commands.expense.DeleteExpenseCommand;
import seedu.moolah.logic.commands.expense.EditExpenseCommand;
import seedu.moolah.logic.commands.expense.FindExpenseCommand;
import seedu.moolah.logic.commands.expense.ListExpensesCommand;
import seedu.moolah.logic.commands.general.ClearCommand;
import seedu.moolah.logic.commands.general.ExitCommand;
import seedu.moolah.logic.commands.general.HelpCommand;
import seedu.moolah.model.alias.Alias;
import seedu.moolah.model.alias.AliasMappings;

/**
 * A utility class representing {@code AliasMappings} objects to be used in tests.
 */
public class AliasTestUtil {

    public static final Alias ALIAS_LIST_SHORTCUT = new Alias("ls", ListExpensesCommand.COMMAND_WORD);
    public static final Alias ALIAS_ADD_WITH_ARGUMENTS = new Alias(
            "addchicken",
            AddExpenseCommand.COMMAND_WORD + " "
                    + PREFIX_DESCRIPTION + "chicken "
                    + PREFIX_PRICE + "2.50 "
                    + PREFIX_CATEGORY + "food");
    public static final Alias ALIAS_FIND_SHORTCUT_INCOMPLETE = new Alias ("f", FindExpenseCommand.COMMAND_WORD);
    // for recursive
    public static final Alias ALIAS_A_TO_B = new Alias("a", "b");
    public static final Alias ALIAS_B_TO_C = new Alias("b", "c");
    public static final Alias ALIAS_C_TO_A = new Alias("c", "a");
    //An alias mappings object with valid aliases stored.
    public static final AliasMappings VALID_ALIAS_MAPPINGS;

    private static final String IGNORED_VALUE = "ignored value";
    // for alias is reserved
    public static final Alias ALIAS_NAME_ADD = new Alias(AddEventCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_ALIAS = new Alias(AddAliasCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_CLEAR = new Alias(ClearCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_DELETE = new Alias(DeleteExpenseCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_EDIT = new Alias(EditExpenseCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_EXIT = new Alias(ExitCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_FIND = new Alias(FindExpenseCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_HELP = new Alias(HelpCommand.COMMAND_WORD, IGNORED_VALUE);
    public static final Alias ALIAS_NAME_LIST = new Alias(ListExpensesCommand.COMMAND_WORD, IGNORED_VALUE);

    static {
        AliasMappings tempValidAliasMappings;
        tempValidAliasMappings = new AliasMappings()
                    .addAlias(ALIAS_LIST_SHORTCUT)
                    .addAlias(ALIAS_ADD_WITH_ARGUMENTS)
                    .addAlias(ALIAS_FIND_SHORTCUT_INCOMPLETE)
                    .addAlias(ALIAS_A_TO_B)
                    .addAlias(ALIAS_B_TO_C);

        VALID_ALIAS_MAPPINGS = tempValidAliasMappings;
    }
}
