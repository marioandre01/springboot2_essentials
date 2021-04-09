package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //anotação para criar teste
@DisplayName("Tests for Anime Repository") //anotação para colocar um nome para o teste, se não usar essa anotação vai ficar o nome da classe "AnimeRepositoryTest"
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when Successful") //anotação para colocar um nome para cada teste feito
    void test(){
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
    }

    private Anime createAnime(){
       return Anime.builder()
               .name("Hajime no ippo")
               .build();
    }
}

//aqui se esta testando o repositorio para ver se ele esta inserindo de acordo como definimos, não esta testando a aplicação em si
//se esta se testando se as querys estão funcionando como o esperado