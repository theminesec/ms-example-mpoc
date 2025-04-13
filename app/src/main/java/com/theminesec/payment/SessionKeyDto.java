package com.theminesec.payment;

import lombok.Getter;

/**
 * @author eric.song
 * @since 2023/7/27 15:28
 */
@Getter
public class SessionKeyDto {

    private String hmacKey;

    private String pinWorkingKey;

    private String cardWorkingKey;

    private String IKID;
}
