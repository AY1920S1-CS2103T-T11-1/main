package seedu.address.logic.commands.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_CATEGORY_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_DESCRIPTION_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_PRICE_BUFFET;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EVENT_TIMESTAMP_BIRTHDAY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TestUtil.makeModelStack;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.address.testutil.TypicalMooLah.getTypicalMooLah;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.event.EditEventCommand.EditEventDescriptor;
import seedu.address.logic.commands.general.ClearCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelHistory;
import seedu.address.model.ModelManager;
import seedu.address.model.MooLah;
import seedu.address.model.UserPrefs;
import seedu.address.model.expense.Event;
import seedu.address.testutil.EditEventDescriptorBuilder;
import seedu.address.testutil.EventBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditEventCommandTest {

    private Model model = new ModelManager(getTypicalMooLah(), new UserPrefs(), new ModelHistory());

    @Test
    public void run_allFieldsSpecifiedUnfilteredList_success() {
        Event editedEvent = new EventBuilder().build();
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder(editedEvent).build();
        EditEventCommand editEventCommand = new EditEventCommand(INDEX_FIRST, descriptor);

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
                new UserPrefs(), new ModelHistory());
        expectedModel.setEvent(model.getFilteredEventList().get(0), editedEvent);
        expectedModel.setModelHistory(new ModelHistory("", makeModelStack(model), makeModelStack()));

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void run_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastEvent = Index.fromOneBased(model.getFilteredEventList().size());
        Event lastEvent = model.getFilteredEventList().get(indexLastEvent.getZeroBased());

        EventBuilder eventInList = new EventBuilder(lastEvent);
        Event editedEvent = eventInList
                .withDescription(VALID_EVENT_DESCRIPTION_BUFFET)
                .withPrice(VALID_EVENT_PRICE_BUFFET)
                .withCategory(VALID_EVENT_CATEGORY_BUFFET)
                .withTimestamp(VALID_EVENT_TIMESTAMP_BIRTHDAY).build();

        EditEventDescriptor descriptor = new EditEventDescriptorBuilder()
                .withDescription(VALID_EVENT_DESCRIPTION_BUFFET)
                .withPrice(VALID_EVENT_PRICE_BUFFET)
                .withCategory(VALID_EVENT_CATEGORY_BUFFET)
                .withTimestamp(VALID_EVENT_TIMESTAMP_BIRTHDAY).build();
        EditEventCommand editEventCommand = new EditEventCommand(indexLastEvent, descriptor);

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
                new UserPrefs(), new ModelHistory());
        expectedModel.setEvent(lastEvent, editedEvent);
        expectedModel.setModelHistory(new ModelHistory("", makeModelStack(model), makeModelStack()));

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void run_noFieldSpecifiedUnfilteredList_success() {
        EditEventCommand editEventCommand =
                new EditEventCommand(INDEX_FIRST, new EditEventDescriptor());
        Event editedEvent = model.getFilteredEventList().get(INDEX_FIRST.getZeroBased());

        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);

        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
                new UserPrefs(), new ModelHistory());
        expectedModel.commitModel("");

        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    }

    //    @Test
    //    public void run_filteredList_success() {
    //        showEventAtIndex(model, INDEX_FIRST_EVENT);
    //
    //        Event eventInFilteredList = model
    //                .getFilteredEventList()
    //                .get(INDEX_FIRST_EVENT.getZeroBased());
    //        Event editedEvent = new EventBuilder(eventInFilteredList)
    //                .withDescription(VALID_DESCRIPTION_TRANSPORT).build();
    //        EditEventCommand editEventCommand = new EditEventCommand(INDEX_FIRST_EVENT,
    //                new EditEventDescriptorBuilder()
    //                        .withDescription(VALID_DESCRIPTION_TRANSPORT).build());
    //
    //        String expectedMessage = String.format(EditEventCommand.MESSAGE_EDIT_EVENT_SUCCESS, editedEvent);
    //
    //        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
    //                new UserPrefs(), new ModelHistory());
    //        expectedModel.setEvent(model.getFilteredEventList().get(0), editedEvent);
    //        expectedModel.setModelHistory(new ModelHistory("", makeModelStack(model), makeModelStack()));
    //
    //        assertCommandSuccess(editEventCommand, model, expectedMessage, expectedModel);
    //    }

    // Editing an event to have the same details as another should not result in failure

    @Test
    public void run_invalidEventIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredEventList().size() + 1);
        EditEventDescriptor descriptor = new EditEventDescriptorBuilder()
                .withDescription(VALID_EVENT_DESCRIPTION_BUFFET).build();
        EditEventCommand editEventCommand = new EditEventCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    }

    //    /**
    //     * Edit filtered list where index is larger than size of filtered list,
    //     * but smaller than size of address book
    //     */
    //    @Test
    //    public void run_invalidEventIndexFilteredList_failure() {
    //        showEventAtIndex(model, INDEX_FIRST_EVENT);
    //        Index outOfBoundIndex = INDEX_SECOND_EVENT;
    //        // ensures that outOfBoundIndex is still in bounds of address book list
    //        assertTrue(outOfBoundIndex.getZeroBased() < model.getMooLah().getEventList().size());
    //
    //        EditEventCommand editEventCommand = new EditEventCommand(outOfBoundIndex,
    //                new EditEventDescriptorBuilder().withDescription(VALID_DESCRIPTION_TRANSPORT).build());
    //
    //        assertCommandFailure(editEventCommand, model, Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
    //    }

    @Test
    public void equals() {
        final EditEventCommand standardCommand = new EditEventCommand(INDEX_FIRST, DESC_BUFFET);

        // same values -> returns true
        EditEventDescriptor copyDescriptor = new EditEventDescriptor(DESC_BUFFET);
        EditEventCommand commandWithSameValues = new EditEventCommand(INDEX_FIRST, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditEventCommand(INDEX_SECOND, DESC_BUFFET)));
    }

}