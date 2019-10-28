package seedu.address.model.budget;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.budget.exceptions.BudgetNotFoundException;
import seedu.address.model.budget.exceptions.DeleteDefaultBudgetException;
import seedu.address.model.budget.exceptions.DuplicateBudgetException;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Timestamp;

/**
 * A list of budgets that enforces uniqueness between its elements and does not allow nulls.
 * A budget is considered unique by comparing using {@code Budget#isSameBudget(Budget)}.
 * As such, adding and updating of budgets use Budget#isSameBudget(Budget) for equality so
 * as to ensure that the budget being added or updated is unique in terms of identity in
 * the UniqueBudgetList. However, the removal of a budget uses Budget#equals(Object) so
 * as to ensure that the budget with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Budget#isSameBudget(Budget)
 */
public class UniqueBudgetList implements Iterable<Budget> {

    private final ObservableList<Budget> internalList = FXCollections.observableArrayList();
    private final ObservableList<Budget> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent budget as the given argument.
     */
    public boolean contains(Budget toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameBudget);
    }

    /**
     * Adds a budget to the list.
     * The budget must not already exist in the list.
     */
    public void add(Budget toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateBudgetException();
        }
        toAdd.normalize(Timestamp.getCurrentTimestamp());
        internalList.add(toAdd);
        setPrimary(toAdd);
    }

    /**
     * Adds a budget to MooLah when reconstructing MooLah from json storage file.
     */
    public void addBudgetFromStorage(Budget toAdd) {
        if (contains(toAdd)) {
            throw new DuplicateBudgetException();
        }
        internalList.add(toAdd);
        if (internalList.size() > 1) {
            getDefaultBudget().setNotPrimary();
        }
    }


    public void setBudgets(UniqueBudgetList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    public void setBudgets(List<Budget> budgets) {
        requireAllNonNull(budgets);
        if (!budgetsAreUnique(budgets)) {
            throw new DuplicateBudgetException();
        }

        internalList.setAll(budgets);
    }

    public void setPrimary(Budget budget) {
        requireAllNonNull(budget);
        if (budget.isPrimary()) {
            return;
        }
        for (Budget b : internalList) {
            if (b.isPrimary()) {
                Budget b1 = Budget.deepCopy(b);
                b1.setNotPrimary();
                setBudget(b, b1);
            }
        }
        Budget b1 = Budget.deepCopy(budget);
        b1.setPrimary();
        setBudget(budget, b1);
    }

    public Budget getPrimaryBudget() {
        Budget primaryBudget = null;
        for (int i = 0; i < internalList.size(); i++) {
            Budget b = internalList.get(i);
            if (b.isPrimary()) {
                primaryBudget = b;
                break;
            }
        }
        return primaryBudget;
    }

    public void changePrimaryBudgetWindow(Timestamp pastDate) {
        Budget copy = Budget.deepCopy(getPrimaryBudget());
        copy.normalize(pastDate);
        copy.updateProportionUsed();
        setBudget(getPrimaryBudget(), copy);
    }

    public boolean hasBudgetWithName(Description targetDescription) {
        return internalList.stream().anyMatch(b -> b.getDescription().equals(targetDescription));
    }

    public Budget getBudgetWithName(Description targetDescription) {
        Budget targetBudget = null;
        for (int i = 0; i < internalList.size(); i++) {
            Budget b = internalList.get(i);
            if (b.getDescription().equals(targetDescription)) {
                targetBudget = b;
                break;
            }
        }
        return targetBudget;
    }

    private Budget getDefaultBudget() {
        return getBudgetWithName(new Description("default budget"));
    }

    /**
     * Removes a budget from this unique budget list.
     */
    public void remove(Budget toRemove) {
        requireNonNull(toRemove);
        if (toRemove.isSameBudget(getDefaultBudget())) {
            throw new DeleteDefaultBudgetException();
        }
        if (!internalList.remove(toRemove)) {
            throw new BudgetNotFoundException();
        }
        if (toRemove.isPrimary()) {
            setPrimary(getDefaultBudget());
        }
    }

    public boolean isEmpty() {
        return internalList.size() == 0;
    }

    public ObservableList<Budget> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Budget> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueBudgetList // instanceof handles nulls
                && internalList.equals(((UniqueBudgetList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code budgets} contains only unique budgets.
     */
    private boolean budgetsAreUnique(List<Budget> budgets) {
        for (int i = 0; i < budgets.size() - 1; i++) {
            for (int j = i + 1; j < budgets.size(); j++) {
                if (budgets.get(i).isSameBudget(budgets.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setBudget(Budget target, Budget editedBudget) {
        requireAllNonNull(target, editedBudget);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new BudgetNotFoundException();
        }
        if (!target.isSameBudget(editedBudget) && contains(editedBudget)) {
            throw new DuplicateBudgetException();
        }
        internalList.set(index, editedBudget);
    }
}
