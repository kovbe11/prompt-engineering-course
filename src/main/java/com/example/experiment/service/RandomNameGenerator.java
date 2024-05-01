package com.example.experiment.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RandomNameGenerator {

    public String generateCodeRunId() {
        return UUID.randomUUID().toString();
    }

}
