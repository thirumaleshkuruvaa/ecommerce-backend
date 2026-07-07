package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.domain.AccountStatus;
import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.VerificationCode;
import com.ecommerce.backend.repository.AddressRepository;
import com.ecommerce.backend.repository.SellerRepository;
import com.ecommerce.backend.repository.VerificationCodeRepository;
import com.ecommerce.backend.service.Impl.SellerServiceImpl;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

        @Mock
        private SellerRepository sellerRepository;

        @Mock
        private VerificationCodeRepository verificationCodeRepository;

        @Mock
        private AddressRepository addressRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private JwtProvider jwtProvider;

        @InjectMocks
        private SellerServiceImpl sellerService;

        private Seller seller;
        private Address address;
        private VerificationCode verificationCode;

        @BeforeEach
        void setUp() {

                address = new Address();
                address.setId(1L);
                address.setCity("Hyderabad");
                address.setState("Telangana");
                address.setAddress("KPHB");
                address.setPinCode("500072");

                seller = new Seller();
                seller.setId(1L);
                seller.setSellerName("ABC Store");
                seller.setEmail("seller@gmail.com");
                seller.setPassword("password");
                seller.setMobile("9876543210");
                seller.setGstin("GST123456");
                seller.setPickedUpAddress(address);
                seller.setRole(USER_ROLE.ROLE_SELLER);
                seller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);

                verificationCode = new VerificationCode();
                verificationCode.setId(1L);
                verificationCode.setEmail("seller@gmail.com");
                verificationCode.setOtp("123456");
        }

        // -------------------------------------------------------
        // GET SELLER PROFILE SUCCESS
        // -------------------------------------------------------

        @Test
        void testGetSellerProfile() throws Exception {

                when(jwtProvider.getEmailFromJwtToken("jwt_token"))
                                .thenReturn("seller@gmail.com");

                when(sellerRepository.findByEmail("seller@gmail.com"))
                                .thenReturn(Optional.of(seller));

                Seller result = sellerService.getSellerProfile("Bearer jwt_token");

                assertNotNull(result);
                assertEquals("seller@gmail.com", result.getEmail());
                assertEquals("ABC Store", result.getSellerName());

                verify(jwtProvider).getEmailFromJwtToken("jwt_token");
                verify(sellerRepository).findByEmail("seller@gmail.com");
        }

        // -------------------------------------------------------
        // GET SELLER PROFILE INVALID JWT
        // -------------------------------------------------------

        @Test
        void testGetSellerProfile_InvalidJwt() {

                when(jwtProvider.getEmailFromJwtToken(any()))
                                .thenThrow(new RuntimeException());

                SellerException exception = assertThrows(
                                SellerException.class,
                                () -> sellerService.getSellerProfile("Bearer invalid"));

                assertEquals(
                                "Invalid JWT or Seller not found",
                                exception.getMessage());

                verify(jwtProvider).getEmailFromJwtToken("invalid");
        }

        // -------------------------------------------------------
        // CREATE SELLER SUCCESS
        // -------------------------------------------------------

        @Test
        void testCreateSeller() throws Exception {

                when(sellerRepository.findByEmail(seller.getEmail()))
                                .thenReturn(Optional.empty());

                when(addressRepository.save(any(Address.class)))
                                .thenReturn(address);

                when(passwordEncoder.encode("password"))
                                .thenReturn("encodedPassword");

                when(sellerRepository.save(any(Seller.class)))
                                .thenAnswer(invocation -> {
                                        Seller s = invocation.getArgument(0);
                                        s.setId(1L);
                                        return s;
                                });

                Seller result = sellerService.createSeller(seller);

                assertNotNull(result);
                assertEquals(1L, result.getId());
                assertEquals(USER_ROLE.ROLE_SELLER, result.getRole());
                assertEquals(
                                AccountStatus.PENDING_VERIFICATION,
                                result.getAccountStatus());
                assertEquals(
                                "encodedPassword",
                                result.getPassword());

                verify(addressRepository).save(any(Address.class));
                verify(passwordEncoder).encode("password");
                verify(sellerRepository).save(any(Seller.class));
        }

        // -------------------------------------------------------
        // CREATE SELLER ALREADY EXISTS
        // -------------------------------------------------------

        @Test
        void testCreateSeller_AlreadyExists() {

                when(sellerRepository.findByEmail(seller.getEmail()))
                                .thenReturn(Optional.of(seller));

                SellerException exception = assertThrows(
                                SellerException.class,
                                () -> sellerService.createSeller(seller));

                assertEquals(
                                "Seller already exists with this email",
                                exception.getMessage());

                verify(addressRepository, never()).save(any());
                verify(sellerRepository, never()).save(any());
        }

        // -------------------------------------------------------
        // CREATE SELLER WITHOUT ADDRESS
        // -------------------------------------------------------

        // @Test
        // void testCreateSeller_WithoutAddress() throws Exception {

        // seller.setPickedUpAddress(null);

        // when(sellerRepository.findByEmail(seller.getEmail()))
        // .thenReturn(Optional.empty());

        // when(passwordEncoder.encode(any()))
        // .thenReturn("encodedPassword");

        // when(sellerRepository.save(any(Seller.class)))
        // .thenAnswer(invocation -> invocation.getArgument(0));

        // Seller result = sellerService.createSeller(seller);

        // assertNotNull(result);
        // assertNull(result.getPickedUpAddress());

        // verify(addressRepository, never()).save(any());
        // verify(sellerRepository).save(any(Seller.class));
        // }

        // // -------------------------------------------------------
        // // GET SELLER BY ID SUCCESS
        // // -------------------------------------------------------

        // @Test
        // void testGetSellerById() throws Exception {

        // when(sellerRepository.findById(1L))
        // .thenReturn(Optional.of(seller));

        // Seller result = sellerService.getSellerById(1L);

        // assertNotNull(result);
        // assertEquals(1L, result.getId());

        // verify(sellerRepository).findById(1L);
        // }

        // // -------------------------------------------------------
        // // GET SELLER BY ID NOT FOUND
        // // -------------------------------------------------------

        // @Test
        // void testGetSellerById_NotFound() {

        // when(sellerRepository.findById(1L))
        // .thenReturn(Optional.empty());

        // SellerException exception = assertThrows(
        // SellerException.class,
        // () -> sellerService.getSellerById(1L));

        // assertEquals(
        // "Seller not found",
        // exception.getMessage());

        // verify(sellerRepository).findById(1L);
        // }

        // // -------------------------------------------------------
        // // GET SELLER BY EMAIL SUCCESS
        // // -------------------------------------------------------

        // @Test
        // void testGetSellerByEmail() throws Exception {

        // when(sellerRepository.findByEmail("seller@gmail.com"))
        // .thenReturn(Optional.of(seller));

        // Seller result = sellerService.getSellerByEmail("seller@gmail.com");

        // assertNotNull(result);
        // assertEquals("seller@gmail.com", result.getEmail());

        // verify(sellerRepository).findByEmail("seller@gmail.com");
        // }

        // // -------------------------------------------------------
        // // GET SELLER BY EMAIL NOT FOUND
        // // -------------------------------------------------------

        // @Test
        // void testGetSellerByEmail_NotFound() {

        // when(sellerRepository.findByEmail("seller@gmail.com"))
        // .thenReturn(Optional.empty());

        // SellerException exception = assertThrows(
        // SellerException.class,
        // () -> sellerService.getSellerByEmail("seller@gmail.com"));

        // assertEquals(
        // "Seller not found",
        // exception.getMessage());

        // verify(sellerRepository).findByEmail("seller@gmail.com");
        // }

        // // -------------------------------------------------------
        // // GET ALL SELLERS WITH STATUS
        // // -------------------------------------------------------

        // @Test
        // void testGetAllSellers_WithStatus() {

        // when(sellerRepository.findByAccountStatus(
        // AccountStatus.PENDING_VERIFICATION))
        // .thenReturn(java.util.List.of(seller));

        // var result = sellerService.getAllSellers(
        // AccountStatus.PENDING_VERIFICATION);

        // assertNotNull(result);
        // assertEquals(1, result.size());
        // assertEquals(seller, result.get(0));

        // verify(sellerRepository)
        // .findByAccountStatus(AccountStatus.PENDING_VERIFICATION);
        // }

        // -------------------------------------------------------
        // GET ALL SELLERS WITHOUT STATUS
        // -------------------------------------------------------

        @Test
        void testGetAllSellers() {

                when(sellerRepository.findAll())
                                .thenReturn(java.util.List.of(seller));

                var result = sellerService.getAllSellers(null);

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(seller, result.get(0));

                verify(sellerRepository).findAll();
        }

        // -------------------------------------------------------
        // UPDATE SELLER SUCCESS
        // -------------------------------------------------------

        @Test
        void testUpdateSeller() throws Exception {

                Seller request = new Seller();
                request.setSellerName("Updated Store");
                request.setMobile("9999999999");
                request.setEmail("updated@gmail.com");
                request.setGstin("GST999");

                when(sellerRepository.findById(1L))
                                .thenReturn(Optional.of(seller));

                when(sellerRepository.save(any(Seller.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                Seller result = sellerService.updateSeller(1L, request);

                assertNotNull(result);
                assertEquals("Updated Store", result.getSellerName());
                assertEquals("9999999999", result.getMobile());
                assertEquals("updated@gmail.com", result.getEmail());
                assertEquals("GST999", result.getGstin());

                verify(sellerRepository).save(any(Seller.class));
        }

        // -------------------------------------------------------
        // UPDATE SELLER PARTIAL UPDATE
        // -------------------------------------------------------

        @Test
        void testUpdateSeller_PartialUpdate() throws Exception {

                Seller request = new Seller();
                request.setSellerName("Only Name Updated");

                when(sellerRepository.findById(1L))
                                .thenReturn(Optional.of(seller));

                when(sellerRepository.save(any(Seller.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                Seller result = sellerService.updateSeller(1L, request);

                assertEquals("Only Name Updated", result.getSellerName());
                assertEquals("9876543210", result.getMobile());
                assertEquals("seller@gmail.com", result.getEmail());
                assertEquals("GST123456", result.getGstin());

                verify(sellerRepository).save(any(Seller.class));
        }

        // -------------------------------------------------------
        // DELETE SELLER SUCCESS
        // -------------------------------------------------------

        @Test
        void testDeleteSeller() throws Exception {

                when(sellerRepository.findById(1L))
                                .thenReturn(Optional.of(seller));

                assertDoesNotThrow(() -> sellerService.deleteSeller(1L));

                verify(sellerRepository).delete(seller);
        }
        // -------------------------------------------------------
        // DELETE SELLER - NOT FOUND
        // -------------------------------------------------------

        @Test
        void testDeleteSeller_NotFound() {

                when(sellerRepository.findById(1L))
                                .thenReturn(Optional.empty());

                SellerException exception = assertThrows(
                                SellerException.class,
                                () -> sellerService.deleteSeller(1L));

                assertEquals(
                                "Seller not found",
                                exception.getMessage());

                verify(sellerRepository, never()).delete(any());
        }

        // -------------------------------------------------------
        // VERIFY EMAIL OTP - SUCCESS
        // -------------------------------------------------------

        @Test
        void testVerifyEmailByOtp() throws Exception {

                when(verificationCodeRepository.findByOtp("123456"))
                                .thenReturn(Optional.of(verificationCode));

                when(sellerRepository.findByEmail("seller@gmail.com"))
                                .thenReturn(Optional.of(seller));

                when(sellerRepository.save(any(Seller.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                Seller result = sellerService.verifyEmailByOtp("123456");

                assertNotNull(result);
                assertTrue(result.isEmailVerified());

                verify(verificationCodeRepository)
                                .delete(verificationCode);

                verify(sellerRepository)
                                .save(seller);
        }

        // -------------------------------------------------------
        // VERIFY EMAIL OTP - INVALID OTP
        // -------------------------------------------------------

        @Test
        void testVerifyEmailByOtp_InvalidOtp() {

                when(verificationCodeRepository.findByOtp("111111"))
                                .thenReturn(Optional.empty());

                SellerException exception = assertThrows(
                                SellerException.class,
                                () -> sellerService.verifyEmailByOtp("111111"));

                assertEquals(
                                "Invalid OTP",
                                exception.getMessage());

                verify(sellerRepository, never()).save(any());
                verify(verificationCodeRepository, never()).delete(any());
        }

        // -------------------------------------------------------
        // UPDATE SELLER ACCOUNT STATUS
        // -------------------------------------------------------

        @Test
        void testUpdateSellerAccountStatus() throws Exception {

                when(sellerRepository.findById(1L))
                                .thenReturn(Optional.of(seller));

                when(sellerRepository.save(any(Seller.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                Seller result = sellerService.updateSellerAccountStatus(
                                1L,
                                AccountStatus.ACTIVE);

                assertNotNull(result);
                assertEquals(
                                AccountStatus.ACTIVE,
                                result.getAccountStatus());

                verify(sellerRepository).save(seller);
        }

        // -------------------------------------------------------
        // UPDATE SELLER ACCOUNT STATUS - SELLER NOT FOUND
        // -------------------------------------------------------

        @Test
        void testUpdateSellerAccountStatus_SellerNotFound() {

                when(sellerRepository.findById(1L))
                                .thenReturn(Optional.empty());

                SellerException exception = assertThrows(
                                SellerException.class,
                                () -> sellerService.updateSellerAccountStatus(
                                                1L,
                                                AccountStatus.ACTIVE));

                assertEquals(
                                "Seller not found",
                                exception.getMessage());

                verify(sellerRepository, never()).save(any());
        }

}