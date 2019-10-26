package seedu.address.testutil;

import seedu.address.model.category.Category;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Price;
import seedu.address.model.expense.Timestamp;
import seedu.address.model.expense.UniqueIdentifier;

/**
 * A utility class to help with building Expense objects.
 */
public class ExpenseBuilder {

    public static final String DEFAULT_DESCRIPTION = "Alices Birthday";
    public static final String DEFAULT_PRICE = "20";
    public static final String DEFAULT_UNIQUE_IDENTIFIER = "Expense@00000000-0000-0000-0000-000000000001";
    public static final String DEFAULT_CATEGORY = "healthcare";
    public static final String DEFAULT_TIMESTAMP = "01-12-2019";
    public static final String DEFAULT_BUDGET = "Default Budget";

    private Description description;
    private Price price;
    private Category category;
    private UniqueIdentifier uniqueIdentifier;
    private Timestamp timestamp;
    private Description budgetName;

    public ExpenseBuilder() {
        description = new Description(DEFAULT_DESCRIPTION);
        price = new Price(DEFAULT_PRICE);
        category = new Category(DEFAULT_CATEGORY);
        uniqueIdentifier = new UniqueIdentifier(DEFAULT_UNIQUE_IDENTIFIER);
        timestamp = Timestamp.createTimestampIfValid(DEFAULT_TIMESTAMP).get();
        budgetName = new Description(DEFAULT_BUDGET);
    }

    /**
     * Initializes the ExpenseBuilder with the data of {@code expenseToCopy}.
     */
    public ExpenseBuilder(Expense expenseToCopy) {
        description = expenseToCopy.getDescription();
        price = expenseToCopy.getPrice();
        category = expenseToCopy.getCategory();
        uniqueIdentifier = expenseToCopy.getUniqueIdentifier();
        timestamp = expenseToCopy.getTimestamp();
        budgetName = expenseToCopy.getBudgetName();
    }

    /**
     * Sets the {@code Description} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    /**
     * Sets the {@code Category} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withCategory(String category) {
        this.category = new Category(category);
        return this;
    }

    /**
     * Sets the {@code Price} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withPrice(String price) {
        this.price = new Price(price);
        return this;
    }

    /**
     * Sets the {@code UniqueIdentifier} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = new UniqueIdentifier(uniqueIdentifier);
        return this;
    }

    /**
     * Sets the {@code Timestamp} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withTimestamp(String rawTimestamp) {
        this.timestamp = Timestamp.createTimestampIfValid(rawTimestamp).get();
        return this;
    }

    /**
     * Sets the {@code Timestamp} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withBudgetName(String budgetName) {
        this.budgetName = new Description(budgetName);
        return this;
    }

    public Expense build() {
        return new Expense(description, price, category, timestamp, budgetName, uniqueIdentifier);
    }

}
