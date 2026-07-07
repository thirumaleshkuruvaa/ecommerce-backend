package com.ecommerce.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.backend.model.SavedCard;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.SavedCardRepository;
import com.ecommerce.backend.request.SaveCardRequest;
import com.ecommerce.backend.service.Impl.SavedCardServiceImpl;

@ExtendWith(MockitoExtension.class)
class SavedCardServiceImplTest {

    @Mock
    private SavedCardRepository savedCardRepository;

    @InjectMocks
    private SavedCardServiceImpl savedCardService;

    private User user;
    private SavedCard savedCard;
    private SaveCardRequest request;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);

        savedCard = new SavedCard();
        savedCard.setId(10L);
        savedCard.setCardHolderName("Thirumalesh");
        savedCard.setCardNumber("**** **** **** 1234");
        savedCard.setExpiryMonth("12");
        savedCard.setExpiryYear("2030");
        savedCard.setCardType("VISA");
        savedCard.setUser(user);

        request = new SaveCardRequest();
        request.setCardHolderName("Thirumalesh");
        request.setCardNumber("1234567812341234");
        request.setExpiryMonth("12");
        request.setExpiryYear("2030");
        request.setCardType("VISA");
    }

    // -------------------------------------------------------
    // GET SAVED CARDS
    // -------------------------------------------------------

    @Test
    void testGetSavedCards() {

        List<SavedCard> cards = Arrays.asList(savedCard);

        when(savedCardRepository.findByUser(user))
                .thenReturn(cards);

        List<SavedCard> result = savedCardService.getSavedCards(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(savedCard, result.get(0));

        verify(savedCardRepository).findByUser(user);
    }

    // -------------------------------------------------------
    // SAVE CARD SUCCESS
    // -------------------------------------------------------

    @Test
    void testSaveCard() {

        when(savedCardRepository.save(any(SavedCard.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SavedCard result = savedCardService.saveCard(user, request);

        assertNotNull(result);
        assertEquals("Thirumalesh", result.getCardHolderName());
        assertEquals("**** **** **** 1234", result.getCardNumber());
        assertEquals("12", result.getExpiryMonth());
        assertEquals("2030", result.getExpiryYear());
        assertEquals("VISA", result.getCardType());
        assertEquals(user, result.getUser());

        verify(savedCardRepository).save(any(SavedCard.class));
    }

    // -------------------------------------------------------
    // SAVE CARD WITH SHORT NUMBER
    // -------------------------------------------------------

    @Test
    void testSaveCard_WithShortCardNumber() {

        request.setCardNumber("123");

        when(savedCardRepository.save(any(SavedCard.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SavedCard result = savedCardService.saveCard(user, request);

        assertEquals("123", result.getCardNumber());

        verify(savedCardRepository).save(any(SavedCard.class));
    }

    // -------------------------------------------------------
    // SAVE CARD WITH NULL NUMBER
    // -------------------------------------------------------

    @Test
    void testSaveCard_WithNullCardNumber() {

        request.setCardNumber(null);

        when(savedCardRepository.save(any(SavedCard.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SavedCard result = savedCardService.saveCard(user, request);

        assertNull(result.getCardNumber());

        verify(savedCardRepository).save(any(SavedCard.class));
    }

    // -------------------------------------------------------
    // DELETE CARD SUCCESS
    // -------------------------------------------------------

    @Test
    void testDeleteCard_Success() {

        when(savedCardRepository.findById(10L))
                .thenReturn(Optional.of(savedCard));

        assertDoesNotThrow(() -> savedCardService.deleteCard(user, 10L));

        verify(savedCardRepository).findById(10L);
        verify(savedCardRepository).delete(savedCard);
    }

    // -------------------------------------------------------
    // DELETE CARD NOT FOUND
    // -------------------------------------------------------

    @Test
    void testDeleteCard_CardNotFound() {

        when(savedCardRepository.findById(10L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> savedCardService.deleteCard(user, 10L));

        assertEquals("Saved card not found", exception.getMessage());

        verify(savedCardRepository).findById(10L);
        verify(savedCardRepository, never()).delete(any());
    }

    // -------------------------------------------------------
    // DELETE CARD UNAUTHORIZED
    // -------------------------------------------------------

    @Test
    void testDeleteCard_UnauthorizedUser() {

        User anotherUser = new User();
        anotherUser.setId(2L);

        savedCard.setUser(anotherUser);

        when(savedCardRepository.findById(10L))
                .thenReturn(Optional.of(savedCard));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> savedCardService.deleteCard(user, 10L));

        assertEquals(
                "You are not allowed to delete this card",
                exception.getMessage());

        verify(savedCardRepository).findById(10L);
        verify(savedCardRepository, never()).delete(any());
    }
}