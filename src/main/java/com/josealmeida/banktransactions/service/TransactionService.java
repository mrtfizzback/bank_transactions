package com.josealmeida.banktransactions.service;

import com.josealmeida.banktransactions.dto.TransactionRequest;
import com.josealmeida.banktransactions.model.Transaction;
import com.josealmeida.banktransactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    //find all transactions
    public List<Transaction> findAllTransactions() {
        return repository.findAll();
    }

    //find transaction by id
    public Optional<Transaction> findTransactionById(Long id) {
        return repository.findById(id);
    }

    //create new transaction vai fazer return de um string, com id da transaction e tax, ou entao aviso de data invalida
    public Transaction createTransaction(TransactionRequest transactionRequest){
        Transaction transaction = Transaction.builder()
                .schedulingDate(transactionRequest.getSchedulingDate())
                .amount(transactionRequest.getAmount())
                .build();
        Transaction newTransaction = calculateTax(transaction);
        return repository.save(newTransaction);
    }
    //delete transaction
    public String deleteTransaction (Long id){
        repository.deleteById(id);
        return "Transaction with id " + id + ", was deleted!";
    }

    //alterar transaction
    public Transaction modifyTransaction(Long id, TransactionRequest transactionRequest) {
        // Retrieve the existing transaction from the repository
        Optional<Transaction> optionalTransaction = repository.findById(id);

        if (optionalTransaction.isPresent()) {
            Transaction existingTransaction = optionalTransaction.get();
            Transaction updatedTransaction = new Transaction();

            // Update the existing transaction with the modified values
            existingTransaction.setAmount(transactionRequest.getAmount());
            existingTransaction.setSchedulingDate(transactionRequest.getSchedulingDate());

            updatedTransaction = calculateTax(existingTransaction);
            // Save the modified transaction
            return repository.save(updatedTransaction);
        } else {
            // Transaction with the given ID not found
            throw new RuntimeException("Transaction with id " + id + " not found");
        }
    }



    public Transaction calculateTax(Transaction transaction) {

        BigDecimal fee = BigDecimal.ZERO;
        BigDecimal amountMinusFee = BigDecimal.ZERO;

        LocalDate currentDate = LocalDate.now();
        LocalDate schedulingDate = transaction.getSchedulingDate();

        // Validate that the scheduling date is not before current date
        if (schedulingDate.isBefore(currentDate)) {
            throw new RuntimeException("Scheduling Date can't be in the past!");
        }

        BigDecimal amount = transaction.getAmount();

        // Taxa A
        if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(new BigDecimal("1000")) <= 0) {
            if (schedulingDate.isEqual(currentDate)) {
                fee = BigDecimal.valueOf(3).add(amount.multiply(BigDecimal.valueOf(0.03)));
                amountMinusFee = amount.subtract(fee);
            } else {
                throw new RuntimeException("For transactions under or equal to 1000, scheduling date has to be today's date!");
            }
        }
        // Taxa B
        else if (amount.compareTo(new BigDecimal("1001")) >= 0 && amount.compareTo(new BigDecimal("2000")) <= 0) {
            if (schedulingDate.isAfter(currentDate.plusDays(1)) && schedulingDate.isBefore(currentDate.plusDays(10))) {
                fee = amount.multiply(BigDecimal.valueOf(0.09));
                amountMinusFee = amount.subtract(fee);
            } else {
                throw new RuntimeException("For transactions between 1001 and 2000, scheduling date must be between 1 and 10 days from today!");
            }
        }
        // Taxa C
        else if (amount.compareTo(new BigDecimal("2001")) > 0) {
            int daysDifference = (int) (schedulingDate.toEpochDay() - currentDate.toEpochDay());

            if (daysDifference >= 11 && daysDifference <= 20) {
                fee = amount.multiply(BigDecimal.valueOf(0.082));
                amountMinusFee = amount.subtract(fee);
            } else if (daysDifference >= 21 && daysDifference <= 30) {
                fee = amount.multiply(BigDecimal.valueOf(0.069));
                amountMinusFee = amount.subtract(fee);
            } else if (daysDifference >= 31 && daysDifference <= 40) {
                fee = amount.multiply(BigDecimal.valueOf(0.047));
                amountMinusFee = amount.subtract(fee);
            } else {
                fee = amount.multiply(BigDecimal.valueOf(0.017));
                amountMinusFee = amount.subtract(fee);
            }
        }

        transaction.setFee(fee);
        transaction.setAmountMinusFee(amountMinusFee);

        return transaction;
    }

}
