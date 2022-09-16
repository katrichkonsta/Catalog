package com.example.catalog.services;

import com.example.catalog.entities.Category;
import com.example.catalog.repositories.ICategoryRepository;
import com.example.catalog.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    public void includeCategoryInsideAnother(Integer parentId, Integer childId) throws NoSuchElementException, IllegalStateException {
        Category parentCategory = categoryRepository.findById(parentId).orElseThrow();
        Category childCategory = categoryRepository.findById(childId).orElseThrow();

        if (getAllSubcategoryIds(childId).contains(parentId) || getAllSubcategoryIds(parentId).contains(childId))
            throw new IllegalStateException();

        parentCategory.getSubcategories().add(childCategory);
        categoryRepository.saveAndFlush(parentCategory);
    }

    public List<Integer> getAllSubcategoryIds(Integer parentId) throws NoSuchElementException {
        Category parentCategory = categoryRepository.findById(parentId).orElseThrow();
        return getSubcategoriesRecursive(parentCategory);
    }

    private List<Integer> getSubcategoriesRecursive(Category category) {
        if (category.getSubcategories().isEmpty() || category.getSubcategories() == null)
            return List.of(category.getId());
        else
            return Stream.concat(
                            category.getSubcategories().stream()
                                    .map(this::getSubcategoriesRecursive)
                                    .flatMap(List::stream),
                            Stream.of(category.getId()))
                    .collect(Collectors.toList());
    }
}
