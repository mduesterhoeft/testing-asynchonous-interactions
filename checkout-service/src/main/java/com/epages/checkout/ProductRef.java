package com.epages.checkout;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE) //why JPA why?
@Getter
@Setter
public class ProductRef {

	@Id
	@Setter(NONE)
	private Long id;

	private String name;

	private BigDecimal salesPrice;
}