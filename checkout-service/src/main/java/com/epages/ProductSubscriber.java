/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package com.epages;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.epages.checkout.ProductRef;
import com.epages.checkout.ProductRefRepository;

@Component
@RequiredArgsConstructor
public class ProductSubscriber {

	private final ProductRefRepository productRefRepository;

	@RabbitListener(bindings = @QueueBinding(
			value = @Queue(value = "test.queue"),
			exchange = @Exchange(value = "test-exchange",
			ignoreDeclarationExceptions = "true")))
	public void handleProductEvent(ProductRef product) {
		productRefRepository.save(product);
	}


}
