package com.gssk.gssk.repository;

import com.gssk.gssk.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface CreateAccountRepository extends CrudRepository<Account, String> {
}
