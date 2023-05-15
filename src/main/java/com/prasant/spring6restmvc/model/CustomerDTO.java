package com.prasant.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class CustomerDTO {

    private String customerName;
    private UUID id;
    private int version;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
