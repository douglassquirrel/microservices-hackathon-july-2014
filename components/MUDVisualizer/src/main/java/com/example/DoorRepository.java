package com.example;

import org.postgresql.jdbc2.optional.SimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

final class DoorRepository {

    private final JdbcTemplate jdbcTemplate;

    public DoorRepository() {
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

    public List<Door> getDoors() {
        return jdbcTemplate.query(
                "SELECT content ->>'room_one_name' as room_one_name, content ->>'room_two_name' as room_two_name, ts FROM facts WHERE topic = 'door_created' ORDER BY ts DESC",
                new RowMapper<Door>() {
                    @Override
                    public Door mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Date date = rs.getDate(3);
                        return new Door(rs.getString(1), rs.getString(2), new java.util.Date(date.getTime()));
                    }
                });
    }
}
