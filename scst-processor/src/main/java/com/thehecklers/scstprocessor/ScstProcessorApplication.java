package com.thehecklers.scstprocessor;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
public class ScstProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScstProcessorApplication.class, args);
	}

}

@EnableBinding(Processor.class)
@MessageEndpoint
class CoffeeTransformer {
	private final Random rnd = new Random();

	@StreamListener(Processor.INPUT)
	@SendTo(Processor.OUTPUT)
	RetailCoffee transform(WholesaleCoffee wCoffee) {

		final RetailCoffee rCoffee = new RetailCoffee(wCoffee.getId(),
				wCoffee.getName(),
				rnd.nextInt(2) == 0 ? RetailCoffee.CoffeeState.WHOLE_BEAN : RetailCoffee.CoffeeState.GROUND);

		System.out.println(rCoffee);

		return rCoffee;
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

class WholesaleCoffee {
	private final String id, name;

	public WholesaleCoffee(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}


}