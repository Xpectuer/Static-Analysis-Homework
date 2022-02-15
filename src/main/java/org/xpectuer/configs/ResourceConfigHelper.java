package org.xpectuer.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ResourceConfigHelper {
    private InputStream inputStream;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getPropertyValue(String key) throws IOException {

        Properties prop = new Properties();

        try {
            String config_file_name = "config.properties";
            inputStream = this.getClass().getClassLoader().getResourceAsStream(config_file_name);
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + config_file_name + "' not found in the classpath");
            }
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        } finally {
            Objects.requireNonNull(inputStream).close();
        }
        return prop.getProperty(key);
    }
}

