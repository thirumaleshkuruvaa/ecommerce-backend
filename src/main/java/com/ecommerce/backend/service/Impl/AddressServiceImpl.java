
package com.ecommerce.backend.service.Impl;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.AddressNotFoundException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.AddressRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.AddressService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AddressServiceImpl implements AddressService {

        private final AddressRepository addressRepository;

        public AddressServiceImpl(
                        UserRepository userRepository,
                        AddressRepository addressRepository) {

                this.addressRepository = addressRepository;
        }

        // ADD ADDRESS

        @Override
        public Address addAddress(User user, Address address) {

                log.info("[ADD ADDRESS] User ID : {}", user.getId());

                address.setUser(user);

                Address savedAddress = addressRepository.save(address);

                log.info(
                                "[ADD ADDRESS] Address created successfully. Address ID : {}",
                                savedAddress.getId());

                return savedAddress;
        }

        // GET ALL ADDRESSES

        @Override
        public Set<Address> getUserAddresses(User user) {

                log.info(
                                "[GET ADDRESSES] Fetching addresses for User ID : {}",
                                user.getId());

                Set<Address> addresses = user.getAddresses();

                log.info(
                                "[GET ADDRESSES] Total Addresses : {}",
                                addresses.size());

                return addresses;
        }

        // UPDATE ADDRESS

        @Override
        public Address updateAddress(
                        User user,
                        Long addressId,
                        Address reqAddress) {

                log.info(
                                "[UPDATE ADDRESS] User ID : {} Address ID : {}",
                                user.getId(),
                                addressId);

                Address address = addressRepository
                                .findById(addressId)
                                .orElseThrow(() -> {

                                        log.error(
                                                        "[UPDATE ADDRESS] Address not found : {}",
                                                        addressId);

                                        return new AddressNotFoundException(
                                                        "Address not found with id : "
                                                                        + addressId);
                                });

                if (!address.getUser().getId().equals(user.getId())) {

                        log.error(
                                        "[UPDATE ADDRESS] Address {} does not belong to User {}",
                                        addressId,
                                        user.getId());

                        throw new AddressNotFoundException(
                                        "Address does not belong to user");
                }

                address.setName(reqAddress.getName());
                address.setMobileNumber(reqAddress.getMobileNumber());
                address.setLocality(reqAddress.getLocality());
                address.setAddress(reqAddress.getAddress());
                address.setCity(reqAddress.getCity());
                address.setState(reqAddress.getState());
                address.setPinCode(reqAddress.getPinCode());

                Address updatedAddress = addressRepository.save(address);

                log.info(
                                "[UPDATE ADDRESS] Successfully updated Address ID : {}",
                                addressId);

                return updatedAddress;
        }

        // DELETE ADDRESS

        @Override
        public void deleteAddress(
                        User user,
                        Long addressId) {

                log.info(
                                "[DELETE ADDRESS] User ID : {} Address ID : {}",
                                user.getId(),
                                addressId);

                Address address = addressRepository
                                .findById(addressId)
                                .orElseThrow(() -> {

                                        log.error(
                                                        "[DELETE ADDRESS] Address not found : {}",
                                                        addressId);

                                        return new AddressNotFoundException(
                                                        "Address not found with id : "
                                                                        + addressId);
                                });

                if (!address.getUser().getId().equals(user.getId())) {

                        log.error(
                                        "[DELETE ADDRESS] Address {} does not belong to User {}",
                                        addressId,
                                        user.getId());

                        throw new AddressNotFoundException(
                                        "Address does not belong to user");
                }

                addressRepository.delete(address);

                log.info(
                                "[DELETE ADDRESS] Successfully deleted Address ID : {}",
                                addressId);
        }
}