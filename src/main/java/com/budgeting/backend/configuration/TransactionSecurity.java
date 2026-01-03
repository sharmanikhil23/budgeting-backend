package com.budgeting.backend.configuration;

import com.budgeting.backend.entity.TransactionEntity;
import com.budgeting.backend.repository.TransactionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("transactionSecurity")
public class TransactionSecurity {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    HouseHoldSecurity houseHoldSecurity;

    public boolean isCreatedByUserOrAdminOrAbove(String houseHoldId,
                                                 String transactionId,
                                                 ObjectId userId){
       TransactionEntity transaction = transactionRepository.findById(new ObjectId(transactionId)).orElse(null);

       if(transaction!=null){
           if(transaction.getCreatedBy().equals(userId)){
               return  true;
           }else{
               return houseHoldSecurity.isAdminOrOwner(houseHoldId, userId.toString());
           }
       }
        return false;
    }
}
