package com.theminesec.payment;

import lombok.Setter;

/**
 * @author eric.song
 * @since 2023/7/26 13:04
 */
@Setter
public class ResultDto {
    private int code;
    private String msg;

    public boolean approved() {
        return (0 == this.code);
    }

    public String toErrorMessage() {
        return this.code + ":" + this.msg;
    }

    public String getRespCode() {
        return "" + this.code;
    }

    public String getRespText() {
        return this.msg;
    }
}

