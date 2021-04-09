package academy.devdojo.springboot2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data //vai gerar get, set, equals, hashcode, toString
@AllArgsConstructor //gerar um construtor com todos os atributos da classe anime

@NoArgsConstructor //Criar construtor sem padrão JPA
@Entity //transformar a classe anime em entidade
@Builder //Builder do lombok, pra usar na classe AnimeService no metodo save()
public class Anime {
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;



}
