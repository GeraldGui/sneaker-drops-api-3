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

   public Sneaker createSneaker(Sneaker sneaker) {
        sneaker.setId(null);
        sneaker.setBrand(resolveBrand(sneaker));
        return sneakerRepository.save(sneaker);
   }

   public Sneaker updateSneaker(long id, Sneaker updated){
        Sneaker existing = byId(id);
        if (existing == null)
            return null;

        existing.setModel(updated.getModel());
        existing.setPrice(updated.getPrice());
        existing.setReleaseYear(updated.getReleaseYear());
        existing.setBrand(resolveBrand(updated));

        return sneakerRepository.save(existing);
   }

    public void deleteSneaker(long id) {
        sneakerRepository.deleteById(id);
    }

    private Brand resolveBrand(Sneaker sneaker) {
        if (sneaker.getBrand() == null || sneaker.getBrand().getId() == null) {
            return null;
        }
            return brandRepository.findById(sneaker.getBrand().getId()).orElse(null);
    }
}
