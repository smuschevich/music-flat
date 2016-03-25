package com.intexsoft.musicflat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.intexsoft.musicflat.remote.config.RemoteServicesConfig;
import com.intexsoft.musicflat.remote.config.SecurityConfig;

@Configuration
@Import({SecurityConfig.class, RemoteServicesConfig.class})
public class AppConfig
{
}
