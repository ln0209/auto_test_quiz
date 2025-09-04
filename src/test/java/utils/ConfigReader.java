package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("无法找到 config.properties 文件");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("加载配置文件失败", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // 新增浏览器相关配置方法
    public static String getBrowserType() {
        return getProperty("browser", "chrome");
    }

    public static String getChromeDriverPath() {
        return getProperty("chrome.driver.path");
    }

    public static String getEdgeDriverPath() {
        return getProperty("edge.driver.path");
    }

    public static String getIEDriverPath() {
        return getProperty("ie.driver.path");
    }

    public static String getEdgeIEPath() {
        return getProperty("edge.ie.path");
    }

    public static String getIESiteList() {
        return getProperty("ie.site.list");
    }

    // 原有的其他方法...
    public static String getBaseUrl() {
        return getProperty("base.url");
    }

    public static String getUsername() {
        return getProperty("username");
    }

    public static String getPassword() {
        return getProperty("password");
    }
}