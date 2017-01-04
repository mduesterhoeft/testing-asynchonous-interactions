package com.epages;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.Assert.fail;
import static org.springframework.amqp.core.MessageProperties.CONTENT_TYPE_JSON;

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import com.epages.checkout.ProductRefRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CheckoutServiceApplication.class)
@AutoConfigureMessageVerifier //just here to get the RabbitMockConnectionFactoryAutoConfiguration
public class ProductSubscriberHardcodedPayloadTest {

	@Autowired
	private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

	@Autowired
	private ProductRefRepository productRefRepository;


	@Test
	public void should_handle_product_created_event() {
		//GIVEN
		String payload = "{\n" +
				"  \"id\": 8,\n" +
				"  \"name\": \"Awesome Jeans\",\n" +
				"  \"salesPrice\": 49.99\n" +
				"}";

		//WHEN
		sendAndCreateMessage(payload);

		//THEN
		then(productRefRepository.exists(8L)).isTrue();
	}

	private void sendAndCreateMessage(String payload) {
		Message message = org.springframework.amqp.core.MessageBuilder
				.withBody(payload.getBytes())
				.andProperties(
						MessagePropertiesBuilder.newInstance()
								.setContentType(CONTENT_TYPE_JSON).build())
				.build();

		SimpleMessageListenerContainer listenerContainer = getListenerContainer();

		((MessageListener) listenerContainer.getMessageListener()).onMessage(message);
	}

	private SimpleMessageListenerContainer getListenerContainer() {

			for (MessageListenerContainer listenerContainer : this.rabbitListenerEndpointRegistry.getListenerContainers()) {
				if (listenerContainer instanceof SimpleMessageListenerContainer) {
					SimpleMessageListenerContainer simpleMessageListenerContainer = (SimpleMessageListenerContainer) listenerContainer;
					if (Stream.of(simpleMessageListenerContainer.getQueueNames()).anyMatch(queueName -> queueName.equals("test.queue"))) {
						return simpleMessageListenerContainer;
					}
				}
			}
			fail("No SimpleMessageListenerContainer found for test.queue");
			return null;
	}

}
