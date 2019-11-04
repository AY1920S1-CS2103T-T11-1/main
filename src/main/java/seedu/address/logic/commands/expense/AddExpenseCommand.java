package seedu.address.logic.commands.expense;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import seedu.address.logic.commands.CommandGroup;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.GenericCommandWord;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.expense.Expense;
import seedu.address.ui.budget.BudgetPanel;

/**
 * Adds a expense to the MooLah.
 */
public class AddExpenseCommand extends UndoableCommand {

    public static final String COMMAND_WORD = GenericCommandWord.ADD + CommandGroup.EXPENSE;
    public static final String COMMAND_WORD_FROM_PRIMARY = GenericCommandWord.ADD + CommandGroup.PRIMARY_BUDGET;

    public static final String COMMAND_DESCRIPTION = "Add expense %1$s (%2$s)";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds an expense to MooLah and the primary budget. \n"
            + "Parameters: "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_PRICE + "PRICE "
            + PREFIX_CATEGORY + "CATEGORY "
            + "[" + PREFIX_TIMESTAMP + "TIMESTAMP]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DESCRIPTION + "Chicken Rice "
            + PREFIX_PRICE + "3.50 "
            + PREFIX_CATEGORY + "Food "
            + PREFIX_TIMESTAMP + "10-10";

    public static final String MESSAGE_SUCCESS = "New expense added:\n %1$s";
    public static final String MESSAGE_DUPLICATE_EXPENSE = "This expense already exists in the MooLah";

    private final Expense toAdd;

    /**
     * Creates an AddExpenseCommand to add the specified {@code Expense}
     */
    public AddExpenseCommand(Expense expense) {
        requireNonNull(expense);
        toAdd = expense;
    }

    @Override
    public String getDescription() {
        return String.format(COMMAND_DESCRIPTION, toAdd.getDescription(), toAdd.getPrice());
    }

    @Override
    protected void validate(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasExpense(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_EXPENSE);
        }
    }

    @Override
    protected CommandResult execute(Model model) {
        requireNonNull(model);

        model.addExpense(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd), BudgetPanel.PANEL_NAME);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddExpenseCommand // instanceof handles nulls
                && toAdd.equals(((AddExpenseCommand) other).toAdd));
    }
}
