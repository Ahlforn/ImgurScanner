package Scanner;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;


/**
 * Created by Anders Hofmeister Brønden on 18/06/2016.
 */
public class ConfigHandler {
    private PropertiesConfiguration config;
    private FileBasedConfigurationBuilder<PropertiesConfiguration> builder;

    public ConfigHandler(String fileName) throws ConfigurationException {
        File file = new File(fileName);

        try {
            if (!file.exists()) file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(new Parameters().properties()
                        .setFileName(fileName)
                        .setThrowExceptionOnMissing(true)
                        .setIncludesAllowed(false));
        config = builder.getConfiguration();
    }

    public String getString(String key) {
        return (config.containsKey(key)) ? config.getString(key) : "";
    }

    public void setProperty(String key, String value) {
        config.setProperty(key, value);
    }

    public void save() throws ConfigurationException {
        builder.save();
    }
}
