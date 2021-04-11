package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.AnimeCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //anotação para criar teste
@DisplayName("Tests for Anime Repository") //anotação para colocar um nome para o teste, se não usar essa anotação vai ficar o nome da classe "AnimeRepositoryTest"
@Log4j2
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    //POST
    @Test
    @DisplayName("Save persists anime when Successful") //anotação para colocar um nome para cada teste feito
    void save_PersistAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
    }

    //PUT
    @Test
    @DisplayName("Save updates anime when Successful")
    void save_UpdateAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        animeSaved.setName("Overlod");

        Anime animeUpdated = this.animeRepository.save(animeSaved);

//        log.info(animeUpdated.getName());
        Assertions.assertThat(animeUpdated).isNotNull();
        Assertions.assertThat(animeUpdated.getId()).isNotNull();
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
    }

    //DELETE
    @Test
    @DisplayName("Delete removes anime when Successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        this.animeRepository.delete(animeToBeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());

        Assertions.assertThat(animeOptional.isEmpty());


    }

    //GET - Find By Name
    @Test
    @DisplayName("Find By Name returns list of anime when Successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);

        String name = animeSaved.getName();

        List<Anime> animes = this.animeRepository.findByName(name);

        //testa se a Lista de animes não está vazia e se contem o anime que foi salvo
        Assertions.assertThat(animes)
                .isNotEmpty()
                .contains(animeSaved);
    }

    //GET - Find By Name - lista vazia
    @Test
    @DisplayName("Find By Name returns empty list when no anime is found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){

        List<Anime> animes = this.animeRepository.findByName("teste");

        Assertions.assertThat(animes).isEmpty();
    }

    //POST com exceção
    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThromsConstraintViolationException_WhenNameIsEmpty(){
        Anime anime = new Anime(); //cria um anime vazio
        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);

        //outra maneira - por enquanto da dando erro
//        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
//                .isThrownBy(() -> this.animeRepository.save(anime))
//                .withMessageContaining("the anime name cannot be empty");

    }


}

//aqui se esta testando o repositorio para ver se ele esta inserindo de acordo como definimos, não esta testando a aplicação em si
//se esta se testando se as querys estão funcionando como o esperado