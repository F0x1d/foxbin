package com.f0x1d.foxbin;

import com.f0x1d.foxbin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.Executors;

@SpringBootApplication
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

	@EventListener(ApplicationReadyEvent.class)
	public void startTokensChecker() {
		Executors.newSingleThreadExecutor().execute(() -> {
			while (true) {
				mUserRepository.checkTokens();

				try {
					Thread.sleep(Constants.HALF_DAY_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
