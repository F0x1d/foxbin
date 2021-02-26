package com.f0x1d.foxbin;

import com.f0x1d.foxbin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class FoxbinApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(FoxbinApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void createRootUser() {
		userRepository.createRootUser();
	}
}
