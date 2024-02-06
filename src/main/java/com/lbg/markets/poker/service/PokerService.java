package com.lbg.markets.poker.service;

import com.lbg.markets.poker.entity.Ticket;
import com.lbg.markets.poker.entity.Vote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class PokerService {

    private final Map<String, Ticket> tickets = new LinkedHashMap<>();
    private final Map<String, Vote> votes = new HashMap<>();

    public Ticket getTicket(String id) {
        return tickets.values().stream().filter(ticket -> ticket.getId().equals(id)).findFirst().orElse(null);
    }

    public void vote(String name, String id, int score) {
        votes.put(id+name, new Vote(name, id, score));
    }

    public List<Vote> allScores() {
        return new ArrayList<>(votes.values());
    }

    public List<Vote> scores(String id) {
        return votes.values().stream().filter(v -> v.getTicket().equals(id)).toList();
    }

    public Ticket getFirstTicket() {
        return tickets.entrySet().stream().findFirst().map(Map.Entry::getValue).orElse(null);
    }

    public List<String> getNextAndPreviousTickets(String currentTicketId) {
        List<String> nextAndPrev = new ArrayList<>(2);
        List<String> keys = new ArrayList<>(tickets.keySet());
        int currentIndex = keys.indexOf(currentTicketId);
        Ticket previousTicket = (currentIndex > 0) ? tickets.get(keys.get(currentIndex - 1)) : getTicket(currentTicketId);
        Ticket nextTicket = (currentIndex < keys.size() - 1) ? tickets.get(keys.get(currentIndex + 1)) : getTicket(currentTicketId);
        nextAndPrev.add(previousTicket.getId()); // Index 0 for previous ticket
        nextAndPrev.add(nextTicket.getId());     // Index 1 for next ticket
        return nextAndPrev;
    }

    public void createTickets(String filename) {
        try (CSVParser parser = new CSVParser(new FileReader(filename), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : parser) {
                String id = record.get(0);
                String summary = record.get(1);
                String description = record.get(2);

                tickets.put(id,new Ticket(id, summary, description));
            }
        } catch (Exception e) {
            log.error("Error loading tickets from file: {}", filename, e);
        }
    }
}
