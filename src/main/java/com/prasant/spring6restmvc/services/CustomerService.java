package com.prasant.spring6restmvc.services;

import com.prasant.spring6restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> list();

    Optional<CustomerDTO> getById(UUID customerId);

    CustomerDTO save(CustomerDTO customer);

    CustomerDTO updateById(UUID customerId, CustomerDTO customer);

    void deleteById(UUID customerId);

    void patchById(UUID customerId, CustomerDTO customer);
}
