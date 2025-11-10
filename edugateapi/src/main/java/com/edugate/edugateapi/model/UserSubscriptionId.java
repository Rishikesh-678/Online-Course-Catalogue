package com.edugate.edugateapi.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserSubscriptionId implements Serializable {
    private Long userId;
    private Long courseId;
}