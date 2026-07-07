package com.ecommerce.backend.service.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.domain.AccountStatus;
import com.ecommerce.backend.domain.USER_ROLE;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.VerificationCode;
import com.ecommerce.backend.repository.AddressRepository;
import com.ecommerce.backend.repository.SellerRepository;
import com.ecommerce.backend.repository.VerificationCodeRepository;
import com.ecommerce.backend.service.SellerService;
import com.ecommerce.backend.config.JwtProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SellerServiceImpl implements SellerService {

        private final SellerRepository sellerRepository;
        private final VerificationCodeRepository verificationCodeRepository;
        private final AddressRepository addressRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtProvider jwtProvider;

        public SellerServiceImpl(
                        SellerRepository sellerRepository,
                        VerificationCodeRepository verificationCodeRepository,
                        AddressRepository addressRepository,
                        PasswordEncoder passwordEncoder,
                        JwtProvider jwtProvider) {

                this.sellerRepository = sellerRepository;
                this.verificationCodeRepository = verificationCodeRepository;
                this.addressRepository = addressRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtProvider = jwtProvider;
        }

        // GET SELLER PROFILE (JWT)

        @Override
        public Seller getSellerProfile(String jwt) throws SellerException {

                log.info("Fetching seller profile using JWT");

                try {
                        if (jwt.startsWith("Bearer ")) {
                                jwt = jwt.substring(7);
                                log.debug("Removed Bearer prefix from token");
                        }

                        String email = jwtProvider.getEmailFromJwtToken(jwt);

                        log.info("Seller email extracted from token: {}", email);

                        Seller seller = getSellerByEmail(email);

                        log.info("Seller fetched successfully: {}", seller.getId());

                        return seller;

                } catch (Exception e) {
                        log.error("ERROR while fetching seller profile: {}", e.getMessage());
                        throw new SellerException("Invalid JWT or Seller not found");
                }
        }

        // CREATE SELLER

        @Override
        public Seller createSeller(Seller seller) throws SellerException {

                log.info("CREATE SELLER START for email: {}", seller.getEmail());

                Optional<Seller> existing = sellerRepository.findByEmail(seller.getEmail());

                if (existing.isPresent()) {
                        log.error("SELLER ALREADY EXISTS: {}", seller.getEmail());
                        throw new SellerException("Seller already exists with this email");
                }

                // save address first
                Address savedAddress = null;
                if (seller.getPickedUpAddress() != null) {
                        savedAddress = addressRepository.save(seller.getPickedUpAddress());
                        log.info("Pickup address saved");
                }

                Seller newSeller = new Seller();

                newSeller.setSellerName(seller.getSellerName());
                newSeller.setEmail(seller.getEmail());
                newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
                newSeller.setMobile(seller.getMobile());
                newSeller.setGstin(seller.getGstin());

                newSeller.setBusinessDetails(seller.getBusinessDetails());
                newSeller.setBankDetails(seller.getBankDetails());

                if (savedAddress != null) {
                        newSeller.setPickedUpAddress(savedAddress);
                }

                newSeller.setRole(USER_ROLE.ROLE_SELLER);
                newSeller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);

                Seller saved = sellerRepository.save(newSeller);

                log.info("SELLER CREATED SUCCESSFULLY with id: {}", saved.getId());

                return saved;
        }

        // GET SELLER BY ID

        @Override
        public Seller getSellerById(Long id) throws SellerException {

                log.info("Fetching seller by ID: {}", id);

                return sellerRepository.findById(id)
                                .orElseThrow(() -> {
                                        log.error("Seller not found: {}", id);
                                        return new SellerException("Seller not found");
                                });
        }

        // =========================
        // GET SELLER BY EMAIL
        // =========================
        @Override
        public Seller getSellerByEmail(String email) throws SellerException {

                log.info("Fetching seller by email: {}", email);

                return sellerRepository.findByEmail(email)
                                .orElseThrow(() -> {
                                        log.error("Seller not found with email: {}", email);
                                        return new SellerException("Seller not found");
                                });
        }

        // GET ALL SELLERS

        @Override
        public List<Seller> getAllSellers(AccountStatus status) {

                log.info("Fetching all sellers with status filter: {}", status);

                if (status != null) {
                        return sellerRepository.findByAccountStatus(status);
                }

                return sellerRepository.findAll();
        }

        // UPDATE SELLER

        @Override
        public Seller updateSeller(Long id, Seller seller) throws SellerException {

                log.info("Updating seller ID: {}", id);

                Seller existing = getSellerById(id);

                if (seller.getSellerName() != null)
                        existing.setSellerName(seller.getSellerName());

                if (seller.getMobile() != null)
                        existing.setMobile(seller.getMobile());

                if (seller.getEmail() != null)
                        existing.setEmail(seller.getEmail());

                if (seller.getGstin() != null)
                        existing.setGstin(seller.getGstin());

                Seller updated = sellerRepository.save(existing);

                log.info("Seller updated successfully: {}", updated.getId());

                return updated;
        }

        // DELETE SELLER

        @Override
        public void deleteSeller(Long id) throws SellerException {

                log.info("Deleting seller ID: {}", id);

                Seller seller = getSellerById(id);

                sellerRepository.delete(seller);

                log.info("Seller deleted successfully: {}", id);
        }

        // VERIFY OTP

        @Override
        public Seller verifyEmailByOtp(String otp) throws SellerException {

                log.info("Verifying seller OTP");

                VerificationCode code = verificationCodeRepository.findByOtp(otp)
                                .orElseThrow(() -> {
                                        log.error("Invalid OTP");
                                        return new SellerException("Invalid OTP");
                                });

                Seller seller = getSellerByEmail(code.getEmail());

                seller.setEmailVerified(true);

                verificationCodeRepository.delete(code);

                Seller saved = sellerRepository.save(seller);

                log.info("Seller email verified: {}", saved.getId());

                return saved;
        }

        // UPDATE STATUS

        @Override
        public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws SellerException {

                log.info("Updating seller status ID: {} to {}", sellerId, status);

                Seller seller = getSellerById(sellerId);

                seller.setAccountStatus(status);

                Seller updated = sellerRepository.save(seller);

                log.info("Seller status updated successfully");

                return updated;
        }
}