package com.theminesec.payment;

import androidx.annotation.NonNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author eric.song
 * @since 2023/7/26 13:06
 */
@Getter
@Setter
@Builder
public class TransactionRequestDto {

    private String paymentMethod;

    private String entryMode;

    /**
     * Transaction ID for SALE.
     */
    private String transactionId;
    /**
     * One Transaction ID can mapper to multiple payId, for example:refund can be partial and multiple times.
     */
    private String payId;

    private String description;

    private String apiVersion;
    /**
     * merchant Number,M
     */
    private String mchParNo;
    /**
     * application id, M
     */
    private String sdkId;    //TODO
    /**
     * payment order Number, M(Refund)
     */
    private String payOrderId;
    /**
     * M(Refund)
     */
    private String mchRefundNo;
    /**
     * M(Refund)
     */
    private String refundReason;
    /**
     * merchant order Number,M(Payment)
     */
    private String mchOrderNo;
    /**
     * payment method,M
     */
    private String wayCode;
    /**
     * transaction amount, M
     * 123 means 1.23, 100 means 1.00
     */
    @Builder.Default
    private Long amount = null; //TODO
    /**
     * currency code, M
     */
    private String currency; //TODO
    /**
     * Goods subject, M
     */
    private String subject;
    /**
     * Goods descriptions,M
     */
    private String body;
    /**
     * notify url,O
     */
    private String notifyUrl; //TODO
    /**
     * expired time, O
     */
//    private long expiredTime;
    /**
     * channel data, O
     */
    private String channelExtra; //TODO
    /**
     * timestamp with length as 13,M
     */
    private long reqTime; //TODO
    /**
     * API version,fix it as 1.0,M
     */
    private String version;
    /**
     * signature value,M
     */
    private String sign;
    /**
     * SHA-256, M
     */
    private String signType;
    /**
     * VOID or Refund pwd.
     */
    private String adminPwd;
    /**
     * Settle All host request.
     */
    private String batches;

}
