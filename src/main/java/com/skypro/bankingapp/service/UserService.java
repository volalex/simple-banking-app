package com.skypro.bankingapp.service;

import com.skypro.bankingapp.dto.UserDTO;
import com.skypro.bankingapp.dto.request.CreateUserRequest;
import com.skypro.bankingapp.exception.InvalidPasswordException;
import com.skypro.bankingapp.exception.UserAlreadyExistsException;
import com.skypro.bankingapp.exception.UserNotFoundException;
import com.skypro.bankingapp.model.Currency;
import com.skypro.bankingapp.model.User;
import com.skypro.bankingapp.repository.UserRepository;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final AccountService accountService;

  public UserService(UserRepository userRepository, AccountService accountService) {
    this.userRepository = userRepository;
    this.accountService = accountService;
  }

  public UserDTO addUser(CreateUserRequest request) {

    if (userRepository.findById(request.username()).isPresent()) {
      throw new UserAlreadyExistsException();
    }
    validateUser(request);
    User user = userRepository.save(request.toUser());
    createNewUserAccounts(user);
    return UserDTO.fromUser(user);
  }

  private void validateUser(CreateUserRequest request) {
  }

  public void updatePassword(String username, String password, String newPassword) {
    User user = userRepository.findById(username).orElseThrow(UserNotFoundException::new);
    if (!user.getPassword().equals(password)) {
      throw new InvalidPasswordException();
    }
    user.setPassword(newPassword);
    userRepository.save(user);
  }

  public void removeUser(String username) {
    User user = userRepository.findById(username).orElseThrow(UserNotFoundException::new);
    userRepository.delete(user);
  }

  public User getUser(String username) {
    return userRepository.findById(username).orElseThrow(UserNotFoundException::new);
  }

  public Collection<UserDTO> getAllUsers() {
    return userRepository.findAll().stream().map(UserDTO::fromUser).collect(Collectors.toList());
  }

  private User createNewUserAccounts(User user) {
    user.addAccount(accountService.createAccount(user, Currency.RUB));
    user.addAccount(accountService.createAccount(user, Currency.USD));
    user.addAccount(accountService.createAccount(user, Currency.EUR));
    return user;
  }
}
