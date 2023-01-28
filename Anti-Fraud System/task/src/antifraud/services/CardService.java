package antifraud.services;

import antifraud.database.Card;
import antifraud.models.DTO.CardResponse;

import java.util.List;

public interface CardService {
    CardResponse saveCard(Card stolenCard);

    void deleteCardFromDB(String number);

    List<CardResponse> findAllCards();

}
