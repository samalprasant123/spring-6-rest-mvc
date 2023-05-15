package com.prasant.spring6restmvc.services;

import com.prasant.spring6restmvc.model.BeerDTO;
import com.prasant.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("99880001")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("99880002")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("99880003")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.beerMap = new HashMap<>();
        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public List<BeerDTO> list() {
        log.debug("BeerServiceImpl.list() called.");
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getById(UUID beerId) {
        log.debug("BeerServiceImpl.gerById() called. UUID = " + beerId.toString());
        return Optional.ofNullable(beerMap.get(beerId));
    }

    @Override
    public BeerDTO save(BeerDTO beer) {
        log.debug("BeerServiceImpl.save() called.");
        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .price(beer.getPrice())
                .upc(beer.getUpc())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public BeerDTO updateById(UUID beerId, BeerDTO beer) {
        log.debug("BeerServiceImpl.updateById() called. UUID = " + beerId.toString());
        BeerDTO existingBeer = beerMap.get(beerId);
        if (!Objects.isNull(existingBeer)) {
            existingBeer.setBeerName(beer.getBeerName());
            existingBeer.setBeerStyle(beer.getBeerStyle());
            existingBeer.setVersion(existingBeer.getVersion() + 1);
            existingBeer.setPrice(beer.getPrice());
            existingBeer.setQuantityOnHand(existingBeer.getQuantityOnHand() + beer.getQuantityOnHand());
            existingBeer.setUpc(beer.getUpc());
            existingBeer.setUpdateDate(LocalDateTime.now());
        }

        beerMap.put(beerId, existingBeer);
        return existingBeer;
    }

    @Override
    public void deleteById(UUID beerId) {
        log.debug("BeerServiceImpl.deleteById() called. UUID = " + beerId.toString());
        beerMap.remove(beerId);
    }

    @Override
    public void patchById(UUID beerId, BeerDTO beer) {
        log.debug("BeerServiceImpl.patchById() called. UUID = " + beerId.toString());
        BeerDTO existingBeer = beerMap.get(beerId);

        if (StringUtils.hasText(beer.getBeerName())) {
            existingBeer.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            existingBeer.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            existingBeer.setPrice(beer.getPrice());
        }

        if (beer.getQuantityOnHand() != null){
            existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existingBeer.setUpc(beer.getUpc());
        }
        existingBeer.setVersion(existingBeer.getVersion() + 1);
        existingBeer.setUpdateDate(LocalDateTime.now());
    }
}
