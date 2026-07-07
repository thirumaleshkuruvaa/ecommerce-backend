package com.ecommerce.backend.controller;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.AddressService;
import com.ecommerce.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/addresses")
@Slf4j
@Tag(name = "6. Address Management", description = "Manage Customer Addresses")
public class AddressController {

        private final AddressService addressService;
        private final UserService userService;

        public AddressController(AddressService addressService,
                        UserService userService) {
                this.addressService = addressService;
                this.userService = userService;
        }

        // ADD ADDRESS
        @Operation(summary = "Add Address", description = "Add new address for logged-in user")
        @PostMapping
        public ResponseEntity<Address> addAddress(
                        @RequestHeader("Authorization") String jwt,
                        @RequestBody Address address) {

                log.info("Add Address API Called");

                User user = userService.findUserByJwtToken(jwt);

                Address savedAddress = addressService.addAddress(user, address);

                log.info("Address added successfully for User ID : {}", user.getId());

                return ResponseEntity.ok(savedAddress);
        }

        // GET ADDRESSES
        @Operation(summary = "Get Addresses", description = "Fetch all addresses of logged-in user")
        @GetMapping
        public ResponseEntity<Set<Address>> getAddresses(
                        @RequestHeader("Authorization") String jwt) {

                log.info("Get Addresses API Called");

                User user = userService.findUserByJwtToken(jwt);

                Set<Address> addresses = addressService.getUserAddresses(user);

                log.info("Fetched {} addresses for User ID : {}", addresses.size(), user.getId());

                return ResponseEntity.ok(addresses);
        }

        // UPDATE ADDRESS
        @Operation(summary = "Update Address", description = "Update address by addressId")
        @PutMapping("/{addressId}")
        public ResponseEntity<Address> updateAddress(
                        @RequestHeader("Authorization") String jwt,
                        @PathVariable Long addressId,
                        @RequestBody Address address) {

                log.info("Update Address API Called for Address ID : {}", addressId);

                User user = userService.findUserByJwtToken(jwt);

                Address updatedAddress = addressService.updateAddress(user, addressId, address);

                log.info("Address updated successfully. Address ID : {}", addressId);

                return ResponseEntity.ok(updatedAddress);
        }

        // DELETE ADDRESS
        @Operation(summary = "Delete Address", description = "Delete address by addressId")
        @DeleteMapping("/{addressId}")
        public ResponseEntity<String> deleteAddress(
                        @RequestHeader("Authorization") String jwt,
                        @PathVariable Long addressId) {

                log.info("Delete Address API Called for Address ID : {}", addressId);

                User user = userService.findUserByJwtToken(jwt);

                addressService.deleteAddress(user, addressId);

                log.info("Address deleted successfully. Address ID : {}", addressId);

                return ResponseEntity.ok("Address Deleted Successfully");
        }
}