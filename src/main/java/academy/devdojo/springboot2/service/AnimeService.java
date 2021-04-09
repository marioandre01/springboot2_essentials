package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service //tranformando a classe AnimeService em spring bean
@RequiredArgsConstructor //para o spring fazer a injeção de dependencia do animeRepository
public class AnimeService {
    //private final AnimeRepository animeRepository; //no futuro vai ser usado isso

//    private static List<Anime> animes;
//
//    static {
//        animes = new ArrayList<>(List.of(new Anime(1L, "Boku no Hero"), new Anime(2L,"Berserk")));
//    }

    private final AnimeRepository animeRepository;

    public List<Anime> listAll(){
//        return animes;
        return animeRepository.findAll();
    }

    public List<Anime> findByName(String name){
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(long id){
//        return animes.stream()
//                .filter(anime -> anime.getId().equals(id))
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));

//        return animeRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));

        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));

    }

    //Qundo se tem uma situação onde se precisa checar se as coisas que estão sendo feita precisa em caso de sessão dar um RollBack
    //anotasse o metodo com @Transactional do pacote springframework
    //so de fazer essa anotação o spring não vai commitar essa transação enquanto o metodo não for finalizado.
    //por padrão o @Transactional não da rollback para exceções do tipo checked, para isso utilizar @Transactional(rollbackFor = Exception.class)
    //coma anotação @Transactional(rollbackFor = Exception.class) vai ser levado em consideração as exceções do tipo checked
    @Transactional
    public Anime save(AnimePostRequestBody animePostRequestBody){
//        anime.setId(ThreadLocalRandom.current().nextLong(3, 1000000));
//        animes.add(anime);
//        return anime;
        return animeRepository.save(Anime.builder().name(animePostRequestBody.getName()).build()); //estava assim antes de usar o mapper

    }

    public void delete(long id) {
//        animes.remove(findById(id));
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        //validar se o anime existe antes de fazer o replace
        Anime saveAnime = findByIdOrThrowBadRequestException(animePutRequestBody.getId());

        Anime anime = Anime.builder()
                .id(saveAnime.getId())
                .name(animePutRequestBody.getName())
                .build();   //antes de usar o mapper

        animeRepository.save(anime);
//        delete(anime.getId());
//        animes.add(anime);
    }

}


