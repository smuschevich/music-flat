package com.intexsoft.musicflat;

import java.io.File;

import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.intexsoft.musicflat.config.WebAppInitializer;

@Configuration
@SpringBootApplication
@Import(WebAppInitializer.class)
public class MusicFlatApp
{
	private static final File BASE_DIR = new File("build");
	private static final File WEBAPP_DIR_LOCATION = new File("../webapp/dist");
	private static final int PORT = 8080;
	
	public static void main(String[] args)
	{
		SpringApplication.run(MusicFlatApp.class, args);
	}

	@Bean
	public EmbeddedServletContainerFactory embeddedServletContainerFactory()
	{
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setBaseDirectory(BASE_DIR);
		factory.setDocumentRoot(WEBAPP_DIR_LOCATION);
		factory.setPort(PORT);
		// enable WS SCI scanning for embedded Tomcat, it is required for web sockets 
		factory.addContextCustomizers(c -> c.addServletContainerInitializer(new WsSci(), null));
		return factory;
	}
}
