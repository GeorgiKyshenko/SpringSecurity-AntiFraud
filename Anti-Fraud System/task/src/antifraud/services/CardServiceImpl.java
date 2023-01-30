package antifraud.services;

import antifraud.models.database.Card;
import antifraud.models.DTO.CardResponse;
import antifraud.repositories.CardRepository;
import antifraud.utils.CardValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper mapper;

    @Override
    public CardResponse saveCard(Card stolenCard) {
        Optional<Card> cardByNumber = cardRepository.findByNumber(stolenCard.getNumber());
        if (cardByNumber.isEmpty()) {
            cardRepository.save(stolenCard);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return new CardResponse(stolenCard.getId(), stolenCard.getNumber());
    }

    @Override
    @Transactional
    public void deleteCardFromDB(String number) {
        CardValidator.validateCardNumber(number);
        Optional<Card> cardByNumber = cardRepository.findByNumber(number);
        if (cardByNumber.isPresent()) {
            cardRepository.deleteById(cardByNumber.get().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<CardResponse> findAllCards() {
        return cardRepository.findAll(Sort.sort(Card.class).by(Card::getId).ascending())
                .stream()
                .map(card -> mapper.map(card, CardResponse.class)).toList();
    }


//    private void validateCardNumber(String number) {
//        int cardDigits = number.length();
//
//        int nSum = 0;
//        boolean isSecond = false;
//        for (int i = cardDigits - 1; i >= 0; i--) {
//
//            int d = number.charAt(i) - '0';
//
//            if (isSecond)
//                d = d * 2;
//
//            nSum += d / 10;
//            nSum += d % 10;
//
//            isSecond = !isSecond;
//        }
//        if (!(nSum % 10 == 0)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//        }
//    }
}
