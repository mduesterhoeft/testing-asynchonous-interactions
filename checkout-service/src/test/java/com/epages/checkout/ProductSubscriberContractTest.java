package com.epages.checkout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CheckoutServiceApplication.class)
@AutoConfigureStubRunner
public class ProductSubscriberContractTest {

	@Autowired
	private StubTrigger stubTrigger;

	@Autowired
	private ProductRefRepository productRefRepository;


	@Test
	public void should_handle_product_created_event() {
		//emit a message from contract with label "product.created.event"
		stubTrigger.trigger("product.created.event");

		then(productRefRepository.exists(9L)).isTrue();
	}
}
