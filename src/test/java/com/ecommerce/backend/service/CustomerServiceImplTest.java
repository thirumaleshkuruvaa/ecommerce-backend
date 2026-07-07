
package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.SellerRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.Impl.CustomUserServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomUserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private CustomUserServiceImpl customUserService;

    private User user;
    private Seller seller;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");
        user.setPassword("password");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);

        seller = new Seller();
        seller.setId(2L);
        seller.setEmail("seller@gmail.com");
        seller.setPassword("sellerPassword");
        seller.setRole(USER_ROLE.ROLE_SELLER);
    }

    // -------------------------------------------------------
    // CUSTOMER LOGIN SUCCESS
    // -------------------------------------------------------

    @Test
    void testLoadUserByUsername_CustomerSuccess() {

        when(userRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        UserDetails result = customUserService.loadUserByUsername("user@gmail.com");

        assertNotNull(result);
        assertEquals("user@gmail.com", result.getUsername());
        assertEquals("password", result.getPassword());

        assertEquals(
                USER_ROLE.ROLE_CUSTOMER.toString(),
                result.getAuthorities().iterator().next().getAuthority());

        verify(userRepository).findByEmail("user@gmail.com");
        verifyNoInteractions(sellerRepository);
    }

    // -------------------------------------------------------
    // CUSTOMER NOT FOUND
    // -------------------------------------------------------

    @Test
    void testLoadUserByUsername_CustomerNotFound() {

        when(userRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserService.loadUserByUsername("user@gmail.com"));

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail("user@gmail.com");
        verifyNoInteractions(sellerRepository);
    }

    // -------------------------------------------------------
    // SELLER LOGIN SUCCESS
    // -------------------------------------------------------

    @Test
    void testLoadUserByUsername_SellerSuccess() {

        when(sellerRepository.findByEmail("seller@gmail.com"))
                .thenReturn(Optional.of(seller));

        UserDetails result = customUserService.loadUserByUsername(
                "seller_seller@gmail.com");

        assertNotNull(result);
        assertEquals("seller@gmail.com", result.getUsername());
        assertEquals("sellerPassword", result.getPassword());

        assertEquals(
                USER_ROLE.ROLE_SELLER.toString(),
                result.getAuthorities().iterator().next().getAuthority());

        verify(sellerRepository).findByEmail("seller@gmail.com");
        verifyNoInteractions(userRepository);
    }

    // -------------------------------------------------------
    // SELLER NOT FOUND
    // -------------------------------------------------------

    @Test
    void testLoadUserByUsername_SellerNotFound() {

        when(sellerRepository.findByEmail("seller@gmail.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserService.loadUserByUsername(
                        "seller_seller@gmail.com"));

        assertEquals("Seller not found", exception.getMessage());

        verify(sellerRepository).findByEmail("seller@gmail.com");
        verifyNoInteractions(userRepository);
    }

    // -------------------------------------------------------
    // CUSTOMER ROLE CHECK
    // -------------------------------------------------------

    @Test
    void testCustomerAuthority() {

        when(userRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        UserDetails details = customUserService.loadUserByUsername("user@gmail.com");

        assertTrue(
                details.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals(
                                USER_ROLE.ROLE_CUSTOMER.toString())));
    }

    // -------------------------------------------------------
    // SELLER ROLE CHECK
    // -------------------------------------------------------

    @Test
    void testSellerAuthority() {

        when(sellerRepository.findByEmail("seller@gmail.com"))
                .thenReturn(Optional.of(seller));

        UserDetails details = customUserService.loadUserByUsername(
                "seller_seller@gmail.com");

        assertTrue(
                details.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals(
                                USER_ROLE.ROLE_SELLER.toString())));
    }

    // -------------------------------------------------------
    // CUSTOMER PASSWORD CHECK
    // -------------------------------------------------------

    @Test
    void testCustomerPassword() {

        when(userRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        UserDetails details = customUserService.loadUserByUsername("user@gmail.com");

        assertEquals("password", details.getPassword());
    }

    // -------------------------------------------------------
    // SELLER PASSWORD CHECK
    // -------------------------------------------------------

    @Test
    void testSellerPassword() {

        when(sellerRepository.findByEmail("seller@gmail.com"))
                .thenReturn(Optional.of(seller));

        UserDetails details = customUserService.loadUserByUsername(
                "seller_seller@gmail.com");

        assertEquals("sellerPassword", details.getPassword());
    }

    // -------------------------------------------------------
    // VERIFY SELLER PREFIX
    // -------------------------------------------------------

    @Test
    void testSellerPrefixDetection() {

        when(sellerRepository.findByEmail("seller@gmail.com"))
                .thenReturn(Optional.of(seller));

        UserDetails details = customUserService.loadUserByUsername(
                "seller_seller@gmail.com");

        assertEquals("seller@gmail.com", details.getUsername());

        verify(sellerRepository).findByEmail("seller@gmail.com");
        verifyNoInteractions(userRepository);
    }

    // -------------------------------------------------------
    // VERIFY CUSTOMER DOES NOT USE SELLER REPOSITORY
    // -------------------------------------------------------

    @Test
    void testCustomerRepositoryOnly() {

        when(userRepository.findByEmail("user@gmail.com"))
                .thenReturn(Optional.of(user));

        customUserService.loadUserByUsername("user@gmail.com");

        verify(userRepository).findByEmail("user@gmail.com");
        verifyNoInteractions(sellerRepository);
    }
}