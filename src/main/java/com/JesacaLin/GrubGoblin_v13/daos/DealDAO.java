package com.JesacaLin.GrubGoblin_v13.daos;

import com.JesacaLin.GrubGoblin_v13.exception.DaoException;
import com.JesacaLin.GrubGoblin_v13.models.Deal;
import com.JesacaLin.GrubGoblin_v13.models.FullDealDetails;
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
public class DealDAO {
    private JdbcTemplate jdbcTemplate;
    public DealDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
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

    public List<FullDealDetails> getAllDealDetails() {
        List<FullDealDetails> dealDetails = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT deal.deal_id, place_name, address, deal.type_of_deal, deal.deal_description, availability.day_of_week, availability.start_time, review.stars, review.review_description, deal.created_by \n" +
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

    public List<FullDealDetails> getAllDealByKeyword(String keyword) {
        List<FullDealDetails> deals = new ArrayList<>();
        String searchString = "%" + keyword + "%";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT deal.deal_id, place_name, address, deal.type_of_deal, deal.deal_description, availability.day_of_week, availability.start_time, review.stars, review.review_description, deal.created_by \n" +
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




    public Deal createDeal(Deal deal) {
        Deal newDeal = null;
        String sql = "INSERT INTO deal (place_id, type_of_deal, deal_description)" + "VALUES (?, ?, ? ) RETURNING deal_id";
        try {
            int dealId = jdbcTemplate.queryForObject(sql, int.class, deal.getPlaceId(), deal.getTypeOfDeal(), deal.getDealDescription());
            newDeal = getDealById(dealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newDeal;
    }

    public Deal updateDeal(int id, Deal deal) {
        Deal updatedDeal = null;
        String sql = "UPDATE deal SET place_id = ?, type_of_deal = ?, deal_description = ?";

        try {
            int numOfRows = jdbcTemplate.update(sql, deal.getPlaceId(), deal.getTypeOfDeal(), deal.getDealDescription());
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
//NEED TO ADD CREATED BY TO BOTH MAPTOS AND ALSO TO MODELS

    public Deal mapRowToDeal (SqlRowSet rowSet) {
        Deal deal = new Deal();
        deal.setDealId(rowSet.getInt("deal_id"));
        deal.setPlaceId(rowSet.getInt("place_id"));
        deal.setTypeOfDeal(rowSet.getString("type_of_deal"));
        deal.setDealDescription((rowSet.getString("deal_description")));
        deal.setCreatedBy(rowSet.getString("created_by"));
        return deal;
    }

    public FullDealDetails mapRowToDealDetails (SqlRowSet rowSet) {
        FullDealDetails dealDetails = new FullDealDetails();
        dealDetails.setDealId(rowSet.getInt("deal_id"));
        dealDetails.setPlaceName(rowSet.getString("place_name"));
        dealDetails.setAddress(rowSet.getString("address"));
        dealDetails.setTypeOfDeal(rowSet.getString("type_of_deal"));
        dealDetails.setDealDescription((rowSet.getString("deal_description")));
        dealDetails.setDayOfWeek(rowSet.getInt("day_of_week"));
        Time sqlStartTime = rowSet.getTime("start_time");
        if (sqlStartTime != null) {
            dealDetails.setStartTime(sqlStartTime.toLocalTime());
        }
        dealDetails.setStars(rowSet.getDouble("stars"));
        dealDetails.setReviewDescription(rowSet.getString("review_description"));
        dealDetails.setCreatedBy(rowSet.getString("created_by"));
        return dealDetails;
    }
}
