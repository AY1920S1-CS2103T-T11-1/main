package seedu.moolah.testutil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;

import seedu.moolah.commons.core.index.Index;
import seedu.moolah.model.Model;
import seedu.moolah.model.ModelManager;
import seedu.moolah.model.expense.Expense;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final Path SANDBOX_FOLDER = Paths.get("src", "test", "data", "sandbox");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting path.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static Path getFilePathInSandboxFolder(String fileName) {
        try {
            Files.createDirectories(SANDBOX_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER.resolve(fileName);
    }

    /**
     * Returns the middle index of the expense in the {@code model}'s expense list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getFilteredExpenseList().size() / 2);
    }

    /**
     * Returns the last index of the expense in the {@code model}'s expense list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getFilteredExpenseList().size());
    }

    /**
     * Returns the expense in the {@code model}'s expense list at {@code index}.
     */
    public static Expense getExpense(Model model, Index index) {
        return model.getFilteredExpenseList().get(index.getZeroBased());
    }

    /**
     * Creates a {@code Model} stack populated by the copies of the {@code models} provided.
     */
    public static Stack<Model> makeModelStack(Model... models) {
        Stack<Model> stack = new Stack<>();
        for (Model model : models) {
            stack.push(new ModelManager(model));
        }
        return stack;
    }
}
