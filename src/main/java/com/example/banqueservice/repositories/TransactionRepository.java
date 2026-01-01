package com.example.banqueservice.repositories;

import com.example.banqueservice.entities.Compte;
import com.example.banqueservice.entities.Transaction;
import com.example.banqueservice.enums.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Trouver les transactions pour un compte donné
    List<Transaction> findByCompte(Compte compte);

    // Calculer la somme par type (utilisé pour les statistiques)
    @Query("SELECT COALESCE(SUM(t.montant), 0) FROM Transaction t WHERE t.type = :type")
    double sumByType(@Param("type") TypeTransaction type);
}