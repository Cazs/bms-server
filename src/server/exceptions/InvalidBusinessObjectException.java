package server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Invalid BusinessObject. Please check object attributes and make sure they conform to standard.")  // 409
public class InvalidBusinessObjectException extends RuntimeException
{
    /*public InvalidBusinessObjectException(String message)
    {
        super(message);
    }*/
}
