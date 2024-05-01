package com.example.experiment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
public abstract class CreatedAt {

    @CreatedDate
    @Column(
            name = "created_at",
            columnDefinition = "timestamp default now()",
            updatable = false,
            nullable = false
    )
    protected LocalDateTime createdAt;

}
