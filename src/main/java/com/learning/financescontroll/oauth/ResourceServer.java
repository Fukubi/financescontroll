package com.learning.financescontroll.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.learning.financescontroll.v1.service.UserInfoService;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {

	public static final String RESOURCE_ID = "financesControll";

	@Autowired
	private UserInfoService detalhesDoUsuario;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId(RESOURCE_ID);
		resources.tokenServices(this.defaultTokenServices());
		resources.tokenStore(this.tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/v2/api-docs", "/swagger*/**", "/webjars/*").permitAll().anyRequest()
				.authenticated().and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler()).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors();
	}

	@Bean
	public UserAuthenticationConverter userTokenConverter() {
		DefaultUserAuthenticationConverter converter = new DefaultUserAuthenticationConverter();
		converter.setUserDetailsService(detalhesDoUsuario);
		return converter;
	}

	@Bean
	public AccessTokenConverter defaultAccessTokenConverter() {
		DefaultAccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();
		tokenConverter.setUserTokenConverter(this.userTokenConverter());
		return tokenConverter;
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setAccessTokenConverter(this.defaultAccessTokenConverter());
		accessTokenConverter.setSigningKey("assinatura-financesControll");
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(this.jwtAccessTokenConverter());
	}

	@Bean
	public DefaultTokenServices defaultTokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(this.tokenStore());
		return tokenServices;
	}

}
