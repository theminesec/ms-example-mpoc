package com.theminesec.payment;

import lombok.Getter;
import lombok.ToString;

/**
 * @author eric.song
 * @since 2023/7/26 13:05
 */
@Getter
@ToString
public class PaymentDataDto {

    private String errCode;

    private String errMsg;

    private String mchOrderNo;

    private int orderState;

    private String payOrderId;

    private String transactionId;

    private String payData;

    private String acquirerMid;

    private String acquirerTid;

    private String acquirerAuthCode;

    private String acquirerRefNo;

    private String acquirerTraceNo;

    private String acquirerTxnTime;

    private String acquirerBatchNo;

    private String acquirerInvoiceNo;
}
