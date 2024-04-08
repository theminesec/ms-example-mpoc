package com.theminesec.payment;

import lombok.Getter;
import lombok.Setter;

/**
 * @author eric.song
 * @since 2023/7/26 13:23
 */
@Getter
@Setter
public class EmvDataDto {
    /**
     * Standard Tag value
     */
    private String tag;
    /**
     * the length of value(Hex length)
     */
    private String length;
    /**
     * the value of EMV tag data
     */
    private String value;
}

