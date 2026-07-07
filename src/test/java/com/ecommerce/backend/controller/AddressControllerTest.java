package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.AddressService;
import com.ecommerce.backend.service.UserService;

class AddressControllerTest {

    @Test
    void shouldAddAndFetchAddresses() {
        AddressService addressService = mock(AddressService.class);
        UserService userService = mock(UserService.class);
        AddressController controller = new AddressController(addressService, userService);

        User user = new User();
        user.setId(1L);
        Address address = new Address();
        address.setId(10L);

        when(userService.findUserByJwtToken("jwt")).thenReturn(user);
        when(addressService.addAddress(user, address)).thenReturn(address);
        when(addressService.getUserAddresses(user)).thenReturn(Set.of(address));

        ResponseEntity<Address> addResponse = controller.addAddress("jwt", address);
        ResponseEntity<Set<Address>> getResponse = controller.getAddresses("jwt");

        assertNotNull(addResponse.getBody());
        assertNotNull(getResponse.getBody());
    }
}
