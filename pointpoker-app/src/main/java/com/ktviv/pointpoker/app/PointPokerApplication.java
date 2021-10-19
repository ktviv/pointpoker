package com.ktviv.pointpoker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ktviv.pointpoker.app", "com.ktviv.pointpoker.domain", "com.ktviv.pointpoker.infra"})
//@EnableJpaRepositories(basePackages = {"com.ktviv.pointpoker.infra"})
public class PointPokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PointPokerApplication.class, args);
    }
//
//    /**
//     * Setting up some default users in db on startup
//     * @param userService
//     * @return
//     */
//    @Bean
//    CommandLineRunner commandLineRunner(UserService userService) {
//
//
//        return args -> {
//
//            User vivek = new User();
//            vivek.setDisplayName("Vivek");
//            vivek.setEmail("vivek@gmail.com");
//
//            User mark = new User();
//            mark.setDisplayName("Mark");
//            mark.setEmail("mark@gmail.com");
//
//            User mina = new User();
//            mina.setDisplayName("Mina");
//            mina.setEmail("mina@gmail.com");
//
//            User edith = new User();
//            mina.setDisplayName("Edith");
//            mina.setEmail("edith@gmail.com");
//
//            userService.saveUsers(asList(vivek, mark, mina, edith));
//        };
//    }
}
