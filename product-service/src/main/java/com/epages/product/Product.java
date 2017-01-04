/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package com.epages.product;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE) //why JPA why?
@Getter @Setter
public class Product {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Setter(NONE)
	private Long id;

	private String name;

	private BigDecimal salesPrice;

	private BigDecimal purchasePrice;

}
