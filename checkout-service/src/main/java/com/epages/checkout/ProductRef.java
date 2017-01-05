package com.epages.checkout;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE) //why JPA why?
@Getter
@Setter
@ToString
public class ProductRef {

	@Id
	@Setter(NONE)
	private Long id;

	private String name;

	private BigDecimal salesPrice;
}