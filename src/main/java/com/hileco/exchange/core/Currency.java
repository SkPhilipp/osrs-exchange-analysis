package com.hileco.exchange.core;

public class Currency {

    /**
     * Performs conversions as such;
     * 20.0k --> 20000.0
     * 20.0m --> 20000000.0
     * 20.0b --> 20000000000.0
     */
    public static Double from(String input) {
        try {
            var normalized = input.replaceAll("\\s", "")
                    .replaceAll("\\+", "")
                    .replaceAll("\\,", "");
            String number;
            var multiplier = 1L;
            if (normalized.endsWith("k") || normalized.endsWith("K")) {
                number = normalized.substring(0, normalized.length() - 1);
                multiplier = 1000L;
            } else if (normalized.endsWith("m") || normalized.endsWith("M")) {
                number = normalized.substring(0, normalized.length() - 1);
                multiplier = 1000000L;
            } else if (normalized.endsWith("b") || normalized.endsWith("B")) {
                number = normalized.substring(0, normalized.length() - 1);
                multiplier = 1000000000L;
            } else {
                number = normalized;
            }
            return Double.parseDouble(number) * multiplier;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
