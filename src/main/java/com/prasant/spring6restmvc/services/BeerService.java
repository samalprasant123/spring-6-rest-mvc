package com.prasant.spring6restmvc.services;

import com.prasant.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {
    List<BeerDTO> list();

    Optional<BeerDTO> getById(UUID beerId);

    BeerDTO save(BeerDTO beer);

    BeerDTO updateById(UUID beerId, BeerDTO beer);

    void deleteById(UUID beerId);

    void patchById(UUID beerId, BeerDTO beer);
}
