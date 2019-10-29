package seedu.address.model.expense;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import seedu.address.model.Timekeeper;

/**
 * Represents an Expense's timestamp in the MooLah.
 * Guarantees: immutable; is valid as declared in {@link #createTimestampIfValid(String)}
 */
public class Timestamp implements Comparable<Timestamp> {

    public static final int CURRENT_YEAR = LocalDate.now().getYear();

    public static final String MESSAGE_CONSTRAINTS_PERIOD =
            "Input period is not day/week/month/year";

    public static final String MESSAGE_CONSTRAINTS_DATE =
            "Timestamps must be in the format dd-MM[-yyyy]";

    private static final DateTimeFormatter FORMATTER_WITH_YEAR =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

    private static final int MONTH_CHANGE = 1;

    private static final DateTimeFormatter FORMATTER_WITHOUT_YEAR =
            new DateTimeFormatterBuilder()
                    .appendPattern("dd-MM")
                    .parseDefaulting(ChronoField.YEAR, CURRENT_YEAR)
                    .toFormatter(Locale.ENGLISH);

    private static final Pattern DDMM_PATTERN =
            Pattern.compile("(?<=\\b)(?<dd>[0-9]{1,2})(?<div1>[\\\\\\-\\/])(?<mm>[0-9]{1,2})");

    public final LocalDateTime fullTimestamp;

    public Timestamp(LocalDateTime fullTimestamp) {
        requireAllNonNull(fullTimestamp);
        this.fullTimestamp = fullTimestamp;
    }


    /**
     * Constructs a Timestamp from a raw date String,
     * only if the date conforms to the format of dd-MM[-yyyy].
     *
     * @param rawTimestamp A String inputted by the user in the date field.
     * @return An Optional Timestamp that will contain a Timestamp
     * if the date given is of the valid format.
     */
    public static Optional<Timestamp> createTimestampIfValid(String rawTimestamp) {
        Matcher m = DDMM_PATTERN.matcher(rawTimestamp);
        if (m.find()) {
            rawTimestamp = m.replaceFirst("$3$2$1");
        }
        System.out.println("after catching DDMM: " + rawTimestamp);
        try {
            Parser parser = new Parser();
            List<DateGroup> groups = parser.parse(rawTimestamp);
            DateGroup group = groups.get(0);
            Date datetime = group.getDates().get(0);
            LocalDateTime fullTimestamp = Timekeeper.convertToLocalDateTime(datetime);
            System.out.println(fullTimestamp);
            return Optional.of(new Timestamp(fullTimestamp));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    /**
     * Constructs a Timestamp from a raw date String,
     * only if the date conforms to the format of dd-MM[-yyyy].
     *
     * @param rawTimestamp A String inputted by the user in the date field.
     * @return An Optional Timestamp that will contain a Timestamp
     * if the date given is of the valid format.
     */
    public static Optional<Timestamp> createTimestampFromStorage(String rawTimestamp) {
        try {
            Parser parser = new Parser();
            List<DateGroup> groups = parser.parse(rawTimestamp);
            DateGroup group = groups.get(0);
            Date datetime = group.getDates().get(0);
            LocalDateTime fullTimestamp = Timekeeper.convertToLocalDateTime(datetime);

            return Optional.of(new Timestamp(fullTimestamp));
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    public LocalDateTime getFullTimestamp() {
        return fullTimestamp;
    }

    public LocalDate getDate() {
        return fullTimestamp.toLocalDate();
    }

    public Timestamp toStartOfDay() {
        return new Timestamp(fullTimestamp.toLocalDate().atStartOfDay());
    }

    public boolean isBefore(Timestamp other) {
        return this.fullTimestamp.isBefore(other.fullTimestamp);
    }

    public boolean isAfter(Timestamp other) {
        return this.fullTimestamp.isAfter(other.fullTimestamp);
    }

    public Timestamp createBackwardTimestamp() {
        return new Timestamp(this.fullTimestamp.minusMonths(MONTH_CHANGE));
    }

    public Timestamp createForwardTimestamp() {
        return new Timestamp(this.fullTimestamp.plusMonths(MONTH_CHANGE));
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(LocalDateTime.now());
    }

    public Timestamp plus(Period period) {
        return new Timestamp(fullTimestamp.plus(period));
    }

    public Timestamp plusDays(long numDays) {
        return new Timestamp(fullTimestamp.plusDays(numDays));
    }

    public Timestamp minusDays(long numDays) {
        return new Timestamp(fullTimestamp.minusDays(numDays));
    }

    public int getDayOfMonth() {
        return fullTimestamp.getDayOfMonth();
    }

    public int getDayOfYear() {
        return fullTimestamp.getDayOfYear();
    }

    public int getMonthValue() {
        return fullTimestamp.getMonthValue();
    }

    public boolean isEqual(Timestamp startDate) {
        return fullTimestamp.isEqual(startDate.getFullTimestamp());
    }

    @Override
    public String toString() {
        return fullTimestamp.format(FORMATTER_WITH_YEAR);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Timestamp // instanceof handles nulls
                && fullTimestamp.equals(((Timestamp) other).fullTimestamp)); // state check
    }

    @Override
    public int hashCode() {
        return fullTimestamp.hashCode();
    }

    @Override
    public int compareTo(Timestamp other) {
        if (this.fullTimestamp.isBefore(other.fullTimestamp)) {
            return -1;
        }
        if (this.fullTimestamp.isAfter(other.fullTimestamp)) {
            return 1;
        }
        return 0;
    }
}
