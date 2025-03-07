package org.zerock.stocktrading;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties"; // src/main/resources/에 위치해야 함

    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new IOException("설정 파일을 찾을 수 없습니다: " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
