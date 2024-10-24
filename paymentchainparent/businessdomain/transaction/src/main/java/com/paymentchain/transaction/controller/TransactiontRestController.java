package com.paymentchain.transaction.controller;

import com.paymentchain.transaction.controller.helper.TransactiontRestControllerHelper;
import com.paymentchain.transaction.entities.Transaction;
import com.paymentchain.transaction.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactiontRestController {

    private final TransactionRepository transactionRepository;

    public TransactiontRestController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping()
    public List<Transaction> list()
    {
        return this.transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id)
    {
        Transaction aTransaction = TransactiontRestControllerHelper.getById(this.transactionRepository, id);
        if (null == aTransaction) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(aTransaction, HttpStatus.FOUND);
    }

    @GetMapping("/iban/{iban}")
    public ResponseEntity<?> getByIban(@PathVariable("iban") String iban)
    {
        System.out.println("getByIban:" + iban);
        List<Transaction> transactions = TransactiontRestControllerHelper.getByIban(this.transactionRepository, iban);
        if (null == transactions) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        System.out.println("transactions:" + transactions.size());
        return new ResponseEntity<>(transactions, HttpStatus.FOUND);
    }

    @PutMapping()
    public ResponseEntity<?> put(@RequestBody Transaction transaction)
    {
        long transactionId = transaction.getId();
        Transaction aTransactionFromBD = TransactiontRestControllerHelper.getById(this.transactionRepository, transactionId);
        if (null == aTransactionFromBD) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Transaction updatedTransaction = TransactiontRestControllerHelper.update(aTransactionFromBD, transaction);

        this.transactionRepository.save(updatedTransaction);

        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Transaction aTransaction)
    {
        Transaction savedTransaction = this.transactionRepository.save(aTransaction);
        return ResponseEntity.ok(savedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id)
    {
        Transaction aTransactionFromBD = TransactiontRestControllerHelper.getById(this.transactionRepository, id);
        if (null == aTransactionFromBD) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.transactionRepository.delete(aTransactionFromBD);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
