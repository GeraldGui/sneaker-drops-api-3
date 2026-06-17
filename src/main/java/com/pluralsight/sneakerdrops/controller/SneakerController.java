package com.pluralsight.sneakerdrops.controller;

import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.SneakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sneakers") // Writes an endpoint for all mapping
@CrossOrigin // Allows the front-end to communicate
public class SneakerController {

    private final SneakerService sneakerService;

    public SneakerController(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @GetMapping
    public List<Sneaker> getAll(@RequestParam(required = false) Integer year,
                                @RequestParam(required = false) String model,
                                @RequestParam(required = false) Double minPrice,
                                @RequestParam(required = false) Double maxPrice,
                                @RequestParam(required = false) String brand,
                                @RequestParam(required = false) String sort) {
        return sneakerService.search(year, model, minPrice, maxPrice, brand, sort);
    }

    @GetMapping("/{id}")
    public Sneaker getById(@PathVariable long id) {
        return sneakerService.byId(id);
    }

    @PostMapping
    public ResponseEntity<Sneaker> create(@RequestBody Sneaker sneaker) {
        Sneaker saved = sneakerService.createSneaker(sneaker);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public Sneaker update(@PathVariable long id, @RequestBody Sneaker sneaker) {
        return sneakerService.updateSneaker(id, sneaker);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
     sneakerService.deleteSneaker(id);
     return ResponseEntity.noContent().build();
    }

}
