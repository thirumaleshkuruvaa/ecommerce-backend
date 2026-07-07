package com.ecommerce.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name is required")
    private String name;

    @Column(unique = true, nullable = false)
    private String categoryId;

    @ManyToOne
    private Category parentCategory;

    @Min(value = 0, message = "Level cannot be negative")
    private Integer level;

    // AUTO-GENERATE BEFORE SAVE
    @PrePersist
    public void generateCategoryId() {
        if (this.categoryId == null) {
            this.categoryId = UUID.randomUUID().toString();
        }
    }
}