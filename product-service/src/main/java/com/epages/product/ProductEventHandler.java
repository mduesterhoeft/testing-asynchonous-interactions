
package com.epages.product;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
@RequiredArgsConstructor
public class ProductEventHandler {

	private final RabbitTemplate rabbitTemplate;

	@HandleAfterCreate
	public void handleCreated(Product product) {
		this.rabbitTemplate.convertAndSend("test-exchange", "person.created.event", product);
	}

	@HandleAfterSave
	public void handleUpdate(Product product) {
		this.rabbitTemplate.convertAndSend("test-exchange", "person.updated.event", product);
	}
}


