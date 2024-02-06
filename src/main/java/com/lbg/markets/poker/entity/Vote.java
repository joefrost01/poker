package com.lbg.markets.poker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    private String user;
    private String ticket;
    private Integer score;

}
