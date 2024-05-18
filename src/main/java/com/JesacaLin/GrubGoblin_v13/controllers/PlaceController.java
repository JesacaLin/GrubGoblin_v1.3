package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.PlaceDAO;
import com.JesacaLin.GrubGoblin_v13.models.Place;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/place")
public class PlaceController {
    private PlaceDAO placeDAO;

    public PlaceController(PlaceDAO placeDAO) {
        this.placeDAO = placeDAO;
    }
    @GetMapping("")
    public List<Place> listOfPlaces() {
        return placeDAO.getAllPlaces();
    }
    @GetMapping("/{id}")
    public Place getPlaceById(@PathVariable int id) {
        return placeDAO.getPlaceById(id);
    }
    @PostMapping
    public Place createPlace(@RequestBody Place place) {
        return placeDAO.createPlace(place);
    }
    @PutMapping("/{id}")
    public Place updatePlace(@PathVariable int id, @RequestBody Place place) {
        return placeDAO.updatePlace(id, place);
    }
    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable int id) {
        return placeDAO.deletePlaceById(id);
    }
}
