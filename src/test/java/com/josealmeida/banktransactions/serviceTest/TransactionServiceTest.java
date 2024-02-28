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


}
