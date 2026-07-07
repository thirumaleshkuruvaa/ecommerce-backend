package com.ecommerce.backend.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ecommerce.backend.request.ChatIntent;
import com.ecommerce.backend.request.ParsedMessage;

@Component
public class MessageParser {

    private static final Set<String> HOW_ARE_YOU = Set.of(
            "how are you",
            "how are u");

    private static final Set<String> WHO_ARE_YOU = Set.of(
            "who are you",
            "who r you");

    private static final Set<String> WHAT_CAN_YOU_DO = Set.of(
            "what can you do",
            "what do you do");

    private static final Set<String> JOKE = Set.of(
            "joke",
            "tell me a joke");

    private static final Set<String> GOOD_MORNING = Set.of(
            "good morning");

    private static final Set<String> GOOD_AFTERNOON = Set.of(
            "good afternoon");

    private static final Set<String> GOOD_EVENING = Set.of(
            "good evening");

    private static final Set<String> GOOD_NIGHT = Set.of(
            "good night");
    // -----------------------------
    // Greetings
    // -----------------------------
    private static final Set<String> GREETINGS = Set.of(
            "hi",
            "hello",
            "hey",
            "hii",
            "hlo");

    // -----------------------------
    // Thanks
    // -----------------------------
    private static final Set<String> THANKS = Set.of(
            "thanks",
            "thank",
            "thankyou",
            "thx");

    // -----------------------------
    // Goodbye
    // -----------------------------
    private static final Set<String> GOODBYE = Set.of(
            "bye",
            "goodbye",
            "seeyou");

    // -----------------------------
    // Help
    // -----------------------------
    private static final Set<String> HELP = Set.of(
            "help",
            "commands");

    // -----------------------------
    // Recommendation words
    // -----------------------------
    private static final Set<String> RECOMMEND_WORDS = Set.of(
            "best",
            "top",
            "recommend",
            "recommended",
            "suggest",
            "suggestion",
            "good");

    // -----------------------------
    // Ignore words
    // -----------------------------
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(

            "show",
            "me",
            "find",
            "search",
            "want",
            "need",
            "give",
            "display",
            "please",
            "products",
            "product",
            "can",
            "you",
            "i",
            "a",
            "an",
            "the",
            "for",
            "with",
            "of",
            "looking"

    ));

    public ParsedMessage parse(String message) {

        ParsedMessage parsed = new ParsedMessage();

        if (message == null || message.isBlank()) {
            return parsed;
        }

        message = message.toLowerCase();
        message = message.replaceAll("[^a-z0-9 ]", "");
        message = message.trim();

        // -----------------------------
        // Direct intents
        // -----------------------------
        if (HOW_ARE_YOU.contains(message)) {
            parsed.setIntent(ChatIntent.HOW_ARE_YOU);
            return parsed;
        }

        if (WHO_ARE_YOU.contains(message)) {
            parsed.setIntent(ChatIntent.WHO_ARE_YOU);
            return parsed;
        }

        if (WHAT_CAN_YOU_DO.contains(message)) {
            parsed.setIntent(ChatIntent.WHAT_CAN_YOU_DO);
            return parsed;
        }

        if (JOKE.contains(message)) {
            parsed.setIntent(ChatIntent.JOKE);
            return parsed;
        }

        if (GOOD_MORNING.contains(message)) {
            parsed.setIntent(ChatIntent.GOOD_MORNING);
            return parsed;
        }

        if (GOOD_AFTERNOON.contains(message)) {
            parsed.setIntent(ChatIntent.GOOD_AFTERNOON);
            return parsed;
        }

        if (GOOD_EVENING.contains(message)) {
            parsed.setIntent(ChatIntent.GOOD_EVENING);
            return parsed;
        }

        if (GOOD_NIGHT.contains(message)) {
            parsed.setIntent(ChatIntent.GOOD_NIGHT);
            return parsed;
        }
        if (GREETINGS.contains(message)) {
            parsed.setIntent(ChatIntent.GREETING);
            return parsed;
        }

        if (THANKS.contains(message)) {
            parsed.setIntent(ChatIntent.THANKS);
            return parsed;
        }

        if (GOODBYE.contains(message)) {
            parsed.setIntent(ChatIntent.GOODBYE);
            return parsed;
        }

        if (HELP.contains(message)) {
            parsed.setIntent(ChatIntent.HELP);
            return parsed;
        }

        parsed.setIntent(ChatIntent.PRODUCT_SEARCH);

        StringBuilder keyword = new StringBuilder();

        String[] words = message.split("\\s+");

        for (int i = 0; i < words.length; i++) {

            String word = words[i];

            // -------------------------
            // Recommendation
            // -------------------------

            if (RECOMMEND_WORDS.contains(word)) {
                parsed.setRecommendation(true);
                continue;
            }

            // -------------------------
            // Under Price
            // -------------------------

            if ((word.equals("under") || word.equals("below"))
                    && i + 1 < words.length) {

                try {
                    parsed.setMaxPrice(Integer.parseInt(words[i + 1]));
                } catch (Exception e) {
                }

                continue;
            }

            // -------------------------
            // Above Price
            // -------------------------

            if ((word.equals("above") || word.equals("over"))
                    && i + 1 < words.length) {

                try {
                    parsed.setMinPrice(Integer.parseInt(words[i + 1]));
                } catch (Exception e) {
                }

                continue;
            }

            // -------------------------
            // Discount
            // -------------------------

            if (word.matches("\\d+")
                    && i + 1 < words.length
                    && words[i + 1].contains("discount")) {

                parsed.setMinDiscount(Integer.parseInt(word));
                continue;
            }

            // -------------------------
            // Ignore stop words
            // -------------------------

            if (STOP_WORDS.contains(word))
                continue;

            // Ignore numbers

            if (word.matches("\\d+"))
                continue;

            keyword.append(word).append(" ");
        }

        parsed.setKeyword(keyword.toString().trim());

        return parsed;
    }
}