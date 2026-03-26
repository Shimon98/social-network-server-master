package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.config.DatabaseConfig;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionProvider {

    public Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection();
    }
}