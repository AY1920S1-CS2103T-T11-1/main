package seedu.moolah.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.moolah.commons.core.GuiSettings;
import seedu.moolah.commons.core.LogsCenter;
import seedu.moolah.logic.commands.Command;
import seedu.moolah.logic.commands.CommandResult;
import seedu.moolah.logic.commands.exceptions.CommandException;
import seedu.moolah.logic.commands.expense.AddExpenseCommand;
import seedu.moolah.logic.parser.MooLahParser;
import seedu.moolah.logic.parser.exceptions.ParseException;
import seedu.moolah.model.Model;
import seedu.moolah.model.ReadOnlyMooLah;
import seedu.moolah.model.alias.AliasMappings;
import seedu.moolah.model.budget.Budget;
import seedu.moolah.model.event.Event;
import seedu.moolah.model.expense.Expense;
import seedu.moolah.model.general.Description;
import seedu.moolah.model.statistics.Statistics;
import seedu.moolah.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final MooLahParser mooLahParser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        mooLahParser = new MooLahParser();
    }

    /**
     * Executes the command entered by the user.
     *
     * @param commandText The command as entered by the user.
     * @param commandGroup The command group pertaining to the input.
     * @return Returns a command result produced from running the command.
     * @throws CommandException If the command fails to run.
     * @throws ParseException If the command fails to parse.
     */
    @Override
    public CommandResult execute(String commandText, String commandGroup) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = mooLahParser.parseCommand(commandText, commandGroup, model.getUserPrefs());
        commandResult = command.run(model);

        save();

        return commandResult;
    }

    /**
     * Saves any changes in MooLah or in user preferences into Storage.
     *
     * @throws CommandException If there is an IO error.
     */
    public void save() throws CommandException {
        try {
            storage.saveMooLah(model.getMooLah());
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }
    }

    @Override
    public boolean hasBudgetWithName(Description targetDescription) {
        return model.hasBudgetWithName(targetDescription);
    }

    @Override
    public Statistics getStatistics() {
        return model.getStatistics();
    }

    @Override
    public ReadOnlyMooLah getMooLah() {
        return model.getMooLah();
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return model.getFilteredExpenseList();
    }

    @Override
    public ObservableList<Budget> getFilteredBudgetList() {
        return model.getFilteredBudgetList();
    }

    public ObservableList<Event> getFilteredEventList() {
        return model.getFilteredEventList();
    }

    /**
     * Deletes a transpired event from the model.
     *
     * @param event The event to be deleted.
     */
    @Override
    public void deleteTranspiredEvent(Event event) {
        model.deleteEvent(event);

        try {
            save();
        } catch (CommandException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an expense corresponding to a transpired event.
     *
     * @param event The event which the expense corresponds to.
     * @return A command result produced from running the adding of the expense.
     * @throws CommandException If the command fails to run.
     */
    @Override
    public CommandResult addExpenseFromEvent(Event event) throws CommandException {
        Expense toBeAdded = event.convertToExpense();
        Command addExpenseCommand = new AddExpenseCommand(toBeAdded);
        CommandResult commandResult = addExpenseCommand.run(model);

        save();

        return commandResult;
    }

    @Override
    public Budget getPrimaryBudget() {
        return model.getPrimaryBudget();
    }

    @Override
    public Path getMooLahFilePath() {
        return model.getMooLahFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public AliasMappings getAliasMappings() {
        return model.getAliasMappings();
    }

    @Override
    public boolean deleteAliasWithName(String aliasName) {
        return model.removeAliasWithName(aliasName);
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    /**
     * Records initial status of primary budget before executing a command.
     *
     * @return A boolean array containing initial {@code isHalf}, {@code isNear}, {@code isExceeded}
     * flags of the primary budget.
     */
    @Override
    public boolean[] recordInitialPrimaryBudgetStatus() {
        Budget primaryBudget = getPrimaryBudget();
        boolean initialIsExceeded = primaryBudget.isExceeded();
        boolean initialIsNear = primaryBudget.isNear();
        boolean initialIsHalf = primaryBudget.isHalf();
        return new boolean[]{initialIsExceeded, initialIsNear, initialIsHalf};
    }

    /**
     * Records final status of primary budget after executing a command.
     *
     * @return A boolean array containing final {@code isHalf}, {@code isNear}, {@code isExceeded}
     * flags of the primary budget.
     */
    @Override
    public boolean[] recordFinalPrimaryBudgetStatus() {
        Budget primaryBudget = getPrimaryBudget();
        boolean finalIsExceeded = primaryBudget.isExceeded();
        boolean finalIsNear = primaryBudget.isNear();
        boolean finalIsHalf = primaryBudget.isHalf();
        return new boolean[]{finalIsExceeded, finalIsNear, finalIsHalf};
    }
}
