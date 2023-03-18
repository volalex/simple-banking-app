package com.skypro.bankingapp.repository;

import com.skypro.bankingapp.model.Account;
import com.skypro.bankingapp.model.Account_;
import com.skypro.bankingapp.model.Comparison;
import com.skypro.bankingapp.model.Currency;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecifications {
  public static Specification<Account> hasCurrency(Currency currency) {
    return (root, query, criteriaBuilder) -> {
      if (currency != null) {
        return criteriaBuilder.equal(root.get(Account_.currency), currency);
      } else {
        return null;
      }
    };
  }

  public static Specification<Account> hasBalanceBasedOnComparison(Comparison comparison,
      double balance) {
    return (root, query, criteriaBuilder) -> switch (comparison) {
      case MORE -> criteriaBuilder.greaterThan(root.get(Account_.balance), balance);
      case LESS -> criteriaBuilder.lessThan(root.get(Account_.balance), balance);
    };
  }
}
