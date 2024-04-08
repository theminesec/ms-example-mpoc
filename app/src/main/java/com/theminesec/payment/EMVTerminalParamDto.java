package com.theminesec.payment;

import lombok.Getter;
import lombok.Setter;

/**
 * @author eric.song
 * @since 2023/12/8 15:13
 */
@Getter
@Setter
public class EMVTerminalParamDto {
    private String terminalType;

    private String terminalCapability;

    private String terminalAddiCapability;

    private String terminalCountryCode;

    private String txnCurrencyCode;

    private String amexReaderCapability;

    private String amexExReaderCapability;

    private String amexFloorLimit;

    private String emvFlags;

    private String ifdSerialNumber;
}
