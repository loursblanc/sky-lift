package fr.apsprevoyance.skylift.exception.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import fr.apsprevoyance.skylift.constants.ErrorMessageConstants;

@Tag("exception")
class ErrorResponseTest {

    private static final int HTTP_STATUS = 400;
    private static final String ERROR_MESSAGE = ErrorMessageConstants.Validation.PREFIX_MODEL;
    private static final List<String> ERROR_LIST = Arrays.asList(ErrorMessageConstants.Errors.NAME_NULL,
            ErrorMessageConstants.Errors.TYPE_NULL);

    @Test
    void constructor_with_list_should_set_properties_correctly() {
        ErrorResponse response = new ErrorResponse(HTTP_STATUS, ERROR_MESSAGE, ERROR_LIST);

        assertEquals(HTTP_STATUS, response.getStatus());
        assertEquals(ERROR_MESSAGE, response.getMessage());
        assertEquals(ERROR_LIST.size(), response.getErrors().size());
        assertTrue(response.getErrors().contains(ErrorMessageConstants.Errors.NAME_NULL));
        assertTrue(response.getErrors().contains(ErrorMessageConstants.Errors.TYPE_NULL));
    }

    @Test
    void constructor_with_single_error_should_create_singleton_list() {
        String singleError = ErrorMessageConstants.Errors.NAME_NULL;
        ErrorResponse response = new ErrorResponse(HTTP_STATUS, ERROR_MESSAGE, singleError);

        assertEquals(HTTP_STATUS, response.getStatus());
        assertEquals(ERROR_MESSAGE, response.getMessage());
        assertEquals(1, response.getErrors().size());
        assertEquals(singleError, response.getErrors().get(0));
    }

    @Test
    void errors_list_should_be_unmodifiable() {
        ErrorResponse response = new ErrorResponse(HTTP_STATUS, ERROR_MESSAGE, ERROR_LIST);

        assertNotNull(response.getErrors());
        try {
            response.getErrors().add("New Error");
            assertTrue(false, "Errors list should be unmodifiable");
        } catch (UnsupportedOperationException ex) {
            assertTrue(true);
        }
    }

    @Test
    void constructor_with_null_errors_should_create_empty_list() {
        ErrorResponse response = new ErrorResponse(HTTP_STATUS, ERROR_MESSAGE, (List<String>) null);

        assertNotNull(response.getErrors());
        assertTrue(response.getErrors().isEmpty());
    }

    @Test
    void errors_should_match_constructor_input() {
        List<String> customErrors = Arrays.asList(ErrorMessageConstants.Errors.ID_NULL,
                ErrorMessageConstants.Errors.NAME_EMPTY);

        ErrorResponse response = new ErrorResponse(HTTP_STATUS, ERROR_MESSAGE, customErrors);

        assertEquals(customErrors.size(), response.getErrors().size());
        for (String error : customErrors) {
            assertTrue(response.getErrors().contains(error));
        }
    }
}