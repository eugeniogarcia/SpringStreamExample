package com.thehecklers.scstsource;

import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ScstSourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScstSourceApplication.class, args);
	}

}

@EnableBinding(Source.class)
@EnableScheduling
class CoffeeSender {
	private final Source source;
	private final CoffeeGenerator generator;

	public Source getSource() {
		return source;
	}

	public CoffeeGenerator getGenerator() {
		return generator;
	}

	public CoffeeSender(Source source, CoffeeGenerator generator) {
		super();
		this.source = source;
		this.generator = generator;
	}

	@Scheduled(fixedRate = 1000)
	private void send() {
		final WholesaleCoffee coffee = generator.generate();

		System.out.println(coffee);
		source.output().send(MessageBuilder.withPayload(coffee).build());
	}
}

@Component
@RefreshScope
class CoffeeGenerator {
	@Value("${names:a,b,c,d,e,f}")
	private String[] names;

	private final Random rnd = new Random();
	private int i = 0;

	@PostConstruct
	private void showConfig() {
		System.out.println("List of Available Coffees: " + String.join(",", names));
	}

	WholesaleCoffee generate() {
		i = rnd.nextInt(names.length);

		final WholesaleCoffee wCoffee = new WholesaleCoffee(UUID.randomUUID().toString(), names[i]);
		return wCoffee;
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