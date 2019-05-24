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


public class Client  {

    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    public static void main(String[] args) {
        SMPPSession session = new SMPPSession();

        try {


            String systemId = session.connectAndBind("localhost", 8081, new BindParameter(BindType.BIND_TRX,
                    "test", "test", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN,
                    null));

            String msg = " >> My test message ";
            for (int i = 0; i < 100; i++) {
                session.submitShortMessage("CMT",
                        TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, "1",
                        TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, "2",
                        new ESMClass(), (byte)0, (byte)1,  TIME_FORMATTER.format(new Date()), null,
                        new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte)0,
                        msg.getBytes());
                Thread.sleep(2000);
            }

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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}