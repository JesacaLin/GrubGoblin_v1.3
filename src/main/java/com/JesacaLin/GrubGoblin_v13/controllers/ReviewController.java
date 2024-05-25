package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.ReviewDAO;
import com.JesacaLin.GrubGoblin_v13.models.Review;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * Controller for managing Review entities.
 */
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/review")
public class ReviewController {
    private ReviewDAO reviewDAO;

    /**
     * Constructs a new ReviewController with the given ReviewDAO.
     *
     * @param reviewDAO the ReviewDAO to use for review management
     */
    public ReviewController(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    /**
     * Retrieves all Reviews.
     *
     * @return a list of all Reviews
     */
    @GetMapping("")
    public List<Review> listOfReviews() {
        return reviewDAO.getAllReviews();
    }

    /**
     * Retrieves a Review by its ID.
     *
     * @param id the ID of the Review to retrieve
     * @return the Review with the given ID
     */
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        return reviewDAO.getReviewById(id);
    }

    /**
     * Creates a new Review for a given deal.
     *
     * @param review the Review to create
     * @param dealId the ID of the deal the review is for
     * @param principal the authenticated user
     * @return the created Review
     */
    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PostMapping("/{dealId}")
    public Review createReview(@RequestBody Review review, @PathVariable int dealId, Principal principal) {
        String userName = principal.getName();
        review.setReviewedBy(userName);
        return reviewDAO.createReview(review, dealId);
    }

    /**
     * Updates a Review.
     *
     * @param id the ID of the Review to update
     * @param review the Review data to update
     * @return the updated Review if authorized person matches the creator of the review
     */
    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PutMapping("/{id}")
    public Review updateReview(@PathVariable int id, @RequestBody Review review, Principal principal) {
        Review existingReview = reviewDAO.getReviewById(id);
        if (existingReview == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
        if (!existingReview.getReviewedBy().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to review this deal");
        }
        return reviewDAO.updateReview(id, review);
    }

    /**
     * Deletes a Review by its ID.
     *
     * @param id the ID of the Review to delete
     * @return the number of rows affected by the delete
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public int deleteReview(@PathVariable int id) {
        return reviewDAO.deleteReviewById(id);
    }

    /**
     * Retrieves all Reviews for a given deal.
     *
     * @param dealId the ID of the deal to retrieve Reviews for
     * @return a list of Reviews for the given deal
     */
    @GetMapping("/deal/{dealId}")
    public List<Review> getReviewByDealId(@PathVariable int dealId) {
        return reviewDAO.getAllReviewsByDealId(dealId);
    }
}
