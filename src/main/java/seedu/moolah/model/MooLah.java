package seedu.moolah.model;

import static java.util.Objects.requireNonNull;
import static seedu.moolah.model.budget.Budget.DEFAULT_BUDGET;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.moolah.model.budget.Budget;
import seedu.moolah.model.budget.UniqueBudgetList;
import seedu.moolah.model.event.Event;
import seedu.moolah.model.event.UniqueEventList;
import seedu.moolah.model.expense.Expense;
import seedu.moolah.model.expense.UniqueExpenseList;
import seedu.moolah.model.general.Description;
import seedu.moolah.model.general.Timestamp;

/**
 * Wraps all data at the MooLah level
 * Duplicates are not allowed (by .isSameExpense comparison)
 */
public class MooLah implements ReadOnlyMooLah {

    private final UniqueExpenseList expenses;
    private final UniqueBudgetList budgets;
    private final UniqueEventList events;

    public MooLah() {
        expenses = new UniqueExpenseList();
        budgets = new UniqueBudgetList();
        Budget db = DEFAULT_BUDGET;
        db.setToPrimary();
        budgets.add(db);
        events = new UniqueEventList();
    }

    /**
     * Creates an MooLah using the Expenses in the {@code toBeCopied}
     */
    public MooLah(ReadOnlyMooLah toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    @Override
    public MooLah copy() {
        return new MooLah(this);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     * {@code expenses} must not contain duplicate expenses.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.setExpenses(expenses);
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets.setBudgets(budgets);
    }

    /**
     * Replaces the contents of the event list with {@code events}.
     * {@code events} must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code MooLah} with {@code newData}.
     */
    public void resetData(ReadOnlyMooLah newData) {
        requireNonNull(newData);

        setExpenses(newData.getExpenseList());
        setBudgets(newData.getBudgetList());
        setEvents(newData.getEventList());
    }

    //=========== Expense-level operations =============================================================

    /**
     * Returns true if an expense with the same identity as {@code expense}
     * exists in the MooLah.
     */
    public boolean hasExpense(Expense expense) {
        requireNonNull(expense);
        return expenses.contains(expense);
    }

    /**
     * Adds an expense to the MooLah.
     * The expense must not already exist in the MooLah.
     */
    public void addExpense(Expense p) {
        if (p.getBudgetName() == null) {
            Budget primaryBudget = budgets.getPrimaryBudget();
            Budget copy = primaryBudget.deepCopy();
            p.setBudget(primaryBudget);
            copy.addExpense(p);
            setBudget(primaryBudget, copy);
        } else {
            Budget budget = budgets.getBudgetWithName(p.getBudgetName());
            if (budget != null) {
                Budget copy = budget.deepCopy();
                copy.addExpense(p);
                setBudget(budget, copy);
            }
        }
        expenses.add(p);
    }

    /**
     * Replaces the given expense {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the MooLah.
     * The expense identity of {@code editedExpense} must not be the same as another existing
     * expense in the MooLah.
     */
    public void setExpense(Expense target, Expense editedExpense) {
        requireNonNull(editedExpense);

        expenses.setExpense(target, editedExpense);
        budgets.setExpense(target, editedExpense);
    }

    /**
     * Removes {@code key} from this {@code MooLah}.
     * {@code key} must exist in the MooLah.
     */
    public void removeExpense(Expense key) {
        expenses.remove(key);
        budgets.removeExpense(key);
    }

    //=========== Budget-level operations ================================================================
    /**
     * Returns true if a budget with the same identity as {@code budget}
     * exists in MooLah.
     *
     * @param budget The budget to check.
     * @return True if MooLah contains budget identical to the budget to check.
     */
    public boolean hasBudget(Budget budget) {
        requireNonNull(budget);
        return budgets.contains(budget);
    }

    /**
     * Adds a budget to MooLah.
     * The budget must not already exist in MooLah.
     *
     * @param budget The budget to be added.
     */
    public void addBudget(Budget budget) {
        requireNonNull(budget);
        budgets.add(budget);
    }

    /**
     * Adds a budget to MooLah when reconstructing MooLah from json storage file.
     * The primary status of budget is maintained.
     *
     * @param budget The budget to be added to MooLah.
     */
    public void addBudgetFromStorage(Budget budget) {
        budgets.addBudgetFromStorage(budget);
    }

    /**
     * Checks whether MooLah has a budget with the specified name.
     *
     * @param targetDescription The description (i.e. name) of the budget to check.
     * @return A boolean indicating whether the MooLah has such budget.
     */
    public boolean hasBudgetWithName(Description targetDescription) {
        requireNonNull(targetDescription);
        return budgets.hasBudgetWithName(targetDescription);
    }

    /**
     * Returns the primary budget in the MooLah.
     *
     * @return The primary budget in MooLah.
     */
    public Budget getPrimaryBudget() {
        return budgets.getPrimaryBudget();
    }

    /**
     * Returns the description of the primary budget.
     *
     * @return A string that represents the primary budget name.
     */
    public String getPrimaryBudgetName() {
        return getPrimaryBudget().getDescription().fullDescription;
    }

    /**
     * Sets the budget with the specified name to primary.
     *
     * @param name The specified name in string.
     */
    public void setPrimaryBudget(String name) {
        budgets.setPrimaryFromString(name);
    }

    /**
     * Switches the primary budget to the budget with the specified name.
     *
     * @param targetDescription The name of the budget to be switched to.
     */
    public void switchBudgetTo(Description targetDescription) {
        requireNonNull(targetDescription);
        budgets.switchBudgetTo(targetDescription);
    }

    /**
     * Replaces the target budget with an edited budget.
     *
     * @param target The budget to be replaced.
     * @param editedBudget The updated budget.
     */
    void setBudget(Budget target, Budget editedBudget) {
        requireNonNull(editedBudget);
        budgets.setBudget(target, editedBudget);
    }

    /**
     * Removes Budget {@code key} from this {@code MooLah}.
     * {@code key} must exist in the MooLah.
     *
     * @param key The budget to remove.
     */
    public void removeBudget(Budget key) {
        requireNonNull(key);
        budgets.remove(key);
    }

    /** Clears all budgets in MooLah, except the default budget. */
    public void clearBudgets() {
        budgets.clearBudgets();
    }

    /**
     * Changes budget window of primary budget to a period in the past, as specified by the anchor date.
     *
     * @param pastDate The date to anchor the period.
     */
    public void changePrimaryBudgetWindow(Timestamp pastDate) {
        requireNonNull(pastDate);
        budgets.changePrimaryBudgetWindow(pastDate);
    }

    /**
     * Deletes the budget with the specified description from MooLah.
     *
     * @param description The specified description.
     */
    public void deleteBudgetWithName(Description description) {
        budgets.deleteBudgetWithName(description);
    }

    //=========== Event-level operations ================================================================

    /**
     * Returns true if an event with the same identity as {@code event}
     * exists in the MooLah.
     */
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return events.contains(event);
    }

    /**
     * Adds an event to the MooLah.
     * The event must not already exist in the MooLah.
     */
    public void addEvent(Event event) {
        if (budgets.isEmpty()) {
            Budget defaultBudget = DEFAULT_BUDGET;
            budgets.add(defaultBudget);
        }
        Budget primaryBudget = budgets.getPrimaryBudget();
        if (event.getBudgetName() == null) {
            event.setBudget(primaryBudget);
        }
        events.add(event);
    }

    /**
     * Removes {@code key} from this {@code MooLah}.
     * {@code key} must exist in the MooLah.
     */
    public void removeEvent(Event key) {
        events.remove(key);
    }
    //// util methods

    /**
     * Replaces the given event {@code target} in the list with {@code editedEvent}.
     * {@code target} must exist in MooLah.
     * The event identity of {@code editedEvent} must not be the same as another existing
     * expense in the MooLah.
     */
    public void setEvent(Event target, Event editedEvent) {
        requireNonNull(editedEvent);

        events.setEvent(target, editedEvent);
    }

    @Override
    public String toString() {
        return expenses.asUnmodifiableObservableList().size() + " expenses";
        // TODO: refine later
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Budget> getBudgetList() {
        return budgets.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Event> getEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MooLah // instanceof handles nulls
                && expenses.equals(((MooLah) other).expenses)
                && events.equals(((MooLah) other).events)
                && budgets.equals(((MooLah) other).budgets));
    }

    @Override
    public int hashCode() {
        return expenses.hashCode();
    }
}
