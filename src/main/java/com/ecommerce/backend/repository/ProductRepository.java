package com.ecommerce.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
        List<Product> findBySellerId(Long id);

        @Query("""
                        SELECT DISTINCT p
                        FROM Product p

                        LEFT JOIN p.category c
                        LEFT JOIN c.parentCategory pc
                        LEFT JOIN pc.parentCategory gpc

                        WHERE

                        LOWER(p.title) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.brand) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.description) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.color) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(c.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(pc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(gpc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        """)
        List<Product> searchProduct(
                        @Param("query") String query);

        @Query("""
                        SELECT DISTINCT p
                        FROM Product p

                        LEFT JOIN p.category c
                        LEFT JOIN c.parentCategory pc
                        LEFT JOIN pc.parentCategory gpc

                        WHERE

                        (

                        LOWER(p.title) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.brand) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.description) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.color) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(c.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(pc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(gpc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        )

                        AND

                        (:minPrice IS NULL OR p.sellingPrice>=:minPrice)

                        AND

                        (:maxPrice IS NULL OR p.sellingPrice<=:maxPrice)

                        """)
        List<Product> searchProductWithPrice(

                        @Param("query") String query,

                        @Param("minPrice") Integer minPrice,

                        @Param("maxPrice") Integer maxPrice

        );

        @Query("""
                        SELECT DISTINCT p
                        FROM Product p

                        LEFT JOIN p.category c
                        LEFT JOIN c.parentCategory pc
                        LEFT JOIN pc.parentCategory gpc

                        WHERE

                        (

                        LOWER(p.title) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.brand) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.description) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.color) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(c.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(pc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(gpc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        )

                        ORDER BY p.discountPercent DESC,
                                 p.sellingPrice ASC
                        """)
        List<Product> searchRecommendedProducts(

                        @Param("query") String query

        );

        @Query("""
                        SELECT DISTINCT p
                        FROM Product p

                        LEFT JOIN p.category c
                        LEFT JOIN c.parentCategory pc
                        LEFT JOIN pc.parentCategory gpc

                        WHERE

                        (

                        LOWER(p.title) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(p.brand) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(c.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(pc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        OR LOWER(gpc.name) LIKE LOWER(CONCAT('%',:query,'%'))

                        )

                        AND p.discountPercent>=:discount

                        ORDER BY p.discountPercent DESC

                        """)
        List<Product> searchProductByDiscount(

                        @Param("query") String query,

                        @Param("discount") Integer discount

        );

        // @Query("""
        // SELECT p FROM Product p
        // WHERE
        // (:query IS NULL
        // OR LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))
        // OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :query, '%'))
        // OR LOWER(p.color) LIKE LOWER(CONCAT('%', :query, '%'))
        // )
        // """)
        // List<Product> searchProduct(
        // @Param("query") String query);

}
