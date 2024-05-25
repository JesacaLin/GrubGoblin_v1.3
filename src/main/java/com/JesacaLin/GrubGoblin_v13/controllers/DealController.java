package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.DealDAO;
import com.JesacaLin.GrubGoblin_v13.models.Deal;
import com.JesacaLin.GrubGoblin_v13.viewmodels.FullDealDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

/**
 * Controller for managing Deal entities.
 */
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/deal")
public class DealController {
    private DealDAO dealDAO;

     /**
     * Constructs a new DealController with the given DealDAO.
     *
     * @param dealDAO the DealDAO to use for deal management
     */
    public DealController(DealDAO dealDAO) {
        this.dealDAO = dealDAO;
    }

    /**
     * Retrieves all Deals.
     *
     * @return a list of all Deals
     */
    @GetMapping("")
    public List<Deal> listOfDeals() {
        return dealDAO.getAllDeals();
    }

    /**
     * Retrieves a Deal by its ID.
     *
     * @param id the ID of the Deal to retrieve
     * @return the Deal with the given ID
     */
    @GetMapping("/{id}")
    public Deal getDealById(@PathVariable int id) {
        return dealDAO.getDealById(id);
    }

    /**
     * Creates a new Deal.
     *
     * @param deal the Deal to create
     * @param principal the authenticated user
     * @return the created Deal
     */
    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PostMapping
    public Deal createDeal(@RequestBody Deal deal, Principal principal) {
        String userName = principal.getName();
        deal.setCreatedBy(userName);
        return dealDAO.createDeal(deal);
    }

     /**
     * Updates a Deal.
     *
     * @param deal the Deal data to update
     * @param principal the authenticated user
     * @return the updated Deal if authenticated user is the same as the creator.
     */
    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PutMapping("/{id}")
    public Deal updateDeal(@RequestBody Deal deal, Principal principal) {
        Deal existingDeal = dealDAO.getDealById(deal.getDealId());
        if (existingDeal == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deal not found");
        }
        if (!existingDeal.getCreatedBy().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized to update this deal");
        }
        return dealDAO.updateDeal(deal);
    }

    /**
     * Deletes a Deal by its ID.
     *
     * @param id the ID of the Deal to delete
     * @return the number of rows affected by the delete
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public int deleteDeal(@PathVariable int id) {
        return dealDAO.deleteDealById(id);
    }

    /**
     * Retrieves full details for all Deals.
     *
     * @return a list of full details for all Deals
     */
    @GetMapping("/details")
    public List<FullDealDetails> getFullDealDetails() {
        return dealDAO.getAllDealDetails();
    }

    /**
     * Retrieves full details for all Deals that match a given keyword.
     *
     * @param keyword the keyword to match Deals against
     * @return a list of full details for all matching Deals
     */
    @GetMapping("/details/keyword/{keyword}")
    public List<FullDealDetails> getFullDealDetailsByKeyword(@PathVariable String keyword) {
        return dealDAO.getAllDealByKeyword(keyword);
    }

    /**
     * Retrieves full details for all Deals that occur on a given day of the week.
     *
     * @param dayOfWeek the day of the week to match Deals against
     * @return a list of full details for all matching Deals
     */
    @GetMapping("/details/day/{dayOfWeek}")
    public List<FullDealDetails> getFullDealDetailsByDayOfWeek(@PathVariable String dayOfWeek) {
        return dealDAO.getAllDealByDayOfWeek(dayOfWeek);
    }

    /**
     * Retrieves full details for all top-rated Deals.
     *
     * @return a list of full details for all top-rated Deals
     */
    @GetMapping("/details/top-rated")
    public List<FullDealDetails> getFullDealDetailsTopRated() {
        return dealDAO.getAllTopRatedDeals();
    }

    /**
     * Retrieves full details for all Deals from a given place.
     *
     * @param place_id the ID of the place to match Deals against
     * @return a list of full details for all matching Deals
     */
    @GetMapping("/details/place/{place_id}")
    public List<FullDealDetails> getFullDealDetailsFromPlace(@PathVariable int place_id) {
        return dealDAO.getAllDealDetailByPlaceId(place_id);
    }

    /**
     * Retrieves full details for a Deal by its ID.
     *
     * @param deal_id the ID of the Deal to retrieve details for
     * @return a list of full details for the Deal
     */
    @GetMapping("/details/deal/{deal_id}")
    public List<FullDealDetails> getFullDealDetailsFromDealId(@PathVariable int deal_id) {
        return dealDAO.getAllDealDetailByDealId(deal_id);
    }
}
