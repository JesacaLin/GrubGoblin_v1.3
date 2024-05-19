package com.JesacaLin.GrubGoblin_v13.daos;

import com.JesacaLin.GrubGoblin_v13.exception.DaoException;
import com.JesacaLin.GrubGoblin_v13.models.Review;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewDAO {
    private JdbcTemplate jdbcTemplate;
    public ReviewDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Review getReviewById (int reviewId) {
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM review WHERE review_id = ?", reviewId);
            if (rowSet.next()) {
                return mapRowToReview(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return null;
    }

    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM review");
            while(rowSet.next()) {
                reviews.add(mapRowToReview(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return reviews;
    }

    public Review createReview(Review review, int dealId) {
        Review newReview = null;
        String sql = "INSERT INTO review (deal_id, stars, review_description)" + "VALUES (?, ?, ? ) RETURNING review_id";
        try {
            int reviewId = jdbcTemplate.queryForObject(sql, int.class, review.getDealId(), review.getStars(), review.getReviewDescription());
            newReview = getReviewById(reviewId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newReview;
    }

    public Review updateReview(int id, Review review) {
        Review updatedReview = null;
        String sql = "UPDATE review SET deal_id = ?, stars = ?, review_description = ? WHERE review_id = ?";

        try {
            int numOfRows = jdbcTemplate.update(sql, review.getDealId(), review.getStars(), review.getReviewDescription(), review.getReviewId());
            if (numOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedReview = getReviewById(review.getReviewId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedReview;
    }

    public int deleteReviewById(int reviewId) {
        int numOfRows = 0;
        try {
            numOfRows = jdbcTemplate.update("DELETE FROM review WHERE review_id = ?;", reviewId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numOfRows;
    }

    public Review mapRowToReview (SqlRowSet rowSet) {
        Review review = new Review();
        review.setReviewId(rowSet.getInt("review_id"));
        review.setDealId(rowSet.getInt("deal_id"));
        review.setStars(rowSet.getDouble("stars"));
        review.setReviewDescription(rowSet.getString("review_description"));
        return review;
    }
}
