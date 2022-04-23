package me.silvios.wasp.itemrelay;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class ItemRelayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemRelayApplication.class, args);
	}

}
