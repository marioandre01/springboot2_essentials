package academy.devdojo.springboot2.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.print.Pageable;
import java.util.List;

//@Configuration -> configuração que se quer que seja feita globalmente no spring
@Configuration
public class DevDojoWebMvcConfigurer implements WebMvcConfigurer {
    //Sobreescrevendo a configuração global do spring sobre a paginação
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageHandler = new PageableHandlerMethodArgumentResolver();
        //page = 0 , retorna as informaçõe na pagina 0 que é a primeira pagina
        //se page = 1 o retorno das informações começa na pagina 1 que é a segunda pagina
        //size = 5 definindo 5 informações por pagina, para outros valores alterar esse valor
        // para testar fazer localhost:8080/animes no navegador ou no insomnia
        //para sobreescrever isso no teste fazer localhost:8080/animes?size=20 . para midar a pagina localhost:8080/animes?page=2
        //para ordernar(sort) a busca por name fazer localhost:8080/animes?size=20&sort=name,desc
        pageHandler.setFallbackPageable(PageRequest.of(0, 5));
        resolvers.add(pageHandler);
    }
}
