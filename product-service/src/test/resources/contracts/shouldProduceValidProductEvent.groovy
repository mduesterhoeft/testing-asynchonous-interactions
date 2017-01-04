package contracts

org.springframework.cloud.contract.spec.Contract.make {
	description 'should produce valid product event'
	label 'product.created.event'
	input {
		// the contract will be triggered by a method
		triggeredBy('emitProductCreatedEvent()')
	}
	outputMessage {
		sentTo 'test-exchange'
		headers {
			header('contentType': 'application/json')
		}
		body ([
				id: $(consumer(9), producer(regex("[0-9]+"))),
				name: "Awesome Jeans",
				salesPrice: 49.99,
				purchasePrice: 25.00
		])
	}
}
