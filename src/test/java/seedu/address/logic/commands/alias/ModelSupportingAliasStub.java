package seedu.address.logic.commands.alias;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyModelHistory;
import seedu.address.model.ReadOnlyMooLah;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.alias.Alias;
import seedu.address.model.alias.AliasMappings;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Event;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Timestamp;
import seedu.address.model.statistics.Statistics;

/**
 * Model Stub which supports Alias methods related to Alias feature.
 */
public class ModelSupportingAliasStub implements Model {

    private AliasMappings aliasMappings;

    ModelSupportingAliasStub() {
        aliasMappings = new AliasMappings();
    }

    @Override
    public void resetData(Model model) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public Model copy() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public ReadOnlyModelHistory getModelHistory() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setModelHistory(ReadOnlyModelHistory history) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public String getLastCommandDesc() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void addToPastHistory(Model model) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void addToFutureHistory(Model model) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void commitModel(String description) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public boolean canRollback() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void rollbackModel() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public boolean canMigrate() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void migrateModel() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public GuiSettings getGuiSettings() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public AliasMappings getAliasMappings() {
        return aliasMappings;
    }

    @Override
    public void setAliasMappings(AliasMappings aliasMappings) {
        this.aliasMappings = aliasMappings;
    }

    @Override
    public void addUserAlias(Alias alias) {
        aliasMappings = aliasMappings.addAlias(alias);
    }

    @Override
    public boolean removeAliasWithName(String aliasName) {
        return aliasMappings.removeAlias(aliasName);
    }

    @Override
    public boolean aliasWithNameExists(String aliasName) {
        return aliasMappings.aliasWithNameExists(aliasName);
    }

    @Override
    public Path getMooLahFilePath() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setMooLahFilePath(Path mooLahFilePath) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public ReadOnlyMooLah getMooLah() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setMooLah(ReadOnlyMooLah mooLah) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public boolean hasExpense(Expense expense) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void deleteExpense(Expense target) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void addExpense(Expense expense) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setExpense(Expense target, Expense editedExpense) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public Predicate<? super Expense> getFilteredExpensePredicate() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void updateFilteredExpenseList(Predicate<? super Expense> predicate) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public boolean hasBudget(Budget budget) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public boolean hasBudgetWithName(Description targetDescription) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public Budget getPrimaryBudget() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public boolean hasPrimaryBudget() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void addBudget(Budget budget) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void switchBudgetTo(Description description) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void deleteBudget(Budget target) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setBudget(Budget target, Budget editedBudget) {

    }

    @Override
    public void changePrimaryBudgetWindow(Timestamp pastDate) {

    }

    @Override
    public ObservableList<Budget> getFilteredBudgetList() {
        return null;
    }

    @Override
    public void updateFilteredBudgetList(Predicate<? super Budget> budget) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public Predicate<? super Budget> getFilteredBudgetPredicate() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void clearBudgets() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void deleteBudgetWithName(Description description) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public ObservableList<Event> getFilteredEventList() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public Predicate<? super Event> getFilteredEventPredicate() {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void updateFilteredEventList(Predicate<? super Event> predicate) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void notifyAboutTranspiredEvents(List<Event> events) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public boolean hasEvent(Event event) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void addEvent(Event event) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void deleteEvent(Event target) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void setEvent(Event eventToEdit, Event editedEvent) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public void calculateStatistics(String command, Timestamp date1, Timestamp date2, boolean isBudget) {
        throw new AssertionError("Method should not be called.");
    }

    @Override
    public Statistics getStatistics() {
        throw new AssertionError("Method should not be called.");
    }
}
