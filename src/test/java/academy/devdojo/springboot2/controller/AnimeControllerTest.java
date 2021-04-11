package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//quando trabalha com teste unitario utilizar a anotação @ExtendWith(SpringExtension.class)
// como SpringExtension.class esta se dizendo que se quer usar o Junit com o spring
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    //@InjectMocks se utiliza quando que se testar a classe em si, no caso a classe AnimeController
    @InjectMocks
    private AnimeController animeController;

    //@Mock se utiliza quando que se testar a classe que AnimeController utiliza, como a classe DateUtil e a classe AnimeService
    @Mock
    private AnimeService animeServiceMock;

    //@BeforeEach - Antes de cada um dos teste executa o metodo setup()
    @BeforeEach
    void setup(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        //quando alguem executar dentro do controller uma chamada pro animeService.listAll(), não importando o argumento passado() não importa o que ele receba
        //pode ser nulo ou qualquer outra coisa. Quando alguem chamar o listAll() então retorne animePage
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        //Criado comportamento com Mockito pro animeService pro listAllNonPageable() para o teste listAll_ReturnsListOfAnimes_WhenSuccessful()
        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        //ArgumentMatchers.anyLong() - qualquer agurmento passado vai retornar AnimeCreator.createValidAnime()
        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        //ArgumentMatchers.anyString() - Ao passar qualquer tipo de String retorne List.of(AnimeCreator.createValidAnime()
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        //ao passar como argumento no save() AnimePostRequestBody.class, se retorna AnimeCreator.createValidAnime()
        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        //quando se passar o argumento AnimePutRequestBody.class se quer que retorne
        //quando o retorno retorna void deve-se alterar a forma de uso do BDDMockito
        //doNothing() - não faz nada , dizendo pro Mockito que quando chamar replace não fazer nada
        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        //Comportamento delete
        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());

    }

    @Test
    @DisplayName("List returns list of anime page object when successfull")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns list of anime when successfull")
    void listAll_ReturnsListOfAnimes_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns anime when successfull")
    void findById_ReturnsAnime_WhenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName returns a list of anime when successfull")
    void findByName_ReturnsListOfAnimes_WhenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnimes_WhenAnimeIsNotFound(){
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("save returns anime when successfull")
    void save_ReturnsAnime_WhenSuccessful(){

        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());


    }

    @Test
    @DisplayName("Replace updates anime when successfull")
    void replace_UpdatesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete removes anime when successfull")
    void delete_RemovesAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.delete(1))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}