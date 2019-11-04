package seedu.moolah.model.expense;

import static seedu.moolah.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Category(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Category(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null category name
        assertThrows(NullPointerException.class, () -> Category.isValidCategoryName(null));
    }

}
