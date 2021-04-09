package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.exception.BadRequestExceptionDetails;
import academy.devdojo.springboot2.exception.ValidationExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//porque chamar de RestExceptionHandler, pois pode haver diversos tipos de sessão nessa classe
//como falar pro controller que essa é uma classe que contem informaçãoes que o Controler precisa utilizar, usar a anotação @ControllerAdvice
//ao usar @ControllerAdvice, basicamente esta dizendo para todos os controllers que eles tem que utilizar o que esta dentro da classe RestExceptionHandler
//baseado em algo como se fosse uma flag, a flag é @ExceptionHandler
//a anotação @ExceptionHandler diz, se for lançada uma excessão e ela for do tipo BadRequestException tem que se utilizar o metodo da classe e retornar o seu valor

//esta se falando para os controllres, caso se tenha uma exceção do tipo BAD_REQUEST vai se utilizar o @ExceptionHandler e
//vai retornar o valor definido no metodo handlerBadRequestException

@ControllerAdvice
@Log4j2
public class RestExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException bre){
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Bad Request Exception, Check the Documentation")
                .details(bre.getMessage())
                .developerMessage(bre.getClass().getName())
                .build(), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDetails> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .title("Bad Request Exception, Invalid Fields")
                    .details("Check the field(s) error")
                    .developerMessage(exception.getClass().getName())
                    .fields(fields)
                    .fieldsMessage(fieldMessage)
                    .build(), HttpStatus.BAD_REQUEST);

    }
}
