package com.JesacaLin.GrubGoblin_v13.daos;

import com.JesacaLin.GrubGoblin_v13.exception.DaoException;
import com.JesacaLin.GrubGoblin_v13.models.Place;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlaceDAO {
    private JdbcTemplate jdbcTemplate;
    public PlaceDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Place getPlaceById(int id) {
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM place WHERE place_id = ?", id);
            if (rowSet.next()) {
                return mapRowToPlace(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return null;
    }

    public List<Place> getAllPlaces() {
        List<Place> places = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM place ORDER BY place_id");
            while(rowSet.next()) {
                places.add(mapRowToPlace(rowSet));
            }
            return places;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    public Place createPlace(Place place) {
        Place newPlace = null;
        String sql = "INSERT INTO place (place_name, address, latitude, longitude, google_rating)" + "VALUES (?, ?, ?, ?, ? ) RETURNING place_id";
        try {
            int placeId = jdbcTemplate.queryForObject(sql, int.class, place.getPlaceName(), place.getAddress(), place.getLatitude(), place.getLongitude(), place.getGoogleRating());
            newPlace = getPlaceById(placeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newPlace;
    }

    public Place updatePlace(int id, Place place) {
        Place updatedPlace = null;
        String sql = "UPDATE place SET place_name = ?, address = ?, latitude = ?, longitude = ?, google_rating = ? WHERE place_id = ?";

        try {
            int numOfRows = jdbcTemplate.update(sql, place.getPlaceName(), place.getAddress(), place.getLatitude(), place.getLongitude(), place.getGoogleRating(), place.getPlaceId());
            if (numOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedPlace = getPlaceById(place.getPlaceId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedPlace;
    }

    public int deletePlaceById(int placeId) {
        int numOfRows = 0;
        try {
            numOfRows = jdbcTemplate.update("DELETE FROM place WHERE place_id = ?;", placeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numOfRows;
    }

    private Place mapRowToPlace (SqlRowSet rowSet) {
        Place place = new Place();
        place.setPlaceId(rowSet.getInt("place_id"));
        place.setPlaceName(rowSet.getString("place_name"));
        place.setAddress(rowSet.getString("address"));
        place.setLatitude(rowSet.getDouble("latitude"));
        place.setLongitude(rowSet.getDouble("longitude"));
        place.setGoogleRating(rowSet.getDouble("google_rating"));
        return place;
    }
}
