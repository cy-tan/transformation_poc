package com.acme.samples.mapping;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@EnableConfigurationProperties(StorageProperties.class)
public class MySpringApplication {
  /**
   * A main method to start this application.
   */
  public static void main(String[] args) {
    SpringApplication.run(MySpringApplication.class, args);
  }

  @Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
