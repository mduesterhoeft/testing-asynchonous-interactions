/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package com.epages.product;

import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@AutoConfigureMockMvc
@AutoConfigureMessageVerifier
public class ProductApiTest {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void should_create_product() throws Exception {
		HashMap<Object, Object> jsonInput = new HashMap<>();
		jsonInput.put("name", "Awesome Jeans");
		jsonInput.put("salesPrice", 49.99);
		jsonInput.put("purchasePrice", 29.99);

		mockMvc.perform(post("/products")
				.content(objectMapper.writeValueAsString(jsonInput))
				.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());

		then(rabbitTemplate).should().convertAndSend(eq("test-exchange"), eq("person.created.event"), any(Product.class), any(CorrelationData.class));
	}
}
