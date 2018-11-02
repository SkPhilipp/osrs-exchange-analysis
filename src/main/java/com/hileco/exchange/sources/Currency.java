package com.hileco.exchange.sources;

import java.math.BigDecimal;

public class Currency {

    /**
     * Performs conversions as such;
     * 20.0k --> 20000.0
     * 20.0m --> 20000000.0
     * 20.0b --> 20000000000.0
     */
    public static BigDecimal from(String input) {
        try {
            var normalized = input.replaceAll("\\s", "")
                    .replaceAll("\\+", "")
                    .replaceAll("\\,", "");
            String number;
            var multiplier = new BigDecimal(1);
            if (normalized.endsWith("k") || normalized.endsWith("K")) {
                number = normalized.substring(0, normalized.length() - 1);
                multiplier = new BigDecimal(1000);
            } else if (normalized.endsWith("m") || normalized.endsWith("M")) {
                number = normalized.substring(0, normalized.length() - 1);
                multiplier = new BigDecimal(1000000);
            } else if (normalized.endsWith("b") || normalized.endsWith("B")) {
                number = normalized.substring(0, normalized.length() - 1);
                multiplier = new BigDecimal(1000000000);
            } else {
                number = normalized;
            }
            return new BigDecimal(number).multiply(multiplier);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
