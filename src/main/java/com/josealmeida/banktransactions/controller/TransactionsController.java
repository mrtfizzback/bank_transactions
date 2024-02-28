package com.josealmeida.banktransactions.controller;

import com.josealmeida.banktransactions.dto.TransactionRequest;
import com.josealmeida.banktransactions.model.Transaction;
import com.josealmeida.banktransactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {
    @Autowired
    private TransactionService transactionService;

//    @PostMapping
//    public Transaction createAccount(@RequestBody TransactionRequest transactionRequest) {
//        return transactionService.createTransaction(transactionRequest);
//    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        try {
            Transaction transaction = transactionService.createTransaction(transactionRequest);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modifyTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        try {
            Transaction modifiedTransaction = transactionService.modifyTransaction(id, transactionRequest);
            return ResponseEntity.ok(modifiedTransaction);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping
    public List<Transaction> ListTransactions() {
        return transactionService.findAllTransactions();
    }


    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable Long id) {
        return transactionService.findTransactionById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }


    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id);
    }



}
