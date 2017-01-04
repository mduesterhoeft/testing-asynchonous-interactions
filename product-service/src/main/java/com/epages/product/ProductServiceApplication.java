package com.epages.product;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ProductServiceApplication {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Bean
	public MessageConverter messageConverter(ObjectMapper objectMapper) {
		Jackson2JsonMessageConverter jsonMessageConverter = new Jackson2JsonMessageConverter();
		jsonMessageConverter.setJsonObjectMapper(objectMapper);
		jsonMessageConverter.setCreateMessageIds(true);
		ContentTypeDelegatingMessageConverter messageConverter = new ContentTypeDelegatingMessageConverter(jsonMessageConverter);
		messageConverter.addDelegate("application/json", jsonMessageConverter);
		return messageConverter;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
}
