package com.thehecklers.scstsink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;

@SpringBootApplication
public class ScstSinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScstSinkApplication.class, args);
	}

}

@EnableBinding(Sink.class)
@MessageEndpoint
class CoffeeDrinker {

	@StreamListener(value = Sink.INPUT)
	private void drink(RetailCoffee coffee) {
		System.out.println(coffee);
	}

}

class RetailCoffee {
	enum CoffeeState {
		WHOLE_BEAN,
		GROUND
	}

	private final String id, name;
	private final CoffeeState state;

	public RetailCoffee(String id, String name, CoffeeState state) {
		super();
		this.id = id;
		this.name = name;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public CoffeeState getState() {
		return state;
	}

}