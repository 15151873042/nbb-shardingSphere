package com.nbb.databaseHorizontal;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
@MapperScan("com.nbb.**.mapper")
public class DatabaseApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DatabaseApplication.class, args);

        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path", "/");
        log.info("\n--------------------------------------------------------------------\n" +
                "\tApplication  is running! Access URLs:\n" +
                "\tLocal access url:    \thttp://localhost:" + port + path + "\n" +
                "--------------------------------------------------------------------");
    }
}
