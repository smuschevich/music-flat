package com.intexsoft.musicflat.remote.config.mapping;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Component(HibernateAwareObjectMapper.BEAN_NAME)
public class HibernateAwareObjectMapper extends ObjectMapper
{
	private static final long serialVersionUID = 4302576592465169888L;

	public static final String BEAN_NAME = "hibernateAwareObjectMapper";

	public HibernateAwareObjectMapper()
	{
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		hibernate5Module.configure(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION, false);
		registerModule(hibernate5Module);
		// Serialization features
		configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// Deserialization features
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// Use only fields for mapping
		setVisibility(getSerializationConfig().getDefaultVisibilityChecker()
			.withFieldVisibility(JsonAutoDetect.Visibility.ANY)
			.withGetterVisibility(JsonAutoDetect.Visibility.NONE)
			.withSetterVisibility(JsonAutoDetect.Visibility.NONE)
			.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
			.withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
	}
}
