
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
