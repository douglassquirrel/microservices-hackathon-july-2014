package com.example;

import org.postgresql.jdbc2.optional.SimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.Collection;

final class RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoomRepository() {
        SimpleDataSource dataSource = new SimpleDataSource();
        try {
            dataSource.setUrl("jdbc:postgresql://microservices.cc9uedlzx2lk.eu-west-1.rds.amazonaws.com:5432/micro");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dataSource.setUser("microservices");
        dataSource.setPassword("microservices");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Collection<String> getRooms() {
        return jdbcTemplate.queryForList(
                "SELECT content ->>'room_name' as room_name FROM facts WHERE topic = 'room_created'",
                String.class);
    }
}
