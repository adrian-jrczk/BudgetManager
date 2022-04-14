package budgetmanager.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final Locale LOCALE = Locale.getDefault();
    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(LOCALE);

    public static String format(double number) {
       return FORMATTER.format(number);
    }
}
