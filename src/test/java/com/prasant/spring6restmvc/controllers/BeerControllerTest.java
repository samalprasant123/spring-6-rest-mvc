package com.prasant.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasant.spring6restmvc.exceptions.ResourceNotFoundException;
import com.prasant.spring6restmvc.model.BeerDTO;
import com.prasant.spring6restmvc.model.BeerStyle;
import com.prasant.spring6restmvc.services.BeerService;
import com.prasant.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    private static final String URI_BASE_PATH_WITH_BEER_ID = BeerController.BASE_PATH + "/" + BeerController.PATH_BEER_ID;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testList() throws Exception {
        List<BeerDTO> beers = beerServiceImpl.list();
        given(beerService.list()).willReturn(beers);

        mockMvc.perform(get(BeerController.BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(beers.size())));
    }

    @Test
    void testGetById() throws Exception {
        BeerDTO testBeer = beerServiceImpl.list().get(0);

        given(beerService.getById(testBeer.getId())).willReturn(Optional.of(testBeer));


        mockMvc.perform(get(URI_BASE_PATH_WITH_BEER_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testCreate() throws Exception {
        BeerDTO beer = BeerDTO.builder()
                .beerName("Mango Bobs - JTs 5")
                .beerStyle(BeerStyle.ALE)
                .upc("99880003")
                .quantityOnHand(100)
                .price(new BigDecimal("11.99"))
                .build();

        BeerDTO beer0 = beerServiceImpl.list().get(0);

        given(beerService.save(any(BeerDTO.class))).willReturn(beer0);

        mockMvc.perform(post(BeerController.BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"));

    }

    @Test
    void testUpdateById() throws Exception {
        BeerDTO beer = beerServiceImpl.list().get(0);
        beer.setBeerName("Changed Name");

        mockMvc.perform(put(URI_BASE_PATH_WITH_BEER_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateById(beer.getId(), beer);
    }

    @Test
    void testDeleteById() throws Exception {
        BeerDTO beer = beerServiceImpl.list().get(0);

        mockMvc.perform(delete(URI_BASE_PATH_WITH_BEER_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchById() throws Exception {
        // given
        BeerDTO beer = beerServiceImpl.list().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "Sample Beer Name");

        // when
        mockMvc.perform(patch(URI_BASE_PATH_WITH_BEER_ID, beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        // then
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void getByIdNotFound() throws Exception {


        //given(beerService.getById(any(UUID.class))).willThrow(ResourceNotFoundException.class);
        // given
        Optional<BeerDTO> optionalBeer = beerServiceImpl.getById(UUID.randomUUID());
        given(beerService.getById(any(UUID.class))).willReturn(optionalBeer);

        // when
        mockMvc.perform(get(URI_BASE_PATH_WITH_BEER_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
    }

}