package academy.devdojo.springboot2.client;

import academy.devdojo.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2 //gerenciamento de Log's, anotação para usar o log()
public class SpringClient {
    public static void main(String[] args) {
        //em algum momento será preciso fazer uma requisição para serviços(URLs externas), para não precesiar fazer manualmente
        //o spring oferece uma biblioteca que facilita essa chamada, e ainda faz o mapeamento dos dados para a classe,
        //a biblkoteca é a RestTemplate()
        //Será feita a requisição e retornaraá um json, deve-se informar para o spring para ele fazer o mapeamento automaticamente usando o jackson
        //deve-se informar o tipo que se quer fazer o mapeamento
        //o Anime.class é o tipo de mapeamento que deve ser feito
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(object);
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