package com.hileco.exchange.sources;

import java.math.BigDecimal;

public class Currency {

    /**
     * Performs conversions as such;
     * 20.0k --> 20000.0
     * 20.0m --> 20000000.0
     * 20.0b --> 20000000000.0
     */
    public static BigDecimal from(String officialNumber) {
        var numberComponent = officialNumber;
        var multiplier = new BigDecimal(1);
        if (officialNumber.endsWith("k") || officialNumber.endsWith("K")) {
            numberComponent = officialNumber.substring(0, officialNumber.length() - 1);
            multiplier = new BigDecimal(1000);
        }
        if (officialNumber.endsWith("m") || officialNumber.endsWith("M")) {
            numberComponent = officialNumber.substring(0, officialNumber.length() - 1);
            multiplier = new BigDecimal(1000000);
        }
        if (officialNumber.endsWith("b") || officialNumber.endsWith("B")) {
            numberComponent = officialNumber.substring(0, officialNumber.length() - 1);
            multiplier = new BigDecimal(1000000000);
        }
        var number = new BigDecimal(numberComponent);
        return number.multiply(multiplier);
    }
}
