package com.microsaas.datastoryteller.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Slf4j
public class AppRegistrationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("Registering DataStoryTeller with Nexus Hub...");
        try {
            String manifestJson = new String(Files.readAllBytes(Paths.get("../integration-manifest.json")));
            URL url = new URL("http://localhost:8090/api/v1/apps/register");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = manifestJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = con.getResponseCode();
            if (code >= 200 && code < 300) {
                log.info("Successfully registered with Nexus Hub");
            } else {
                log.warn("Failed to register with Nexus Hub. Response code: {}", code);
            }
        } catch (Exception e) {
            log.warn("Could not register with Nexus Hub. Is it running? Error: {}", e.getMessage());
        }
    }
}
