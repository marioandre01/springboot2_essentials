package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody createAnimePutRequestBody(){

        //quando executar AnimePutRequestBodyCreator se quer que ele retorne um anime que foi atualizado
        //esta se mockando a resposta do nosso service e esta se criando esse valor que vai ser passado para o controller
        return AnimePutRequestBody.builder()
                .id(AnimeCreator.createValidUpdateAnime().getId())
                .name(AnimeCreator.createAnimeToBeSaved().getName())
                .build();
    }
}
