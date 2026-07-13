package dev.vorstu.services;

import dev.vorstu.models.dto.ValidationResult;
import dev.vorstu.models.dto.RegistrationCsvRow;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@NoArgsConstructor
public class RegistrationValidator {
    public ValidationResult validateRow(RegistrationCsvRow row, int lineNumber) {
        List<String> errors = new ArrayList<>();

        // 1. Common validations
        if (row.getUserName() == null || row.getUserName().isBlank()) {
            errors.add("Username is required");
        }

        if (row.getEmail() == null || !isValidEmail(row.getEmail())) {
            errors.add("Invalid email format");
        }

        String role = row.getRole();
        if (role == null || (!"STUDENT".equals(role) && !"TEACHER".equals(role))) {
            errors.add("Role must be STUDENT or TEACHER");
        }
        else {
            if ("STUDENT".equals(row.getRole())) {
                validateStudentFields(row, errors);
            } else if ("TEACHER".equals(row.getRole())) {
                validateTeacherFields(row, errors);
            }
        }

        return new ValidationResult(errors.isEmpty(), errors, row);
    }

    private void validateStudentFields(RegistrationCsvRow row, List<String> errors) {
        if (row.getGroupId() == null) {
            errors.add("Group id is required for STUDENT");
        }
        if (row.getPhoneNumber() == null || row.getPhoneNumber().isBlank()) {
            errors.add("Phone number is required for STUDENT");
        }
        if (row.getFio() == null || row.getFio().isBlank()) {
            errors.add("FIO is required for STUDENT");
        }
    }

    private void validateTeacherFields(RegistrationCsvRow row, List<String> errors) {
        if (row.getPhoneNumber() == null || row.getPhoneNumber().isBlank()) {
            errors.add("Phone number is required for TEACHER");
        }
        if (row.getFio() == null || row.getFio().isBlank()) {
            errors.add("FIO is required for TEACHER");
        }
        if (row.getSubject() == null || row.getSubject().isBlank()) {
            errors.add("Subject is required for TEACHER");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
