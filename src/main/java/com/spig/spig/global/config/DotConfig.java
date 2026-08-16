package com.spig.spig.global.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DotConfig {
    @Bean
    public Dotenv dotenv(){
        return Dotenv.configure().directory("./").ignoreIfMissing().load();
    }
}
