package com.github.yhao3.line.notify.service;

import java.io.*;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccessTokenFileHandler implements AccessTokenHandler {

    private static final String FILE_NAME = "line-notify-token.txt";

    @Override
    public void saveAccessToken(String accessToken) {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_NAME));
            writer.write(accessToken);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> getAccessToken() {
        File file = new File("line-notify-token.txt");
        BufferedReader reader;
        String accessToken;
        try {
            reader = new BufferedReader(new FileReader(file));
            accessToken = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(accessToken);
    }

    @Override
    public Boolean isAccessTokenExist() {
        return new File(FILE_NAME).exists();
    }

    @Override
    public void deleteAccessToken() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            boolean delete = file.delete();
            if (delete) {
                log.info("Delete access token file successfully");
            }
        }
    }
}
