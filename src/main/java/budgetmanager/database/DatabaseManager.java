package budgetmanager.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DATABASE_URL = "jdbc:h2:" + System.getProperty("user.home") + "\\.budgetmanager\\database";
    private final String USER = "11admiN";
    private final String PASSWORD = "11passworD";

    private Connection connection;

    public DatabaseManager() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            connection.setAutoCommit(true);
            initializeTables();
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            System.out.println("Could not establish connection to database");
            System.exit(0);
        }
    }

    private void initializeTables() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.INITIALIZE_BALANCE_TABLE);
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement(QueryStrings.INITIALIZE_BALANCE_VALUE);
        preparedStatement.execute();
        preparedStatement = connection.prepareStatement(QueryStrings.INITIALIZE_PURCHASES_TABLE);
        preparedStatement.execute();
    }

    public void addPurchase(Purchase purchase) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.ADD_PURCHASE);
        preparedStatement.setString(1, purchase.getName());
        preparedStatement.setDouble(2, purchase.getPrice());
        preparedStatement.setInt(3, purchase.getQuantity());
        preparedStatement.setString(4, purchase.getCategory());
        preparedStatement.setDate(5, purchase.getDate());
        preparedStatement.execute();
        updateBalance(getCurrentBalance() - (purchase.getPrice() * purchase.getQuantity()));
    }

    public void addIncome(double income) throws SQLException {
        double balance = getCurrentBalance();
        PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.UPDATE_BALANCE);
        preparedStatement.setDouble(1, income + balance);
        preparedStatement.execute();
    }

    public void updateBalance(double newValue) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.UPDATE_BALANCE);
        preparedStatement.setDouble(1, newValue);
        preparedStatement.execute();
    }

    public double getCurrentBalance() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.GET_BALANCE);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getDouble(1);
    }

    public List<Purchase> getPurchases(String category, String orderType, String timeScope, int... dateNumbers) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append(QueryStrings.COMPONENT_GET_PURCHASES);

        boolean timeScopeSpecified = true;
        switch (timeScope) {
            case "last-week" -> queryBuilder.append(QueryStrings.COMPONENT_LAST_WEEK);
            case "month" -> queryBuilder.append(String.format(QueryStrings.COMPONENT_BY_MONTH, dateNumbers[1], dateNumbers[0])); //year first, month second
            case "year" -> queryBuilder.append(String.format(QueryStrings.COMPONENT_BY_YEAR, dateNumbers[0]));
            case "all" -> timeScopeSpecified = false;
        }

        if (!category.equals("all")) {
            if (timeScopeSpecified) {
                queryBuilder.append("AND ");
            }
            queryBuilder.append(String.format(QueryStrings.COMPONENT_CATEGORY, category));
        }

        int basicQueryLength = QueryStrings.COMPONENT_GET_PURCHASES.length();
        if (queryBuilder.length() != basicQueryLength) {
            queryBuilder.replace(basicQueryLength, basicQueryLength, "WHERE ");
        }

        switch (orderType) {
            case "oldest" -> queryBuilder.append(QueryStrings.COMPONENT_ORDER_DATE_ASC);
            case "recent" -> queryBuilder.append(QueryStrings.COMPONENT_ORDER_DATE_DESC);
            case "cheapest" -> queryBuilder.append(QueryStrings.COMPONENT_ORDER_PRICE_ASC);
            case "expensive" -> queryBuilder.append(QueryStrings.COMPONENT_ORDER_PRICE_DESC);
        }

        PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());
        return collectPurchasesFromQuery(preparedStatement.executeQuery());
    }

    private List<Purchase> collectPurchasesFromQuery(ResultSet resultSet) throws SQLException {
        List<Purchase> purchases = new ArrayList<>();
        while (resultSet.next()) {
            purchases.add(
                    new Purchase(
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getInt(4),
                            resultSet.getString(5),
                            resultSet.getDate(6)
                    ));
        }
        return purchases;
    }

    public void deletePurchase(String name, Date date) throws SQLException {
        double purchaseCost = getPurchaseCostByDateAndName(name, date);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.DELETE_PURCHASE);
        preparedStatement.setDate(1, date);
        preparedStatement.setString(2, name);
        preparedStatement.execute();
        updateBalance(getCurrentBalance() + purchaseCost);
    }

    double getPurchaseCostByDateAndName(String name, Date date) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(QueryStrings.GET_PURCHASE_BY_DATE_AND_NAME);
        preparedStatement.setDate(1, date);
        preparedStatement.setString(2, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            throw new SQLException("Given purchase doesn't exists");
        }
        return resultSet.getDouble(3) * resultSet.getInt(4);
    }
}
