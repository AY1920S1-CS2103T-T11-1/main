package seedu.address.model.expense;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.category.Category;

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
    private final Category category;

    /**
     * Every field must be present and not null.
     */
    public Expense(Description description, Price price, Category category, UniqueIdentifier uniqueIdentifier) {
        requireAllNonNull(description, price, category, uniqueIdentifier);
        this.description = description;
        this.price = price;
        this.uniqueIdentifier = uniqueIdentifier;
        this.category = category;
        this.timestamp = Timestamp.getCurrentTimestamp();
    }

    public Expense(Description description, Price price, Category category, Timestamp timestamp,
                   UniqueIdentifier uniqueIdentifier) {
        requireAllNonNull(description, price, category, uniqueIdentifier);
        this.description = description;
        this.price = price;
        this.uniqueIdentifier = uniqueIdentifier;
        this.category = category;
        this.timestamp = timestamp;
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

    public Category getCategory() {
        return category;
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
        return otherExpense.getUniqueIdentifier().equals(getUniqueIdentifier())
                && otherExpense.getDescription().equals(getDescription())
                && otherExpense.getPrice().equals(getPrice())
                && otherExpense.getTimestamp().equals(getTimestamp())

                && otherExpense.getCategory().equals(getCategory());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(description, price, category, uniqueIdentifier);
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
                .append(" Category: ")
                .append(getCategory())
                .append("Timestamp: ")
                .append(getTimestamp());
        return builder.toString();
    }
}
