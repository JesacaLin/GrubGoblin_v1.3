package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.AvailabilityDAO;
import com.JesacaLin.GrubGoblin_v13.models.Availability;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    private AvailabilityDAO availabilityDAO;
    public AvailabilityController(AvailabilityDAO availabilityDAO) {
        this.availabilityDAO = availabilityDAO;
    }
    @GetMapping("")
    public List<Availability> listOfAvailability() {
        return availabilityDAO.getAllAvailabilities();
    }
    @GetMapping("/{id}")
    public Availability getAvailabilityById(@PathVariable int id) {
        return availabilityDAO.getAvailabilityById(id);
    }
    @PostMapping
    public Availability createAvailability(@RequestBody Availability availability, int dealId) {
        return availabilityDAO.createAvailability(availability, dealId);
    }
    @PutMapping("/{id}")
    public Availability updateAvailability(@PathVariable int id, @RequestBody Availability availability) {
        return availabilityDAO.updateAvailability(id, availability);
    }
    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable int id) {
        return availabilityDAO.deleteAvailabilityById(id);
    }

}
