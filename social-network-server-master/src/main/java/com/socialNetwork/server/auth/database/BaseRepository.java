package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.utils.ConstantLogger;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository {
    private final DatabaseConnectionProvider connectionProvider;

    protected BaseRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    protected boolean executeUpdate(Logger logger, String sql, Object... params) {//object...params זה פשוט דרך להגיד שזה מתודה שמקבלת מס' לא ידוע של פרמטרים זה בעצם מערך של אובצ'קט שאתה לא יודע כמה נכנס אליו
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    protected <T> T queryOne(Logger logger, String sql, ResultSetMapper<T> mapper, Object... params) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapper.map(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return null;
        }
    }

    protected <T> List<T> queryList(Logger logger, String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> result = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }

        return result;
    }

    protected boolean exists(Logger logger, String sql, Object... params) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    protected Integer queryInt(Logger logger, String sql, Object... params) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setParameters(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return null;
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return null;
        }
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException { //זה בעצם הכנסה של הפרמטרים של אובצ'קט בתוך הסימני שאלה של השאילתה שאנחנו שולחים למסד נתונים לדוגמה: יש שאילתה עם ? אחד אז מערך הפרמטרים יהיה באורך 1 לכן נציב ב- statment.setObject(1,param)
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}