package talium.system;

import talium.system.panelAuth.exceptions.AuthenticationRejected;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {AuthenticationRejected.class})
    public String authorizationRejected(AuthenticationRejected e) {
        return "Authentication rejected or expired!";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public String generalException(Exception e) {
        return "An Exception occurred!";
    }
}
