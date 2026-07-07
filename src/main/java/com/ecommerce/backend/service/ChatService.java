
package com.ecommerce.backend.service;

import com.ecommerce.backend.response.ChatResponse;

public interface ChatService {

    ChatResponse processMessage(String message);

}