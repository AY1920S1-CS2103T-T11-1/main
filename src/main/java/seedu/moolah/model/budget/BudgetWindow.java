package seedu.moolah.model.budget;

import static java.util.Objects.requireNonNull;
import static seedu.moolah.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Objects;

import seedu.moolah.model.general.Timestamp;

/**
 * Represents the current budget window, with start and end dates, and a period.
 */
public class BudgetWindow {
    private Timestamp startDate;
    private Timestamp endDate;
    private final BudgetPeriod period;

    /**
     * Constructs a BudgetWindow with a start date and a period.
     *
     * @param startDate The start date of this BudgetWindow.
     * @param period The fixed length of this BudgetWindow.
     */
    public BudgetWindow(Timestamp startDate, BudgetPeriod period) {
        requireAllNonNull(startDate, period);
        this.startDate = startDate.toStartOfDay();
        this.period = period;
        this.endDate = calculateEndDate();
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public Timestamp getEndDate() {
        return this.endDate;
    }

    public BudgetPeriod getBudgetPeriod() {
        return this.period;
    }

    /**
     * Checks if this budget window contains the specified timestamp. That is, the timestamp
     * is between start date and end date, inclusive.
     *
     * @param timestamp The specified timestamp to check against this window.
     * @return True if the window contains the timestamp, false otherwise.
     */
    public boolean contains(Timestamp timestamp) {
        requireNonNull(timestamp);
        return isAfterOrEqualStartDate(timestamp)
                && isBeforeOrEqualEndDate(timestamp);
    }

    /**
     * Checks if a timestamp is after or equal to the start date of this BudgetWindow.
     *
     * @param timestamp The timestamp to be compared with the start date.
     * @return True if the timestamp is after or equal to the start date, false otherwise.
     */
    private boolean isAfterOrEqualStartDate(Timestamp timestamp) {
        requireNonNull(timestamp);
        return timestamp.dateIsAfter(startDate.minusDays(1));
    }

    /**
     * Checks if a timestamp is before or equal to the end date of this BudgetWindow.
     *
     * @param timestamp The timestamp to be compared with the end date.
     * @return True if the timestamp is before or equal to the end date, false otherwise.
     */
    private boolean isBeforeOrEqualEndDate(Timestamp timestamp) {
        requireNonNull(timestamp);
        return timestamp.dateIsBefore(endDate.plusDays(1));
    }

    /**
     * Normalizes this window to a period containing the specified timestamp.
     *
     * @param rawAnchor The timestamp to anchor the period.
     */
    public void normalize(Timestamp rawAnchor) {
        requireNonNull(rawAnchor);
        LocalDateTime anchor = rawAnchor.toStartOfDay().fullTimestamp;
        LocalDateTime normalized;
        int currentDayOfMonth = startDate.getDayOfMonth();

        switch(period) {
        case YEAR:
            int currentDayOfYear = startDate.getDayOfYear();
            int anchorDayOfYear = anchor.getDayOfYear();
            int currentMonth = startDate.getMonthValue();
            normalized = (anchorDayOfYear >= currentDayOfYear)
                    ? withDayOfMonthValid(anchor.withMonth(currentMonth),
                    currentDayOfMonth)
                    : withDayOfMonthValid(anchor.minusYears(1).withMonth(currentMonth),
                    currentDayOfMonth);
            break;
        case MONTH:
            int anchorDayOfMonth = anchor.getDayOfMonth();
            normalized = (anchorDayOfMonth >= currentDayOfMonth)
                    ? withDayOfMonthValid(anchor, currentDayOfMonth)
                    : withDayOfMonthValid(anchor.minusMonths(1), currentDayOfMonth);
            break;
        case WEEK:
            long daysDiff = ChronoUnit.DAYS.between(startDate.getDate(), anchor.toLocalDate());
            long offset = daysDiff % 7 >= 0 ? daysDiff % 7 : daysDiff % 7 + 7;
            normalized = anchor.minusDays(offset);
            break;
        default:
            normalized = anchor;
        }
        startDate = new Timestamp(normalized);
        endDate = calculateEndDate();
    }

    /**
     * Returns a LocalDateTime with specified day of month. If the specified day of month is larger than
     * total number of days in the month, the last day of month is taken instead.
     *
     * @param anchor The original LocalDateTime object.
     * @param dayOfMonth The specified day of month.
     * @return A new LocalDateTime object with the specified day of month, other fields unchanged.
     */
    private LocalDateTime withDayOfMonthValid(LocalDateTime anchor, int dayOfMonth) {
        boolean isLeapYear = isLeapYear(anchor.getYear());
        int anchorMonthLength = anchor.getMonth().length(isLeapYear);
        if (anchorMonthLength < dayOfMonth) {
            return anchor.withDayOfMonth(anchorMonthLength);
        } else {
            return anchor.withDayOfMonth(dayOfMonth);
        }
    }

    /**
     * Checks if a given year is leap year.
     *
     * @param year The year to check.
     * @return True if the year is leap year, false otherwise.
     */
    private boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

    /**
     * Calculates proper end date based on given start date and period.
     *
     * @return A Timestamp representing the end date of this BudgetWindow.
     */
    private Timestamp calculateEndDate() {
        Timestamp endDate = startDate.plus(period.getPeriod());
        if ((period == BudgetPeriod.MONTH || period == BudgetPeriod.YEAR)
                && (endDate.getDayOfMonth() < startDate.getDayOfMonth())) {
            endDate = endDate.plusDays(1);
        }
        return endDate.minusDays(1).toEndOfDay();
    }

    /**
     * Creates a hash code by hashing all relevant attributes of this BudgetWindow object.
     *
     * @return A hash code of this BudgetWindow object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, period);
    }

    /**
     * Checks whether another object is identical to this BudgetWindow.
     *
     * @param other The other object to be compared.
     * @return True if the other object is a BudgetWindow with the same attributes, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof BudgetWindow)) {
            return false;
        }

        BudgetWindow otherWindow = (BudgetWindow) other;
        return otherWindow.startDate.getDate().equals(startDate.getDate())
                && otherWindow.period.equals(period)
                && otherWindow.endDate.getDate().equals(endDate.getDate());
    }

    /**
     * Generates a string representation of this BudgetWindow.
     *
     * @return A string that describes the period, start and end dates.
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Period: ")
                .append(period)
                .append(" Start date: ")
                .append(startDate)
                .append(" End date: ")
                .append(endDate);
        return builder.toString();
    }
}
