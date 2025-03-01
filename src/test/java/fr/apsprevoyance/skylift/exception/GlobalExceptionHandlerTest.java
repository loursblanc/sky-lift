package fr.apsprevoyance.skylift.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.enums.ValidationContextType;
import fr.apsprevoyance.skylift.exception.response.ErrorResponse;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void handleValidationException_shouldReturnBadRequestWithErrors() {
        List<String> errors = Collections.singletonList(ErrorMessageConstants.Errors.NAME_NULL);
        ValidationException exception = new ValidationException("Sport", ValidationContextType.MODEL, errors);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_MODEL, response.getBody().getMessage());
        assertEquals(errors, response.getBody().getErrors());
    }

    @Test
    void handleEntityNotFoundException_shouldReturnNotFoundStatus() {
        EntityNotFoundException exception = new EntityNotFoundException("SkiLift", "123");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleEntityNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_PERSISTENCE, response.getBody().getMessage());
        String expectedMessage = String.format(ErrorMessageConstants.Validation.ENTITY_NOT_FOUND_SAFE, "ski lift",
                "123");
        assertEquals(expectedMessage, response.getBody().getErrors().get(0));
    }

    @Test
    void handleDuplicateEntityException_shouldReturnConflictStatus() {
        DuplicateEntityException exception = new DuplicateEntityException("Sport", "name", "Ski");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateEntityException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_BUSINESS, response.getBody().getMessage());
        String expectedMessage = String.format(ErrorMessageConstants.Validation.DUPLICATE_ENTITY_SAFE, "sport");
        assertEquals(expectedMessage, response.getBody().getErrors().get(0));
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturnBadRequestWithFieldErrors() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = mock(FieldError.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(fieldError.getField()).thenReturn("name");
        when(fieldError.getDefaultMessage()).thenReturn("Name cannot be null");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_REQUEST, response.getBody().getMessage());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals(String.format(ErrorMessageConstants.Formats.FIELD_ERROR, "name", "Name cannot be null"),
                response.getBody().getErrors().get(0));
    }

    @Test
    void handleHttpMessageNotReadable_withJsonParseException_shouldReturnBadRequest() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        JsonParseException rootCause = mock(JsonParseException.class);
        JsonLocation location = mock(JsonLocation.class);

        when(exception.getRootCause()).thenReturn(rootCause);
        when(rootCause.getLocation()).thenReturn(location);
        when(location.getLineNr()).thenReturn(10);
        when(location.getColumnNr()).thenReturn(20);
        when(rootCause.getOriginalMessage()).thenReturn("Invalid JSON");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadable(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_REQUEST, response.getBody().getMessage());
        String expectedError = String.format(ErrorMessageConstants.Json.PARSE_ERROR_SAFE, 10, 20);
        assertEquals(expectedError, response.getBody().getErrors().get(0));
    }

    @Test
    void handleHttpMessageNotReadable_withUnrecognizedPropertyException_shouldReturnBadRequest() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        UnrecognizedPropertyException rootCause = mock(UnrecognizedPropertyException.class);

        when(exception.getRootCause()).thenReturn(rootCause);
        when(rootCause.getPropertyName()).thenReturn("invalidField");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadable(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_REQUEST, response.getBody().getMessage());
        String expectedError = String.format(ErrorMessageConstants.Json.UNKNOWN_PROPERTY_SAFE, "invalidField");
        assertEquals(expectedError, response.getBody().getErrors().get(0));
    }

    @Test
    void handleHttpMessageNotReadable_withJsonMappingException_shouldReturnBadRequest() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        JsonMappingException rootCause = mock(JsonMappingException.class);

        when(exception.getRootCause()).thenReturn(rootCause);
        when(rootCause.getMessage()).thenReturn("Mapping error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadable(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_REQUEST, response.getBody().getMessage());
        assertEquals(ErrorMessageConstants.Json.MAPPING_ERROR_SAFE, response.getBody().getErrors().get(0));
    }

    @Test
    void handleHttpMessageNotReadable_withOtherException_shouldReturnBadRequest() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        RuntimeException rootCause = mock(RuntimeException.class);

        when(exception.getRootCause()).thenReturn(rootCause);
        when(rootCause.getMessage()).thenReturn("Other error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleHttpMessageNotReadable(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorMessageConstants.Validation.PREFIX_REQUEST, response.getBody().getMessage());
        assertEquals(ErrorMessageConstants.Validation.REQUEST_PARSING_ERROR, response.getBody().getErrors().get(0));
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        Exception exception = new RuntimeException("Test exception");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorMessageConstants.General.INTERNAL_ERROR, response.getBody().getMessage());
        assertEquals(List.of(ErrorMessageConstants.General.INTERNAL_ERROR), response.getBody().getErrors());
    }
}