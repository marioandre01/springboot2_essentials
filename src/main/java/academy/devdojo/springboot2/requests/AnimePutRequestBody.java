package academy.devdojo.springboot2.requests;

//Representa uma classe DTO

import lombok.Data;

@Data //vai gerar get, set, equals, hashcode, toString
public class AnimePutRequestBody {
    private Long id;
    private String name;
}
