package br.edu.ifrs.restinga.requisicoes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.edu.ifrs.restinga.requisicoes"))
                .paths(PathSelectors.any()) //PathSelectors.any()
                .build()	
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Sistema de Requisição de Aproveitamento de Estudos e Certificação - IFRS RESTINGA",
                "Sistema desenvolvido com o intuito de automatizar os processos de pedido de requisições de aproveitamento de estudos e certificação, desenvolvido na disciplina de Desenvolvimento de Sistemas II, em parceria com o Campos Restinga.",
                "1.0",
                "Terms of Service",
                new Contact("Repos do Projeo: ", "\n\n https://github.com/DCommicsBP/sistema-certificacao-aproveitamento, \n\n https://github.com/VictorSilva97/ifrs-dev2-sistema-certificacao-aproveitamento-front  ",
                        "dayonne.p@gmail.com, vic"),
                
                "Apache License Version 2.0",

                "https://www.apache.org/licesen.html", new ArrayList<VendorExtension>()
        );

        return apiInfo;
    }

}
