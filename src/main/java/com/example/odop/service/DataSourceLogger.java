package com.example.odop.service;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@Component
public class DataSourceLogger {

    public DataSourceLogger(DataSource dataSource) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            String url = conn.getMetaData().getURL();
            String userName = conn.getMetaData().getUserName();

            log.info("### JDBC URL   = {}", url);
            log.info("### DB USER    = {}", userName);
        }
    }
}
