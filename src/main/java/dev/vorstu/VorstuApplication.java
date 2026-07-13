package dev.vorstu;

import dev.vorstu.repositories.RegRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class VorstuApplication {

	private static Initializer initiator;
	private static RegRequestRepository regRequestRepository;

	@Autowired
	public void setInitialLoader(Initializer initiator){
		VorstuApplication.initiator=initiator;
	}

	public static void main(String[] args) {
		SpringApplication.run(VorstuApplication.class, args);
		//initiator.initial();
	}

	@Scheduled(cron = "0 0 3 * * ?") // каждый день в 3:00
	public void cleanExpiredRequests() {
		Object registrationRequestRepository;
		regRequestRepository.deleteByExpiryDateBeforeAndUsedFalse(LocalDateTime.now());
	}

}
