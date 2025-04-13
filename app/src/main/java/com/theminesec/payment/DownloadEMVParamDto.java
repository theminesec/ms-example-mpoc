package com.theminesec.payment;

import lombok.Getter;

/**
 * @author eric.song
 * @since 2023/12/8 13:26
 */
@Getter
public class DownloadEMVParamDto {
    String emvAIDParam;

    String signAID;

    String emvCAPKParam;

    String signCAPK;

    String emvTerminalParam;

    String signTerminalParam;
}
