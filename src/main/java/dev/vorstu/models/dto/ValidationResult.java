package dev.vorstu.models.dto;

import java.util.List;

public class ValidationResult {
    private final boolean valid;
    private final List<String> errors;
    private final RegistrationCsvRow row;

    public ValidationResult(boolean valid, List<String> errors, RegistrationCsvRow row) {
        this.valid = valid;
        this.errors = errors;
        this.row = row;
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public RegistrationCsvRow getRow() {
        return row;
    }

    public static ValidationResult success(RegistrationCsvRow row) {
        return new ValidationResult(true, List.of(), row);
    }

    public static ValidationResult failure(List<String> errors, RegistrationCsvRow row) {
        return new ValidationResult(false, errors, row);
    }
}