package com.eka.farmerconnect;

import java.time.Duration;
import java.util.Arrays;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.eka.db.MongoClientUtil;
import com.eka.db.MongoDBProperties;
import com.eka.farmerconnect.interceptor.RestTemplateRequestLoggerInterceptor;

@SpringBootApplication
@EnableCaching
public class FarmerconnectApplication {

	@Value("${mongo.container.name}")
	String mongoContainerName;
	
	@Value("${mongo.port}")
	int mongoPort;
	
	@Value("${mongo.database}")
	String mongoDatabase;
	
	@Value("${mongo.userName}")
	String mongoUserName;

	@Value("${mongo.password}")
	String mongoPassword;	
	
	@Value("${spring.data.mongodb.uri}")
	 String dbURI;
	
	public static void main(String[] args) {
		SpringApplication.run(FarmerconnectApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		// Do any additional configuration here
		RestTemplate restTemplate = builder
				.setConnectTimeout(Duration.ofMinutes(5))
				.interceptors(new RestTemplateRequestLoggerInterceptor())
				.requestFactory(
						() -> new BufferingClientHttpRequestFactory(
								new SimpleClientHttpRequestFactory()))
				.setReadTimeout(Duration.ofMinutes(5)).build();

		// Do any additional configuration here
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter
		.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8,
				MediaType.APPLICATION_OCTET_STREAM, MediaType.MULTIPART_FORM_DATA, MediaType.TEXT_PLAIN));
		return restTemplate;
	}

	@PostConstruct
	void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}
	
	@PostConstruct
	private void initializeMongoDB() throws Exception {

		MongoDBProperties dbProperties = new MongoDBProperties();
		dbProperties.setPort(mongoPort);
		dbProperties.setIpAddress(mongoContainerName);
		dbProperties.setDatabase(mongoDatabase);
		dbProperties.setUser(mongoUserName);
		dbProperties.setPassword(mongoPassword);
		dbProperties.setAuthDB(mongoDatabase);
		dbProperties.setURL(dbURI);
		MongoClientUtil.initialize(dbProperties);
	}
	
	@Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        FilterRegistrationBean<ForwardedHeaderFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ForwardedHeaderFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
