package antifraud.utils;

import antifraud.errors.IncorrectIpInput;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Validator {

    public static void validateIpFormat(String ip) throws IncorrectIpInput {
        String pattern = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
        if (!ip.matches(pattern)) {
            throw new IncorrectIpInput("Not a valid IP");
        }
    }

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
