package hivemq_plugins.IOTProjectHivePlugin.configuration;

import com.google.inject.Inject;

import java.util.Properties;

public class Configuration {


    private final Properties properties;

    @Inject
    public Configuration(PluginReader pluginReader) {
        properties = pluginReader.getProperties();
    }

    public String getMyProperty() {
        if (properties == null) {
            return null;
        }

        return properties.getProperty("myProperty");
    }
}
