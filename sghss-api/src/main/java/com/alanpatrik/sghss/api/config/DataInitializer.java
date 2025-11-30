package com.alanpatrik.sghss.api.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;

@Profile({"dev", "test"})
@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner runScripts(DataSource dataSource) {
        return args -> {
            Path dataPath = Path.of("db", "data.sql");

            try (Connection conn = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(conn, new ClassPathResource("db/data.sql"));
            }
        };
    }
}
