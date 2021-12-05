package main.config;

import main.entities.YamlConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;

@Configuration
public class AppConfig {

    @Bean
    public YamlConfig getYamlConfig() {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        Yaml yaml = new Yaml(new Constructor(YamlConfig.class), representer);

        InputStream inputStream = this
                .getClass()
                .getClassLoader()
                .getResourceAsStream("config/application.yaml");

        return yaml.load(inputStream);
    }
}