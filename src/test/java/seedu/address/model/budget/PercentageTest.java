package seedu.address.model.budget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PercentageTest {

    private static final Percentage SAMPLE_PERCENTAGE = new Percentage(33);
    private static final Percentage SAMPLE_THRESHOLD = new Percentage(50);

    @Test
    public void constructor_upperInvalidInput_throwsIllegalArgumentException() {
        int upperInvalidInput = 101;
        assertThrows(IllegalArgumentException.class, () -> new Percentage(upperInvalidInput));
    }

    @Test
    public void constructor_lowerInvalidInput_throwsIllegalArgumentException() {
        int lowerInvalidInput = -1;
        assertThrows(IllegalArgumentException.class, () -> new Percentage(lowerInvalidInput));
    }

    @Test
    public void isValidPercentage() {
        // invalid percentage
        assertFalse(Percentage.isValidPercentage(-1)); // negative
        assertFalse(Percentage.isValidPercentage(101)); // larger than 100

        // valid percentage
        assertTrue(Percentage.isValidPercentage(0)); // smallest non-negative number
        assertTrue(Percentage.isValidPercentage(100)); // largest percentage
        assertTrue(Percentage.isValidPercentage(33)); // normal percentage
    }

    @Test
    public void calculate_validInput_successful() {
        Percentage percentage = Percentage.calculate(57.50, 200);
        assertEquals(new Percentage(29), percentage);
    }

    @Test
    public void calculate_numLargerThanDen_ReturnsFullPercentage() {
        Percentage percentage = Percentage.calculate(300, 200);
        assertEquals(new Percentage(100), percentage);
    }

    @Test
    public void calculate_differentSign_exceptionThrown() {
        try {
            Percentage percentage = Percentage.calculate(-50, 200);
            fail(); // the test should not reach this line
        } catch (IllegalArgumentException e) {
            assertEquals("Numerator and denominator must have same sign", e.getMessage());
        }
    }

    @Test
    public void testReach() {
        // smaller than threshold
        assertFalse(new Percentage(30).reach(SAMPLE_THRESHOLD));

        // larger than threshold
        assertTrue(new Percentage(70).reach(SAMPLE_THRESHOLD));
    }

    @Test
    public void testStringConversion() {
        assertEquals("30%", new Percentage(30).toString());
    }

    @Test
    public void testEquals() {
        //Not a Percentage object
        assertFalse(SAMPLE_PERCENTAGE.equals(""));

        //Same as this object
        assertTrue(SAMPLE_PERCENTAGE.equals(SAMPLE_PERCENTAGE));

        //Percentage with different value
        assertFalse(SAMPLE_PERCENTAGE.equals(new Percentage(40)));

        //Percentage with same value
        assertTrue(SAMPLE_PERCENTAGE.equals(new Percentage(33)));
    }
}
