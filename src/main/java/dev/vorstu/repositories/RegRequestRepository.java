package dev.vorstu.repositories;

import dev.vorstu.models.entities.RegistrationRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegRequestRepository extends JpaRepository<RegistrationRequest, Long> {
    void deleteByExpiryDateBeforeAndUsedFalse(LocalDateTime now);

    <S extends RegistrationRequest> List<S> saveAll(Iterable<S> entities);

    @Transactional
    Optional<RegistrationRequest> findByEmailAndUsedFalseAndExpiryDateAfter(String email, LocalDateTime dateTime);

    Optional<RegistrationRequest> findByToken(String token);
}

