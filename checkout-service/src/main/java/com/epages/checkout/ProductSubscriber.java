
package com.epages.checkout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.springframework.amqp.core.ExchangeTypes.TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductSubscriber {

	private final ProductRefRepository productRefRepository;

	@RabbitListener(bindings = @QueueBinding(
					value = @Queue(value = "test.queue"),
					exchange = @Exchange(value = "test-exchange", type = TOPIC),
					key="#"))
	public void handleProductEvent(ProductRef product) {
		ProductRef savedProduct = productRefRepository.save(product);
		log.info("saved product ref {}", savedProduct);
	}
}
