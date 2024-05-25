package com.JesacaLin.GrubGoblin_v13.controllers;

import com.JesacaLin.GrubGoblin_v13.daos.PlaceDAO;
import com.JesacaLin.GrubGoblin_v13.models.Place;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing Place entities.
 */
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/place")
public class PlaceController {
    private PlaceDAO placeDAO;

    /**
     * Constructs a new PlaceController with the given PlaceDAO.
     *
     * @param placeDAO the PlaceDAO to use for place management
     */
    public PlaceController(PlaceDAO placeDAO) {
        this.placeDAO = placeDAO;
    }

    /**
     * Retrieves all Places.
     *
     * @return a list of all Places
     */
    @GetMapping("")
    public List<Place> listOfPlaces() {
        return placeDAO.getAllPlaces();
    }

    /**
     * Retrieves a Place by its ID.
     *
     * @param id the ID of the Place to retrieve
     * @return the Place with the given ID
     */
    @GetMapping("/{id}")
    public Place getPlaceById(@PathVariable int id) {
        return placeDAO.getPlaceById(id);
    }

    /**
     * Creates a new Place.
     *
     * @param place the Place to create
     * @return the created Place
     */
    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PostMapping
    public Place createPlace(@RequestBody Place place) {
        return placeDAO.createPlace(place);
    }

    /**
     * Updates a Place.
     *
     * @param id the ID of the Place to update
     * @param place the Place data to update
     * @return the updated Place
     */
    @PreAuthorize("hasAuthority('contributor') or hasAuthority('admin')")
    @PutMapping("/{id}")
    public Place updatePlace(@PathVariable int id, @RequestBody Place place) {
        return placeDAO.updatePlace(id, place);
    }

    /**
     * Deletes a Place by its ID.
     *
     * @param id the ID of the Place to delete
     * @return the number of rows affected by the delete
     */
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable int id) {
        return placeDAO.deletePlaceById(id);
    }
}
