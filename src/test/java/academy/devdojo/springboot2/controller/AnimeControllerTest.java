package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
}