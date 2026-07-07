package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.model.SavedCard;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.request.SaveCardRequest;

public interface SavedCardService {

    List<SavedCard> getSavedCards(User user);

    SavedCard saveCard(User user, SaveCardRequest request);

    void deleteCard(User user, Long cardId);
}