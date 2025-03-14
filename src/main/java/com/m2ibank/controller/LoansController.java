package com.m2ibank.controller;

import com.m2ibank.model.Loans;
import com.m2ibank.repository.LoanRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoansController {

    @Autowired
    private LoanRepository loanRepository;

    @GetMapping("/myLoans")
    public List<Loans> getLoanDetails(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(userId);
        if (loans != null) {
            return loans;
        } else {
            return null;
        }
    }

    @PostMapping("/createLoan")
    public Loans createLoan(@RequestBody Loans loan) {
        return loanRepository.save(loan);
    }

}
