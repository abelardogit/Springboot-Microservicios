package com.paymentchain.transaction.controller.helper;

import com.paymentchain.transaction.entities.Transaction;
import com.paymentchain.transaction.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

public class TransactiontRestControllerHelper {

    public static Transaction getById(TransactionRepository transactionRepository, long id)
    {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        return optionalTransaction.orElse(null);
    }

    public static Transaction update(Transaction fromBD, Transaction fromUser)
    {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setId(fromBD.getId());
        Transaction from;
        if (null != fromUser.getReference()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedTransaction.setReference(from.getReference());

        if (null != fromUser.getIban()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedTransaction.setIban(from.getIban());

        if (null != fromUser.getDate()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedTransaction.setDate(from.getDate());

//        if (null != fromUser.getAmount()) {
//            from = fromUser;
//        } else {
//            from = fromBD;
//        }
//        updatedTransaction.setAmount(from.getAmount());

        if (null != fromUser.getDescription()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedTransaction.setDescription(from.getDescription());

        if (null != fromUser.getStatus()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedTransaction.setStatus(from.getStatus());

        if (null != fromUser.getChannel()) {
            from = fromUser;
        } else {
            from = fromBD;
        }
        updatedTransaction.setChannel(from.getChannel());

        return updatedTransaction;
    }

    public static List<Transaction> getByIban(TransactionRepository transactionRepository, String iban)
    {
        return transactionRepository.findByIban(iban);
    }

}
