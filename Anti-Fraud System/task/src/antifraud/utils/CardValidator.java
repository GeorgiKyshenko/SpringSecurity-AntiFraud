package antifraud.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CardValidator {

    public static void validateCardNumber(String number) {
        int cardDigits = number.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = cardDigits - 1; i >= 0; i--) {

            int d = number.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        if (!(nSum % 10 == 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
