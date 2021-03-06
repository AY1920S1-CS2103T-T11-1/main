package seedu.moolah.logic.commands.budget;

import static java.util.Objects.requireNonNull;
import static seedu.moolah.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.moolah.model.budget.Budget.DEFAULT_BUDGET_DESCRIPTION;

import seedu.moolah.commons.core.Messages;
import seedu.moolah.logic.commands.CommandGroup;
import seedu.moolah.logic.commands.CommandResult;
import seedu.moolah.logic.commands.GenericCommandWord;
import seedu.moolah.logic.commands.UndoableCommand;
import seedu.moolah.logic.commands.exceptions.CommandException;
import seedu.moolah.model.Model;
import seedu.moolah.model.general.Description;
import seedu.moolah.ui.budget.BudgetListPanel;

/**
 * Deletes a budget identified by name.
 */
public class DeleteBudgetByNameCommand extends UndoableCommand {

    public static final String COMMAND_WORD = GenericCommandWord.DELETE + CommandGroup.BUDGET;
    public static final String COMMAND_DESCRIPTION = "Delete budget with name %1$s";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the budget identified by the name.\n"
            + "Parameters: "
            + PREFIX_DESCRIPTION + "DESCRIPTION \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DESCRIPTION + "Holiday";

    public static final String MESSAGE_DELETE_BUDGET_SUCCESS = "Deleted Budget:\n %1$s";

    private final Description description;

    /**
     * Creates an DeleteBudgetByNameCommand to delete the budget with the specified {@code description}.
     *
     * @param description The description of the budget to be deleted.
     */
    public DeleteBudgetByNameCommand(Description description) {
        requireNonNull(description);
        this.description = description;
    }

    /**
     * Returns a description of this DeleteBudgetByNameCommand.
     *
     * @return A string that describes this DeleteBudgetByNameCommand.
     */
    @Override
    public String getDescription() {
        return String.format(COMMAND_DESCRIPTION, description);
    }

    /**
     * Validates this DeleteBudgetByNameCommand with the current model, before execution.
     *
     * @param model The current model.
     * @throws CommandException If the budget does not exist, or if the budget to delete is default budget.
     */
    @Override
    protected void validate(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.hasBudgetWithName(description)) {
            throw new CommandException(Messages.MESSAGE_BUDGET_NOT_FOUND);
        }

        if (description.equals(DEFAULT_BUDGET_DESCRIPTION)) {
            throw new CommandException(Messages.MESSAGE_CANNOT_DELETE_DEFAULT_BUDGET);
        }
    }

    /**
     * Executes this DeleteBudgetByNameCommand with the current model.
     *
     * @param model {@code Model} which the command should operate on.
     * @return A CommandResult consisting of success message and panel change request.
     */
    @Override
    protected CommandResult execute(Model model) {
        requireNonNull(model);

        model.deleteBudgetWithName(description);
        return new CommandResult(String.format(MESSAGE_DELETE_BUDGET_SUCCESS, description),
                BudgetListPanel.PANEL_NAME);
    }

    /**
     * Checks whether another object is identical to this DeleteBudgetByNameCommand.
     *
     * @param other The other object to be compared.
     * @return True if the other object is a DeleteBudgetByNameCommand with the same description, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteBudgetByNameCommand // instanceof handles nulls
                && description.equals(((DeleteBudgetByNameCommand) other).description));
    }
}
