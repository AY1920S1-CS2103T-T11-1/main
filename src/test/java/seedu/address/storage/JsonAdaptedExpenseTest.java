package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedExpense.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalExpenses.BUSAN_TRIP;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Price;
import seedu.address.model.expense.Timestamp;
import seedu.address.model.expense.UniqueIdentifier;

public class JsonAdaptedExpenseTest {
    private static final String INVALID_DESCRIPTION = "R@chel";
    private static final String INVALID_PRICE = "+651234";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_UNIQUE_IDENTIFIER = "Expense1245";
    private static final String INVALID_TIMESTAMP = "40-20-2019";

    private static final String VALID_DESCRIPTION = BUSAN_TRIP.getDescription().toString();
    private static final String VALID_PRICE = BUSAN_TRIP.getPrice().toString();
    private static final String VALID_CATEGORY = BUSAN_TRIP.getCategory().categoryName;
    private static final String VALID_UNIQUE_IDENTIFIER = BUSAN_TRIP.getUniqueIdentifier().toString();
    private static final String VALID_TIMESTAMP = BUSAN_TRIP.getTimestamp().toString();

    @Test
    public void toModelType_validExpenseDetails_returnsExpense() throws Exception {
        JsonAdaptedExpense expense = new JsonAdaptedExpense(BUSAN_TRIP);
        assertEquals(BUSAN_TRIP, expense.toModelType());
    }

    @Test
    public void toModelType_invalidDescription_throwsIllegalValueException() {
        JsonAdaptedExpense expense =
                new JsonAdaptedExpense(INVALID_DESCRIPTION, VALID_PRICE,
                        VALID_CATEGORY, VALID_UNIQUE_IDENTIFIER, VALID_TIMESTAMP);
        String expectedMessage = Description.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullDescription_throwsIllegalValueException() {
        JsonAdaptedExpense expense =
                new JsonAdaptedExpense(null, VALID_PRICE, VALID_CATEGORY, VALID_UNIQUE_IDENTIFIER, VALID_TIMESTAMP);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_invalidPrice_throwsIllegalValueException() {
        JsonAdaptedExpense expense =
                new JsonAdaptedExpense(VALID_DESCRIPTION, INVALID_PRICE,
                        VALID_CATEGORY, VALID_UNIQUE_IDENTIFIER, VALID_TIMESTAMP);
        String expectedMessage = Price.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullPrice_throwsIllegalValueException() {
        JsonAdaptedExpense expense =
                new JsonAdaptedExpense(VALID_DESCRIPTION, null,
                        VALID_CATEGORY, VALID_UNIQUE_IDENTIFIER, VALID_TIMESTAMP);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Price.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }


    @Test
    public void toModelType_invalidUniqueIdentifier_throwsIllegalValueException() {
        JsonAdaptedExpense expense =
                new JsonAdaptedExpense(VALID_DESCRIPTION, VALID_PRICE,
                        VALID_CATEGORY, INVALID_UNIQUE_IDENTIFIER, VALID_TIMESTAMP);
        String expectedMessage = UniqueIdentifier.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_nullUniqueIdentifier_throwsIllegalValueException() {
        JsonAdaptedExpense expense =
                new JsonAdaptedExpense(VALID_DESCRIPTION, VALID_PRICE,
                        VALID_CATEGORY, VALID_TIMESTAMP, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, UniqueIdentifier.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }

    @Test
    public void toModelType_invalidTimestamp_throwsIllegalValueException() {
        JsonAdaptedExpense expense =
                new JsonAdaptedExpense(VALID_DESCRIPTION, VALID_PRICE,
                        VALID_CATEGORY, INVALID_TIMESTAMP, VALID_UNIQUE_IDENTIFIER);
        String expectedMessage = Timestamp.MESSAGE_CONSTRAINTS_DATE;
        assertThrows(IllegalValueException.class, expectedMessage, expense::toModelType);
    }
}
