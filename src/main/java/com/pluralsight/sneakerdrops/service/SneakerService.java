package com.pluralsight.sneakerdrops.service;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SneakerService {

    private final SneakerRepository sneakerRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public SneakerService(SneakerRepository sneakerRepository, BrandRepository brandRepository) {
        this.sneakerRepository = sneakerRepository;
        this.brandRepository = brandRepository;
    }

    public Sneaker byId(long id) {
        return sneakerRepository.findById(id).orElseThrow(null);
    }

    public List<Sneaker> search(Integer year, String model, Double minPrice ,Double maxPrice, String brand, String sort){
        List<Sneaker> results = new ArrayList<>(sneakerRepository.findAll().stream()
                .filter(s -> year == null || s.getReleaseYear() == year)
                .filter(s -> model == null || s.getModel().equalsIgnoreCase(model))
                .filter(s -> minPrice == null || s.getPrice() >= minPrice)
                .filter(s -> maxPrice == null || s.getPrice() <= maxPrice)
                .filter(s -> brand == null || s.getBrand().getName().equalsIgnoreCase(brand)).toList());

        if ("price".equalsIgnoreCase(sort)) {
            results.sort(Comparator.comparingDouble(Sneaker::getPrice).reversed());
        } else if ("model".equalsIgnoreCase(sort)) {
            results.sort(Comparator.comparing(Sneaker::getModel));
        }
        return results;
    }

    public Sneaker addSneaker(String model, int year, double price, long brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new NotFoundException("No brand with id " + brandId));
        return sneakerRepository.save(new Sneaker(model, price, year, brand));
    }

    public Sneaker updatePrice(long id, double price) {
        Sneaker sneaker = byId(id);
        sneaker.setPrice(price);
        return sneakerRepository.save(sneaker);
    }

    public void deleteSneaker(long id) {
        if (!sneakerRepository.existsById(id)) {
            throw new NotFoundException("No sneaker found by id " + id);
        }

        sneakerRepository.deleteById(id);
    }
}
