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

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/deal")
public class DealController {
    private DealDAO dealDAO;
    public DealController(DealDAO dealDAO) {
        this.dealDAO = dealDAO;
    }

    @GetMapping("")
    public List<Deal> listOfDeals() {
        return dealDAO.getAllDeals();
    }

    @GetMapping("/{id}")
    public Deal getDealById(@PathVariable int id) {
        return dealDAO.getDealById(id);
    }

    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PostMapping
    public Deal createDeal(@RequestBody Deal deal, Principal principal) {
        String userName = principal.getName();
        deal.setCreatedBy(userName);
        return dealDAO.createDeal(deal);
    }

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

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public int deleteDeal(@PathVariable int id) {
        return dealDAO.deleteDealById(id);
    }

    @GetMapping("/details")
    public List<FullDealDetails> getFullDealDetails() {
        return dealDAO.getAllDealDetails();
    }
    @GetMapping("/details/keyword/{keyword}")
    public List<FullDealDetails> getFullDealDetailsByKeyword(@PathVariable String keyword) {
        return dealDAO.getAllDealByKeyword(keyword);
    }
    @GetMapping("/details/day/{dayOfWeek}")
    public List<FullDealDetails> getFullDealDetailsByDayOfWeek(@PathVariable String dayOfWeek) {
        return dealDAO.getAllDealByDayOfWeek(dayOfWeek);
    }
    @GetMapping("/details/top-rated")
    public List<FullDealDetails> getFullDealDetailsTopRated() {
        return dealDAO.getAllTopRatedDeals();
    }

    @GetMapping("/details/place/{place_id}")
    public List<FullDealDetails> getFullDealDetailsFromPlace(@PathVariable int place_id) {
        return dealDAO.getAllDealDetailByPlaceId(place_id);
    }
    @GetMapping("/details/deal/{deal_id}")
    public List<FullDealDetails> getFullDealDetailsFromDealId(@PathVariable int deal_id) {
        return dealDAO.getAllDealDetailByDealId(deal_id);
    }
}
