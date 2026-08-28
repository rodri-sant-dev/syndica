package com.syndica.backend.beans;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileTypeConfig {

    @Bean
    public Tika tika() {
        return new Tika();
    }
}
