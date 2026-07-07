package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.config.JwtProvider;
import com.ecommerce.backend.domain.AccountStatus;
import com.ecommerce.backend.exceptions.OtpException;
import com.ecommerce.backend.exceptions.SellerException;
import com.ecommerce.backend.model.Seller;
import com.ecommerce.backend.model.SellerReport;
import com.ecommerce.backend.model.VerificationCode;
import com.ecommerce.backend.repository.VerificationCodeRepository;
import com.ecommerce.backend.request.SellerLoginOtpRequest;
import com.ecommerce.backend.request.LoginRequest;
import com.ecommerce.backend.response.AuthResponse;
import com.ecommerce.backend.service.AuthService;
import com.ecommerce.backend.service.EmailService;
import com.ecommerce.backend.service.SellerReportService;
import com.ecommerce.backend.service.SellerService;
import com.ecommerce.backend.util.OtpUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/sellers")
@Tag(name = "8. Seller Management", description = "Seller registration, login, profile, reports")
public class SellerController {

        private final EmailService emailService;

        private final SellerService sellerService;

        private final VerificationCodeRepository verificationCodeRepository;

        private final AuthService authService;

        private final SellerReportService sellerReportService;

        private final JwtProvider jwtProvider;

        public SellerController(
                        SellerService sellerService,
                        VerificationCodeRepository verificationCodeRepository,
                        AuthService authService,
                        EmailService emailService,
                        SellerReportService sellerReportService,
                        JwtProvider jwtProvider) {

                this.sellerService = sellerService;
                this.verificationCodeRepository = verificationCodeRepository;
                this.authService = authService;
                this.emailService = emailService;
                this.sellerReportService = sellerReportService;
                this.jwtProvider = jwtProvider;
        }

        // CREATE SELLER
        @Operation(summary = "Create Seller", description = "Register new seller account and send OTP")

        @PostMapping("")
        public ResponseEntity<Seller> createSeller(
                        @RequestBody Seller seller)
                        throws Exception {

                log.info("Creating seller account for email : {}", seller.getEmail());

                // SAVE SELLER

                Seller savedSeller = sellerService.createSeller(seller);

                log.info("Seller account created successfully with id : {}", savedSeller.getId());

                // DELETE OLD OTP

                verificationCodeRepository.deleteByEmail(seller.getEmail());
                log.info("Deleted old OTP records for email : {}", seller.getEmail());

                // GENERATE OTP

                String otp = OtpUtil.generateOtp();

                log.info("Generated OTP for seller email : {}", seller.getEmail());

                // SAVE OTP

                VerificationCode verificationCode = new VerificationCode();

                verificationCode.setEmail(seller.getEmail());

                verificationCode.setOtp(otp);

                verificationCodeRepository.save(verificationCode);

                log.info("OTP saved successfully for email : {}", seller.getEmail());

                // SEND EMAIL

                String subject = "Mana Bazaar Seller Verification OTP";

                String text = "Welcome To Mana Bazaar\n\n"
                                + "Your OTP is : "
                                + otp;

                emailService.sendVerificationOtpEmail(
                                seller.getEmail(),
                                otp,
                                subject,
                                text);

                log.info("Verification OTP email sent successfully to : {}", seller.getEmail());

                return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
        }

        // SEND SELLER LOGIN OTP
        @Operation(summary = "Seller Login OTP", description = "Send OTP for seller login")

        @PostMapping("/send-login-otp")
        public ResponseEntity<String> sendSellerLoginOtp(
                        @RequestBody SellerLoginOtpRequest req)
                        throws Exception {

                log.info("Sending login OTP to seller email : {}", req.getEmail());

                // CHECK SELLER EXISTS

                Seller seller = sellerService.getSellerByEmail(req.getEmail());

                log.info("Seller found with id : {}", seller.getId());

                // DELETE OLD OTP

                verificationCodeRepository.deleteByEmail(req.getEmail());

                log.info("Deleted old login OTP for email : {}", req.getEmail());

                // GENERATE OTP

                String otp = OtpUtil.generateOtp();

                log.info("Generated login OTP for email : {}", req.getEmail());

                // SAVE OTP

                VerificationCode verificationCode = new VerificationCode();

                verificationCode.setEmail(req.getEmail());

                verificationCode.setOtp(otp);

                verificationCodeRepository.save(verificationCode);

                log.info("Login OTP saved successfully for email : {}", req.getEmail());

                // SEND EMAIL

                String subject = "Mana Bazaar Seller Login OTP";

                String text = "Your Seller Login OTP is : "
                                + otp;

                emailService.sendVerificationOtpEmail(
                                req.getEmail(),
                                otp,
                                subject,
                                text);

                log.info("Login OTP email sent successfully to : {}", req.getEmail());

                return ResponseEntity.ok("Seller Login OTP Sent");
        }

        // VERIFY SELLER OTP
        @Operation(summary = "Verify Seller Email", description = "Verify seller account using OTP")

        @PostMapping("/verify/{otp}")
        public ResponseEntity<Seller> verifySellerEmail(
                        @PathVariable String otp)
                        throws SellerException {

                log.info("Verifying seller email with OTP");

                Seller seller = sellerService.verifyEmailByOtp(otp);

                log.info("Seller email verified successfully for seller id : {}", seller.getId());

                return ResponseEntity.ok(seller);
        }

        // LOGIN SELLER
        @Operation(summary = "Seller Login", description = "Authenticate seller and generate JWT")

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> loginSeller(
                        @RequestBody LoginRequest req)
                        throws OtpException {

                log.info("Seller login request received for email : {}", req.getEmail());

                AuthResponse response = authService.signing(req);

                log.info("Seller login successful for email : {}", req.getEmail());

                return ResponseEntity.ok(response);
        }

        // GET SELLER BY ID
        @Operation(summary = "Get Seller By ID", description = "Fetch seller details by ID")

        @GetMapping("/{id}")
        public ResponseEntity<Seller> getSellerById(
                        @PathVariable Long id)
                        throws SellerException {

                log.info("Fetching seller details for id : {}", id);

                Seller seller = sellerService.getSellerById(id);

                log.info("Seller fetched successfully for id : {}", id);

                return new ResponseEntity<>(seller, HttpStatus.OK);
        }

        // GET SELLER PROFILE
        @Operation(summary = "Get Seller Profile", description = "Fetch seller profile using JWT")

        @GetMapping("/profile")
        public ResponseEntity<Seller> getSellerByJwt(
                        @RequestHeader("Authorization") String jwt)
                        throws SellerException {

                log.info(
                                "Fetching seller profile using JWT token");

                Seller seller = sellerService.getSellerProfile(jwt);

                log.info("Seller profile fetched successfully for seller id : {}", seller.getId());

                return new ResponseEntity<>(seller, HttpStatus.OK);
        }

        // GET SELLER REPORT
        @GetMapping("/report")
        public ResponseEntity<SellerReport> getSellerReport(
                        @RequestHeader("Authorization") String jwt)
                        throws SellerException {

                String token = jwt.replace("Bearer ", "");

                String email = jwtProvider.getEmailFromJwtToken(token);

                Seller seller = sellerService.getSellerByEmail(email);

                SellerReport report = sellerReportService.getSellerReport(seller);

                return ResponseEntity.ok(report);
        }

        // GET ALL SELLERS
        @Operation(summary = "Get All Sellers", description = "Admin view of all sellers")

        @GetMapping("")
        public ResponseEntity<List<Seller>> getAllSellers(
                        @RequestParam(required = false) AccountStatus accountStatus) {

                log.info("Fetching all sellers with accountStatus : {}", accountStatus);

                List<Seller> sellers = sellerService.getAllSellers(accountStatus);

                log.info("Total sellers fetched : {}", sellers.size());

                return ResponseEntity.ok(sellers);
        }

        // UPDATE SELLER
        @Operation(summary = "Update Seller", description = "Update seller profile")

        @PatchMapping("")
        public ResponseEntity<Seller> updateSeller(
                        @RequestHeader("Authorization") String jwt,

                        @RequestBody Seller seller)
                        throws SellerException {

                log.info("Updating seller profile");

                Seller profile = sellerService.getSellerProfile(jwt);

                Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);

                log.info("Seller updated successfully with id : {}", updatedSeller.getId());

                return ResponseEntity.ok(updatedSeller);
        }

        // DELETE SELLER
        @Operation(summary = "Delete Seller", description = "Delete seller account")
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteSeller(
                        @PathVariable Long id)
                        throws SellerException {

                log.info("Deleting seller with id : {}", id);

                sellerService.deleteSeller(id);

                log.info("Seller deleted successfully with id : {}", id);

                return ResponseEntity.noContent().build();
        }
}