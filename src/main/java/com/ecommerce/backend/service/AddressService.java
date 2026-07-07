package com.ecommerce.backend.service;

import java.util.Set;

import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.User;

public interface AddressService {

    Address addAddress(User user, Address address);

    Set<Address> getUserAddresses(User user);

    Address updateAddress(
            User user,
            Long addressId,
            Address address);

    void deleteAddress(
            User user,
            Long addressId);
}