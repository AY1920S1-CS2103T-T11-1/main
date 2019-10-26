package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.Alias;
import seedu.address.commons.core.AliasMappings;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.RecursiveAliasException;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Event;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Timestamp;
import seedu.address.model.statistics.Statistics;

/**
 * Represents the in-memory model of the MooLah data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final MooLah mooLah;
    private final UserPrefs userPrefs;
    private final ModelHistory modelHistory;
    private final FilteredList<Expense> filteredExpenses;
    private final FilteredList<Event> filteredEvents;
    //private final FilteredList<Budget> filteredBudgets;
    private StringBuilder statsBuilder;

    /**
     * Initializes a ModelManager with the given mooLah and userPrefs.
     */
    public ModelManager(ReadOnlyMooLah mooLah, ReadOnlyUserPrefs userPrefs,
                        ReadOnlyModelHistory modelHistory) {
        requireAllNonNull(mooLah, userPrefs, modelHistory);

        logger.fine("Initializing with MooLah: " + mooLah + " and user prefs " + userPrefs);

        this.mooLah = new MooLah(mooLah);
        this.userPrefs = new UserPrefs(userPrefs);
        this.modelHistory = new ModelHistory(modelHistory);
        filteredEvents = new FilteredList<>(this.mooLah.getEventList());
        filteredExpenses = new FilteredList<>(this.mooLah.getExpenseList());
        //filteredBudgets = new FilteredList<>(this.mooLah.getBudgetList());
    }

    public ModelManager() {
        this(new MooLah(), new UserPrefs(), new ModelHistory());
    }

    /**
     * Copy constructor for ModelManager.
     */
    public ModelManager(Model model) {
        this();
        resetData(model);
    }

    @Override
    public void resetData(Model model) {
        requireNonNull(model);
        setMooLah(model.getMooLah());
        setUserPrefs(model.getUserPrefs());
        setModelHistory(model.getModelHistory());

        if (model.getFilteredEventPredicate() != null) {
            updateFilteredEventList(model.getFilteredEventPredicate());
        } else {
            updateFilteredEventList(model.PREDICATE_SHOW_ALL_EVENTS);
        }

        if (model.getFilteredExpensePredicate() != null) {
            updateFilteredExpenseList(model.getFilteredExpensePredicate());
        } else {
            updateFilteredExpenseList(model.PREDICATE_SHOW_ALL_EXPENSES);
        }

        //if (model.getFilteredBudgetList() != null) {
        //  updateFilteredBudgetList(model.getFilteredBudgetPredicate());
        //} else {
        //  updateFilteredBudgetList(model.PREDICATE_SHOW_ALL_BUDGETS);
        //}
    }

    //=========== ModelHistory ==================================================================================

    @Override
    public ReadOnlyModelHistory getModelHistory() {
        return modelHistory;
    }

    @Override
    public void setModelHistory(ReadOnlyModelHistory modelHistory) {
        requireNonNull(modelHistory);
        this.modelHistory.resetData(modelHistory);
    }

    @Override
    public void addToHistory() {
        modelHistory.addToPastModels(new ModelManager(this));
        modelHistory.clearFutureModels();
    }

    @Override
    public void addToPastHistory(Model model) {
        requireNonNull(model);
        modelHistory.addToPastModels(new ModelManager(model));
    }

    @Override
    public void addToFutureHistory(Model model) {
        requireNonNull(model);
        modelHistory.addToFutureModels(new ModelManager(model));
    }

    @Override
    public boolean canRollback() {
        return !modelHistory.isPastModelsEmpty();
    }

    @Override
    public Optional<Model> rollbackModel() {
        Optional<Model> prevModel = modelHistory.getPrevModel();
        if (prevModel.isEmpty()) {
            return Optional.empty();
        }

        Model pastModel = prevModel.get();
        pastModel.addToFutureHistory(this);
        return Optional.of(pastModel);
    }

    @Override
    public boolean canMigrate() {
        return !modelHistory.isFutureModelsEmpty();
    }

    @Override
    public Optional<Model> migrateModel() {
        Optional<Model> nextModel = modelHistory.getNextModel();
        if (nextModel.isEmpty()) {
            return Optional.empty();
        }

        Model futureModel = nextModel.get();
        futureModel.addToPastHistory(this);
        return Optional.of(futureModel);
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public Path getMooLahFilePath() {
        return userPrefs.getMooLahFilePath();
    }

    @Override
    public void setMooLahFilePath(Path mooLahFilePath) {
        requireNonNull(mooLahFilePath);
        userPrefs.setMooLahFilePath(mooLahFilePath);
    }

    //=========== AliasSettings ==============================================================================

    @Override
    public AliasMappings getAliasMappings() {
        return userPrefs.getAliasMappings();
    }

    @Override
    public void setAliasMappings(AliasMappings aliasMappings) {
        requireNonNull(aliasMappings);
        userPrefs.setAliasMappings(aliasMappings);
    }

    @Override
    public void addUserAlias(Alias alias) {
        try {
            userPrefs.addUserAlias(alias);
        } catch (RecursiveAliasException e) {
            // should should be prevented by validation
            e.printStackTrace();
        }
    }

    //=========== GuiSettings ===============================================================================

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    //=========== Expense ================================================================================

    @Override
    public ReadOnlyMooLah getMooLah() {
        return mooLah;
    }

    @Override
    public void setMooLah(ReadOnlyMooLah mooLah) {
        requireNonNull(mooLah);
        this.mooLah.resetData(mooLah);
    }

    @Override
    public boolean hasExpense(Expense expense) {
        requireNonNull(expense);
        return mooLah.hasExpense(expense);
    }

    @Override
    public void deleteExpense(Expense target) {
        mooLah.removeExpense(target);
    }

    @Override
    public void addExpense(Expense expense) {
        mooLah.addExpense(expense);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
    }

    @Override
    public void setExpense(Expense target, Expense editedExpense) {
        requireAllNonNull(target, editedExpense);

        mooLah.setExpense(target, editedExpense);
    }

    //=========== Budget ================================================================================

    @Override
    public boolean hasBudget(Budget budget) {
        requireNonNull(budget);
        return mooLah.hasBudget(budget);
    }

    @Override
    public void addBudget(Budget budget) {
        mooLah.addBudget(budget);
    }

    @Override
    public boolean hasBudgetWithName(Description targetDescription) {
        return mooLah.hasBudgetWithName(targetDescription);
    }

    @Override
    public Budget getPrimaryBudget() {
        return mooLah.getPrimaryBudget();
    }

    @Override
    public void switchBudgetTo(Description targetDescription) {
        mooLah.switchBudgetTo(targetDescription);
    }

    //=========== Event ================================================================================

    @Override
    public void notifyAboutTranspiredEvents(List<Event> events) {

    }
    @Override
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return mooLah.hasEvent(event);
    }

    @Override
    public void deleteEvent(Event target) {
        mooLah.removeEvent(target);
    }

    @Override
    public void addEvent(Event event) {
        mooLah.addEvent(event);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
    }



    //=========== Statistics ================================================================================

    @Override
    public Statistics calculateStatistics(String command, Timestamp date1, Timestamp date2, Period period) {
        ObservableList<Expense> statsExpenses = getFilteredExpenseList();
        return Statistics.calculateStats(statsExpenses, command, date1, date2, period);
    }

    @Override
    public boolean hasStatistic() {
        return statsBuilder == null;
    }

    @Override
    public StringBuilder getStatistic() {
        return statsBuilder;
    }



    //=========== Filtered Expense List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of
     * {@code versionedMooLah}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenses;
    }

    @Override
    public Predicate<? super Expense> getFilteredExpensePredicate() {
        return filteredExpenses.getPredicate();
    }

    @Override
    public void updateFilteredExpenseList(Predicate<? super Expense> predicate) {
        requireNonNull(predicate);
        filteredExpenses.setPredicate(predicate);
    }

    //=========== Filtered Event List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of
     * {@code versionedMooLah}
     */
    @Override
    public ObservableList<Event> getFilteredEventList() {
        return filteredEvents;
    }

    @Override
    public Predicate<? super Event> getFilteredEventPredicate() {
        return filteredEvents.getPredicate();
    }

    @Override
    public void updateFilteredEventList(Predicate<? super Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
    }

    //=========== Filtered Budget List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of
     * {@code versionedMooLah}
     */
    //@Override
    //public ObservableList<Budget> getFilteredBudgetList() {
    //return filteredBudgets;
    //}

    //@Override
    //public void updateFilteredBudgetList(Predicate<? super Budget> predicate) {
    //  requireNonNull(predicate);
    //  filteredBudgets.setPredicate(predicate);
    //  }

    //@Override
    //public Predicate<? super Budget> getFilteredBudgetPredicate() {
    //   return filteredBudgets.getPredicate();
    //}

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return mooLah.equals(other.mooLah)
                && userPrefs.equals(other.userPrefs)
                && filteredExpenses.equals(other.filteredExpenses)
                && filteredEvents.equals(other.filteredEvents)
                && modelHistory.equals(other.modelHistory);
    }
}
