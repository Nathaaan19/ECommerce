package org.example;

import org.example.paymentMethods.BoletoPayment;
import org.example.paymentMethods.CreditCardPayment;
import org.example.paymentMethods.PaymentMethod;
import org.example.paymentMethods.PaymentType;
import org.example.paymentMethods.PixPayment;

public class PaymentMethodFactory {
    public static PaymentMethod create(PaymentType type) {
        switch (type) {
            case PIX:
                return new PixPayment();
            case CARTAO:
                return new CreditCardPayment();
            case BOLETO:
                return new BoletoPayment();
            default:
                return new PixPayment();
        }
    }
}