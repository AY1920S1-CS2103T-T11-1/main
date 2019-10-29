package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.Period;
import java.util.Optional;

import seedu.address.commons.core.Alias;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.category.Category;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Price;
import seedu.address.model.expense.Timestamp;


/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String description} into a {@code Description}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code description} is invalid.
     */
    public static Description parseDescription(String description) throws ParseException {
        requireNonNull(description);
        String trimmedDescription = description.trim();
        if (!Description.isValidDescription(trimmedDescription)) {
            throw new ParseException(Description.MESSAGE_CONSTRAINTS);
        }
        return new Description(trimmedDescription);
    }

    /**
     * Parses a {@code String price} into a {@code Price}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code price} is invalid.
     */
    public static Price parsePrice(String price) throws ParseException {
        requireNonNull(price);
        String trimmedPrice = price.trim();
        if (!Price.isValidPrice(trimmedPrice)) {
            throw new ParseException(Price.MESSAGE_CONSTRAINTS);
        }
        return new Price(trimmedPrice);
    }

    /**
     * Parses a {@code String category} into a {@code Category}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code category} is invalid.
     */
    public static Category parseCategory(String category) throws ParseException {
        requireNonNull(category);
        String trimmedCategory = category.trim();
        if (!Category.isValidCategoryName(trimmedCategory)) {
            throw new ParseException(Category.MESSAGE_CONSTRAINTS);
        }
        return new Category(trimmedCategory);
    }


    /**
     * Parses {@code String aliasName} and {@code String input} into a user defined {@code Alias}.
     *
     * @throws ParseException if the given {@code aliasName} is invalid.
     */
    public static Alias parseAlias(String aliasName, String input) throws ParseException {
        if (!Alias.isValidAliasName(aliasName)) {
            throw new ParseException(Alias.MESSAGE_NAME_CONSTRAINTS);
        }
        if (!Alias.isValidInput(input)) {
            throw new ParseException(Alias.MESSAGE_INPUT_CONSTRAINTS);
        }
        return new Alias(aliasName, input);
    }

    /**
     * Parses a {@code String timestamp} into a {@code Timestamp}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code timestamp} is invalid.
     */
    public static Timestamp parseTimestamp(String timestamp) throws ParseException {
        requireNonNull(timestamp);
        String trimmedTimestamp = timestamp.trim();
        Optional<Timestamp> potentialTimestamp = Timestamp.createTimestampIfValid(trimmedTimestamp);
        if (potentialTimestamp.isPresent()) {
            return potentialTimestamp.get();
        } else {
            throw new ParseException(Timestamp.MESSAGE_CONSTRAINTS_DATE);
        }
    }

    /**
     * Parses {@code String period} into a {@code Period}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code period} is invalid.
     */
    public static Period parsePeriod(String period) throws ParseException {
        String trimmedPeriod = period.trim();
        switch (trimmedPeriod) {
        case "day":
            return Period.ofDays(1);
        case "week":
            return Period.ofWeeks(1);
        case "month":
            return Period.ofMonths(1);
        case "year":
            return Period.ofYears(1);
        case "infinity":
            return Period.ofYears(999);
        default:
            throw new ParseException(Timestamp.MESSAGE_CONSTRAINTS_PERIOD);
        }
    }

    /**
     * Dummy.
     * @param period
     * @return dummy.
     */

    public static String formatPeriod(Period period) {
        String periodString = period.toString();
        switch (periodString) {
        case "P1D":
            return "day";
        case "P7D":
            //fallthrough
        case "P1W":
            return "week";
        case "P1M":
            return "month";
        case "P1Y":
            return "year";
        case "P999Y":
            return "infinity";
        default:
            return periodString;
        }
    }
}
