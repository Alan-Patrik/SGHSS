package com.alanpatrik.sghss.api.repository;

import com.alanpatrik.sghss.api.model.Auditoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Repository
public class AuditoriaRepositoryImpl implements AuditoriaRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Auditoria> search(String username, String action, String resource,
                                  LocalDateTime from, LocalDateTime to,
                                  Pageable pageable) {

        var criteriaBuilder = em.getCriteriaBuilder();

        // Query principal
        CriteriaQuery<Auditoria> criteriaBuilderQuery = criteriaBuilder.createQuery(Auditoria.class);
        Root<Auditoria> root = criteriaBuilderQuery.from(Auditoria.class);
        var predicates = new ArrayList<Predicate>();

        if (StringUtils.hasText(username)) {
            predicates.add(criteriaBuilder.equal(root.get("username"), username));
        }
        if (StringUtils.hasText(action)) {
            predicates.add(criteriaBuilder.equal(root.get("action"), action));
        }
        if (StringUtils.hasText(resource)) {
            predicates.add(criteriaBuilder.like(root.get("resource"), "%" + resource + "%"));
        }
        if (from != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventTime"), from));
        }
        if (to != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventTime"), to));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        // Ordenação
        if (pageable.getSort().isUnsorted()) {
            criteriaBuilderQuery.orderBy(criteriaBuilder.desc(root.get("eventTime")));
        } else {
            var orders = new ArrayList<Order>();
            for (Sort.Order o : pageable.getSort()) {
                Path<?> path = root.get(o.getProperty());
                orders.add(o.isAscending() ? criteriaBuilder.asc(path) : criteriaBuilder.desc(path));
            }
            criteriaBuilderQuery.orderBy(orders);
        }

        var query = em.createQuery(criteriaBuilderQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        var content = query.getResultList();

        // Count
        var countQuery = criteriaBuilder.createQuery(Long.class);
        var countRoot = countQuery.from(Auditoria.class);
        countQuery.select(criteriaBuilder.count(countRoot));
        var countPreds = new ArrayList<Predicate>();
        if (StringUtils.hasText(username)) {
            countPreds.add(criteriaBuilder.equal(countRoot.get("username"), username));
        }
        if (StringUtils.hasText(action)) {
            countPreds.add(criteriaBuilder.equal(countRoot.get("action"), action));
        }
        if (StringUtils.hasText(resource)) {
            countPreds.add(criteriaBuilder.like(countRoot.get("resource"), "%" + resource + "%"));
        }
        if (from != null) {
            countPreds.add(criteriaBuilder.greaterThanOrEqualTo(countRoot.get("eventTime"), from));
        }
        if (to != null) {
            countPreds.add(criteriaBuilder.lessThanOrEqualTo(countRoot.get("eventTime"), to));
        }
        countQuery.where(countPreds.toArray(new Predicate[0]));

        var total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }
}

