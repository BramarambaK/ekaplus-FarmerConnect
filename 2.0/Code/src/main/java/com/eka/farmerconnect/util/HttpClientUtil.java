package com.eka.farmerconnect.util;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class HttpClientUtil
{
  protected RestTemplate restTemplate;
  
  public HttpClientUtil() {}
  
  @PostConstruct
  private void initialize(){
	  
    restTemplate = new RestTemplate();
    HttpMessageConverter<Resource> resource = new ResourceHttpMessageConverter();
    FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
    formHttpMessageConverter.addPartConverter(new MappingJackson2HttpMessageConverter(new CustomObjectMapper()));
    formHttpMessageConverter.addPartConverter(resource);
    
    List<HttpMessageConverter<?>> modifiedConverters = new ArrayList<HttpMessageConverter<?>>();
    modifiedConverters.addAll(restTemplate.getMessageConverters());
    modifiedConverters.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
    modifiedConverters.add(1, resource);
    modifiedConverters.add(2, formHttpMessageConverter);
    
    restTemplate.setMessageConverters(modifiedConverters);
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
  }
  
  public <T, E> ResponseEntity<E> fireHttpRequest(URI uri, HttpMethod method, T requestBody, HttpHeaders headers, Class<E> responseEntityClass){
	  
	  HttpEntity<T> request = new HttpEntity<T>(requestBody, headers);
	  return restTemplate.exchange(uri, method, request, responseEntityClass);
  }
}


@Component
class CustomObjectMapper extends ObjectMapper {
	
    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        this.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}