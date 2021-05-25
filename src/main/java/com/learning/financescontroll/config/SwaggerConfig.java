package com.learning.financescontroll.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

	public static final String CATEGORY = "Categoria";
	public static final String ENTRY = "Lancamento";
	public static final String USER = "Usuario";

	@Value("${host.full.dns.auth.link}")
	private String authLink;

	@Bean
	public Docket financesControllApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("v1").select()
				.apis(RequestHandlerSelectors.basePackage("com.learning.financescontroll.v1.controller"))
				.paths(PathSelectors.any()).build().apiInfo(this.metaData())
				.securitySchemes(Collections.singletonList(this.securitySchema()))
				.securityContexts(Collections.singletonList(this.securityContext()))
				.tags(new Tag(CATEGORY, "Operações referentes a entidade de categoria"))
				.tags(new Tag(ENTRY, "Operações referentes a entidade de lancamento"))
				.tags(new Tag(USER, "Operações referentes a entidade de usuario"));
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("FINANCES CONTROLL")
				.description("API responsável pelo manuseamento das informações financeiras da aplicação")
				.version("1.0.0").license("").build();
	}

	private OAuth securitySchema() {
		List<AuthorizationScope> authScope = new ArrayList<>();
		authScope.add(new AuthorizationScope("cw_logado", "acesso área logada"));
		authScope.add(new AuthorizationScope("cw_naologado", "acesso área não logada"));

		List<GrantType> grantTypes = new ArrayList<>();
		grantTypes.add(new ResourceOwnerPasswordCredentialsGrant(this.authLink));

		return new OAuth("auth2-Schema", authScope, grantTypes);
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(this.defaultAuth()).build();
	}
	
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope[] authScopes = new AuthorizationScope[2];
		authScopes[0] = new AuthorizationScope("cw_logado", "acesso área logada");
		authScopes[1] = new AuthorizationScope("cw_naologado", "acesso área não logada");
		
		return Collections.singletonList(new SecurityReference("auth2-Schema", authScopes));
	}
}
