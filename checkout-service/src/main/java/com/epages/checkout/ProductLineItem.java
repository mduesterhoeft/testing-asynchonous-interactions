package com.epages.checkout;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Data
@Builder
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class ProductLineItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    private ProductRef product;
}
