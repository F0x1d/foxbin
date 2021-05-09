package com.f0x1d.foxbin;

import com.f0x1d.foxbin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoxbinApplication {

	@Autowired
	private UserRepository mUserRepository;

	public static void main(String[] args) {
		SpringApplication.run(FoxbinApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void createRootUser() {
		mUserRepository.createRootUser();
	}
}
