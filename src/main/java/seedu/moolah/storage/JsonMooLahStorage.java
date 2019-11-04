package seedu.moolah.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.moolah.commons.core.LogsCenter;
import seedu.moolah.commons.exceptions.DataConversionException;
import seedu.moolah.commons.exceptions.IllegalValueException;
import seedu.moolah.commons.util.FileUtil;
import seedu.moolah.commons.util.JsonUtil;
import seedu.moolah.model.ReadOnlyMooLah;

/**
 * A class to access MooLah data stored as a json file on the hard disk.
 */
public class JsonMooLahStorage implements MooLahStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonMooLahStorage.class);

    private Path filePath;

    public JsonMooLahStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getMooLahFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyMooLah> readMooLah() throws DataConversionException {
        return readMooLah(filePath);
    }

    /**
     * Similar to {@link #readMooLah()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyMooLah> readMooLah(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableMooLah> jsonMooLah = JsonUtil.readJsonFile(
                filePath, JsonSerializableMooLah.class);
        if (!jsonMooLah.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonMooLah.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveMooLah(ReadOnlyMooLah mooLah) throws IOException {
        saveMooLah(mooLah, filePath);
    }

    /**
     * Similar to {@link #saveMooLah(ReadOnlyMooLah)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveMooLah(ReadOnlyMooLah mooLah, Path filePath) throws IOException {
        requireNonNull(mooLah);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableMooLah(mooLah), filePath);
    }

}
