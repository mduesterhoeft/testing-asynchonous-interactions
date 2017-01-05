
package com.epages.product;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@AutoConfigureMessageVerifier
public abstract class ProductPublisherTestBase {

	@Autowired
	private ProductEventHandler productEventHandler;

	public void emitProductCreatedEvent() {
		Product product = new Product(1L, "Awesome Jeans",
				BigDecimal.valueOf(49.99), BigDecimal.valueOf(25.00));
		productEventHandler.handleCreated(product);
	}
}
