package com.prasant.spring6restmvc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer {
    @Id
    private UUID id;
    @Version
    private int version;
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
