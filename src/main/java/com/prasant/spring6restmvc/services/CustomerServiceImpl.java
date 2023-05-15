package com.prasant.spring6restmvc.services;

import com.prasant.spring6restmvc.model.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, CustomerDTO> customerMap;
    public CustomerServiceImpl() {

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("John Doe")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Sunil Pal")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Nitin Gadkari")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.customerMap = new HashMap<>();
        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public List<CustomerDTO> list() {
        log.debug("CustomerServiceImpl.list() called.");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getById(UUID customerId) {
        log.debug("CustomerServiceImpl.getById() called. UUID = " + customerId.toString());
        return Optional.ofNullable(customerMap.get(customerId));
    }

    @Override
    public CustomerDTO save(CustomerDTO customer) {
        log.debug("CustomerServiceImpl.save() called.");

        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public CustomerDTO updateById(UUID customerId, CustomerDTO customer) {
        log.debug("CustomerServiceImpl.updateById() called. UUID = " + customerId);

        CustomerDTO existingCustomer = customerMap.get(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setUpdateDate(LocalDateTime.now());

        customerMap.put(customerId, existingCustomer);
        return existingCustomer;
    }

    @Override
    public void deleteById(UUID customerId) {
        log.debug("CustomerServiceImpl.deleteById() called. UUID = " + customerId);
        customerMap.remove(customerId);
    }

    @Override
    public void patchById(UUID customerId, CustomerDTO customer) {
        log.debug("CustomerServiceImpl.patchById() called. UUID = " + customerId);
        CustomerDTO existingCustomer = customerMap.get(customerId);
        if (StringUtils.hasText(customer.getCustomerName())) {
            existingCustomer.setCustomerName(customer.getCustomerName());
        }
        existingCustomer.setVersion(existingCustomer.getVersion() + 1);
        existingCustomer.setUpdateDate(LocalDateTime.now());
    }
}
