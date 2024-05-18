package com.JesacaLin.GrubGoblin_v13.daos;

import com.JesacaLin.GrubGoblin_v13.exception.DaoException;
import com.JesacaLin.GrubGoblin_v13.models.Availability;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Component
public class AvailabilityDAO {
    private JdbcTemplate jdbcTemplate;
    public AvailabilityDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Availability getAvailabilityById (int availabilityId) {
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM availability WHERE availability_id = ?", availabilityId);
            if (rowSet.next()) {
                return mapRowtoAvailability(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return null;
    }
    public List<Availability> getAllAvailabilities() {
        List<Availability> availabilities = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM availability");
            while(rowSet.next()) {
                availabilities.add(mapRowtoAvailability(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return availabilities;
    }
    public Availability createAvailability(Availability availability, int dealId) {
        Availability newAvailability = null;
        String sql = "INSERT INTO availability (day_of_week, start_time, end_time)" + "VALUES (?, ?, ? ) RETURNING availability_id";
        try {
            int availabilityId = jdbcTemplate.queryForObject(sql, int.class, availability.getDayOfWeek(), availability.getStartTime(), availability.getEndTime());
            newAvailability = getAvailabilityById(availabilityId);

            //-------------------LINK DEAL TO AVAILABILITY FOR LINKING TABLE-------------------
            jdbcTemplate.update("INSERT INTO deal_availability (deal_id, availability_id) VALUES (?, ?)", dealId, availabilityId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newAvailability;
    }

    public Availability updateAvailability(Availability availability) {
        Availability updatedAvailability = null;
        String sql = "UPDATE availability SET day_of_week = ?, start_time = ?, end_time = ?";

        try {
            int numOfRows = jdbcTemplate.update(sql, availability.getDayOfWeek(), availability.getStartTime(), availability.getEndTime());
            if (numOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedAvailability = getAvailabilityById(availability.getAvailabilityId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedAvailability;
    }

    public int deleteAvailabilityById(int availabilityId) {
        int numOfRows = 0;
        try {
            numOfRows = jdbcTemplate.update("DELETE FROM availability WHERE availability_id = ?;", availabilityId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numOfRows;
    }

    public Availability mapRowtoAvailability (SqlRowSet rowSet) {
        Availability availability = new Availability();
        availability.setAvailabilityId(rowSet.getInt("availability_id"));
        availability.setDayOfWeek(rowSet.getInt("day_of_week"));
        Time sqlStartTime = rowSet.getTime("start_time");
        if (sqlStartTime != null) {
            availability.setStartTime(sqlStartTime.toLocalTime());
        }
        Time sqlEndTime = rowSet.getTime("end_time");
        if (sqlEndTime != null) {
            availability.setEndTime(sqlEndTime.toLocalTime());
        }
        return availability;
    }
}
