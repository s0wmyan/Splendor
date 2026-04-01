package util;
import java.io.*;
import java.util.*;

// Generated with Gemini 3.1 Pro, modified with a method to get an int property
public class ConfigLoader {
    private Properties properties;

    public ConfigLoader(String filePath) {
        properties = new Properties();
        
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("Unable to load config file! " + e.getMessage());
        }
    }

    /**
     * retrieves file path as a string value from the config file
     */
    public String getFilePath(String key) {
        return properties.getProperty(key);
    }

    /**
     * retrieves an int value from the config file
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim()); 
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for key: " + key + ". Using default value.");
            }
        }
        return defaultValue;
    }

}