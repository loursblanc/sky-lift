package fr.apsprevoyance.skylift.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;
import fr.apsprevoyance.skylift.exception.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {

        log.warn(ErrorMessageConstants.Logs.VALIDATION_FAILED, ex.getModelName(), ex.getContextType(),
                String.join(", ", ex.getValidationErrors()));

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), getErrorPrefix(ex),
                ex.getValidationErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn(ErrorMessageConstants.Logs.METHOD_ARGS_VALIDATION_FAILED, ex.getMessage());

        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(this::formatFieldError)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ErrorMessageConstants.Validation.PREFIX_REQUEST, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorDetail = ErrorMessageConstants.Validation.REQUEST_PARSING_ERROR;
        String logDetail = ex.getMessage();

        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            if (rootCause instanceof JsonParseException) {
                JsonParseException jpe = (JsonParseException) rootCause;
                JsonLocation location = jpe.getLocation();

                logDetail = String.format(ErrorMessageConstants.Json.PARSE_ERROR, location.getLineNr(),
                        location.getColumnNr(), jpe.getOriginalMessage());

                errorDetail = String.format(ErrorMessageConstants.Json.PARSE_ERROR_SAFE, location.getLineNr(),
                        location.getColumnNr());
            } else if (rootCause instanceof UnrecognizedPropertyException) {
                UnrecognizedPropertyException upe = (UnrecognizedPropertyException) rootCause;

                logDetail = String.format(ErrorMessageConstants.Json.UNKNOWN_PROPERTY, upe.getPropertyName());

                errorDetail = String.format(ErrorMessageConstants.Json.UNKNOWN_PROPERTY_SAFE, upe.getPropertyName());
            } else if (rootCause instanceof JsonMappingException) {
                logDetail = rootCause.getMessage();
                errorDetail = ErrorMessageConstants.Json.MAPPING_ERROR_SAFE;
            } else {
                logDetail = rootCause.getMessage();
                errorDetail = ErrorMessageConstants.Validation.REQUEST_PARSING_ERROR;
            }
        }

        log.warn(ErrorMessageConstants.Logs.REQUEST_PARSING_FAILED, logDetail);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ErrorMessageConstants.Validation.PREFIX_REQUEST, errorDetail);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {

        log.warn(ErrorMessageConstants.Logs.ENTITY_NOT_FOUND, ex.getEntityName(), ex.getIdentifier());

        String clientMessage = String.format(ErrorMessageConstants.Validation.ENTITY_NOT_FOUND_SAFE,
                sanitizeEntityName(ex.getEntityName()), ex.getIdentifier());

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ErrorMessageConstants.Validation.PREFIX_PERSISTENCE, clientMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEntityException(DuplicateEntityException ex) {

        log.warn(ErrorMessageConstants.Logs.DUPLICATE_ENTITY, ex.getEntityName(), ex.getField(), ex.getValue());

        String clientMessage = String.format(ErrorMessageConstants.Validation.DUPLICATE_ENTITY_SAFE,
                sanitizeEntityName(ex.getEntityName()));

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(),
                ErrorMessageConstants.Validation.PREFIX_BUSINESS, clientMessage);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        log.error(ErrorMessageConstants.Logs.UNCAUGHT_EXCEPTION, ex);

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ErrorMessageConstants.General.INTERNAL_ERROR, List.of(ErrorMessageConstants.General.INTERNAL_ERROR));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private String getErrorPrefix(ValidationException ex) {
        switch (ex.getContextType()) {
        case MODEL:
            return ErrorMessageConstants.Validation.PREFIX_MODEL;
        case REQUEST:
            return ErrorMessageConstants.Validation.PREFIX_REQUEST;
        case BUSINESS_RULE:
            return ErrorMessageConstants.Validation.PREFIX_BUSINESS;
        case PERSISTENCE:
            return ErrorMessageConstants.Validation.PREFIX_PERSISTENCE;
        default:
            return ErrorMessageConstants.Validation.PREFIX_DEFAULT;
        }
    }

    private String formatFieldError(FieldError fieldError) {
        return String.format(ErrorMessageConstants.Formats.FIELD_ERROR, fieldError.getField(),
                fieldError.getDefaultMessage());
    }

    private String sanitizeEntityName(String entityName) {
        switch (entityName) {
        case "SkiLift":
            return "ski lift";
        case "Sport":
            return "sport";
        default:
            return "resource";
        }
    }
}