package budgetmanager.database;

public class QueryStrings {

    static final String INITIALIZE_PURCHASES_TABLE = """
            CREATE TABLE IF NOT EXISTS purchases (
            id INT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(255) NOT NULL,
            price DOUBLE NOT NULL,
            quantity INT NOT NULL,
            category VARCHAR(255) NOT NULL,
            date DATE
            );""";

    static final String INITIALIZE_BALANCE_TABLE = """
            CREATE TABLE IF NOT EXISTS balance (
            id INT PRIMARY KEY AUTO_INCREMENT,
            balance DOUBLE NOT NULL
            );""";

    static final String INITIALIZE_BALANCE_VALUE = """
            INSERT INTO balance (balance)
            SELECT '0'
            WHERE NOT EXISTS (SELECT TOP 1 FROM balance)
            ;""";

    static final String ADD_PURCHASE = """
            INSERT INTO purchases (name, price, quantity, category, date)
            VALUES(?, ?, ?, ?, ?)
            ;""";

    static final String DELETE_PURCHASE = """
            DELETE FROM purchases
            WHERE date = ? AND name = ?
            ;""";

    static final String GET_BALANCE = """
            SELECT balance
            FROM balance
            ;""";

    static final String UPDATE_BALANCE = """
            UPDATE balance
            SET balance = ?
            WHERE id = 1
            ;""";

    static final String GET_PURCHASE_BY_DATE_AND_NAME = """
            SELECT *
            FROM purchases
            WHERE date = ? AND name = ?
            """;

    static final String COMPONENT_GET_PURCHASES = """
            SELECT *
            FROM purchases
            """;

    static final String COMPONENT_LAST_WEEK = """
            DATEDIFF(week, date, CURRENT_DATE) <= 1
            """;

    static final String COMPONENT_BY_MONTH = """
            EXTRACT(YEAR FROM date) = '%d'
            AND EXTRACT(MONTH FROM date) = '%d'
            """;

    static final String COMPONENT_BY_YEAR = """
            EXTRACT(YEAR FROM date) = '%d'
            """;

    static final String COMPONENT_CATEGORY = """
            category = '%s'
            """;

    static final String COMPONENT_ORDER_DATE_ASC = """
            ORDER BY date ASC
            """;

    static final String COMPONENT_ORDER_DATE_DESC = """
            ORDER BY date DESC
            """;

    static final String COMPONENT_ORDER_PRICE_ASC = """
            ORDER BY price ASC
            """;

    static final String COMPONENT_ORDER_PRICE_DESC = """
            ORDER BY price DESC
            """;
}
