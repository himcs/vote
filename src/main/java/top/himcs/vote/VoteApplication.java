package top.himcs.vote;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication

public class VoteApplication {


    public static void main(String[] args) {
        SpringApplication.run(VoteApplication.class, args);
    }

    @Bean
    public CommandLineRunner test(Environment environment, @Value("${catalog.name}") String catalog) {
        return args -> {
           // System.out.println(catalog);
        };
    }
}
