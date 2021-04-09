package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class  AnimeController {
    //atributo final -> valor atribuído uma única vez, seja na própria declaração ou no construtor.
    //Usado para garantir que um valor ou referência de objeto não vai mudar.(imutabilidade)
    private final DateUtil dateUtil;
    private final AnimeService animeService;  //adicionado final para fazer a injeção de dependencia com usando a anotação @RequiredArgsConstructor e automaticamente vamos ter um objeto de "animeService"

    //mas para conseguir fazer a injeção de dependecia a classe AnimeService precisa ser um spingbean, foi colocado nela @Service

    //localhost:8080/anime/list
    //Alterar o retorno de List<> para Page<> - o spring fornece o Page<>, em list deve receber o parametro Pageable pageable
    //findAll deve receber o parametro pageable
    @GetMapping
    public ResponseEntity<Page<Anime>> list(Pageable pageable){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAll(pageable));
    }
    //por boa pratica na resposta é bom retornar algumas informações extras, como o status do Http
    //Usar ResponseEntity<>

    //usar path verbs "{/id}" variaveis que vão vir pela url
    //quem acessar localhost:8080/animes e passar um id ex: localhost:8080/animes/1
    //para pegar esse valor precisa-se mapear essa variavel no java usando @PathVariabel colocar o mesmo nome do parametro
    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
//      return new ResponseEntity<>(animeService.findById(id), HttpStatus.OK); //outro jeito de fazer

    }

    //quando precisar passar parametros na URL usar @RequestParam
    //no navegador colocar localhost:8080/animes/find?name=nome_do_anime
    //o spring ja associa o name da URL ao parametro name do metodo findByName
    //poderia colocar algumas opções a mais no @RequestParam -> findByName(@RequestParam(required = false) String name, @RequestParam String id)
    ///no navegador colocar localhost:8080/animes/find?name=nome_do_anime&id=valor_id
    //por padrao required = true, quando colocado required = false, nao precisa coloca o nome na url, ficaria assim localhost:8080/animes/find
    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
        return ResponseEntity.ok(animeService.findByName(name));

    }

    //ResponseEntity<Anime> - retorna o objeto inteiro
    //NO POST precisa receber um Body, para mapear esse Body para o java usa-se @RequestBody
    //O @RequestBody que esta sendo enviado vai ser mapeado com o "jackson"(tem que ver o que é isso)
    //o sprig faz a validação automatica usando a notação @Valid, na classe AnimePostRequestBody tem a anotação @NotEmpty no atributo name então o @Valid vai verificar isso
    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }
    //ttpStatus.CREATED - resposta 201 created

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody animePutRequestBody) {
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //reorna um NO_CONTENT status

}


//quando o dominios forem entidades e for no na classe Controller(AnimeController), vai ter um warnning falando que não
// se deve utilizar as classes que representa a tabela no banco de dados ou as classes de persistencias, como os
// parametros do @RequestBody da classe POST diretamente no Controller

//criar o package request e criar as classes AnimePostRequestBody e AnimePutRequestBody para o nome ficar diferente do banco de dados