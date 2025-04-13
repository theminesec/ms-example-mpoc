package com.theminesec.payment;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * @author eric.song
 * @since 2023/7/26 13:04
 */
public interface PaymentService {

    @POST("mpoc/sale")
    Single<TransactionResponseDto> pay(@Body TransactionRequestDto requestDto);

    @POST("mpoc/keyload")
    Observable<DownloadSessionKeyResponseDto> downloadKeys(@HeaderMap Map<String, String> headers, @Body DownloadKeyRequest request);

    @POST("mpoc/emv")
    Observable<DownloadEMVParamResponseDto> downloadEMVParameters(@Body DownloadEMVParamRequest request);

    @AllArgsConstructor
    class DownloadKeyRequest {
        private final String sdkId;
        private final String serverUrl;
        private final String customerId;
        private final String licenseId;
        private final boolean update;
    }

    @AllArgsConstructor
    class DownloadEMVParamRequest {
        String sdkId;

        String customerId;

        String action;
    }

}
