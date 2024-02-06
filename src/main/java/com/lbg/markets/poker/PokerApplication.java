package com.lbg.markets.poker;

import com.lbg.markets.poker.service.PokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
@RequiredArgsConstructor
public class PokerApplication implements ApplicationRunner {

    private final PokerService pokerService;

    public static void main(String[] args) {
        SpringApplication.run(PokerApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("filename")) {
            String filename = args.getOptionValues("filename").get(0); // Get the first value of the '-f' option
            pokerService.createTickets(filename);
        } else {
            System.out.println("No filename provided with the '-f' option.");
            System.exit(1);
        }
    }
}
