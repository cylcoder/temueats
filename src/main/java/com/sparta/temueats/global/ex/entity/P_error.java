package com.sparta.temueats.global.ex.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "P_ERROR")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class P_error {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID errorId;

    private String exceptionType;

    private String message;

    @Column(columnDefinition = "text")
    private String stackTrace;

    private LocalDateTime timestamp;

}
