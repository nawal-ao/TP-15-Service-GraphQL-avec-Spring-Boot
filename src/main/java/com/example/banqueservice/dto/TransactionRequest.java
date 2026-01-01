package com.example.banqueservice.dto;

import com.example.banqueservice.enums.TypeTransaction;
import java.util.Date;

public record TransactionRequest(
        Long compteId,
        double montant,
        Date date,
        TypeTransaction type
) {
}