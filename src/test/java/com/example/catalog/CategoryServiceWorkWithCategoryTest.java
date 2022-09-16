package com.example.catalog;

import com.example.catalog.entities.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceWorkWithCategoryTest extends BaseTest {

    Category firstCategory, secondCategory, thirdCategory, fourthCategory, fifthCategory;

    @BeforeEach
    public void categoryInitialization() {
        firstCategory = categoryRepository.saveAndFlush(Category.builder().name("1").build());
        secondCategory = categoryRepository.saveAndFlush(Category.builder().name("2").build());
        thirdCategory = categoryRepository.saveAndFlush(Category.builder().name("3").build());
        fourthCategory = categoryRepository.saveAndFlush(Category.builder().name("4").build());
        fifthCategory = categoryRepository.saveAndFlush(Category.builder().name("5").build());

        secondCategory.setSubcategories(new HashSet<>(List.of(fourthCategory, fifthCategory)));
        firstCategory.setSubcategories(new HashSet<>(List.of(secondCategory, thirdCategory)));
        secondCategory = categoryRepository.saveAndFlush(secondCategory);
        firstCategory = categoryRepository.saveAndFlush(firstCategory);
    }

    //     1
    //    / \
    //   2   3
    //  / \
    // 4   5

    @AfterEach
    public void cleanDatabase() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void getAllSubcategoryIdsOfRootCategory() {
        List<Integer> listOfIds = categoryService.getAllSubcategoryIds(firstCategory.getId());
        assertEquals(5, listOfIds.size());
        List<Integer> correctListOfIds = Stream.of(firstCategory, secondCategory, fourthCategory, thirdCategory, fifthCategory).map(Category::getId).collect(Collectors.toList());
        assertTrue(correctListOfIds.containsAll(listOfIds) && listOfIds.containsAll(correctListOfIds));
    }

    @Test
    public void getAllSubcategoryIdsOfSubcategory() {
        List<Integer> listOfIds = categoryService.getAllSubcategoryIds(secondCategory.getId());
        assertEquals(3, listOfIds.size());
        List<Integer> correctListOfIds = Stream.of(secondCategory, fourthCategory, fifthCategory).map(Category::getId).collect(Collectors.toList());
        assertTrue(correctListOfIds.containsAll(listOfIds) && listOfIds.containsAll(correctListOfIds));
    }

    @Test
    public void includeCategoryInsideAnotherTest() {
        Category newCategory = Category.builder().name("6").build();
        newCategory = categoryRepository.saveAndFlush(newCategory);

        categoryService.includeCategoryInsideAnother(firstCategory.getId(), newCategory.getId());

        firstCategory = categoryRepository.findById(firstCategory.getId()).orElseThrow();
        List<Integer> correctListOfIds = firstCategory.getSubcategories().stream().map(Category::getId).collect(Collectors.toList());
        assertTrue(correctListOfIds.contains(newCategory.getId()));
    }

    @Test
    public void includeCategoryInsideItselfException() {
        assertThrows(IllegalStateException.class, () -> {
            categoryService.includeCategoryInsideAnother(secondCategory.getId(), secondCategory.getId());
        });
    }

    @Test
    public void includeCategoryInsideParentException() {
        assertThrows(IllegalStateException.class, () -> {
            categoryService.includeCategoryInsideAnother(secondCategory.getId(), firstCategory.getId());
        });
    }

    @Test
    public void includeCategoryInsideChildException() {
        assertThrows(IllegalStateException.class, () -> {
            categoryService.includeCategoryInsideAnother(secondCategory.getId(), fourthCategory.getId());
        });
    }

}
