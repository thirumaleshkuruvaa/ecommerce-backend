package com.ecommerce.backend.service.Impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.request.ChatIntent;
import com.ecommerce.backend.request.ParsedMessage;
import com.ecommerce.backend.response.ChatResponse;
import com.ecommerce.backend.service.ChatService;
import com.ecommerce.backend.util.MessageParser;

@Service
public class ChatServiceImpl implements ChatService {

        private final ProductRepository productRepository;
        private final MessageParser messageParser;

        public ChatServiceImpl(
                        ProductRepository productRepository,
                        MessageParser messageParser) {

                this.productRepository = productRepository;
                this.messageParser = messageParser;
        }

        @Override
        public ChatResponse processMessage(String message) {

                if (message == null || message.isBlank()) {
                        return new ChatResponse(
                                        "Please type something. I am here to help 😊");
                }

                ParsedMessage parsed = messageParser.parse(message);

                if (parsed.getIntent() == ChatIntent.HOW_ARE_YOU) {

                        return new ChatResponse("""
                                        😊 I'm doing great!

                                        Thanks for asking.

                                        I'm here to help you find products, compare prices,
                                        recommend items and answer shopping questions.
                                        """);
                }
                if (parsed.getIntent() == ChatIntent.WHO_ARE_YOU) {

                        return new ChatResponse("""
                                        🤖 I'm Glomo Assistant.

                                        I'm your shopping assistant for the Glomo Store.

                                        I can help you search products, recommend products,
                                        compare prices and much more.
                                        """);
                }

                if (parsed.getIntent() == ChatIntent.WHAT_CAN_YOU_DO) {

                        return new ChatResponse("""
                                        🤖 Here are a few things I can do:

                                        ✔ Search products
                                        ✔ Recommend products
                                        ✔ Filter by price
                                        ✔ Search by discount
                                        ✔ Search by brand

                                        Examples:

                                        • Shoes
                                        • Best mobiles
                                        • Shoes under 2000
                                        • Nike shoes
                                        • Shirts 50 discount
                                        """);
                }
                if (parsed.getIntent() == ChatIntent.JOKE) {

                        return new ChatResponse("""
                                        😂 Here's one!

                                        Why don't programmers like shopping?

                                        Because they keep looking for the best "cache" deals. 😄
                                        """);
                }
                if (parsed.getIntent() == ChatIntent.GOOD_MORNING) {

                        return new ChatResponse("""
                                        ☀️ Good Morning!

                                        Hope you have an amazing day.

                                        Ready to shop? 😊
                                        """);
                }

                if (parsed.getIntent() == ChatIntent.GOOD_AFTERNOON) {

                        return new ChatResponse("""
                                        🌤 Good Afternoon!

                                        Hope you're having a wonderful day.

                                        Let me know what you're looking for.
                                        """);
                }

                if (parsed.getIntent() == ChatIntent.GOOD_EVENING) {

                        return new ChatResponse("""
                                        🌇 Good Evening!

                                        Welcome back.

                                        What would you like to shop today?
                                        """);
                }

                if (parsed.getIntent() == ChatIntent.GOOD_NIGHT) {

                        return new ChatResponse("""
                                        🌙 Good Night!

                                        Have a peaceful sleep.

                                        See you again soon! 😊
                                        """);
                }
                // --------------------------------------------------
                // Greeting
                // --------------------------------------------------

                if (parsed.getIntent() == ChatIntent.GREETING) {

                        return new ChatResponse(
                                        """
                                                        👋 Hello!

                                                        Welcome to Glomo.

                                                        I can help you find products.

                                                        Try asking:

                                                        • Show me shoes
                                                        • Best mobiles
                                                        • Sarees under 2000
                                                        • Shoes above 3000
                                                        • Help
                                                        """);
                }

                // --------------------------------------------------
                // Thanks
                // --------------------------------------------------

                if (parsed.getIntent() == ChatIntent.THANKS) {

                        return new ChatResponse(
                                        """
                                                        😊 You're welcome!

                                                        Happy shopping.

                                                        Let me know if you need anything else.
                                                        """);
                }

                // --------------------------------------------------
                // Goodbye
                // --------------------------------------------------

                if (parsed.getIntent() == ChatIntent.GOODBYE) {

                        return new ChatResponse(
                                        """
                                                        👋 Bye!

                                                        Thanks for visiting Glomo.

                                                        Have a wonderful day!
                                                        """);
                }

                // --------------------------------------------------
                // Help
                // --------------------------------------------------

                if (parsed.getIntent() == ChatIntent.HELP) {

                        return new ChatResponse(
                                        """
                                                        🤖 I can understand these commands:

                                                        Product Search
                                                        • Shoes
                                                        • Mobiles
                                                        • Sarees

                                                        Price Filter
                                                        • Shoes under 2000
                                                        • Mobiles above 30000

                                                        Recommendation
                                                        • Best shoes
                                                        • Recommend laptops

                                                        Discount
                                                        • Shoes 50 discount

                                                        Brand
                                                        • Nike shoes
                                                        • Samsung mobiles
                                                        """);
                }

                // --------------------------------------------------
                // Product Search
                // --------------------------------------------------

                Set<Product> results = new LinkedHashSet<>();

                if (parsed.getMinDiscount() != null) {

                        results.addAll(
                                        productRepository.searchProductByDiscount(
                                                        parsed.getKeyword(),
                                                        parsed.getMinDiscount()));

                } else if (parsed.isRecommendation()) {

                        results.addAll(
                                        productRepository.searchRecommendedProducts(
                                                        parsed.getKeyword()));

                } else {

                        results.addAll(
                                        productRepository.searchProductWithPrice(
                                                        parsed.getKeyword(),
                                                        parsed.getMinPrice(),
                                                        parsed.getMaxPrice()));

                }
                // if (results.isEmpty()) {

                // return new ChatResponse(
                // "😔 Sorry, I couldn't find any matching products.");
                // }
                if (results.isEmpty()) {

                        StringBuilder reply = new StringBuilder();

                        reply.append("😔 Sorry!\n\n");

                        reply.append("I couldn't find any products");

                        if (parsed.getKeyword() != null && !parsed.getKeyword().isBlank()) {

                                reply.append(" for \"")
                                                .append(parsed.getKeyword())
                                                .append("\"");

                        }

                        reply.append(".\n\n");

                        reply.append("You can try:\n\n");

                        reply.append("• Different spelling\n");

                        reply.append("• Another brand\n");

                        reply.append("• Remove price filter\n");

                        reply.append("• Search by category\n");

                        reply.append("\nExamples:\n");

                        reply.append("• Shoes\n");

                        reply.append("• Nike shoes\n");

                        reply.append("• Shirts under 1000\n");

                        reply.append("• Best mobiles\n");

                        return new ChatResponse(reply.toString());
                }
                List<Product> products = new ArrayList<>(results);

                StringBuilder reply = new StringBuilder();

                // --------------------------------------------------
                // Heading
                // --------------------------------------------------

                if (parsed.isRecommendation()) {

                        reply.append("⭐ Recommended Products ⭐\n\n");

                } else {

                        reply.append("Found ")
                                        .append(products.size())
                                        .append(" Product(s)\n\n");
                }

                // --------------------------------------------------
                // Products
                // --------------------------------------------------

                for (Product p : products) {

                        reply.append("🛍 ")
                                        .append(p.getTitle())
                                        .append("\n");

                        reply.append("Brand : ")
                                        .append(p.getBrand())
                                        .append("\n");

                        reply.append("Price : ₹")
                                        .append(p.getSellingPrice())
                                        .append("\n");

                        reply.append("Discount : ")
                                        .append(p.getDiscountPercent())
                                        .append("%\n");

                        reply.append("---------------------------------\n");
                }

                reply.append("\nNeed anything else? 😊");

                return new ChatResponse(reply.toString());

        }
}