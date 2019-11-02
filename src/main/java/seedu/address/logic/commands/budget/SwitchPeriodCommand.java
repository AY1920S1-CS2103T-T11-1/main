package seedu.address.logic.commands.budget;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.UndoableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Timestamp;
import seedu.address.ui.budget.BudgetPanel;

/**
 * Switches budget window to a period in the past.
 */
public class SwitchPeriodCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "switchperiod";
    public static final String COMMAND_DESCRIPTION = "Switch budget to period %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches the budget to a past period anchored by "
            + "the specified date and displays a list of expenses under this budget during that period.\n"
            + "Parameters: "
            + PREFIX_TIMESTAMP + "DATE \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TIMESTAMP + "05-01-2019";

    public static final String MESSAGE_SWITCH_PERIOD_SUCCESS = "Budget window switched back to the period "
            + "anchored by: %1$s";

    private final Timestamp pastDate;

    public SwitchPeriodCommand(Timestamp pastDate) {
        this.pastDate = pastDate;
    }

    @Override
    public String getDescription() {
        return String.format(COMMAND_DESCRIPTION, pastDate);
    }

    @Override
    protected void validate(Model model) throws CommandException {
        // No validation necessary.
        Budget currentPeriod = Budget.deepCopy(model.getPrimaryBudget());
        currentPeriod.normalize(Timestamp.getCurrentTimestamp());

        if (pastDate.dateIsAfter(currentPeriod.getEndDate())) {
            throw new CommandException("You cannot switch to a period in the future.");
        }
    }

    @Override
    protected CommandResult execute(Model model) {
        requireNonNull(model);

        model.changePrimaryBudgetWindow(pastDate);
        return new CommandResult(
                String.format(MESSAGE_SWITCH_PERIOD_SUCCESS, pastDate),
                BudgetPanel.PANEL_NAME);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SwitchPeriodCommand // instanceof handles nulls
                && pastDate.equals(((SwitchPeriodCommand) other).pastDate)); // state check
    }
}
