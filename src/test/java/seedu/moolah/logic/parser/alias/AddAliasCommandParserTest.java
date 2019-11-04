package seedu.moolah.logic.parser.alias;

import static seedu.moolah.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.moolah.logic.parser.CliSyntax.PREFIX_ALIAS_ALIAS_INPUT;
import static seedu.moolah.logic.parser.CliSyntax.PREFIX_ALIAS_ALIAS_NAME;
import static seedu.moolah.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.moolah.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.moolah.logic.commands.alias.AddAliasCommand;
import seedu.moolah.model.alias.Alias;

public class AddAliasCommandParserTest {
    private AddAliasCommandParser parser = new AddAliasCommandParser();

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAliasCommand.MESSAGE_USAGE);

        // missing alias name and input
        assertParseFailure(parser,
                String.format(" %s %s", PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                expectedMessage);

        // missing alias prefix
        assertParseFailure(parser,
                String.format(" %s", PREFIX_ALIAS_ALIAS_INPUT),
                expectedMessage);

        // missing input prefix
        assertParseFailure(parser,
                String.format(" %s", PREFIX_ALIAS_ALIAS_NAME),
                expectedMessage);

        // missing input
        assertParseFailure(parser,
                String.format(" %s aliasName %s", PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                expectedMessage);

        // missing alias name
        assertParseFailure(parser,
                String.format(" %s %s input here", PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid aliasname
        assertParseFailure(parser,
                String.format(" %s #asd@## %s input here", PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                Alias.MESSAGE_NAME_CONSTRAINTS);
        // multiple words which would be individually valid
        assertParseFailure(parser,
                String.format(" %s multiple words %s input here", PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                Alias.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    void parse_allFieldsPresent_success() {
        // valid alias name and input
        assertParseSuccess(parser,
                String.format(" %s name %s input", PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                new AddAliasCommand(new Alias("name", "input")));
        // valid alias name and multiple word input
        assertParseSuccess(parser,
                String.format(" %s name %s input input", PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                new AddAliasCommand(new Alias("name", "input input")));
    }
}
