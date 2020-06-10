package jmsmessaging;

public class Constants {
    public static final String ING = "amount <= 100000 && time <= 10";
    public static final String ABN_AMRO = "amount >= 200000 && amount <= 300000 && time <= 20";
    public static final String RABO_BANK = "amount <= 250000 && time <= 15";

    public static final String TCP_CONNECTION = "tcp://localhost:61616";
    public static final String HTTP_ARCHIVE = "http://localhost:8080/archive/rest/accepted";
    public static final String HTTP_CREDIT_HISTORY = "http://localhost:8080/credit/rest/history/";

    public static final String REQUEST_CLIENT_QUEUE = "requestLoanClientQueue";
    public static final String REPLY_CLIENT_QUEUE = "replyLoanClientQueue_";
    public static final String REQUEST_BANK_QUEUE_IGN = "ingRequestQueue";
    public static final String REQUEST_BANK_QUEUE_ABN_AMRO = "abnRequestQueue";
    public static final String REQUEST_BANK_QUEUE_RABO_BANK = "raboRequestQueue";
    public static final String REPLY_BANK_QUEUE = "bankInterestReplyQueue";
}
