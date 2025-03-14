package com.m2ibank.controller;

import com.m2ibank.model.AccountTransactions;
import com.m2ibank.repository.AccountTransactionsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BalanceController {

    @Autowired
    private AccountTransactionsRepository accountTransactionsRepository;

    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<AccountTransactions> accountTransactions = accountTransactionsRepository
                .findByCustomerIdOrderByTransactionDtDesc(userId);
        if (accountTransactions != null) {
            return accountTransactions;
        } else {
            return null;
        }
    }

    @PostMapping("/createTransaction")
    public AccountTransactions createTransaction(@RequestBody AccountTransactions accountTransaction) {
        return accountTransactionsRepository.save(accountTransaction);
    }
}
