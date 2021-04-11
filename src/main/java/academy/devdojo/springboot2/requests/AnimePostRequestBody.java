package academy.devdojo.springboot2.requests;

//Representa uma classe DTO

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;


@Data //vai gerar get, set, equals, hashcode, toString
//@Builder permite que você produza automaticamente o código necessário para que sua classe seja instanciada com códigos como:
//Person.builder().name("Adam Savage").city("San Francisco").job("Mythbusters").job("Unchained Reaction").build();
//@Builder pode ser colocado em uma classe, ou em um construtor, ou em um método.
@Builder //produz APIs de construtor complexas para suas classes
public class AnimePostRequestBody {
    //anotação da dependencia spring-boot-starter-validation, validando o atributo name passado no POST, não podendo ser vazio e nem nulo
    @NotEmpty(message = "The anime name cannot be empty")
    private String name;
}
