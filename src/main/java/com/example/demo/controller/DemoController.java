package com.example.demo.controller;

import com.example.demo.domain.GetProductGroupDto;
import com.example.demo.service.DemoService;
import com.example.demo.util.web.CustomResponseExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@RestController
@Slf4j
public class DemoController {
    @Autowired
    DemoService service;

    @GetMapping("/test")
    public ResponseEntity<StreamingResponseBody> streamTest() {

        StreamingResponseBody stream = this::writeTo;

        return new ResponseEntity<>(stream, HttpStatus.OK);

    }

    private void writeTo(OutputStream outputStream) throws IOException {
        service.writeToOutputStream(outputStream);
    }

    @GetMapping("/test2")
    public ResponseEntity<StreamingResponseBody> streamTest2(){
        StreamingResponseBody responseBody = response -> {
//            for (int i = 1; i <= 1000; i++) {
//                try {
//                    Thread.sleep(10);
//                    response.write(("Data stream line - " + i + "\n").getBytes());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            URL url = new URL("http://ep2.interpark.com/partner/channel/CHANNEL_PRODUCT_INFO_202106291551.txt");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setConnectTimeout(5*60* 1000);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String decodedString;
            while ((decodedString = in.readLine()) != null) {
                //log.info("{}", decodedString);
                response.write(decodedString.getBytes(StandardCharsets.UTF_8));
            }
            log.info("전송종료");
        };

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(responseBody);
    }

    @GetMapping("/test3")
    public void streamTest3(HttpServletResponse httpServletResponse) {
        RestTemplate restTemplate = new RestTemplate();

        httpServletResponse.setStatus(HttpStatus.OK.value());

        ResponseExtractor responseExtractor = response -> {
            log.info("{}", response.getBody());
            IOUtils.copy(response.getBody(), httpServletResponse.getOutputStream());
            return null;
        };
        CustomResponseExtractor customResponseExtractor = new CustomResponseExtractor(restTemplate, httpServletResponse);
        restTemplate.execute("http://ep2.interpark.com/partner/channel/CHANNEL_PRODUCT_INFO_202106291551.txt",
                HttpMethod.GET,
                (ClientHttpRequest callback) ->  {},
//                responseExtractor -> {
//                    IOUtils.copy(responseExtractor.getBody(), httpServletResponse.getOutputStream());
//                    return null;
//                }
                customResponseExtractor
                );

    }

}
