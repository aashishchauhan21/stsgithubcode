package com.bankapp.api.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bankapp.api.Entity.AccountTransaction;

@Repository
public interface AccountTransactionDAO extends CrudRepository<AccountTransaction, Long>{

}
