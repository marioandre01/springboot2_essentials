package academy.devdojo.springboot2.requests;

//Representa uma classe DTO

import lombok.Builder;
import lombok.Data;

@Data //vai gerar get, set, equals, hashcode, toString
//@Builder permite que você produza automaticamente o código necessário para que sua classe seja instanciada com códigos como:
//Person.builder().name("Adam Savage").city("San Francisco").job("Mythbusters").job("Unchained Reaction").build();
//@Builder pode ser colocado em uma classe, ou em um construtor, ou em um método.
@Builder //produz APIs de construtor complexas para suas classes
public class AnimePutRequestBody {
    private Long id;
    private String name;
}
