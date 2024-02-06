package com.lbg.markets.poker.controller;

import com.lbg.markets.poker.entity.Ticket;
import com.lbg.markets.poker.entity.Vote;
import com.lbg.markets.poker.service.PokerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PokerController {

    private final PokerService pokerService;

    @GetMapping("/")
    public String index(Model model) {
        populateModel(model, null);
        return "index";
    }

    @GetMapping("/{id}")
    public String ticket(@PathVariable String id, Model model) {
        populateModel(model, id);
        return "index";
    }

    @GetMapping("/{id}/show")
    public String show(@PathVariable String id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        populateModel(model, id);
        if (auth.getName().contains("leader")){
            List<Vote> votes = pokerService.scores(id);
            Double average = votes.stream().map(v->v.getScore()).mapToDouble(Integer::doubleValue)
                    .average()
                    .orElse(0.0);
            Optional<Integer> mode = findModalScore(votes);
            model.addAttribute("votes", votes);
            model.addAttribute("average", Math.round(average));
            if(mode.isPresent()){
                model.addAttribute("mode", mode.get());
            }else {
                model.addAttribute("mode", 0);
            }
        }
        return "index";
    }

    private void populateModel(Model model, String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        Ticket ticket = id == null ? pokerService.getFirstTicket() : pokerService.getTicket(id);
        model.addAttribute("ticket", ticket);
        List<String> nextAndPrev = pokerService.getNextAndPreviousTickets(ticket.getId());
        model.addAttribute("previousTicket", nextAndPrev.get(0));
        model.addAttribute("nextTicket", nextAndPrev.get(1));
    }

    private Optional<Integer> findModalScore(List<Vote> votes) {
        return votes.stream()
                .collect(Collectors.groupingBy(Vote::getScore, Collectors.counting())) // Group by score and count
                .entrySet().stream()
                .max(Map.Entry.comparingByValue()) // Find the entry with the maximum count
                .map(Map.Entry::getKey); // Extract the score
    }

}
