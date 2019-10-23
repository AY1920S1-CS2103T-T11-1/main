package seedu.address.model.expense;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.budget.Budget;
import seedu.address.model.tag.Tag;

/**
 * Represents a Expense in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Expense {

    // identity fields
    private final UniqueIdentifier uniqueIdentifier;
    // data fields
    private final Description description;
    private final Price price;
    private final Timestamp timestamp;
    private Description budgetName;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Expense(Description description, Price price, Set<Tag> tags, UniqueIdentifier uniqueIdentifier) {
        requireAllNonNull(description, price, tags, uniqueIdentifier);
        this.description = description;
        this.price = price;
        this.uniqueIdentifier = uniqueIdentifier;
        this.tags.addAll(tags);
        this.timestamp = Timestamp.getCurrentTimestamp();
        this.budgetName = null;
    }

    public Expense(Description description, Price price, Set<Tag> tags, Timestamp timestamp, Description budgetName,
                   UniqueIdentifier uniqueIdentifier) {
        requireAllNonNull(description, price, tags, timestamp, budgetName, uniqueIdentifier);
        this.description = description;
        this.price = price;
        this.uniqueIdentifier = uniqueIdentifier;
        this.tags.addAll(tags);
        this.timestamp = timestamp;
        this.budgetName = budgetName;
    }

    public Description getDescription() {
        return description;
    }

    public Price getPrice() {
        return price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public UniqueIdentifier getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public Description getBudgetName() {
        return budgetName;
    }

    public void setBudget(Budget budget) {
        this.budgetName = budget.getDescription();
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both expenses of the same unique identifier.
     * This defines a weaker notion of equality between two expenses.
     */
    public boolean isSameExpense(Expense otherExpense) {
        if (otherExpense == this) {
            return true;
        }

        return otherExpense != null
                && otherExpense.getUniqueIdentifier().equals(getUniqueIdentifier());
    }

    /**
     * Returns true if both expenses have the same unique identifier and data fields.
     * This defines a stronger notion of equality between two expenses.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Expense)) {
            return false;
        }

        Expense otherExpense = (Expense) other;
        return otherExpense.uniqueIdentifier.equals(uniqueIdentifier)
                && otherExpense.description.equals(description)
                && otherExpense.price.equals(price)
                && otherExpense.timestamp.equals(timestamp)
                && otherExpense.tags.equals(tags)
                && otherExpense.budgetName.equals(budgetName);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(description, price, timestamp, budgetName, tags, uniqueIdentifier);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Expense: ")
                .append(getDescription())
                .append(" Price: ")
                .append(getPrice())
                .append(" Date: ")
                .append(getTimestamp())
                .append(" [Tags: ");
        getTags().forEach(builder::append);
        builder.append("]");
        builder.append("Timestamp: ")
                .append(getTimestamp());
        return builder.toString();
    }
}
