package com.intexsoft.musicflat.remote.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.intexsoft.musicflat.remote.model.RestResponse;

@ControllerAdvice
public class RestControllerAdvice implements ResponseBodyAdvice<Object>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(RestControllerAdvice.class);

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestResponse<?> handleException(Exception ex)
	{
		LOGGER.error("Unexpected error occurred while processing request", ex);
		return RestResponse.failure(ex.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestResponse<?> handleHttpMessageNotReadableException(Exception ex)
	{
		LOGGER.error("Could not read JSON", ex);
		return RestResponse.failure("Could not read input parameters");
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType)
	{
		return !returnType.getParameterType().isAssignableFrom(RestResponse.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
		Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response)
	{
		if (!(body instanceof RestResponse))
		{
			LOGGER.info("Request successfully processed: {}", request.getURI());
			body = RestResponse.success(body);
		}
		return body;
	}
}
