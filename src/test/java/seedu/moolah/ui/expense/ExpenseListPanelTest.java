package seedu.moolah.ui.expense;

import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static seedu.moolah.testutil.TypicalIndexes.INDEX_SECOND;
import static seedu.moolah.testutil.TypicalMooLah.getTypicalExpenses;
import static seedu.moolah.ui.testutil.GuiTestAssert.assertCardDisplaysExpense;
import static seedu.moolah.ui.testutil.GuiTestAssert.assertCardEqualsExpense;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import guitests.guihandles.expense.ExpenseCardHandle;
import guitests.guihandles.expense.ExpenseListPanelHandle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.moolah.model.expense.Expense;
import seedu.moolah.model.expense.UniqueIdentifier;
import seedu.moolah.model.general.Category;
import seedu.moolah.model.general.Description;
import seedu.moolah.model.general.Price;
import seedu.moolah.model.general.Timestamp;
import seedu.moolah.ui.GuiUnitTest;


/**
 * Contains tests for {@code ExpenseListPanel}.
 *
 * Refactored from:
 * https://github.com/se-edu/addressbook-level4/blob/master/src/test/java/seedu/address/ui/PersonListPanelTest.java
 */
public class ExpenseListPanelTest extends GuiUnitTest {
    private static final ObservableList<Expense> TYPICAL_EXPENSES =
            FXCollections.observableList(getTypicalExpenses());

    private static final long CARD_CREATION_AND_DELETION_TIMEOUT = 3500;

    private final SimpleObjectProperty<Expense> selectedExpense = new SimpleObjectProperty<>();
    private ExpenseListPanelHandle expenseListPanelHandle;

    @Test
    public void display() {
        initUi(TYPICAL_EXPENSES);

        for (int i = 0; i < TYPICAL_EXPENSES.size(); i++) {
            expenseListPanelHandle.navigateToCard(TYPICAL_EXPENSES.get(i));
            Expense expectedExpense = TYPICAL_EXPENSES.get(i);
            ExpenseCardHandle actualCard = expenseListPanelHandle.getCardHandle(i);

            assertCardDisplaysExpense(expectedExpense, actualCard);
            assertEquals(Integer.toString(i + 1), actualCard.getIndex());
        }
    }

    @Test
    public void selection_modelSelectedExpenseChanged_selectionChanges() {
        initUi(TYPICAL_EXPENSES);
        Expense secondExpense = TYPICAL_EXPENSES.get(INDEX_SECOND.getZeroBased());
        guiRobot.interact(() -> {
            selectedExpense.set(secondExpense);
            expenseListPanelHandle.select(INDEX_SECOND.getZeroBased());
        });

        guiRobot.pauseForHuman();

        ExpenseCardHandle expectedExpense = expenseListPanelHandle.getCardHandle(INDEX_SECOND.getZeroBased());
        ExpenseCardHandle selectedExpense = expenseListPanelHandle.getHandleToSelectedCard();
        assertCardEqualsExpense(expectedExpense, selectedExpense);
    }

    /**
     * Verifies that creating and deleting large number of expenses in {@code ExpenseListPanel} requires lesser than
     * {@code CARD_CREATION_AND_DELETION_TIMEOUT} milliseconds to execute.
     */
    @Test
    public void performanceTest() {
        ObservableList<Expense> backingList = createBackingList(10000);

        assertTimeoutPreemptively(ofMillis(CARD_CREATION_AND_DELETION_TIMEOUT), () -> {
            initUi(backingList);
            guiRobot.interact(backingList::clear);
        }, "Creation and deletion of expense cards exceeded time limit");
    }

    /**
     * Returns a list of expenses containing {@code expenseCount} expenses that is used to populate the
     * {@code ExpenseListPanel}.
     */
    private ObservableList<Expense> createBackingList(int expenseCount) {
        ObservableList<Expense> backingList = FXCollections.observableArrayList();
        for (int i = 0; i < expenseCount; i++) {
            Description name = new Description(i + "a");
            Description budgetName = new Description("budgey");
            Price price = new Price("1");
            Category category = new Category("FOOD");
            UniqueIdentifier uniqueIdentifier =
                    new UniqueIdentifier(String.format("Expense@00000000-0000-0000-0000-0000000%5d", expenseCount));
            Timestamp timestamp = new Timestamp(LocalDateTime.of(2019, 11, 1, 0, 0));
            backingList.add(new Expense(name, price, category, timestamp, budgetName, uniqueIdentifier));
        }
        return backingList;
    }

    /**
     * Initializes {@code expenseListPanelHandle} with a {@code ExpenseListPanel} backed by {@code backingList}.
     * Also shows the {@code Stage} that displays only {@code ExpenseListPanel}.
     */
    private void initUi(ObservableList<Expense> backingList) {
        ExpenseListPanel listPanel =
                new ExpenseListPanel(backingList, true);

        uiPartExtension.setUiPart(listPanel);
        expenseListPanelHandle = new ExpenseListPanelHandle(getChildNode(listPanel.getRoot(),
                ExpenseListPanelHandle.EXPENSE_LIST_VIEW_ID));
    }
}
