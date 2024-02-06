package com.lbg.markets.poker.controller;

import com.lbg.markets.poker.service.PokerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiController {

    private final PokerService pokerService;

    @GetMapping("/vote/{id}/{score}")
    public void vote(@PathVariable String id, @PathVariable int score, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        pokerService.vote(auth.getName(), id, score);
    }

    @GetMapping("/download-csv")
    public ResponseEntity<byte[]> downloadCsv() {
        StringBuilder sb = new StringBuilder("id,user,vote\n");
        pokerService.allScores().forEach(v ->
                sb.append(v.getTicket()).append(",").append(v.getUser()).append(",").append(v.getScore()).append("\n"));
        byte[] csvBytes = sb.toString().getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("filename", "scores.csv");
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}
