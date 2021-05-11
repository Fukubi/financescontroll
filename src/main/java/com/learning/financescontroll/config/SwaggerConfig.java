package com.learning.financescontroll.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
	
	public static final String CATEGORY = "Categoria";
	public static final String ENTRY = "Lancamento";
	public static final String USER = "Usuario";

	@Bean
	public Docket financesControllApi() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).groupName("v1").select()
				.apis(RequestHandlerSelectors.basePackage("com.learning.financescontroll.v1")).build()
				.apiInfo(this.metaData()).tags(new Tag(CATEGORY, "Operações referentes a entidade de categoria"))
				.tags(new Tag(ENTRY, "Operações referentes a entidade de lancamento"))
				.tags(new Tag(USER, "Operações referentes a entidade de usuario"));
	}
	
	@Bean
	public LinkDiscoverers discoverers() {
		List<LinkDiscoverer> listPlugins = new ArrayList<>();
		listPlugins.add(new CollectionJsonLinkDiscoverer());
		return new LinkDiscoverers(SimplePluginRegistry.create(listPlugins));
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("FINANCES CONTROLL")
				.description("API responsável pelo manuseamento das informações financeiras da aplicação")
				.version("1.0.0").license("").build();
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:META-INF/resources/webjars/");
	}
}
