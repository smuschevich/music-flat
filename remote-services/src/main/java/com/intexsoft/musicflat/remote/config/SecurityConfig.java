package com.intexsoft.musicflat.remote.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intexsoft.musicflat.remote.model.RestResponse;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			// LogoutFilter and UsernamePasswordAuthenticationFilter do not continue a chain in case of success login/logout
			.addFilterBefore(corsFilter(), LogoutFilter.class)
			.addFilterBefore(ssoCompositeFilter(), BasicAuthenticationFilter.class)
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/service/**").permitAll()
			.antMatchers("/service/security/**").permitAll()
			.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginProcessingUrl("/service/security/login")
			.usernameParameter("email")
			.passwordParameter("password")
			.successHandler((req, res, a) ->
			{
				res.setStatus(HttpServletResponse.SC_OK);
				res.getWriter().write(objectMapper.writeValueAsString(RestResponse.success(true)));
			})
			.failureHandler((req, res, e) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))
		.and()
			.logout()
			.logoutUrl("/service/security/logout")
			.logoutSuccessHandler((req, res, a) ->
			{
				res.setStatus(HttpServletResponse.SC_OK);
				res.getWriter().write(objectMapper.writeValueAsString(RestResponse.success(true)));
			})
		.and()
			.userDetailsService(new InMemoryUserDetailsManager(createUsers()));
	}

	@Bean
	public FilterRegistrationBean oauth2ClientContextFilterRegistration(OAuth2ClientContextFilter filter)
	{
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(Integer.MIN_VALUE);
		return registration;
	}

	@Bean
	public CompositeFilter ssoCompositeFilter()
	{
		CompositeFilter filter = new CompositeFilter();
		List<Filter> filters = new ArrayList<>();
		filters.add(createSsoFilter(facebookClient()));
		filters.add(createSsoFilter(githubClient()));
		filter.setFilters(filters);
		return filter;
	}

	private OAuth2ClientAuthenticationProcessingFilter createSsoFilter(ClientResources client)
	{
		OAuth2ClientAuthenticationProcessingFilter filter =
			new OAuth2ClientAuthenticationProcessingFilter(client.getSso().getLoginPath());
		filter.setRestTemplate(new OAuth2RestTemplate(client.getClient(), oauth2ClientContext));
		filter.setTokenServices(
			new UserInfoTokenServices(client.getResource().getUserInfoUri(), client.getClient().getClientId()));
		return filter;
	}

	@Bean
	@ConfigurationProperties("facebook.oauth2")
	public ClientResources facebookClient()
	{
		return new ClientResources();
	}

	@Bean
	@ConfigurationProperties("github.oauth2")
	public ClientResources githubClient()
	{
		return new ClientResources();
	}

	@Bean
	public CorsFilter corsFilter()
	{
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin(CorsConfiguration.ALL);
		config.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
		configSource.setCorsConfigurations(Collections.singletonMap("/**", config));
		return new CorsFilter(configSource);
	}

	private Collection<UserDetails> createUsers()
	{
		return Collections.singleton(new User("1@1", "1", Collections.emptyList()));
	}

	private class ClientResources
	{
		private OAuth2ProtectedResourceDetails client = new AuthorizationCodeResourceDetails();
		private ResourceServerProperties resource = new ResourceServerProperties();
		private OAuth2SsoProperties sso = new OAuth2SsoProperties();

		public OAuth2ProtectedResourceDetails getClient()
		{
			return client;
		}

		public ResourceServerProperties getResource()
		{
			return resource;
		}

		public OAuth2SsoProperties getSso()
		{
			return sso;
		}
	}
}
