package com.unifun;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.*;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import java.io.IOException;
import java.util.Date;


public class Client   {
   static  DeliverSm deliverSm;
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    static String message = getMsg();
    private static byte[] empty_arr = new byte[0];
    public static void main(String[] args) {
        SMPPSession session = new SMPPSession();

        try {

            OptionalParameter payload  =
                    new OptionalParameter.Message_payload(message.getBytes());



            String systemId = session.connectAndBind("localhost", 8081, new BindParameter(BindType.BIND_TX,
                    "test", "test", null, TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN,
                    null));



                session.submitShortMessage("CMT",
                        TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, "1",
                        TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, "2",
                        new ESMClass(), (byte)0, (byte)1,  TIME_FORMATTER.format(new Date()), null,
                        new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte)0,
                        empty_arr,payload);



            session.unbindAndClose();


        } catch (IOException e) {
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (NegativeResponseException e) {
            e.printStackTrace();
        } catch (ResponseTimeoutException e) {
            e.printStackTrace();
        } catch (PDUException e) {
            e.printStackTrace();
        }
    }
    private static String getMsg() {
        StringBuilder msg = new StringBuilder();
        for (int countSymbols = 0; countSymbols < 999; countSymbols++) {

            msg.append("a");
        }
        return msg.toString() + "J";
}

}