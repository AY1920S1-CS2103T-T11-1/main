package seedu.address.model.statistics;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.collections.ObservableList;
import seedu.address.model.category.Category;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Timestamp;

/**
 * Represents the Statistics class that provides a PieChart as its Visual Representation method
 */
public class PieChartStatistics extends Statistics {


    private List<String> formattedCategories;

    private List<Double> formattedPercentages;

    private List<Category> usedCategories;

    private Timestamp startDate;

    private Timestamp endDate;

    private ObservableList<Expense> expenses;

    private PieChartStatistics(ObservableList<Expense> expenses, List<Category> validCategories,
                               Timestamp startDate, Timestamp endDate) {
        super(expenses, validCategories);
        this.startDate = startDate;
        this.endDate = endDate;
        this.expenses = getExpenses();
    }

    /**
     * Creates a PieChartStatistics object with all the required information filled in its attributes
     * @param expenses List of expenses
     * @param validCategories List of allowed categories in MooLah
     * @param startDate The start date of the tracking period
     * @param endDate The end date of the tracking period
     */
    public static PieChartStatistics run(ObservableList<Expense> expenses, List<Category>
            validCategories, Timestamp startDate, Timestamp endDate) {

        PieChartStatistics statistics = new PieChartStatistics(expenses, validCategories, startDate, endDate);
        statistics.generatePieChartData();
        return statistics;
    }


    /**
     * Gathers the data to be used for the elements of the PieChart
     */
    private void generatePieChartData() {
        requireNonNull(startDate);
        requireNonNull(endDate);
        this.usedCategories = collateUsedCategories(expenses);


        ArrayList<ArrayList<Expense>> expensesInCategories = extractRelevantExpenses(startDate, endDate);

        String title = String.format("Statistics Summary from %s to %s\n", startDate, endDate);

        ArrayList<Double> percentages = new ArrayList<>();
        ArrayList<Integer> numberOfEntries = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        for (Category category : usedCategories) {
            percentages.add(0.0);
            numberOfEntries.add(0);
            names.add(category.getCategoryName());
        }

        generatePercentages(expensesInCategories, percentages, numberOfEntries, names, title);


    }

    /**
     * Extracts the expenses that are between the 2 dates
     *
     * @return A list of categorised expenses according to their categories
     */
    private ArrayList<ArrayList<Expense>> extractRelevantExpenses(Timestamp startDate, Timestamp endDate) {
        ArrayList<ArrayList<Expense>> expensesInCategories = new ArrayList<>();

        for (int i = 0; i < usedCategories.size(); i++) {
            expensesInCategories.add(new ArrayList<>());
        }

        for (Expense expense : expenses) {
            Timestamp date = expense.getTimestamp();
            if (date.compareTo(startDate) != -1 && date.compareTo(endDate) != 1) {
                int index = usedCategories.indexOf(expense.getCategory());
                expensesInCategories.get(index).add(expense);
            }
        }
        return expensesInCategories;
    }

    /**
     * Returns a list of categories used among all expenses. Meant for PieChart usage
     */
    private static List<Category> collateUsedCategories(ObservableList<Expense> expenses) {
        Set<Category> categories = new HashSet<>();
        for (Expense expense: expenses) {
            categories.add(expense.getCategory());
        }

        List<Category> result = new ArrayList<>(categories);
        return result;
    }

    /**
     * Fills in the data to be passed to a GUI
     * @param data Expenses grouped together under their Categories
     * @param percentages List of all percentages under each category
     * @param numberOfEntries List of number of entries under each category
     * @param names List of all names to be shown in the legend representing the category
     * @param titleWithPeriod String containing the period of time the statistics is taken
     */
    private void generatePercentages(ArrayList<ArrayList<Expense>> data, ArrayList<Double> percentages,
                                     ArrayList<Integer> numberOfEntries, ArrayList<String> names,
                                     String titleWithPeriod) {
        double totalAmount = 0.0;

        for (int i = 0; i < usedCategories.size(); i++) {
            ArrayList<Expense> categoryStats = data.get(i);
            for (Expense expense : categoryStats) {
                double oldCategoricalTotal = percentages.get(i);
                double price = Double.parseDouble(expense.getPrice().value);
                percentages.set(i, oldCategoricalTotal + price);
                totalAmount += price;
                int oldNumberOfEntries = numberOfEntries.get(i);
                numberOfEntries.set(i, oldNumberOfEntries + 1);
            }
        }


        for (int i = 0; i < usedCategories.size(); i++) {
            double categoricalTotal = percentages.get(i);
            double roundedResult = Math.round(categoricalTotal * 10000 / totalAmount) / 100.0;
            percentages.set(i, roundedResult);
            String oldName = names.get(i);
            names.set(i, String.format("%s(%.2f%%)", oldName, roundedResult));
        }

        this.formattedCategories = names;
        this.formattedPercentages = percentages;
        setTitle(String.format("%s\nTotal amount: $%.2f", titleWithPeriod, totalAmount));
    }

    /**
     * Returns the formatted validCategories to be used as labels for the PieChart
     * @return
     */
    public List<String> getFormattedCategories() {
        return formattedCategories;
    }

    public List<Double> getFormattedPercentages() {
        return formattedPercentages;
    }
}






