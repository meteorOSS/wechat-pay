package com.meteor.wechatpay;

import com.meteor.wechatpay.model.Order;

import java.sql.*;

public class OrderStorage {

    private PluginMain pluginMain;

    private Connection connection;

    private final String createTable = "CREATE TABLE IF NOT EXISTS orders (\n" +
            "    notes TEXT PRIMARY KEY,\n" +
            "    insertTime INTEGER,\n" +
            "    amount REAL\n" +
            ");";

    public OrderStorage(PluginMain pluginMain){
        this.pluginMain = pluginMain;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+pluginMain.getDataFolder().getPath()+"/data.db");
            connection.prepareStatement(createTable).execute();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insert(Order order){
        String sql = "insert into orders values(?,?,?)";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,order.getNotes());
            preparedStatement.setLong(2,System.currentTimeMillis());
            preparedStatement.setDouble(3,order.getAmount());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order queryOrder(String notes){
        String sql = "select * from orders where notes = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,notes);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = new Order();
                order.setNotes(resultSet.getString("notes"));
                order.setTime(resultSet.getLong("insertTime"));
                order.setAmount(resultSet.getDouble("amount"));
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
