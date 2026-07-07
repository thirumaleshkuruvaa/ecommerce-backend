package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.model.SavedCard;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.repository.SavedCardRepository;
import com.ecommerce.backend.request.SaveCardRequest;
import com.ecommerce.backend.service.SavedCardService;

@Service
public class SavedCardServiceImpl implements SavedCardService {

    private final SavedCardRepository savedCardRepository;

    public SavedCardServiceImpl(SavedCardRepository savedCardRepository) {
        this.savedCardRepository = savedCardRepository;
    }

    @Override
    public List<SavedCard> getSavedCards(User user) {
        return savedCardRepository.findByUser(user);
    }

    @Override
    public SavedCard saveCard(User user, SaveCardRequest request) {

        SavedCard card = new SavedCard();
        card.setCardHolderName(request.getCardHolderName());

        // simple masking logic
        String cardNumber = request.getCardNumber();
        if (cardNumber != null && cardNumber.length() >= 4) {
            String last4 = cardNumber.substring(cardNumber.length() - 4);
            card.setCardNumber("**** **** **** " + last4);
        } else {
            card.setCardNumber(cardNumber);
        }

        card.setExpiryMonth(request.getExpiryMonth());
        card.setExpiryYear(request.getExpiryYear());
        card.setCardType(request.getCardType());
        card.setUser(user);

        return savedCardRepository.save(card);
    }

    @Override
    public void deleteCard(User user, Long cardId) {
        SavedCard card = savedCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Saved card not found"));

        if (!card.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this card");
        }

        savedCardRepository.delete(card);
    }
}