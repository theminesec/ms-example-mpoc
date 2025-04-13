package com.theminesec.payment;

/**
 * @author eric.song
 * @since 2023/7/26 13:21
 */
public interface PaymentMessage {
    String ONLINE_RESP_APPROVED_CODE = "0";
    String ONLINE_RESP_APPROVED_MSG = "Approved";
    String ONLINE_RESP_DECLINED_CODE = "01";
    String ONLINE_RESP_DECLINED_MSG = "Declined";
    String ONLINE_PIN_ENTRY_CODE = "10";
    String ONLINE_PIN_ENTRY_MSG = "PinEntry";
    String ONLINE_PIN_ENTRY_ERROR = "20";
    String EMV_APPROVED_CODE = "0";
    String DEFAULT_TIP_AMOUNT = "0.00";
    String DEFAULT_ACQUIRER_MID = "12321412412";
    String DEFAULT_ACQUIRER_TID = "3fe3e2323g";
    String DEFAULT_REFERENCE_NO = "123456789012";
    String DEFAULT_USER = "Minesec guest01";
    String DEFAULT_BATCH_NO = "000088";
    String DEFAULT_UNKNOWN_PAN = "XXXX-unknown";
    String TITLE_REQUEST = "online_request";
    String TITLE_RECEIVE = "online_receive";
    String TITLE_PROCESSING = "processing";
    String REQUEST_PARTNER_NUM = "mchParNo";
    String REQUEST_SDK_ID = "sdkId";
    String REQUEST_ADMIN_PASSWORD = "adminPwd";
    String REQUEST_MERCHANT_NUM = "mchNo";
    String REQUEST_APPLICATION_ID = "appId";
    String REQUEST_MERCHANT_ORDER_NO = "mchOrderNo";
    String REQUEST_AMOUNT = "amount";
    String REFUND_AMOUNT = "refundAmount";
    String PAYMENT_ORDER_ID = "payOrderId";
    String REFUND_NUMBER = "mchRefundNo";
    String REFUND_REASON = "refundReason";
    String REQUEST_WAY_CODE = "wayCode";
    String REQUEST_CURRENCY_CODE = "currency";
    String REQUEST_CLIENT_IP = "clientIp";
    String REQUEST_SUBJECT = "subject";
    String REQUEST_DESCRIPTION = "description";
    String REQUEST_NOTIFY_URL = "notifyUrl";
    String REQUEST_RETURN_URL = "returnUrl";
    String REQUEST_VERSION = "version";
    String REQUEST_REQUEST_TIME = "reqTime";
    String REQUEST_EXTRA_PARAM = "extParam";
    String REQUEST_SIGNATURE_TYPE = "signType";
    String REQUEST_CHANNEL_EXTRA = "channelExtra";
    String REQUEST_EXPIRED_TIME = "expiredTime";
    String REQUEST_DIVISION_MODE = "divisionMode";
    String SDK_PRIVATE_KEY_NAME = "minesecsk";
    String SIGN_KEY_NAME = "signkey";
    String REQUEST_PAYMENT_METHOD = "paymentMethod";
    String REQUEST_TRANSACTION_ID = "transactionId";
    String REQUEST_PAY_ID = "payId";
    String REQUEST_PAYMENT_DESCRIPTION = "description";
    String REQUEST_ENTRY_MODE = "entryMode";
    String REQUEST_API_VERSION = "apiVersion";
    String REQUEST_SETTLE_BATCH = "batches";
}
