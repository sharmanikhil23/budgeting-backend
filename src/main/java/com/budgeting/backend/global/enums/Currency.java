package com.budgeting.backend.global.enums;

import lombok.Getter;

@Getter
public enum Currency {

    // -------- Major Global --------
    USD("USD", "United States Dollar", "$"),
    EUR("EUR", "Euro", "€"),
    GBP("GBP", "British Pound Sterling", "£"),
    JPY("JPY", "Japanese Yen", "¥"),
    CNY("CNY", "Chinese Yuan", "¥"),
    CHF("CHF", "Swiss Franc", "CHF"),
    CAD("CAD", "Canadian Dollar", "$"),
    AUD("AUD", "Australian Dollar", "$"),
    NZD("NZD", "New Zealand Dollar", "$"),

    // -------- Asia --------
    INR("INR", "Indian Rupee", "₹"),
    PKR("PKR", "Pakistani Rupee", "₨"),
    BDT("BDT", "Bangladeshi Taka", "৳"),
    LKR("LKR", "Sri Lankan Rupee", "Rs"),
    NPR("NPR", "Nepalese Rupee", "₨"),
    IDR("IDR", "Indonesian Rupiah", "Rp"),
    MYR("MYR", "Malaysian Ringgit", "RM"),
    SGD("SGD", "Singapore Dollar", "$"),
    THB("THB", "Thai Baht", "฿"),
    PHP("PHP", "Philippine Peso", "₱"),
    KRW("KRW", "South Korean Won", "₩"),
    VND("VND", "Vietnamese Dong", "₫"),
    HKD("HKD", "Hong Kong Dollar", "$"),
    TWD("TWD", "New Taiwan Dollar", "NT$"),
    KZT("KZT", "Kazakhstani Tenge", "₸"),
    UZS("UZS", "Uzbekistani Som", "soʻm"),
    ILS("ILS", "Israeli New Shekel", "₪"),

    // -------- Middle East --------
    AED("AED", "UAE Dirham", "د.إ"),
    SAR("SAR", "Saudi Riyal", "﷼"),
    QAR("QAR", "Qatari Riyal", "﷼"),
    OMR("OMR", "Omani Rial", "﷼"),
    KWD("KWD", "Kuwaiti Dinar", "د.ك"),
    BHD("BHD", "Bahraini Dinar", ".د.ب"),
    JOD("JOD", "Jordanian Dinar", "د.ا"),
    TRY("TRY", "Turkish Lira", "₺"),
    IRR("IRR", "Iranian Rial", "﷼"),
    IQD("IQD", "Iraqi Dinar", "د.ع"),

    // -------- Europe --------
    NOK("NOK", "Norwegian Krone", "kr"),
    SEK("SEK", "Swedish Krona", "kr"),
    DKK("DKK", "Danish Krone", "kr"),
    PLN("PLN", "Polish Zloty", "zł"),
    CZK("CZK", "Czech Koruna", "Kč"),
    HUF("HUF", "Hungarian Forint", "Ft"),
    RON("RON", "Romanian Leu", "lei"),
    BGN("BGN", "Bulgarian Lev", "лв"),
    HRK("HRK", "Croatian Kuna", "kn"),
    ISK("ISK", "Icelandic Krona", "kr"),
    UAH("UAH", "Ukrainian Hryvnia", "₴"),
    RUB("RUB", "Russian Ruble", "₽"),
    RSD("RSD", "Serbian Dinar", "дин"),
    GEL("GEL", "Georgian Lari", "₾"),

    // -------- Africa --------
    ZAR("ZAR", "South African Rand", "R"),
    NGN("NGN", "Nigerian Naira", "₦"),
    GHS("GHS", "Ghanaian Cedi", "₵"),
    KES("KES", "Kenyan Shilling", "Sh"),
    TZS("TZS", "Tanzanian Shilling", "Sh"),
    UGX("UGX", "Ugandan Shilling", "Sh"),
    EGP("EGP", "Egyptian Pound", "£"),
    MAD("MAD", "Moroccan Dirham", "د.م."),
    DZD("DZD", "Algerian Dinar", "د.ج"),
    TND("TND", "Tunisian Dinar", "د.ت"),
    ETB("ETB", "Ethiopian Birr", "Br"),
    XOF("XOF", "West African CFA Franc", "CFA"),
    XAF("XAF", "Central African CFA Franc", "CFA"),

    // -------- Americas --------
    MXN("MXN", "Mexican Peso", "$"),
    BRL("BRL", "Brazilian Real", "R$"),
    ARS("ARS", "Argentine Peso", "$"),
    CLP("CLP", "Chilean Peso", "$"),
    COP("COP", "Colombian Peso", "$"),
    PEN("PEN", "Peruvian Sol", "S/"),
    BOB("BOB", "Bolivian Boliviano", "Bs"),
    PYG("PYG", "Paraguayan Guarani", "₲"),
    UYU("UYU", "Uruguayan Peso", "$"),
    DOP("DOP", "Dominican Peso", "RD$"),
    CRC("CRC", "Costa Rican Colon", "₡"),
    GTQ("GTQ", "Guatemalan Quetzal", "Q"),
    HNL("HNL", "Honduran Lempira", "L"),

    // -------- Oceania --------
    FJD("FJD", "Fijian Dollar", "$"),
    PGK("PGK", "Papua New Guinean Kina", "K"),
    WST("WST", "Samoan Tala", "T"),

    // -------- Others --------
    XCD("XCD", "East Caribbean Dollar", "$"),
    BBD("BBD", "Barbadian Dollar", "$"),
    JMD("JMD", "Jamaican Dollar", "$"),
    BSD("BSD", "Bahamian Dollar", "$");

    private final String code;
    private final String name;
    private final String symbol;

    Currency(String code, String name, String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }

    public static Currency fromCode(String code) {
        for (Currency c : values()) {
            if (c.code.equalsIgnoreCase(code)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unsupported currency: " + code);
    }
}
