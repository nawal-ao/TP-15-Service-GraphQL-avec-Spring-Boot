package com.example.banqueservice.dto;

public record TransactionStats(
        long count,
        double sumDepots,
        double sumRetraits
) {
}