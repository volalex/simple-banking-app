package com.skypro.bankingapp.service;

import com.skypro.bankingapp.dto.AccountDTO;
import com.skypro.bankingapp.exception.AccountNotFoundException;
import com.skypro.bankingapp.exception.InsufficientFundsException;
import com.skypro.bankingapp.exception.InvalidChangeAmountException;
import com.skypro.bankingapp.model.Account;
import com.skypro.bankingapp.model.Currency;
import com.skypro.bankingapp.model.User;
import com.skypro.bankingapp.repository.AccountRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public void changeBalance(
      String username, String accountNumber, Operation operation, double amount) {
    if (amount <= 0) {
      throw new InvalidChangeAmountException();
    }
    Account account =
        accountRepository
            .findById(accountNumber)
            .filter(acc -> acc.getUser().getUsername().equals(username))
            .orElseThrow(AccountNotFoundException::new);
    if (operation.equals(Operation.DEPOSIT)) {
      depositOnAccount(account, amount);
    } else {
      withdrawFromAccount(account, amount);
    }
  }

  public Account createAccount(User user, Currency currency) {
    Account account = new Account(UUID.randomUUID().toString(), 0.0, currency);
    account.setUser(user);
    accountRepository.save(account);
    return account;
  }

  public AccountDTO getAccount(String username, String accountNumber) {
    return accountRepository
        .findById(accountNumber)
        .filter(acc -> acc.getUser().getUsername().equals(username))
        .map(AccountDTO::fromAccount)
        .orElseThrow(AccountNotFoundException::new);
  }

  private void withdrawFromAccount(Account account, double amount) {
    if (account.getBalance() < amount) {
      throw new InsufficientFundsException();
    }
    account.setBalance(account.getBalance() - amount);
  }

  private void depositOnAccount(Account account, double amount) {
    account.setBalance(account.getBalance() + amount);
  }
}
