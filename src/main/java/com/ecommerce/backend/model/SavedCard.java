package com.ecommerce.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saved_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardHolderName;

    private String cardNumber; // store masked or tokenized in real projects
    private String expiryMonth;
    private String expiryYear;

    private String cardType; // VISA / MASTERCARD / RUPAY etc

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}