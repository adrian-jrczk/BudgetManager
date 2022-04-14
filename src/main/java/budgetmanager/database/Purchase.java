package budgetmanager.database;

import budgetmanager.util.CurrencyFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Purchase {

    private String name;
    private double price;
    private int quantity;
    private String category;
    private Date date;

    @Override
    public String toString() {
        return String.format("| %-40s | %-20s | %-8d | %-13s | %-10s |",
                name, CurrencyFormatter.format(price), quantity, category, date.toString());
    }
}
