
package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.model.SavedCard;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.SaveCardRequest;
import com.ecommerce.backend.response.ApiResponse;
import com.ecommerce.backend.service.SavedCardService;
import com.ecommerce.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Tag(name = "2. User Management", description = "User Profile and Account Management APIs")
public class UserController {

    private final UserService userService;
    private final SavedCardService savedCardService;

    public UserController(UserService userService, SavedCardService savedCardService) {
        this.userService = userService;
        this.savedCardService = savedCardService;
    }

    // GET USER PROFILE
    @Operation(summary = "Get User Profile", description = "Fetch authenticated user's profile using JWT token")
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(
            @RequestHeader("Authorization") String jwt) {

        log.info("Get user profile API called");

        User user = userService.findUserByJwtToken(jwt);

        log.info("User profile fetched successfully for User ID : {}", user.getId());

        return ResponseEntity.ok(user);
    }

    // GET SAVED CARDS
    @GetMapping("/saved-cards")
    public ResponseEntity<List<SavedCard>> getSavedCards(
            @RequestHeader("Authorization") String jwt) {

        User user = userService.findUserByJwtToken(jwt);
        List<SavedCard> cards = savedCardService.getSavedCards(user);

        return ResponseEntity.ok(cards);
    }

    // SAVE CARD
    @PostMapping("/saved-cards")
    public ResponseEntity<SavedCard> saveCard(
            @RequestHeader("Authorization") String jwt,
            @RequestBody SaveCardRequest request) {

        User user = userService.findUserByJwtToken(jwt);
        SavedCard savedCard = savedCardService.saveCard(user, request);

        return ResponseEntity.ok(savedCard);
    }

    // DELETE CARD
    @DeleteMapping("/saved-cards/{cardId}")
    public ResponseEntity<ApiResponse> deleteSavedCard(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long cardId) {

        User user = userService.findUserByJwtToken(jwt);
        savedCardService.deleteCard(user, cardId);

        return ResponseEntity.ok(new ApiResponse("Saved card deleted successfully", true));
    }
}