package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.DealDAO;
import com.JesacaLin.GrubGoblin_v13.models.Deal;
import com.JesacaLin.GrubGoblin_v13.viewmodels.FullDealDetails;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

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
    @PostMapping
    public Deal createDeal(@RequestBody Deal deal) {
        return dealDAO.createDeal(deal);
    }

    //I REMOVED THE int id Pathvariable from update...
    @PutMapping("/{id}")
    public Deal updateDeal(@RequestBody Deal deal) {
        return dealDAO.updateDeal(deal);
    }
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
    public List<FullDealDetails> getFullDealDetailsByDayOfWeek(@PathVariable int dayOfWeek) {
        return dealDAO.getAllDealByDayOfWeek(dayOfWeek);
    }
    @GetMapping("/details/top-rated")
    public List<FullDealDetails> getFullDealDetailsTopRated() {
        return dealDAO.getAllTopRatedDeals();
    }
}
