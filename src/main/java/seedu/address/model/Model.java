package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Alias;
import seedu.address.commons.core.AliasMappings;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.budget.Budget;
import seedu.address.model.budget.BudgetPeriod;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Event;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Timestamp;
import seedu.address.model.statistics.Statistics;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Expense> PREDICATE_SHOW_ALL_EXPENSES = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Event> PREDICATE_SHOW_ALL_EVENTS = unused -> true;

    Predicate<Budget> PREDICATE_SHOW_ALL_BUDGETS = unused -> true;

    /**
     * Resets data to the given model.
     */
    void resetData(Model model);

    /**
     * Creates a copy of the current model.
     */
    Model copy();

    // ======== MODEL HISTORY ===============

    /**
     * Returns the model history.
     */
    ReadOnlyModelHistory getModelHistory();

    /**
     * Replaces model history with the data in {@code history}.
     */
    void setModelHistory(ReadOnlyModelHistory history);

    /**
     * Returns the description of the last command executed at the time.
     */
    String getLastCommandDesc();

    /**
     * Adds an entry to the past history.
     */
    void addToPastHistory(Model model);

    /**
     * Adds an entry to the future history.
     */
    void addToFutureHistory(Model model);

    /**
     * Commits the current model to the history.
     */
    void commitModel(String description);

    /**
     * Checks whether model can be rolled-back.
     */
    boolean canRollback();

    /**
     * Rolls back model to the immediate previous state.
     */
    void rollbackModel();

    /**
     * Checks whether model can be migrated.
     */
    boolean canMigrate();

    /**
     * Migrates model to the immediate next state.
     */
    void migrateModel();

    // ======== USER PREFS ===============
    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    // ======== GUI SETTINGS ===============

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    // ======== ALIAS SETTINGS ===========
    /**
     * Return's the user prefs' alias mappings.
     */
    AliasMappings getAliasMappings();

    /**
     * Sets the user prefs' alias mappings.
     */
    void setAliasMappings(AliasMappings aliasMappings);

    /**
     * Add a user defined alias to the user prefs' alias mappings.
     */
    void addUserAlias(Alias alias);

    /**
     * Removes an alias with the given name if it exists, and returns true, otherwise return false.
     */
    boolean removeAliasWithName(String aliasName);

    boolean aliasWithNameExists(String aliasName);

    // ======== MOOLAH SETTINGS ===============
    /**
     * Returns the user prefs' MooLah file path.
     */
    Path getMooLahFilePath();

    /**
     * Sets the user prefs' MooLah file path.
     */
    void setMooLahFilePath(Path mooLahFilePath);

    /** Returns the MooLah */
    ReadOnlyMooLah getMooLah();

    /**
     * Replaces MooLah data with the data in {@code mooLah}.
     */
    void setMooLah(ReadOnlyMooLah mooLah);

    // ======== MOOLAH ACTIONS ===============
    // ================================ EXPENSE =======================================
    /**
     * Returns true if a expense with the same identity as {@code expense}
     * exists in the MooLah.
     */
    boolean hasExpense(Expense expense);

    /**
     * Deletes the given expense.
     * The expense must exist in the MooLah.
     */
    void deleteExpense(Expense target);

    /**
     * Adds the given expense.
     * {@code expense} must not already exist in the MooLah.
     */
    void addExpense(Expense expense);

    /**
     * Replaces the given expense {@code target} with {@code editedExpense}.
     * {@code target} must exist in the MooLah.
     * The expense identity of {@code editedExpense} must not be the same as another
     * existing expense in the MooLah.
     */
    void setExpense(Expense target, Expense editedExpense);

    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Expense> getFilteredExpenseList();

    /** Returns the predicate of the filtered expense list. **/
    Predicate<? super Expense> getFilteredExpensePredicate();

    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredExpenseList(Predicate<? super Expense> predicate);

    // ================================ BUDGET =======================================

    boolean hasBudget(Budget budget);

    boolean hasBudgetWithName(Description targetDescription);

    Budget getPrimaryBudget();

    void addBudget(Budget budget);

    void switchBudgetTo(Description description);

    void deleteBudget(Budget target);

    void setBudget(Budget target, Budget editedBudget);

    void changePrimaryBudgetWindow(Timestamp pastDate);

    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Budget> getFilteredBudgetList();

    void updateFilteredBudgetList(Predicate<? super Budget> budget);

    Predicate<? super Budget> getFilteredBudgetPredicate();

    void clearBudgets();

    void deleteBudgetWithName(Description description);

    // ================================ EVENT =======================================
    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Event> getFilteredEventList();

    /** Returns the predicate of the filtered expense list */
    Predicate<? super Event> getFilteredEventPredicate();

    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<? super Event> predicate);

    void notifyAboutTranspiredEvents(List<Event> events);

    boolean hasEvent(Event event);

    void addEvent(Event event);

    void deleteEvent(Event target);

    void setEvent(Event eventToEdit, Event editedEvent);

    // ================================ STATS =======================================

    void calculateStatistics(String command, Timestamp date1, Timestamp date2, BudgetPeriod period, boolean isBudget);

    Statistics getStatistics();

}
