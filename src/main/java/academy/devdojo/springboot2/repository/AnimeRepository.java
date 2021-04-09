package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Repositiory representa o banco de dados

//extender da classe JpaRepository deve passar a classe q representa o repositorio e o atributo que representa o Id
//Não precisa implementar nenhum metodo pra acessar o banco pois JpaRepository ja fornece isso pra nós
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    //retornar uma lista de animes, e essa lista vai ser retornada pelo nome
    //o Name de findBy é o atraibuto name da classe Anime em domain se fosse nome ficaria findByNome
    //se quer o parametro name, o spring fará o mapeamento automaticamente
    List<Anime> findByName(String name);
}
