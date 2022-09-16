package com.example.catalog;

import com.example.catalog.criteria.ProductCriteria;
import com.example.catalog.entities.Category;
import com.example.catalog.entities.Product;
import com.example.catalog.enums.Country;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CategoryServiceWorkWithProductTest extends BaseTest {

    Category firstCategory, secondCategory;

    Long firstId, secondId, thirdId, fourthId, fifthId;

    @BeforeAll
    public void productInitialization() {
        firstCategory = categoryRepository.saveAndFlush(Category.builder().name("1").build());
        secondCategory = categoryRepository.saveAndFlush(Category.builder().name("2").build());
        firstCategory.setSubcategories(new HashSet<>(List.of(secondCategory)));
        firstCategory = categoryRepository.saveAndFlush(firstCategory);

        firstId = productRepository.saveAndFlush(Product.builder().name("product 1").amount(6).price(1.0).weight(1).yearOfManufacture(1).category(secondCategory).issuingCountry(Country.UA).build()).getId();
        secondId = productRepository.saveAndFlush(Product.builder().name("product 2").amount(2).price(2.0).weight(2).yearOfManufacture(2).category(secondCategory).issuingCountry(Country.UK).build()).getId();
        thirdId = productRepository.saveAndFlush(Product.builder().name("abcd").amount(3).price(3.0).weight(3).yearOfManufacture(3).category(secondCategory).build()).getId();
        fourthId = productRepository.saveAndFlush(Product.builder().name("product 4").amount(4).price(4.0).weight(4).yearOfManufacture(4).category(secondCategory).build()).getId();
        fifthId = productRepository.saveAndFlush(Product.builder().name("product 5").amount(5).price(5.0).weight(5).yearOfManufacture(5).category(secondCategory).build()).getId();
    }

    @Test
    public void moveProductToAnotherCategoryTest() {
        productService.moveProductToAnotherCategory(firstId, firstCategory.getId());
        Product actualProduct = productRepository.findById(firstId).orElseThrow();
        assertEquals(firstCategory.getId(), actualProduct.getCategory().getId());
    }

    @Test
    public void searchProductByNameTest() {
        ProductCriteria criteria = ProductCriteria.builder().name("bc").build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(1, list.size());
        assertEquals(thirdId, list.get(0).getId());
    }

    @Test
    public void searchProductByAmountTest() {
        ProductCriteria criteria = ProductCriteria.builder().amount(2).build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(1, list.size());
        assertEquals(secondId, list.get(0).getId());
    }

    @Test
    public void searchProductByAmountRangeTest() {
        ProductCriteria criteria = ProductCriteria.builder().amountLo(2).amountHi(4).build();
        List<Long> expectedList = List.of(secondId, thirdId, fourthId);
        List<Long> actualList = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria).stream().map(Product::getId).collect(Collectors.toList());

        assertEquals(3, actualList.size());
        assertTrue(expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
    }

    @Test
    public void searchProductByPriceTest() {
        ProductCriteria criteria = ProductCriteria.builder().price(3.0).build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(1, list.size());
        assertEquals(thirdId, list.get(0).getId());
    }

    @Test
    public void searchProductByPriceRangeTest() {
        ProductCriteria criteria = ProductCriteria.builder().priceLo(2.0).priceHi(4.0).build();
        List<Long> expectedList = List.of(secondId, thirdId, fourthId);
        List<Long> actualList = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria).stream().map(Product::getId).collect(Collectors.toList());

        assertEquals(3, actualList.size());
        assertTrue(expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
    }

    @Test
    public void searchProductByWeightTest() {
        ProductCriteria criteria = ProductCriteria.builder().amount(4).build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(1, list.size());
        assertEquals(fourthId, list.get(0).getId());
    }

    @Test
    public void searchProductByWeightRangeTest() {
        ProductCriteria criteria = ProductCriteria.builder().weightLo(2).weightHi(4).build();
        List<Long> expectedList = List.of(secondId, thirdId, fourthId);
        List<Long> actualList = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria).stream().map(Product::getId).collect(Collectors.toList());

        assertEquals(3, actualList.size());
        assertTrue(expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
    }

    @Test
    public void searchProductByYearTest() {
        ProductCriteria criteria = ProductCriteria.builder().year(1).build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(1, list.size());
        assertEquals(firstId, list.get(0).getId());
    }

    @Test
    public void searchProductByYearRangeTest() {
        ProductCriteria criteria = ProductCriteria.builder().yearLo(2).yearHi(4).build();
        List<Long> expectedList = List.of(secondId, thirdId, fourthId);
        List<Long> actualList = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria).stream().map(Product::getId).collect(Collectors.toList());

        assertEquals(3, actualList.size());
        assertTrue(expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
    }

    @Test
    public void searchProductByCountryTest() {
        ProductCriteria criteria = ProductCriteria.builder().country(Country.UA).build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(1, list.size());
        assertEquals(firstId, list.get(0).getId());
    }

    @Test
    public void searchProductWithSortTest() {
        ProductCriteria criteria = ProductCriteria.builder().sortBy("amount").build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(5, list.size());
        assertEquals(secondId, list.get(0).getId());
    }

    @Test
    public void searchProductWithOffsetTest() {
        ProductCriteria criteria = ProductCriteria.builder().offset(3).build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(2, list.size());
        assertEquals(fourthId, list.get(0).getId());
    }

    @Test
    public void searchProductWithLimitTest() {
        ProductCriteria criteria = ProductCriteria.builder().limit(3).build();
        List<Product> list = productService.searchProductFromWholeCategory(firstCategory.getId(), criteria);

        assertEquals(3, list.size());
        assertEquals(firstId, list.get(0).getId());
    }

}
