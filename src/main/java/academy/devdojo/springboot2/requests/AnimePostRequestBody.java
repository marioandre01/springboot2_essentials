package academy.devdojo.springboot2.requests;

//Representa uma classe DTO

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;


@Data //vai gerar get, set, equals, hashcode, toString
public class AnimePostRequestBody {
    //anotação da dependencia spring-boot-starter-validation, validando o atributo name passado no POST, não podendo ser vazio e nem nulo
    @NotEmpty(message = "The anime name cannot be empty")
    private String name;
}
