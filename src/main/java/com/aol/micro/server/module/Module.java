package com.aol.micro.server.module;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextListener;

import nonautoscan.com.aol.micro.server.AopConfig;
import nonautoscan.com.aol.micro.server.MiscellaneousConfig;
import nonautoscan.com.aol.micro.server.PropertyFileConfig;
import nonautoscan.com.aol.micro.server.ScheduleAndAsyncConfig;

import org.springframework.web.context.ContextLoaderListener;

import com.aol.micro.server.auto.discovery.RestResource;
import com.aol.micro.server.events.ConfigureActiveJobsAspect;
import com.aol.micro.server.rest.jersey.JerseyRestApplication;
import com.aol.micro.server.rest.jersey.JerseySpringIntegrationContextListener;
import com.aol.micro.server.rest.resources.ActiveResource;
import com.aol.micro.server.rest.resources.ConfigureResources;
import com.aol.micro.server.rest.resources.ManifestResource;
import com.aol.micro.server.rest.swagger.SwaggerInitializer;
import com.aol.micro.server.servers.model.ServerData;
import com.aol.micro.server.spring.metrics.CodahaleMetricsConfigurer;
import com.aol.micro.server.web.filter.QueryIPRetriever;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public interface Module {

	
	default Set<Class> getSpringConfigurationClasses(){
		return Sets.newHashSet(PropertyFileConfig.class,
				MiscellaneousConfig.class, AopConfig.class, CodahaleMetricsConfigurer.class,
				ConfigureActiveJobsAspect.class, ScheduleAndAsyncConfig.class,ConfigureResources.class);
	}
	default List<Class<? extends RestResource>> getRestResourceClasses() {
		return Arrays.asList(RestResource.class);
	}
	
	default List<Class> getDefaultResources(){
		return Arrays.asList(ActiveResource.class, ManifestResource.class);
	}
	
	default List<ServletContextListener> getListeners(ServerData data){
		return ImmutableList.of(new ContextLoaderListener(data
				.getRootContext()),
				new JerseySpringIntegrationContextListener(data),
				new SwaggerInitializer(data));
	}
	default Map<String,Filter> getFilters(ServerData data) {
		return ImmutableMap.of("/*",new QueryIPRetriever());
	}
	default Map<String,Servlet> getServlets(ServerData data) {
		return ImmutableMap.of();
	}
	
	default  String getJaxWsRsApplication(){
		return JerseyRestApplication.class.getCanonicalName();
	}
	default String getProviders(){
		return "com.aol.micro.server.rest.providers";
	}
	
	public String getContext();
}