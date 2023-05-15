package com.prasant.spring6restmvc.controllers;

import com.prasant.spring6restmvc.exceptions.ResourceNotFoundException;
import com.prasant.spring6restmvc.model.BeerDTO;
import com.prasant.spring6restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(BeerController.BASE_PATH)
public class BeerController {
    public static final String BASE_PATH = "/api/v1/beer";
    public static final String PATH_BEER_ID = "{beerId}";
    private final BeerService beerService;

    @GetMapping
    public ResponseEntity<List<BeerDTO>> list() {
        log.debug("BeerController.list() called.");
        return new ResponseEntity<>(beerService.list(), HttpStatus.OK);
    }

    @GetMapping(PATH_BEER_ID)
    public ResponseEntity<BeerDTO> getById(@PathVariable("beerId") UUID beerId) {
        log.debug("BeerController.getById() called. UUID = " + beerId.toString());
        BeerDTO beer = beerService.getById(beerId).orElseThrow(ResourceNotFoundException::new);
        return new ResponseEntity<>(beer, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDTO> create(@RequestBody BeerDTO beer) {
        log.debug("BeerController.add() called.");
        BeerDTO savedBeer = beerService.save(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());
        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }

    @PutMapping(PATH_BEER_ID)
    public ResponseEntity<BeerDTO> updateById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        log.debug("BeerController.updateById() called. UUID = " + beerId.toString());
        BeerDTO updatedBeer = beerService.updateById(beerId, beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PATH_BEER_ID)
    public ResponseEntity<BeerDTO> deleteById(@PathVariable UUID beerId) {
        log.debug("BeerController.deleteById() called. UUID = " + beerId.toString());
        beerService.deleteById(beerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(PATH_BEER_ID)
    public ResponseEntity<BeerDTO> patchById(@PathVariable UUID beerId, @RequestBody BeerDTO beer) {
        log.debug("BeerController.patchById() called. UUID = " + beerId.toString());
        beerService.patchById(beerId, beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException() {
        log.debug("BeerController.handleResourceNotFoundException() called.");
        return ResponseEntity.notFound().build();
    }*/
}
