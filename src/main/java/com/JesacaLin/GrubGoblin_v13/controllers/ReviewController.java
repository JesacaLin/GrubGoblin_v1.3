package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.ReviewDAO;
import com.JesacaLin.GrubGoblin_v13.models.Review;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {
    private ReviewDAO reviewDAO;
    public ReviewController(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }
    @GetMapping("")
    public List<Review> listOfReviews() {
        return reviewDAO.getAllReviews();
    }
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        return reviewDAO.getReviewById(id);
    }
    //DO I NEED THE DEALID??
    @PostMapping
    public Review createReview(@RequestBody Review review, int dealId) {
        return reviewDAO.createReview(review, dealId);
    }
    @PutMapping("/{id}")
    public Review updateReview(@PathVariable int id, @RequestBody Review review) {
        return reviewDAO.updateReview(id, review);
    }
    @DeleteMapping("/{id}")
    public int deleteReview(@PathVariable int id) {
        return reviewDAO.deleteReviewById(id);
    }
}
