package com.example.banqueservice.controllers;

import com.example.banqueservice.dto.Stats;
import com.example.banqueservice.dto.TransactionStats;
import com.example.banqueservice.entities.Compte;
import com.example.banqueservice.repositories.CompteRepository;
import com.example.banqueservice.repositories.TransactionRepository;
import com.example.banqueservice.entities.Transaction;
import com.example.banqueservice.dto.TransactionRequest;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
public class CompteControllerGraphQL {
    private CompteRepository compteRepository;
    private TransactionRepository transactionRepository;

    @QueryMapping
    public List<Compte> allComptes(){
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument Long id){
        Compte compte = compteRepository.findById(id).orElse(null);
        if(compte == null) throw new RuntimeException(String.format("Compte %s not found", id));
        else return compte;
    }

    @MutationMapping
    public Compte saveCompte(@Argument Compte compte){
        return compteRepository.save(compte);
    }

    @QueryMapping
    public Stats totalSolde() {
        long count = compteRepository.count();
        double sum = compteRepository.sumSoldes();
        double average = count > 0 ? sum / count : 0;
        return new Stats(count, sum, average);
    }

    @MutationMapping
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        Compte compte = compteRepository.findById(transactionRequest.compteId())
                .orElseThrow(() -> new RuntimeException("Compte " + transactionRequest.compteId() + " not found"));

        Transaction transaction = new Transaction();
        transaction.setMontant(transactionRequest.montant());
        transaction.setDate(transactionRequest.date());
        transaction.setType(transactionRequest.type());
        transaction.setCompte(compte);

        return transactionRepository.save(transaction);
    }

    @QueryMapping
    public List<Transaction> compteTransactions(@Argument Long id) {
        Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte " + id + " not found"));
        return transactionRepository.findByCompte(compte);
    }

    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    @QueryMapping
    public TransactionStats transactionStats() {
        long count = transactionRepository.count();
        double sumDepots = transactionRepository.sumByType(com.example.banqueservice.enums.TypeTransaction.DEPOT);
        double sumRetraits = transactionRepository.sumByType(com.example.banqueservice.enums.TypeTransaction.RETRAIT);

        return new TransactionStats(count, sumDepots, sumRetraits);
    }
}
