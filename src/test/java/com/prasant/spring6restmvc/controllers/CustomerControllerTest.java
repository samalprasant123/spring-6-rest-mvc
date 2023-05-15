package com.prasant.spring6restmvc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prasant.spring6restmvc.exceptions.ResourceNotFoundException;
import com.prasant.spring6restmvc.model.CustomerDTO;
import com.prasant.spring6restmvc.services.CustomerService;
import com.prasant.spring6restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    private static final String URI_BASE_PATH_WITH_CUSTOMER_ID = CustomerController.BASE_PATH + "/" + CustomerController.PATH_CUSTOMER_ID;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testList() throws Exception {
        List<CustomerDTO> customers = customerServiceImpl.list();

        given(customerService.list()).willReturn(customers);

        mockMvc.perform(get(CustomerController.BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(customers.size())));
    }

    @Test
    void testGetById() throws Exception {
        CustomerDTO customer = customerServiceImpl.list().get(0);

        given(customerService.getById(customer.getId())).willReturn(Optional.of(customer));

        mockMvc.perform(get(URI_BASE_PATH_WITH_CUSTOMER_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(customer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));
    }

    @Test
    void testCreate() throws Exception {
        CustomerDTO customer = CustomerDTO.builder()
                .customerName("Raga Pats")
                .build();

        CustomerDTO customer0 = customerServiceImpl.list().get(0);

        given(customerService.save(any(CustomerDTO.class))).willReturn(customer0);

        mockMvc.perform(post(CustomerController.BASE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateById() throws Exception {
        CustomerDTO customer = customerServiceImpl.list().get(0);
        customer.setCustomerName("Ohh Lala la");

        mockMvc.perform(put(URI_BASE_PATH_WITH_CUSTOMER_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateById(customer.getId(), customer);
    }

    @Test
    void testDeleteById() throws Exception {
        CustomerDTO customer = customerServiceImpl.list().get(0);

        mockMvc.perform(delete(URI_BASE_PATH_WITH_CUSTOMER_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchById() throws Exception {

        // given
        CustomerDTO customer = customerServiceImpl.list().get(0);

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "Dummy Customer Name");

        // when
        mockMvc.perform(patch(URI_BASE_PATH_WITH_CUSTOMER_ID, customer.getId())
                        .content(objectMapper.writeValueAsString(customerMap))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).patchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        // then
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

    @Test
    void getByIdNotFound() throws Exception {
        //(customerService.getById(any(UUID.class))).willThrow(ResourceNotFoundException.class);
        // given
        given(customerService.getById(any(UUID.class))).willReturn(Optional.empty());

        // when
        mockMvc.perform(get(URI_BASE_PATH_WITH_CUSTOMER_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));
                //.andExpect(result -> assertEquals("Resource Not Found.", result.getResolvedException().getMessage()));
        // then
    }
}