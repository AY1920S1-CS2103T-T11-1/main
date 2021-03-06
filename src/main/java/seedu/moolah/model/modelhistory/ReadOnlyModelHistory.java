package seedu.moolah.model.modelhistory;

import java.util.Stack;

/**
 * Unmodifiable view of a model history.
 */
public interface ReadOnlyModelHistory {

    /**
     * Creates a read-only copy of the model.
     * @return a copy of the current model.
     */
    ReadOnlyModelHistory copy();

    Stack<ModelChanges> getPastChanges();

    Stack<ModelChanges> getFutureChanges();

}
