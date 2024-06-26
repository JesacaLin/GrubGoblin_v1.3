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

/**
 * DAO class for managing Place entities in the database.
 */
@Component
public class PlaceDAO {
    private JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new PlaceDAO with the given DataSource.
     *
     * @param dataSource the DataSource to use for database access
     */
    public PlaceDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

     /**
     * Retrieves a Place by its ID.
     *
     * @param id the ID of the Place to retrieve
     * @return the Place with the given ID, or null if no such Place exists
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Retrieves all Places.
     *
     * @return a list of all Places
     * @throws DaoException if a database access error occurs
     */
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

     /**
     * Creates a new Place.
     *
     * @param place the Place to create
     * @return the created Place
     * @throws DaoException if a database access error occurs or if the Place data violates database constraints
     */
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

    /**
     * Updates a Place.
     *
     * @param id the ID of the Place to update
     * @param place the Place data to update
     * @return the updated Place
     * @throws DaoException if a database access error occurs, if the Place data violates database constraints, or if no rows were affected by the update
     */
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

    /**
     * Deletes a Place by its ID.
     *
     * @param placeId the ID of the Place to delete
     * @return the number of rows affected by the delete
     * @throws DaoException if a database access error occurs or if the Place data violates database constraints
     */
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

    /**
     * Maps a SqlRowSet to a Place.
     *
     * @param rowSet the SqlRowSet to map
     * @return the mapped Place
     */
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
