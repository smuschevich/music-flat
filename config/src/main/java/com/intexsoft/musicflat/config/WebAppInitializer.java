package com.intexsoft.musicflat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@Import(AppConfig.class)
public class WebAppInitializer extends SpringBootServletInitializer
{
	private static final String MAPPING = "/service/*";
	private static final String SERVLET_NAME = "DispatcherServlet";

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
	{
		// errors should be handled via a servlet container and not via spring boot (required for proper work of OAuth)
		setRegisterErrorPageFilter(false);
		return builder.sources(WebAppInitializer.class);
	}

	@Bean
	public ServletRegistrationBean dispatcherServlet()
	{
		DispatcherServlet servlet = new DispatcherServlet(webApplicationContext);
		ServletRegistrationBean registration = new ServletRegistrationBean(servlet);
		registration.setName(SERVLET_NAME);
		registration.setAsyncSupported(true);
		registration.setLoadOnStartup(1);
		registration.addUrlMappings(MAPPING);
		return registration;
	}
}
