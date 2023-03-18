package com.skypro.bankingapp.repository;

import com.skypro.bankingapp.model.Account;
import com.skypro.bankingapp.model.Account_;
import com.skypro.bankingapp.model.Comparison;
import com.skypro.bankingapp.model.Currency;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepositoryCustomImpl {

  private final EntityManager entityManager;

  public AccountRepositoryCustomImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public List<Account> findAllAccountsByCurrency(Currency currency, Comparison comparison,
      double value) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Account> query = criteriaBuilder.createQuery(
        Account.class);
    Root<Account> root = query.from(Account.class);
    List<Predicate> predicates = new ArrayList<>();
    if (currency != null) {
      predicates.add(criteriaBuilder.equal(root.get(Account_.currency), currency));
    }
    switch (comparison) {
      case MORE -> predicates.add(criteriaBuilder.greaterThan(root.get(Account_.balance), value));
      case LESS -> predicates.add(criteriaBuilder.lessThan(root.get(Account_.balance), value));
    }
    query.where(predicates.toArray(new Predicate[0]));
    return entityManager.createQuery(query).getResultList();
  }
}
