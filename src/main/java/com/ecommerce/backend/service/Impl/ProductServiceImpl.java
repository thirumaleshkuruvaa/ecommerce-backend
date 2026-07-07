
package com.ecommerce.backend.service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.ProductException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.repository.CategoryRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.WishListRepository;
import com.ecommerce.backend.request.CreateProductRequest;
import com.ecommerce.backend.service.ProductService;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;
        private final CategoryRepository categoryRepository;
        private final WishListRepository wishListRepository;

        public ProductServiceImpl(
                        ProductRepository productRepository,
                        WishListRepository wishListRepository,
                        CategoryRepository categoryRepository) {
                this.productRepository = productRepository;
                this.categoryRepository = categoryRepository;
                this.wishListRepository = wishListRepository;
        }

        // CREATE PRODUCT
        @Override
        public Product createProduct(CreateProductRequest req, Seller seller) {

                Category c1 = categoryRepository.findByName(req.getCategory());
                if (c1 == null) {
                        c1 = new Category();
                        c1.setName(req.getCategory());
                        c1.setLevel(1);
                        c1 = categoryRepository.save(c1);
                }

                Category c2 = categoryRepository.findByNameAndParentCategory(req.getCategory2(), c1);
                if (c2 == null) {
                        c2 = new Category();
                        c2.setName(req.getCategory2());
                        c2.setParentCategory(c1);
                        c2.setLevel(2);
                        c2 = categoryRepository.save(c2);
                }

                Category c3 = categoryRepository.findByNameAndParentCategory(req.getCategory3(), c2);
                if (c3 == null) {
                        c3 = new Category();
                        c3.setName(req.getCategory3());
                        c3.setParentCategory(c2);
                        c3.setLevel(3);
                        c3 = categoryRepository.save(c3);
                }

                int discount = calculateDiscountPercentage(req.getMrpPrice(), req.getSellingPrice());

                Product p = new Product();
                p.setTitle(req.getTitle());
                p.setDescription(req.getDescription());
                p.setMrpPrice(req.getMrpPrice());
                p.setSellingPrice(req.getSellingPrice());
                p.setDiscountPercent(discount);
                p.setColor(req.getColor());
                p.setBrand(req.getBrand());
                p.setImages(req.getImages());
                p.setSizes(req.getSizes());
                p.setQuantity(req.getQuantity());
                p.setSeller(seller);
                p.setCategory(c3);
                p.setCreatedAt(LocalDateTime.now());

                return productRepository.save(p);
        }

        // FIND PRODUCT BY ID
        @Override
        public Product findProductById(Long productId) throws ProductException {
                return productRepository.findById(productId)
                                .orElseThrow(() -> new ProductException("Product not found"));
        }

        // UPDATE PRODUCT
        @Override
        public Product updateProduct(Long productId, Product updated) throws ProductException {

                Product p = findProductById(productId);

                p.setTitle(updated.getTitle());
                p.setDescription(updated.getDescription());
                p.setMrpPrice(updated.getMrpPrice());
                p.setSellingPrice(updated.getSellingPrice());
                p.setColor(updated.getColor());
                p.setBrand(updated.getBrand());
                p.setSizes(updated.getSizes());
                p.setImages(updated.getImages());
                p.setQuantity(updated.getQuantity());

                p.setDiscountPercent(
                                calculateDiscountPercentage(updated.getMrpPrice(), updated.getSellingPrice()));

                return productRepository.save(p);
        }

        // DELETE PRODUCT
        @Transactional
        @Override
        public void deleteProduct(Long productId) throws ProductException {

                Product product = findProductById(productId);

                // remove product from wishlist join table first
                wishListRepository.deleteFromWishlist(productId);

                // then delete product
                productRepository.delete(product);
        }

        // DISCOUNT CALCULATION
        @Override
        public int calculateDiscountPercentage(int mrp, int selling) {
                if (mrp == 0)
                        return 0;
                return ((mrp - selling) * 100) / mrp;
        }

        // SEARCH PRODUCTS
        @Override
        public List<Product> searchProducts(String query) {
                if (query == null || query.isBlank()) {
                        return productRepository.findAll();
                }
                return productRepository.searchProduct(query);
        }

        // GET ALL PRODUCTS
        @Override
        public Page<Product> getAllProducts(
                        String category,
                        String brand,
                        String colors,
                        String sizes,
                        Integer minPrice,
                        Integer maxPrice,
                        Integer minDiscount,
                        String sort,
                        String stock,
                        Integer pageNumber) {

                Specification<Product> spec = (root, query, cb) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        // CATEGORY FILTER
                        // Supports:
                        // 1) level-3 click -> exact category (Samsung)
                        // 2) level-2 click -> all children under that category (Mobiles ->
                        // Samsung/iPhone/etc)
                        if (category != null && !category.isBlank()) {

                                Join<Product, Category> categoryJoin = root.join("category", JoinType.INNER);
                                Join<Category, Category> parentJoin = categoryJoin.join("parentCategory",
                                                JoinType.LEFT);

                                Predicate exactCategoryMatch = cb.equal(cb.lower(categoryJoin.get("name")),
                                                category.toLowerCase());

                                Predicate parentCategoryMatch = cb.equal(cb.lower(parentJoin.get("name")),
                                                category.toLowerCase());

                                predicates.add(cb.or(exactCategoryMatch, parentCategoryMatch));
                        }

                        // BRAND FILTER
                        if (brand != null && !brand.isBlank()) {
                                String[] brandArray = brand.split(",");
                                predicates.add(root.get("brand").in((Object[]) brandArray));
                        }

                        // COLOR FILTER
                        if (colors != null && !colors.isBlank()) {
                                String[] colorArray = colors.split(",");
                                predicates.add(root.get("color").in((Object[]) colorArray));
                        }

                        // SIZE FILTER
                        if (sizes != null && !sizes.isBlank()) {
                                String[] sizeArray = sizes.split(",");
                                List<Predicate> sizePredicates = new ArrayList<>();

                                for (String size : sizeArray) {
                                        sizePredicates.add(cb.isMember(size.trim(), root.get("sizes")));
                                }

                                predicates.add(cb.or(sizePredicates.toArray(new Predicate[0])));
                        }

                        // PRICE FILTER
                        if (minPrice != null) {
                                predicates.add(cb.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice));
                        }

                        if (maxPrice != null) {
                                predicates.add(cb.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice));
                        }

                        // DISCOUNT FILTER
                        if (minDiscount != null) {
                                predicates.add(cb.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
                        }

                        // STOCK FILTER
                        if (stock != null && !stock.isBlank()) {
                                if ("in_stock".equalsIgnoreCase(stock)) {
                                        predicates.add(cb.greaterThan(root.get("quantity"), 0));
                                } else if ("out_of_stock".equalsIgnoreCase(stock)) {
                                        predicates.add(cb.equal(root.get("quantity"), 0));
                                }
                        }

                        return cb.and(predicates.toArray(new Predicate[0]));
                };

                // SORTING
                Sort sortBy = Sort.by("createdAt").descending();

                if ("lowToHigh".equalsIgnoreCase(sort)) {
                        sortBy = Sort.by("sellingPrice").ascending();
                } else if ("highToLow".equalsIgnoreCase(sort)) {
                        sortBy = Sort.by("sellingPrice").descending();
                } else if ("newest".equalsIgnoreCase(sort)) {
                        sortBy = Sort.by("createdAt").descending();
                }

                Pageable pageable = PageRequest.of(pageNumber, 12, sortBy);

                log.info("GET ALL PRODUCTS SERVICE -> category: {}, brand: {}, colors: {}, sizes: {}, minPrice: {}, maxPrice: {}, minDiscount: {}, sort: {}, stock: {}, pageNumber: {}",
                                category, brand, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock,
                                pageNumber);

                Page<Product> page = productRepository.findAll(spec, pageable);

                log.info("Products found: {}", page.getTotalElements());

                return page;
        }

        // SELLER PRODUCTS
        @Override
        public List<Product> getProductBySellerId(Long sellerId) {
                return productRepository.findBySellerId(sellerId);
        }
}