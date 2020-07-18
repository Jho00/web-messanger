package com.akutin.messaginglogic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class AppConfig {
    private static AppConfigModel model = null;
    public static AppConfigModel getConfig() throws IOException {
        if (model != null) {
            return model;
        }

        try {
            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
            model = yamlMapper.readValue(new File("src/main/resources/config.yml"), AppConfigModel.class);
            return model;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
