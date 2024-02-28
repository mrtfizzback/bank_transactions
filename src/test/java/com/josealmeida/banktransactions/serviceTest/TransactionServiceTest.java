package com.josealmeida.banktransactions.serviceTest;

import com.josealmeida.banktransactions.dto.TransactionRequest;
import com.josealmeida.banktransactions.model.Transaction;
import com.josealmeida.banktransactions.repository.TransactionRepository;
import com.josealmeida.banktransactions.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransaction() {

        // Arrange
        TransactionRequest request = TransactionRequest.builder()
                .schedulingDate(LocalDate.now())
                .amount(BigDecimal.valueOf(500))
                .build();
        Transaction expectedTransaction = Transaction.builder()
                .schedulingDate(request.getSchedulingDate())
                .amount(request.getAmount())
                .build();

        when(transactionRepository.save(any())).thenReturn(expectedTransaction);

        // Act
        Transaction createdTransaction = transactionService.createTransaction(request);

        // Assert
        assertEquals(expectedTransaction.getSchedulingDate(), createdTransaction.getSchedulingDate());
        assertEquals(expectedTransaction.getAmount(), createdTransaction.getAmount());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    public void testModifyTransaction_Success() {
        // Arrange
        Long id = 1L;
        TransactionRequest request = TransactionRequest.builder()
                .schedulingDate(LocalDate.now().plusDays(2))
                .amount(BigDecimal.valueOf(1500)) // Entre 1001 e 2000 para Taxa B
                .build();
        Transaction existingTransaction = Transaction.builder()
                .id(id)
                .schedulingDate(LocalDate.now().plusDays(1))
                .amount(BigDecimal.valueOf(1200))
                .build();
        Transaction updatedTransaction = Transaction.builder()
                .id(id)
                .schedulingDate(request.getSchedulingDate())
                .amount(request.getAmount())
                .build();

        when(transactionRepository.findById(id)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any())).thenReturn(updatedTransaction);

        // Act
        Transaction modifiedTransaction = transactionService.modifyTransaction(id, request);

        // Assert
        assertEquals(updatedTransaction.getSchedulingDate(), modifiedTransaction.getSchedulingDate());
        assertEquals(updatedTransaction.getAmount(), modifiedTransaction.getAmount());
        verify(transactionRepository, times(1)).findById(id);

    }

    @Test
    public void testModifyTransaction_NotFound() {
        // Arrange
        Long id = 999L; // Id de Transação que não existe
        TransactionRequest request = TransactionRequest.builder()
                .schedulingDate(LocalDate.now().plusDays(2))
                .amount(BigDecimal.valueOf(1500))
                .build();

        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        try {
            transactionService.modifyTransaction(id, request);
        } catch (RuntimeException e) {
            assertEquals("Transaction with id " + id + " not found", e.getMessage());
        }

        // Assert
        verify(transactionRepository, times(1)).findById(id);
    }

}
