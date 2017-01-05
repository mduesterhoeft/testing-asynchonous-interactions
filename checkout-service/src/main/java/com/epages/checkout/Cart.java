package com.epages.checkout;

import lombok.Data;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
public class Cart implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToMany(cascade = ALL, orphanRemoval = true)
    private List<ProductLineItem> lineItems = new ArrayList<>();
}
