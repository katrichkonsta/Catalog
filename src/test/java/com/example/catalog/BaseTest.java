package com.example.catalog;

import com.example.catalog.repositories.ICategoryRepository;
import com.example.catalog.repositories.IProductRepository;
import com.example.catalog.services.CategoryService;
import com.example.catalog.services.ProductService;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogApplication.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {

    @Autowired
    protected ICategoryRepository categoryRepository;

    @Autowired
    protected IProductRepository productRepository;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected ProductService productService;
}
