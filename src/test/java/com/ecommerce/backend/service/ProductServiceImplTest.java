package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.WishListRepository;
import com.ecommerce.backend.request.CreateProductRequest;
import com.ecommerce.backend.service.Impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WishListRepository wishListRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Seller seller;
    private Product product;
    private CreateProductRequest request;
    private Category c1;
    private Category c2;
    private Category c3;

    @BeforeEach
    void setup() {

        seller = new Seller();
        seller.setId(1L);

        c1 = new Category();
        c1.setId(1L);
        c1.setName("Electronics");
        c1.setLevel(1);

        c2 = new Category();
        c2.setId(2L);
        c2.setName("Mobiles");
        c2.setParentCategory(c1);
        c2.setLevel(2);

        c3 = new Category();
        c3.setId(3L);
        c3.setName("Samsung");
        c3.setParentCategory(c2);
        c3.setLevel(3);

        request = new CreateProductRequest();
        request.setTitle("Galaxy S25");
        request.setDescription("Android Mobile");
        request.setMrpPrice(100000);
        request.setSellingPrice(90000);
        request.setBrand("Samsung");
        request.setColor("Black");
        request.setQuantity(10);
        request.setImages(Arrays.asList("img1"));
        request.setSizes(Arrays.asList("128GB"));
        request.setCategory("Electronics");
        request.setCategory2("Mobiles");
        request.setCategory3("Samsung");

        product = new Product();
        product.setId(100L);
        product.setTitle("Galaxy S25");
        product.setDescription("Android Mobile");
        product.setMrpPrice(100000);
        product.setSellingPrice(90000);
        product.setQuantity(10);
        product.setBrand("Samsung");
        product.setColor("Black");
        product.setCategory(c3);
        product.setSeller(seller);
    }

    // -------------------------------------------------------
    // CREATE PRODUCT
    // -------------------------------------------------------

    @Test
    void testCreateProduct_CategoryAlreadyExists() {

        when(categoryRepository.findByName("Electronics")).thenReturn(c1);
        when(categoryRepository.findByNameAndParentCategory("Mobiles", c1)).thenReturn(c2);
        when(categoryRepository.findByNameAndParentCategory("Samsung", c2)).thenReturn(c3);

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product saved = productService.createProduct(request, seller);

        assertNotNull(saved);
        assertEquals("Galaxy S25", saved.getTitle());
        assertEquals("Samsung", saved.getBrand());
        assertEquals(10, saved.getDiscountPercent());
        assertEquals(c3, saved.getCategory());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testCreateProduct_NewCategoriesCreated() {

        when(categoryRepository.findByName(anyString())).thenReturn(null);

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(c1)
                .thenReturn(c2)
                .thenReturn(c3);

        when(categoryRepository.findByNameAndParentCategory(anyString(), any()))
                .thenReturn(null);

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.createProduct(request, seller);

        assertNotNull(result);

        verify(categoryRepository, times(3)).save(any(Category.class));
        verify(productRepository).save(any(Product.class));
    }

    // -------------------------------------------------------
    // FIND PRODUCT
    // -------------------------------------------------------

    @Test
    void testFindProductById() throws Exception {

        when(productRepository.findById(100L))
                .thenReturn(Optional.of(product));

        Product result = productService.findProductById(100L);

        assertEquals(product, result);

        verify(productRepository).findById(100L);
    }

    @Test
    void testFindProductById_NotFound() {

        when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ProductException.class,
                () -> productService.findProductById(100L));

        verify(productRepository).findById(100L);
    }

    // -------------------------------------------------------
    // UPDATE PRODUCT
    // -------------------------------------------------------

    @Test
    void testUpdateProduct() throws Exception {

        Product updated = new Product();
        updated.setTitle("Updated");
        updated.setDescription("Updated Desc");
        updated.setMrpPrice(120000);
        updated.setSellingPrice(100000);
        updated.setBrand("Samsung");
        updated.setColor("Blue");
        updated.setImages(Arrays.asList("img"));
        updated.setSizes(Arrays.asList("256GB"));
        updated.setQuantity(15);

        when(productRepository.findById(100L))
                .thenReturn(Optional.of(product));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateProduct(100L, updated);

        assertEquals("Updated", result.getTitle());
        assertEquals(16, result.getDiscountPercent());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {

        when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ProductException.class,
                () -> productService.updateProduct(100L, new Product()));
    }
    // -------------------------------------------------------
    // DELETE PRODUCT
    // -------------------------------------------------------

    @Test
    void testDeleteProduct() throws Exception {

        when(productRepository.findById(100L))
                .thenReturn(Optional.of(product));

        doNothing().when(wishListRepository)
                .deleteFromWishlist(100L);

        doNothing().when(productRepository)
                .delete(product);

        productService.deleteProduct(100L);

        verify(wishListRepository).deleteFromWishlist(100L);
        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {

        when(productRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(ProductException.class,
                () -> productService.deleteProduct(100L));

        verify(wishListRepository, never())
                .deleteFromWishlist(anyLong());

        verify(productRepository, never())
                .delete(any(Product.class));
    }

    // -------------------------------------------------------
    // DISCOUNT PERCENTAGE
    // -------------------------------------------------------

    @Test
    void testCalculateDiscountPercentage() {

        int discount = productService.calculateDiscountPercentage(1000, 800);

        assertEquals(20, discount);
    }

    @Test
    void testCalculateDiscountPercentage_WhenMrpZero() {

        int discount = productService.calculateDiscountPercentage(0, 500);

        assertEquals(0, discount);
    }

    // -------------------------------------------------------
    // SEARCH PRODUCTS
    // -------------------------------------------------------

    @Test
    void testSearchProducts_WithQuery() {

        List<Product> products = Arrays.asList(product);

        when(productRepository.searchProduct("Samsung"))
                .thenReturn(products);

        List<Product> result = productService.searchProducts("Samsung");

        assertEquals(1, result.size());

        verify(productRepository)
                .searchProduct("Samsung");
    }

    @Test
    void testSearchProducts_QueryNull() {

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(product));

        List<Product> result = productService.searchProducts(null);

        assertEquals(1, result.size());

        verify(productRepository)
                .findAll();
    }

    @Test
    void testSearchProducts_QueryBlank() {

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(product));

        List<Product> result = productService.searchProducts("");

        assertEquals(1, result.size());

        verify(productRepository)
                .findAll();
    }

    // -------------------------------------------------------
    // SELLER PRODUCTS
    // -------------------------------------------------------

    @Test
    void testGetProductBySellerId() {

        List<Product> products = Arrays.asList(product);

        when(productRepository.findBySellerId(1L))
                .thenReturn(products);

        List<Product> result = productService.getProductBySellerId(1L);

        assertEquals(1, result.size());
        assertEquals(product, result.get(0));

        verify(productRepository)
                .findBySellerId(1L);
    }

    @Test
    void testGetProductBySellerId_EmptyList() {

        when(productRepository.findBySellerId(1L))
                .thenReturn(Collections.emptyList());

        List<Product> result = productService.getProductBySellerId(1L);

        assertTrue(result.isEmpty());

        verify(productRepository)
                .findBySellerId(1L);
    }
    // -------------------------------------------------------
    // GET ALL PRODUCTS
    // -------------------------------------------------------

    @Test
    void testGetAllProducts() {

        List<Product> productList = Arrays.asList(product);

        Page<Product> page = new PageImpl<>(productList);

        when(productRepository.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.getAllProducts(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0);

        assertEquals(1, result.getTotalElements());

        verify(productRepository)
                .findAll(any(Specification.class),
                        any(Pageable.class));
    }

    @Test
    void testGetAllProducts_WithFilters() {

        Page<Product> page = new PageImpl<>(Arrays.asList(product));

        when(productRepository.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.getAllProducts(
                "Mobiles",
                "Samsung",
                "Black",
                "M,L",
                10000,
                50000,
                20,
                "lowToHigh",
                "in_stock",
                0);

        assertEquals(1, result.getContent().size());

        verify(productRepository)
                .findAll(any(Specification.class),
                        any(Pageable.class));
    }

    @Test
    void testGetAllProducts_HighToLowSorting() {

        Page<Product> page = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.getAllProducts(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "highToLow",
                null,
                0);

        assertEquals(1, result.getContent().size());

        verify(productRepository)
                .findAll(any(Specification.class),
                        any(Pageable.class));
    }

    @Test
    void testGetAllProducts_NewestSorting() {

        Page<Product> page = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.getAllProducts(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "newest",
                null,
                0);

        assertEquals(1, result.getContent().size());

        verify(productRepository)
                .findAll(any(Specification.class),
                        any(Pageable.class));
    }

    @Test
    void testGetAllProducts_OutOfStock() {

        Page<Product> page = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.getAllProducts(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "out_of_stock",
                0);

        assertEquals(1, result.getContent().size());

        verify(productRepository)
                .findAll(any(Specification.class),
                        any(Pageable.class));
    }

    @Test
    void testGetAllProducts_EmptyResult() {

        Page<Product> page = new PageImpl<>(Collections.emptyList());

        when(productRepository.findAll(
                any(Specification.class),
                any(Pageable.class)))
                .thenReturn(page);

        Page<Product> result = productService.getAllProducts(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0);

        assertTrue(result.isEmpty());

        verify(productRepository)
                .findAll(any(Specification.class),
                        any(Pageable.class));
    }

}