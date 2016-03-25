package com.intexsoft.musicflat.remote.model;

public class RestResponse<T>
{
	private boolean success;
	private String message;
	private T data;

	private RestResponse(boolean success, String message, T data)
	{
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public String getMessage()
	{
		return message;
	}

	public T getData()
	{
		return data;
	}

	public static RestResponse<?> success(String message)
	{
		return new RestResponse<Object>(true, message, null);
	}

	public static <R> RestResponse<R> success(R data)
	{
		return new RestResponse<R>(true, null, data);
	}

	public static <R> RestResponse<R> success(String message, R data)
	{
		return new RestResponse<R>(true, message, data);
	}

	public static RestResponse<?> failure(String message)
	{
		return new RestResponse<Object>(false, message, null);
	}
}
