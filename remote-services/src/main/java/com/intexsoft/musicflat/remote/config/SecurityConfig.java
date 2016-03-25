package com.intexsoft.musicflat.remote.config;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intexsoft.musicflat.remote.model.RestResponse;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http
			// LogoutFilter and UsernamePasswordAuthenticationFilter do not continue a chain in case of success login/logout
			.addFilterBefore(corsFilter(), LogoutFilter.class)
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
}
