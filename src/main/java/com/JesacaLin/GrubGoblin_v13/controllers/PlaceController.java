package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.PlaceDAO;
import com.JesacaLin.GrubGoblin_v13.models.Place;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
