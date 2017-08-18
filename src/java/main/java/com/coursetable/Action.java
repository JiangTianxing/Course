package com.coursetable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Action {

    private static int start = 0;

    /**
     * 插入
     * @param data
     * @return
     */
    public static boolean insertCourseData(List<Map<String, String>> data) {
        boolean status = true;
        PreparedStatement statement = null;
        try {
            Connection connection = DataBase.getConnection();
            String sql = "insert into course(teacher, name, location, time, type, courseId, stuId) values(?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            for (Map<String, String> item : data) {
                statement.setString(1, item.get("teacher"));
                statement.setString(2, item.get("name"));
                statement.setString(3, item.get("location"));
                statement.setString(4, item.get("time"));
                statement.setString(5, item.get("type"));
                statement.setString(6, item.get("courseId"));
                statement.setString(7, item.get("stuId"));
                statement.addBatch();
            }
            statement.executeBatch();
            statement.clearBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBase.close(statement);
        }
        return status;
    }

    /**
     * 查询
     * @return
     */
    public static List<String> selectStuId() {
        List<String> stuIds = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = DataBase.getConnection();
            statement = connection.createStatement();
            StringBuilder temp = new StringBuilder();
            //每次查找100行
            synchronized (Action.class) {
                temp.append("select stuId from student where id between ")
                        .append(start)
                        .append(" and ")
                        .append((start += 100) - 1);
            }
            String sql = temp.toString();
            resultSet = statement.executeQuery(sql);
            stuIds = new ArrayList<>();
            while (resultSet.next()) {
                String stuId = resultSet.getString("stuId");
                stuIds.add(stuId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBase.close(resultSet, statement);
        }
        return stuIds;
    }
}
