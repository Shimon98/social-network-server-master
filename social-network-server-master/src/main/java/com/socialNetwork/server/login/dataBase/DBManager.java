package com.socialNetwork.server.login.dataBase;

import com.socialNetwork.server.login.config.DatabaseConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DBManager {

    private Connection connection;

    @PostConstruct
    public void connect()  {
        try {
            this.connection= DatabaseConfig.getConnection();
            System.out.println("DB connected successfully");
        } catch (SQLException e) {
            System.out.println("Failed to create db connection");
            e.printStackTrace();
        }
    }


}
