package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_REPEATED_PREFIX_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_CATEGORY_DESC_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_DESCRIPTION_DESC_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_PRICE_DESC_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.EVENT_TIMESTAMP_DESC_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_CATEGORY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EVENT_TIMESTAMP_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_CATEGORY_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_DESCRIPTION_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_PRICE_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TIMESTAMP_BUFFET;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.event.AddEventCommand;
import seedu.address.model.category.Category;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Price;
import seedu.address.model.expense.Timestamp;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    // addCommand should never return a same command as another, so cannot check if the command is same as expected

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing description prefix
        assertParseFailure(parser, VALID_EVENT_DESCRIPTION_BUFFET + EVENT_PRICE_DESC_BUFFET
                + EVENT_TIMESTAMP_DESC_BUFFET + EVENT_CATEGORY_DESC_BUFFET, expectedMessage);

        // missing price prefix
        assertParseFailure(parser, EVENT_DESCRIPTION_DESC_BUFFET + VALID_EVENT_PRICE_BUFFET
                + EVENT_TIMESTAMP_DESC_BUFFET + EVENT_CATEGORY_DESC_BUFFET, expectedMessage);

        // missing category prefix
        assertParseFailure(parser, EVENT_DESCRIPTION_DESC_BUFFET + EVENT_PRICE_DESC_BUFFET
                + EVENT_TIMESTAMP_DESC_BUFFET + VALID_EVENT_CATEGORY_BUFFET, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_EVENT_DESCRIPTION_BUFFET + VALID_EVENT_PRICE_BUFFET
                + VALID_EVENT_TIMESTAMP_BUFFET + VALID_EVENT_CATEGORY_BUFFET, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid description
        assertParseFailure(parser,
                INVALID_EVENT_DESCRIPTION_DESC + EVENT_PRICE_DESC_BUFFET
                        + EVENT_TIMESTAMP_DESC_BUFFET + EVENT_CATEGORY_DESC_BUFFET,
                Description.MESSAGE_CONSTRAINTS);

        // invalid price
        assertParseFailure(parser,
                EVENT_DESCRIPTION_DESC_BUFFET + INVALID_EVENT_PRICE_DESC
                        + EVENT_TIMESTAMP_DESC_BUFFET + EVENT_CATEGORY_DESC_BUFFET,
                Price.MESSAGE_CONSTRAINTS);

        // invalid category
        assertParseFailure(parser,
                EVENT_DESCRIPTION_DESC_BUFFET + EVENT_PRICE_DESC_BUFFET
                        + EVENT_TIMESTAMP_DESC_BUFFET + INVALID_EVENT_CATEGORY_DESC,
                Category.MESSAGE_CONSTRAINTS);

        // invalid timestamp
        assertParseFailure(parser,
                EVENT_DESCRIPTION_DESC_BUFFET + EVENT_PRICE_DESC_BUFFET
                        + INVALID_EVENT_TIMESTAMP_DESC + EVENT_CATEGORY_DESC_BUFFET,
                Timestamp.MESSAGE_CONSTRAINTS_GENERAL);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser,
                INVALID_EVENT_DESCRIPTION_DESC + INVALID_EVENT_PRICE_DESC
                        + EVENT_CATEGORY_DESC_BUFFET,
                Description.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + EVENT_DESCRIPTION_DESC_BUFFET + EVENT_PRICE_DESC_BUFFET
                        + EVENT_TIMESTAMP_DESC_BUFFET + EVENT_CATEGORY_DESC_BUFFET,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedPrefix_failure() {
        assertParseFailure(parser, EVENT_DESCRIPTION_DESC_BUFFET + EVENT_PRICE_DESC_BUFFET
                        + EVENT_TIMESTAMP_DESC_BUFFET + EVENT_CATEGORY_DESC_BUFFET + EVENT_CATEGORY_DESC_BUFFET,
                MESSAGE_REPEATED_PREFIX_COMMAND);
    }

}
