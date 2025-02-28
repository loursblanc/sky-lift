package fr.apsprevoyance.skylift.exception.response;

import java.util.Collections;
import java.util.List;

public class ErrorResponse {
    private final int status;
    private final String message;
    private final List<String> errors;

    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors != null ? errors : Collections.emptyList();
    }

    public ErrorResponse(int status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(error);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}