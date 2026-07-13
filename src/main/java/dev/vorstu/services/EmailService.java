package dev.vorstu.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Асинхронно отправляет письмо для подтверждения регистрации.
     *
     * @param to       email получателя
     * @param fullName ФИО пользователя (для персонализации)
     * @param token    уникальный токен для ссылки подтверждения
     */
    @Async
    public void sendRegistrationEmail(String to, String fullName, String token) {
        try {
            String subject = "Подтверждение регистрации";
            String confirmationLink = baseUrl + "/api/registration/confirm-registration?token=" + token;
            String text = String.format(
                    "Здравствуйте, %s!\n\n" +
                            "Для завершения регистрации перейдите по ссылке:\n%s\n\n" +
                            "Ссылка действительна в течение 24 часов.\n" +
                            "Если вы не регистрировались, проигнорируйте это письмо.",
                    fullName, confirmationLink
            );

            sendEmail(to, subject, text);
            log.info("Registration email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send registration email to {}: {}", to, e.getMessage());
        }
    }

    /**
     * Отправляет приветственное письмо с логином и паролем после успешной регистрации.
     *
     * @param to       email пользователя
     * @param username логин
     * @param password временный пароль (или сгенерированный)
     */
    @Async
    public void sendWelcomeEmail(String to, String username, String password) {
        try {
            String subject = "Добро пожаловать!";
            String text = String.format(
                    "Здравствуйте!\n\n" +
                            "Ваша учётная запись создана.\n" +
                            "Логин: %s\n" +
                            "Пароль: %s\n\n" +
                            "Рекомендуем сменить пароль после первого входа. Но у нас пока нет функционала так сделать, поэтому просто бойтесь",
                    username, password
            );

            sendEmail(to, subject, text);
            log.info("Welcome email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", to, e.getMessage());
        }
    }

    /**
     * Базовая отправка письма.
     */
    private void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false);
        mailSender.send(message);
    }
}
