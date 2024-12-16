package com.umanski.eventfinder.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@RequiredArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "creation_timestamp")
    private LocalDateTime creationTimestamp;

    @UpdateTimestamp
    @Column(nullable = false, name = "update_timestamp")
    private LocalDateTime updateTimestamp;

}
