package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class DemoService {
    public void writeToOutputStream(final OutputStream outputStream) throws IOException {
        URL url = new URL("http://ep2.interpark.com/partner/channel/CHANNEL_PRODUCT_INFO_202106291551.txt");
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5*60*1000);
        connection.setDoOutput(true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String decodedString;
        while ((decodedString = in.readLine()) != null) {
            log.info("{}", decodedString);
            outputStream.write(decodedString.getBytes(StandardCharsets.UTF_8));
        }

    }
}
