package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.exceptions.AddressNotFoundException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.AddressRepository;
import com.ecommerce.backend.repository.UserRepository;
import com.ecommerce.backend.service.Impl.AddressServiceImpl;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        address = new Address();
        address.setId(100L);
        address.setName("Thirumalesh");
        address.setMobileNumber("9876543210");
        address.setLocality("MG Road");
        address.setAddress("House No 10");
        address.setCity("Hyderabad");
        address.setState("Telangana");
        address.setPinCode("500001");
        address.setUser(user);
    }

    @Test
    void testAddAddress() {

        when(addressRepository.save(any(Address.class)))
                .thenReturn(address);

        Address saved = addressService.addAddress(user, address);

        assertNotNull(saved);
        assertEquals(100L, saved.getId());
        assertEquals(user, saved.getUser());

        verify(addressRepository, times(1))
                .save(address);
    }

    @Test
    void testGetUserAddresses() {

        Set<Address> addresses = new HashSet<>();
        addresses.add(address);

        user.setAddresses(addresses);

        Set<Address> result = addressService.getUserAddresses(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(address));
    }

    // -------------------------------------------------------
    // UPDATE ADDRESS - SUCCESS
    // -------------------------------------------------------

    @Test
    void testUpdateAddress_Success() {

        Address existingAddress = new Address();
        existingAddress.setId(100L);
        existingAddress.setUser(user);

        Address request = new Address();
        request.setName("Updated Name");
        request.setMobileNumber("9999999999");
        request.setLocality("New Locality");
        request.setAddress("New Address");
        request.setCity("Bangalore");
        request.setState("Karnataka");
        request.setPinCode("560001");

        when(addressRepository.findById(100L))
                .thenReturn(Optional.of(existingAddress));

        when(addressRepository.save(any(Address.class)))
                .thenReturn(existingAddress);

        Address result = addressService.updateAddress(user, 100L, request);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("9999999999", result.getMobileNumber());
        assertEquals("New Locality", result.getLocality());
        assertEquals("New Address", result.getAddress());
        assertEquals("Bangalore", result.getCity());
        assertEquals("Karnataka", result.getState());
        assertEquals("560001", result.getPinCode());

        verify(addressRepository).findById(100L);
        verify(addressRepository).save(existingAddress);
    }

    // -------------------------------------------------------
    // UPDATE ADDRESS - NOT FOUND
    // -------------------------------------------------------

    @Test
    void testUpdateAddress_AddressNotFound() {

        when(addressRepository.findById(100L))
                .thenReturn(Optional.empty());

        Address request = new Address();

        assertThrows(
                AddressNotFoundException.class,
                () -> addressService.updateAddress(user, 100L, request));

        verify(addressRepository).findById(100L);
        verify(addressRepository, never()).save(any());
    }

    // -------------------------------------------------------
    // UPDATE ADDRESS - WRONG USER
    // -------------------------------------------------------

    @Test
    void testUpdateAddress_AddressBelongsToAnotherUser() {

        User anotherUser = new User();
        anotherUser.setId(2L);

        Address existingAddress = new Address();
        existingAddress.setId(100L);
        existingAddress.setUser(anotherUser);

        when(addressRepository.findById(100L))
                .thenReturn(Optional.of(existingAddress));

        Address request = new Address();

        assertThrows(
                AddressNotFoundException.class,
                () -> addressService.updateAddress(user, 100L, request));

        verify(addressRepository).findById(100L);
        verify(addressRepository, never()).save(any());
    }

    // -------------------------------------------------------
    // DELETE ADDRESS - SUCCESS
    // -------------------------------------------------------

    @Test
    void testDeleteAddress_Success() {

        Address existingAddress = new Address();
        existingAddress.setId(100L);
        existingAddress.setUser(user);

        when(addressRepository.findById(100L))
                .thenReturn(Optional.of(existingAddress));

        assertDoesNotThrow(() -> addressService.deleteAddress(user, 100L));

        verify(addressRepository).findById(100L);
        verify(addressRepository).delete(existingAddress);
    }

    // -------------------------------------------------------
    // DELETE ADDRESS - NOT FOUND
    // -------------------------------------------------------

    @Test
    void testDeleteAddress_AddressNotFound() {

        when(addressRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                AddressNotFoundException.class,
                () -> addressService.deleteAddress(user, 100L));

        verify(addressRepository).findById(100L);
        verify(addressRepository, never()).delete(any());
    }

    // -------------------------------------------------------
    // DELETE ADDRESS - WRONG USER
    // -------------------------------------------------------

    @Test
    void testDeleteAddress_AddressBelongsToAnotherUser() {

        User anotherUser = new User();
        anotherUser.setId(2L);

        Address existingAddress = new Address();
        existingAddress.setId(100L);
        existingAddress.setUser(anotherUser);

        when(addressRepository.findById(100L))
                .thenReturn(Optional.of(existingAddress));

        assertThrows(
                AddressNotFoundException.class,
                () -> addressService.deleteAddress(user, 100L));

        verify(addressRepository).findById(100L);
        verify(addressRepository, never()).delete(any());

    }
}