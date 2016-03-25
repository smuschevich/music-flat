package com.intexsoft.musicflat.remote.config;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.WebContentGenerator;

import com.intexsoft.musicflat.remote.config.mapping.HibernateAwareObjectMapper;

@Configuration
@ComponentScan({"com.intexsoft.musicflat.remote.config.mapping", "com.intexsoft.musicflat.remote.controller"})
public class RemoteServicesConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware
{
	private ApplicationContext applicationContext;
	@Autowired
	private WebContentGenerator webContentGenerator;

	@PostConstruct
	public void disableCache()
	{
		webContentGenerator.setCacheControl(CacheControl.noStore());
		webContentGenerator.setCacheSeconds(0);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
	{
		// equivalent to <mvc:message-converters>
		converters.add(converter());
	}

	@Bean
	@DependsOn(HibernateAwareObjectMapper.BEAN_NAME)
	public MappingJackson2HttpMessageConverter converter()
	{
		HibernateAwareObjectMapper objectMapper = applicationContext.getBean(HibernateAwareObjectMapper.class);
		return new MappingJackson2HttpMessageConverter(objectMapper);
	}

	@Bean
	public FormattingConversionService conversionService()
	{
		// Use the DefaultFormattingConversionService but do not register defaults
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);
		// Ensure @NumberFormat is still supported
		conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		// Register date-time conversion for the JSR-310 java.time formatting system
		DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
		registrar.registerFormatters(conversionService);
		return conversionService;
	}

	/**
	 * Creates MultipartResolver bean which is used to upload files.
	 * @return the instance of multipart resolver
	 */
	@Bean
	public MultipartResolver multipartResolver()
	{
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		return commonsMultipartResolver;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry)
	{
		// enable cross-origin resource sharing for all domains
		registry.addMapping("/**");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}
