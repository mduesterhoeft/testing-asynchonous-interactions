/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package com.epages.checkout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMessageVerifier //just here to get the RabbitMockConnectionFactoryAutoConfiguration
public class CartApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRefRepository productRefRepository;

    private ProductRef product;
    private ResultActions resultActions;
    private String cartLocation;

    @Test
    public void should_add_product() throws Exception {
        givenProduct();
        givenCart();

        whenProductLineItemAdded();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("lineItems", hasSize(1)))
                .andExpect(jsonPath("lineItems[0].quantity", is(2)))
                .andExpect(jsonPath("lineItems[0].product.id", is(product.getId().intValue())))
        ;
    }

    private void whenProductLineItemAdded() throws Exception {
        resultActions = mockMvc.perform(put(cartLocation)
                .contentType(APPLICATION_JSON)
                .content("{\"quantity\": 2, \"productId\": " + product.getId() + "}"))
                .andDo(print());
    }

    private void givenCart() throws Exception {
        resultActions = mockMvc.perform(post("/carts"))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, not(isEmptyOrNullString())))
                .andDo(print());

        cartLocation = resultActions.andReturn().getResponse().getHeader(LOCATION);
    }

    private void givenProduct() {
        product = productRefRepository.saveAndFlush(new ProductRef(1L, "some", BigDecimal.valueOf(50)));
    }
}
