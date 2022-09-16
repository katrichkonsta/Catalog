package com.example.catalog.services;

import com.example.catalog.criteria.ProductCriteria;
import com.example.catalog.entities.Category;
import com.example.catalog.entities.Product;
import com.example.catalog.repositories.ICategoryRepository;
import com.example.catalog.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {

    @Autowired
    private ICategoryRepository categoryRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    @PersistenceContext
    public EntityManager entityManager;

    public void moveProductToAnotherCategory(Long productId, Integer newCategoryId) {
        Product product = productRepository.findById(productId).orElseThrow();
        Category category = categoryRepository.findById(newCategoryId).orElseThrow();

        product.setCategory(category);
        productRepository.saveAndFlush(product);
    }

    public List<Product> searchProductFromWholeCategory(Integer categoryId, ProductCriteria criteria) throws NoSuchElementException {
        List<Integer> subcategoryIds = categoryService.getAllSubcategoryIds(categoryId);
        CriteriaQuery<Product> query = criteriaParsing(subcategoryIds, criteria);
        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        if (criteria.offset != null) typedQuery = typedQuery.setFirstResult(criteria.offset);
        if (criteria.limit != null) typedQuery = typedQuery.setMaxResults(criteria.limit);
        return typedQuery.getResultList();
    }

    private CriteriaQuery<Product> criteriaParsing(List<Integer> subcategoryIds, ProductCriteria criteria) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);

        Root<Product> product = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(product.get("category").in(subcategoryIds));

        if (criteria.name != null)
            predicates.add(builder.like(product.get("name"), "%" + criteria.name + "%"));

        if (criteria.amount != null)
            predicates.add(builder.equal(product.get("amount"), criteria.amount));
        else {
            if (criteria.amountLo != null)
                predicates.add(builder.greaterThanOrEqualTo(product.get("amount"), criteria.amountLo));
            if (criteria.amountHi != null)
                predicates.add(builder.lessThanOrEqualTo(product.get("amount"), criteria.amountHi));
        }

        if (criteria.price != null)
            predicates.add(builder.equal(product.get("price"), criteria.price));
        else {
            if (criteria.priceLo != null)
                predicates.add(builder.greaterThanOrEqualTo(product.get("price"), criteria.priceLo));
            if (criteria.priceHi != null)
                predicates.add(builder.lessThanOrEqualTo(product.get("price"), criteria.priceHi));
        }

        if (criteria.weight != null)
            predicates.add(builder.equal(product.get("weight"), criteria.weight));
        else {
            if (criteria.weightLo != null)
                predicates.add(builder.greaterThanOrEqualTo(product.get("weight"), criteria.weightLo));
            if (criteria.weightHi != null)
                predicates.add(builder.lessThanOrEqualTo(product.get("weight"), criteria.weightHi));
        }

        if (criteria.year != null)
            predicates.add(builder.equal(product.get("yearOfManufacture"), criteria.year));
        else {
            if (criteria.yearLo != null)
                predicates.add(builder.greaterThanOrEqualTo(product.get("yearOfManufacture"), criteria.yearLo));
            if (criteria.yearHi != null)
                predicates.add(builder.lessThanOrEqualTo(product.get("yearOfManufacture"), criteria.yearHi));
        }

        if (criteria.country != null) predicates.add(builder.equal(product.get("issuingCountry"), criteria.country));

        if (criteria.sortBy != null) query.orderBy(builder.asc(product.get(criteria.sortBy)));

        query.where(predicates.toArray(new Predicate[0]));
        return query;
    }
}
