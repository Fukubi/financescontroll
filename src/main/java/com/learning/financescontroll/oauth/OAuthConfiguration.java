package com.learning.financescontroll.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration
public class OAuthConfiguration {

	public static final String RESOURCE_ID = "financesControll";

	@EnableAuthorizationServer
	public static class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

		@Autowired
		private AuthenticationManager authenticationManager;

		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory().withClient("cliente-web")
					.secret("$2a$10$hiogJgb5h4kJ0sRuI/.8IuvYltUdI4WQqBWVqniGRoRXiHIu3Yemi")
					.authorizedGrantTypes("password", "client_credentials", "refresh_token")
					.accessTokenValiditySeconds(3601).scopes("cw_logado", "cw_naologado").resourceIds(RESOURCE_ID).and()
					.withClient("cliente-canva").secret("$2a$10$hiogJgb5h4kJ0sRuI/.8IuvYltUdI4WQqBWVqniGRoRXiHIu3Yemi")
					.authorizedGrantTypes("authorization_code", "implicit").redirectUris("https://www.canva.com/pt_br/")
					.autoApprove(true).accessTokenValiditySeconds(3601).scopes("cc_logado").resourceIds(RESOURCE_ID);
		}

	}

	@EnableResourceServer
	public static class ResourceServer extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.resourceId(RESOURCE_ID);
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated().and().cors();
		}

	}
	
	
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class OAuthExpressionHandler extends GlobalMethodSecurityConfiguration {
		
		@Override
		protected MethodSecurityExpressionHandler createExpressionHandler() {
			return new OAuth2MethodSecurityExpressionHandler();
		}
		
	}

}
