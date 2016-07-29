package Scanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilder;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;


/**
 * Created by Anders Hofmeister Brønden on 18-06-2016.
 */
public class ConfigHandler {
    private PropertiesConfiguration config;
    private FileBasedConfigurationBuilder<PropertiesConfiguration> builder;

    public ConfigHandler(String fileName) throws ConfigurationException {
        builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().properties()
                        .setFileName(fileName)
                        .setThrowExceptionOnMissing(true)
                        .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
                        .setIncludesAllowed(false));
        config = builder.getConfiguration();
    }

    public String getString(String key) {
        try {
            return config.getString(key);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setProperty(String key, String value) {
        config.setProperty(key, value);
    }

    public void save() throws ConfigurationException {
        builder.save();
    }
}
