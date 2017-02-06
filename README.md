# Testing asynchronous interactions in distributed systems using Spring Cloud Contract

This is a sample application code of the blog post _Testing asynchronous interactions in distributed systems using Spring Cloud Contract_ [published in the epages developer blog](https://developer.epages.com/blog/2017/01/17/how-to-test-eventbased-services-using-contracts.html).

It illustrates how [Spring Cloud Contract](https://cloud.spring.io/spring-cloud-contract/) can be used to test asynchronous service interactions.

Please see the blog post for details.

## Run the application

A `docker-compose` setup exists to start the `product-service`, the `checkout-service`, and RabbitMQ.

First build the services to generate the jar files. And then use `docker-compose` to start the services.

```bash
cd <checkout-directory>/product-service
./gradlew check bootRepackage
```

For the product service we also need to make sure that the contracts have been published to the local maven repository.

```bash
./gradlew publishToMavenLocal
```

```bash
cd <checkout-directory>/checkout-service
./gradlew check bootRepackage
```

```bash
docker-compose up -d
```

## Create a Product and add it to a cart

We use [httpie](https://github.com/jkbrzt/httpie) as command line HTTP client.

*Create a product*

```bash
http POST <docker-ip>:8081/products name=some salesPrice=49.99 purchasePrice=39.99
```

*Create a cart*

```bash
http POST <docker-ip>:8082/carts
```
Use the uri in the location header for the next request

*Add a product to the cart*

The product data should be available in the cart now and we should be able to add it.
```bash
http PUT <docker-ip>:8082/carts/1 quantity=2 productId=1
```
