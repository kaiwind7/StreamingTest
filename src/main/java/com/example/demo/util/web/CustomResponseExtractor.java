package com.example.demo.util.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
public class CustomResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {
    private HttpMessageConverterExtractor<String> delegate;
    HttpServletResponse httpServletResponse;
    private Class<T> type;

    public CustomResponseExtractor (RestTemplate restTemplate, HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
        Type responseType = String.class;
        if (responseType != null && Void.class != responseType) {
            this.delegate = new HttpMessageConverterExtractor(responseType, restTemplate.getMessageConverters());
        } else {
            this.delegate = null;
        }

    }

    @Override
    public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
        if (this.delegate != null) {
            Object body = this.delegate.extractData(response);
            log.info(":body {} :: ", String.valueOf(body));
            if(type != null && !type.getName().equalsIgnoreCase("java.lang.String")){
                body = new ObjectMapper().readValue((String) body, type);
            }

            IOUtils.copy(response.getBody(), httpServletResponse.getOutputStream());


            return new ResponseEntity(body, response.getHeaders(), response.getStatusCode());
        }else{
            return new ResponseEntity(response.getHeaders(), response.getStatusCode());

        }
    }
}
