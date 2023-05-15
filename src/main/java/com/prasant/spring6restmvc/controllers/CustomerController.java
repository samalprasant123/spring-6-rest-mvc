package com.prasant.spring6restmvc.controllers;

import com.prasant.spring6restmvc.exceptions.ResourceNotFoundException;
import com.prasant.spring6restmvc.model.CustomerDTO;
import com.prasant.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(CustomerController.BASE_PATH)
public class CustomerController {

    public static final String BASE_PATH = "/api/v1/customer";

    public static final String PATH_CUSTOMER_ID  = "{customerId}";

    private final CustomerService customerService;

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public List<CustomerDTO> list() {
        log.debug("CustomerController.listCustomers() called.");
        return customerService.list();
    }

    //@RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    @GetMapping(PATH_CUSTOMER_ID)
    public CustomerDTO getById(@PathVariable("customerId") UUID customerId) {
        log.debug("CustomerController.getCustomerById() called. UUID = " + customerId.toString());
        return customerService.getById(customerId).orElseThrow(ResourceNotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> save(@RequestBody CustomerDTO customer) {
        log.debug("CustomerController.saveCustomer() called.");
        CustomerDTO savedCustomer = customerService.save(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());
        return new ResponseEntity<>(savedCustomer, headers, HttpStatus.CREATED);
    }

    @PutMapping(PATH_CUSTOMER_ID)
    public ResponseEntity<CustomerDTO> updateById(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        log.debug("CustomerController.updateCustomerById() called. UUID = " + customerId.toString());
        CustomerDTO updatedCustomer = customerService.updateById(customerId, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PATH_CUSTOMER_ID)
    public ResponseEntity<CustomerDTO> deleteById(@PathVariable UUID customerId) {
        log.debug("CustomerController.deleteCustomerById() called. UUID = " + customerId.toString());
        customerService.deleteById(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(PATH_CUSTOMER_ID)
    public ResponseEntity<CustomerDTO> patchById(@PathVariable UUID customerId, @RequestBody CustomerDTO customer) {
        log.debug("CustomerController.patchById() called. UUID = " + customerId.toString());
        customerService.patchById(customerId, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
