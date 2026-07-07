package com.ecommerce.backend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.ecommerce.backend.model.SavedCard;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.service.SavedCardService;
import com.ecommerce.backend.service.UserService;

class UserControllerTest {

    @Test
    void shouldFetchUserProfileAndCards() {
        UserService userService = mock(UserService.class);
        SavedCardService savedCardService = mock(SavedCardService.class);
        UserController controller = new UserController(userService, savedCardService);

        User user = new User();
        user.setId(8L);
        SavedCard card = new SavedCard();
        card.setId(4L);

        when(userService.findUserByJwtToken("jwt")).thenReturn(user);
        when(savedCardService.getSavedCards(user)).thenReturn(List.of(card));

        ResponseEntity<User> profileResponse = controller.getUserProfileHandler("jwt");
        ResponseEntity<List<SavedCard>> cardsResponse = controller.getSavedCards("jwt");

        assertEquals(200, profileResponse.getStatusCode().value());
        assertEquals(user, profileResponse.getBody());
        assertEquals(1, cardsResponse.getBody().size());
    }
}
