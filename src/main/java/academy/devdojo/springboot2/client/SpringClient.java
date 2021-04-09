package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2 //gerenciamento de Log's, anotação para usar o log()
public class SpringClient {
    public static void main(String[] args) {
        //em algum momento será preciso fazer uma requisição para serviços(URLs externas), para não precesiar fazer manualmente
        //o spring oferece uma biblioteca que facilita essa chamada, e ainda faz o mapeamento dos dados para a classe,
        //a biblkoteca é a RestTemplate()
        //Será feita a requisição e retornaraá um json, deve-se informar para o spring para ele fazer o mapeamento automaticamente usando o jackson
        //deve-se informar o tipo que se quer fazer o mapeamento
        //o Anime.class é o tipo de mapeamento que deve ser feito
        //GET
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(object);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));

        //usado exchange() junto com ParameterizedTypeReference para converter para um List
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                });
        log.info(exchange.getBody());

        //POST
        //metodo Post usando postForObject
//        Anime kingdom = Anime.builder().name("kingdom").build(); //construindo o objeto a ser enviado
        //Enviando a requisição
        //em postForObject passar (url, objeto a ser enviado, o que vai ser retornado), olhando em Anime Controller no me to save(), esta retornando um Anime, -> ResponseEntity<Anime>
//        Anime KingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes", kingdom, Anime.class);
//        log.info("Saved anime {}", KingdomSaved);

        //POST usando exchange, com o exchange pode-se enviar HeadersHttp dentro do HttpEntity<>()
        Anime samuraiChamploo = Anime.builder().name("Samurai Champloo").build(); //construindo o objeto a ser enviado
        //em exchange passar (url, metodo HTTP usado, objeto a ser enviado, o tipo que vai ser retornado)
        ResponseEntity<Anime> samuraiChamplooSaved = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.POST,
                new HttpEntity<>(samuraiChamploo, createJsonHeaders()),
                Anime.class);

        log.info("Saved anime {}", samuraiChamplooSaved);

        //PUT
        //Retorna Void
        //Não tem nhum tipo de objeto na requisição por isso se esta usando Void.class
        //Se esta usando ResponseEntity<> para se ter junto com a resposta a resposta HTTP, no caso para fazer um tratamento
        // de erros caso for diferente da famalia 2xx
        Anime animeToBeUpdated = samuraiChamplooSaved.getBody();
        animeToBeUpdated.setName("Samaurai Champloo 2");

        ResponseEntity<Void> samuraiChamplooUpadated = new RestTemplate().exchange("http://localhost:8080/animes",
                HttpMethod.PUT,
                new HttpEntity<>(animeToBeUpdated, createJsonHeaders()),
                Void.class);

        log.info(samuraiChamplooUpadated);

        //DELETE
        //no DELETE não precisa o HttpEntity<>, com isso ficou null, mas poderia passar o Header no DELETE caso se precise estar autenticado
        ResponseEntity<Void> samuraiChamplooDeletet = new RestTemplate().exchange("http://localhost:8080/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                animeToBeUpdated.getId());

        log.info(samuraiChamplooDeletet);

    }

    //exemplo para enviar um header na requisição HTTP dizendo que o contentType da requisição é um APPLICATION_JSON
    //chamar o metodo createJsonHeaders() no parametro new HttpEntity<>() do exchange
    private static HttpHeaders createJsonHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}

//No Java existem algumas bibliotecas para fazer conversão Java <-> JSON, dentre elas: Jackson, Jettison e Gson.
//O Spring por padrão utiliza o Jackson, mas é possível substituir por essas outras, caso você queira.

/*
O Jackson funciona da seguinte maneira:
------------------------------------
ObjectMapper mapper = new ObjectMapper();
TopicoDto topico = mapper.readValue(json, TopicoDto.class);
-------------------------------------
*/

//A classe ObjectMapper do Jackson é que faz a conversão da String que contém o Json para um objeto java equivalente.
//Nas classes RestController por padrão o Spring considera automaticamente que vamos devolver no formato JSON, e por isso
//ele chama o Jackson para converter nosso DTO em JSON.

/*DTO - Objeto de Transferência de Dados, é um padrão de projeto de software usado para transferir dados entre subsistemas de um
software. DTOs são frequentemente usados em conjunção com objetos de acesso a dados para obter dados de um banco de dados.*/