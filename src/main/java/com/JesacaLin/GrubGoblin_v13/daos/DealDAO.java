package com.JesacaLin.GrubGoblin_v13.daos;

import com.JesacaLin.GrubGoblin_v13.exception.DaoException;
import com.JesacaLin.GrubGoblin_v13.models.Deal;
import com.JesacaLin.GrubGoblin_v13.viewmodels.FullDealDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is a Data Access Object (DAO) for dealing with the 'deal' table in the GrubGoblin v1.3 PostgreSQL database. Will also deal with view model: FullDealDetails
 * Annotation means this class is a Spring Component, Spring will create the class if there is a need.
 */
@Component
public class DealDAO {
    private JdbcTemplate jdbcTemplate;
    public DealDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Fetches all deals from the 'deal' table in the database.
     *
     * @return a list of all deals in the database
     * @throws DaoException if unable to connect to the server or database
     */
    public List<Deal> getAllDeals() {
        List<Deal> deals = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM deal");

            while(rowSet.next()) {
                deals.add(mapRowToDeal(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return deals;
    }

    /**
     * Fetches a specific deal from the 'deal' table in the database using its ID.
     *
     * @param dealId the ID of the deal to fetch
     * @return the deal with the given ID, or null if id doesn't exist
     * @throws DaoException if unable to connect to the server or database
     */
    public Deal getDealById (int dealId) {
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM deal WHERE deal_id = ?", dealId);
            if (rowSet.next()) {
                return mapRowToDeal(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return null;
    }

    /**
     * Creates a new deal in the 'deals' table in the database.
     *
     * @param deal the deal to create. More info on the related Deal model: {@link Deal}. Each object need the following info:
     *             -placeId: the ID of the place where the deal is available
     *             -typeOfDeal: The type of deal it is
     *             -dealDescription: a description of what the deal is
     *             -createdBy: the user that created the deal
     *
     * @return the newly created deal with all details filled in.
     * @throws DaoException if unable to connect to the server/database or data integrity violation. For ex: if placeId doesn't exist
     */
    public Deal createDeal(Deal deal) {
        Deal newDeal = null;
        String sql = "INSERT INTO deal (place_id, type_of_deal, deal_description, created_by)" + "VALUES (?, ?, ?, ?) RETURNING deal_id";
        try {
            int dealId = jdbcTemplate.queryForObject(sql, int.class, deal.getPlaceId(), deal.getTypeOfDeal(), deal.getDealDescription(), deal.getCreatedBy());
            newDeal = getDealById(dealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newDeal;
    }

    /**
     * Updates an existing deal in the 'deals' table in the database.
     *
     * @param deal the deal to update. More info on the related Deal model: {@link Deal}. Each object need the following info:
     *             -placeId: the ID of the place where the deal is available
     *             -typeOfDeal: The type of deal it is
     *             -dealDescription: a description of what the deal is
     *             -createdBy: the user that created the deal
     *
     * @return the newly updated deal with edits added.
     * @throws DaoException if unable to connect to the server/database or data integrity violation. For ex: if placeId doesn't exist in the 'places' table.
     */
    public Deal updateDeal(Deal deal) {
        Deal updatedDeal = null;
        String sql = "UPDATE deal SET place_id = ?, type_of_deal = ?, deal_description = ?, updated_at = NOW(), created_by = ? WHERE deal_id = ?";

        try {
            int numOfRows = jdbcTemplate.update(sql, deal.getPlaceId(), deal.getTypeOfDeal(), deal.getDealDescription(), deal.getCreatedBy(), deal.getDealId());
            if (numOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedDeal = getDealById(deal.getDealId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedDeal;
    }

    /**
     * Deletes an existing deal in the 'deals' table in the database.
     *
     * @param dealId the specific deal to delete, based on id.
     * @return the number of rows affected. Should be 1. If returning 0, it means nothing was deleted.
     * @throws DaoException if unable to connect to the server/database or data integrity violation. For ex: if dealId doesn't exist in the 'deal' table.
     */
    public int deleteDealById(int dealId) {
        int numOfRows = 0;
        try {
            numOfRows = jdbcTemplate.update("DELETE FROM deal WHERE deal_id = ?;", dealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numOfRows;
    }

    /**
     * Fetches all deals from the 'deal' table, according to 'FullDealDetails' view model.
     *
     * @return a list of all deals in the database that conforms to the view model's required data. Full list here: {@link FullDealDetails}
     * @throws DaoException if unable to connect to the server or database
     */
    public List<FullDealDetails> getAllDealDetails() {
        List<FullDealDetails> dealDetails = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT deal.deal_id, place_name, address, deal.type_of_deal, deal.deal_description, availability.day_of_week, availability.start_time, review.stars, review.review_description, deal.updated_at, deal.created_by \n" +
                    "FROM place\n" +
                    "JOIN deal ON deal.place_id = place.place_id\n" +
                    "JOIN deal_availability ON deal_availability.deal_id = deal.deal_id\n" +
                    "JOIN availability ON availability.availability_id = deal_availability.availability_id\n" +
                    "JOIN review ON review.deal_id = deal.deal_id\n" +
                    "ORDER BY deal_id");

            while(rowSet.next()) {
                dealDetails.add(mapRowToDealDetails(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return dealDetails;
    }

    /**
     * Fetches all deals from the 'deal' table by specific keyword, according to 'FullDealDetails' view model.
     *
     * @param keyword the word to search for in the deal descriptions
     * @return a list of all deals in the database that contains the keyword and conforms to the view model's required data. Full list here: {@link FullDealDetails}
     * @throws DaoException if unable to connect to the server or database
     */
    public List<FullDealDetails> getAllDealByKeyword(String keyword) {
        List<FullDealDetails> deals = new ArrayList<>();
        String searchString = "%" + keyword + "%";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT deal.deal_id, place_name, address, deal.type_of_deal, deal.deal_description, availability.day_of_week, availability.start_time, review.stars, review.review_description, deal.updated_at, deal.created_by \n" +
                    "FROM place\n" +
                    "JOIN deal ON deal.place_id = place.place_id\n" +
                    "JOIN deal_availability ON deal_availability.deal_id = deal.deal_id\n" +
                    "JOIN availability ON availability.availability_id = deal_availability.availability_id\n" +
                    "JOIN review ON review.deal_id = deal.deal_id\n" +
                    "WHERE deal_description LIKE ?", searchString);

            while(rowSet.next()) {
                deals.add(mapRowToDealDetails(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return deals;
    }


    /**
     * Fetches all deals from the 'deal' table by specific day of the week, according to 'FullDealDetails' view model.
     * Day of the week is an int!
     * @param dayOfWeek the specific day of the week being searched for (1 for Monday, 2 for Tuesday...etc. 8 for deals that are available all week)
     * @return a list of all deals in the database that occurs on a specific day of the week and conforms to the view model's required data. Full list here: {@link FullDealDetails}
     * @throws DaoException if unable to connect to the server or database
     */
    public List<FullDealDetails> getAllDealByDayOfWeek(int dayOfWeek) {
        List<FullDealDetails> deals = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT deal.deal_id, place_name, address, deal.type_of_deal, deal.deal_description, availability.day_of_week, availability.start_time, review.stars, review.review_description, deal.updated_at, deal.created_by \n" +
                    "FROM place\n" +
                    "JOIN deal ON deal.place_id = place.place_id\n" +
                    "JOIN deal_availability ON deal_availability.deal_id = deal.deal_id\n" +
                    "JOIN availability ON availability.availability_id = deal_availability.availability_id\n" +
                    "JOIN review ON review.deal_id = deal.deal_id\n" +
                    "WHERE day_of_week = ?\n" +
                    "ORDER BY start_time", dayOfWeek);

            while(rowSet.next()) {
                deals.add(mapRowToDealDetails(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return deals;
    }

    /**
     * Fetches all deals from the 'deal' table if stars rating is 4 or above, according to 'FullDealDetails' view model. Note: Star rating is the user's rating of the deal, is NOT google rating
     *
     * @return a list of all deals in the database that has a deal rating of 4 or higher and conforms to the view model's required data. Full list here: {@link FullDealDetails}
     * @throws DaoException if unable to connect to the server or database
     */
    public List<FullDealDetails> getAllTopRatedDeals() {
        List<FullDealDetails> deals = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT deal.deal_id, place_name, address, deal.type_of_deal, deal.deal_description, availability.day_of_week, availability.start_time, review.stars, review.review_description, deal.updated_at, deal.created_by \n" +
                    "FROM place\n" +
                    "JOIN deal ON deal.place_id = place.place_id\n" +
                    "JOIN deal_availability ON deal_availability.deal_id = deal.deal_id\n" +
                    "JOIN availability ON availability.availability_id = deal_availability.availability_id\n" +
                    "JOIN review ON review.deal_id = deal.deal_id\n" +
                    "WHERE stars >= 4\n" +
                    "ORDER BY start_time");

            while(rowSet.next()) {
                deals.add(mapRowToDealDetails(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return deals;
    }

    /**
     * Fetches all deals from the 'deal' table from a specific place, according to 'FullDealDetails' view model.
     * @param place_id the specific place searched for.
     * @return a list of all deals in the database that occurs at a specific place and conforms to the view model's required data. Full list here: {@link FullDealDetails}
     * @throws DaoException if unable to connect to the server or database
     */
    public List<FullDealDetails> getAllDealDetailByPlaceId(int place_id) {
        List<FullDealDetails> deals = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT deal.deal_id, place_name, address, deal.type_of_deal, deal.deal_description, availability.day_of_week, availability.start_time, review.stars, review.review_description, deal.updated_at, deal.created_by \n" +
                    "FROM place\n" +
                    "JOIN deal ON deal.place_id = place.place_id\n" +
                    "JOIN deal_availability ON deal_availability.deal_id = deal.deal_id\n" +
                    "JOIN availability ON availability.availability_id = deal_availability.availability_id\n" +
                    "JOIN review ON review.deal_id = deal.deal_id\n" +
                    "WHERE deal.place_id = ?\n" +
                    "ORDER BY start_time", place_id);

            while(rowSet.next()) {
                deals.add(mapRowToDealDetails(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return deals;
    }

    /**
     * A utility method that maps data from a SqlRowSet, obtained from a SQL query, to a {@link Deal} object.
     *
     * @param rowSet a SqlRowSet containing data from a row in the 'deal' table.
     * @return new deal object that is populated with data from the rowSet. For more info on the fields in the Deal object, see {@link Deal}.
     */
    public Deal mapRowToDeal (SqlRowSet rowSet) {
        Deal deal = new Deal();
        deal.setDealId(rowSet.getInt("deal_id"));
        deal.setPlaceId(rowSet.getInt("place_id"));
        deal.setTypeOfDeal(rowSet.getString("type_of_deal"));
        deal.setDealDescription((rowSet.getString("deal_description")));
        deal.setDaysOfWeek(rowSet.getString("daysOfWeek"));

        Time sqlStartTime = rowSet.getTime("start_time");
        if (sqlStartTime != null) {
            deal.setStartTime(sqlStartTime.toLocalTime());
        }
        Time sqlEndTime = rowSet.getTime("end_time");
        if (sqlEndTime != null) {
            deal.setEndTime(sqlEndTime.toLocalTime());
        }

        Timestamp createdAtTimestamp = rowSet.getTimestamp("created_at");
        if (createdAtTimestamp != null) {
            deal.setCreatedAt(createdAtTimestamp.toLocalDateTime());
        }

        Timestamp updatedAtTimestamp = rowSet.getTimestamp("updated_at");
        if (updatedAtTimestamp != null) {
            deal.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
        }

        deal.setCreatedBy(rowSet.getString("created_by"));
        return deal;
    }

    /**
     * A utility method that maps data from a SqlRowSet, obtained from a SQL query, to a {@link FullDealDetails} object.
     *
     * @param rowSet a SqlRowSet containing data from a row in the 'deal' table.
     * @return new deal object that is populated with data from the rowSet. For more info on the fields in the Deal object, see {@link FullDealDetails}.
     */
    public FullDealDetails mapRowToDealDetails (SqlRowSet rowSet) {
        FullDealDetails dealDetails = new FullDealDetails();
        dealDetails.setDealId(rowSet.getInt("deal_id"));
        dealDetails.setPlaceName(rowSet.getString("place_name"));
        dealDetails.setAddress(rowSet.getString("address"));
        dealDetails.setTypeOfDeal(rowSet.getString("type_of_deal"));
        dealDetails.setDealDescription((rowSet.getString("deal_description")));
        dealDetails.setDaysOfWeek(rowSet.getString("days_of_week"));
        Time sqlStartTime = rowSet.getTime("start_time");
        if (sqlStartTime != null) {
            dealDetails.setStartTime(sqlStartTime.toLocalTime());
        }
        dealDetails.setStars(rowSet.getDouble("stars"));
        dealDetails.setReviewDescription(rowSet.getString("review_description"));

        Timestamp updatedAtTimestamp = rowSet.getTimestamp("updated_at");
        if (updatedAtTimestamp != null) {
            dealDetails.setUpdatedAt(updatedAtTimestamp.toLocalDateTime());
        }
        dealDetails.setCreatedBy(rowSet.getString("created_by"));
        return dealDetails;
    }
}
