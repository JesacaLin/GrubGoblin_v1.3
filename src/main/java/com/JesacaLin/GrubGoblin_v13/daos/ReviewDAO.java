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

/**
 * DAO class for managing Review entities in the database.
 */
@Component
public class ReviewDAO {
    private JdbcTemplate jdbcTemplate;

     /**
     * Constructs a new ReviewDAO with the given DataSource.
     *
     * @param dataSource the DataSource to use for database access
     */
    public ReviewDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Retrieves a Review by its ID.
     *
     * @param reviewId the ID of the Review to retrieve
     * @return the Review with the given ID, or null if no such Review exists
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Retrieves all Reviews.
     *
     * @return a list of all Reviews
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Creates a new Review.
     *
     * @param review the Review to create
     * @param dealId the ID of the deal associated with the review
     * @return the created Review
     * @throws DaoException if a database access error occurs or if the Review data violates database constraints
     */
    public Review createReview(Review review, int dealId) {
        Review newReview = null;
        String sql = "INSERT INTO review (deal_id, stars, review_description, reviewed_by)" + "VALUES (?, ?, ?, ? ) RETURNING review_id";
        try {
            int reviewId = jdbcTemplate.queryForObject(sql, int.class, review.getDealId(), review.getStars(), review.getReviewDescription(), review.getReviewedBy());
            newReview = getReviewById(reviewId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newReview;
    }

     /**
     * Updates a Review.
     *
     * @param id the ID of the Review to update
     * @param review the Review data to update
     * @return the updated Review
     * @throws DaoException if a database access error occurs, if the Review data violates database constraints, or if no rows were affected by the update
     */
    public Review updateReview(int id, Review review) {
        Review updatedReview = null;
        String sql = "UPDATE review SET deal_id = ?, stars = ?, review_description = ?, reviewed_by = ? WHERE review_id = ?";

        try {
            int numOfRows = jdbcTemplate.update(sql, review.getDealId(), review.getStars(), review.getReviewDescription(), review.getReviewedBy(), review.getReviewId());
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

    /**
     * Deletes a Review by its ID.
     *
     * @param reviewId the ID of the Review to delete
     * @return the number of rows affected by the delete
     * @throws DaoException if a database access error occurs or if the Review data violates database constraints
     */
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

    /**
     * Retrieves all Reviews for a specific deal.
     *
     * @param dealId the ID of the deal to retrieve Reviews for
     * @return a list of all Reviews for the specified deal
     * @throws DaoException if a database access error occurs
     */
    public List<Review> getAllReviewsByDealId(int dealId) {
        List<Review> reviews = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM review WHERE deal_id = ?", dealId);
            while(rowSet.next()) {
                reviews.add(mapRowToReview(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return reviews;
    }

    /**
     * Maps a SqlRowSet to a Review.
     *
     * @param rowSet the SqlRowSet to map
     * @return the mapped Review
     */
    public Review mapRowToReview (SqlRowSet rowSet) {
        Review review = new Review();
        review.setReviewId(rowSet.getInt("review_id"));
        review.setDealId(rowSet.getInt("deal_id"));
        review.setStars(rowSet.getDouble("stars"));
        review.setReviewDescription(rowSet.getString("review_description"));
        review.setReviewedBy(rowSet.getString("reviewed_by"));
        return review;
    }
}
