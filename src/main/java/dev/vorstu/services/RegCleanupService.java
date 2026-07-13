package dev.vorstu.services;

import dev.vorstu.repositories.RegRequestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class RegCleanupService {

    private final RegRequestRepository regRequestRepository;

    public RegCleanupService(RegRequestRepository regRequestRepository) {
        this.regRequestRepository = regRequestRepository;
    }

    // Запускать каждый день в 3:00 ночи
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanProcessedOrExpiredRequests() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = regRequestRepository.deleteProcessedOrExpired(now);
        System.out.println("Deleted " + deletedCount + " processed or expired registration requests.");
    }
}
