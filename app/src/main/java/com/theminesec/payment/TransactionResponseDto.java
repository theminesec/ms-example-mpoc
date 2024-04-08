package com.theminesec.payment;

import lombok.Getter;

/**
 * @author eric.song
 * @since 2023/7/26 13:05
 */
@Getter
public class TransactionResponseDto extends ResultDto {

    private String sign;

    private PaymentDataDto data;
}
