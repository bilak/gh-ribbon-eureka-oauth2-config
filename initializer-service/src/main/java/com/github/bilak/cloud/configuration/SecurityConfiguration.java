package com.github.bilak.cloud.configuration;

import com.github.bilak.cloud.oauth2.PocPrincipalExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lvasek on 28/10/2016.
 */
@Configuration
public class SecurityConfiguration {

	public static final String LOAD_BALANCED_TEMPLATE = "balancedRestTemplate";

	@Autowired
	private AuthorizationCodeResourceDetails resourceDetails;

	@Bean(name = LOAD_BALANCED_TEMPLATE)
	@LoadBalanced
	RestTemplate balancedRestTemplate() {
		AccessTokenRequest atr = new DefaultAccessTokenRequest();
		OAuth2RestTemplate template = new OAuth2RestTemplate(clientResourceDetails(), new DefaultOAuth2ClientContext(atr));
		template.setAccessTokenProvider(new CloudClientCredentialsAccessTokenProvider(testTemplate()));
		return template;
	}

	@LoadBalanced
	@Bean
	RestTemplate testTemplate() {
		return new RestTemplate();
	}

	private OAuth2ProtectedResourceDetails clientResourceDetails() {
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(resourceDetails.getAccessTokenUri());
		resource.setClientId(resourceDetails.getClientId());
		resource.setClientSecret(resourceDetails.getClientSecret());
		resource.setScope(resourceDetails.getScope());
		return resource;
	}

	@Configuration
	@EnableResourceServer
	public static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

		private ResourceServerProperties ssoProperties;
		private OAuth2RestOperations restOperations;

		@Autowired
		public ResourceServerConfiguration(ResourceServerProperties ssoProperties,
				@Qualifier(value = SecurityConfiguration.LOAD_BALANCED_TEMPLATE) OAuth2RestOperations restOperations) {
			this.ssoProperties = ssoProperties;
			this.restOperations = restOperations;
		}

		@Bean
		@Primary
		UserInfoTokenServices defaultUserInfoTokenServices() {
			UserInfoTokenServices userInfoTokenServices = new UserInfoTokenServices(
					ssoProperties.getUserInfoUri(),
					ssoProperties.getClientId()
			);
			userInfoTokenServices.setPrincipalExtractor(pocPrincipalExtractor());
			userInfoTokenServices.setRestTemplate(restOperations);
			return userInfoTokenServices;
		}

		@Bean
		PrincipalExtractor pocPrincipalExtractor() {
			return new PocPrincipalExtractor();
		}

		@Override
		public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
			resources.tokenServices(defaultUserInfoTokenServices());
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().authenticated();
		}
	}

	public static class CloudClientCredentialsAccessTokenProvider extends ClientCredentialsAccessTokenProvider {
		private RestOperations restOperations;

		public CloudClientCredentialsAccessTokenProvider(RestOperations restOperations) {
			this.restOperations = restOperations;
		}

		@Override
		protected RestOperations getRestTemplate() {
			setMessageConverters(new RestTemplate().getMessageConverters());
			return this.restOperations;
		}

	}

}
