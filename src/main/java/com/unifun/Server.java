package com.unifun;

import org.apache.log4j.Logger;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.*;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.*;
import org.jsmpp.util.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Server implements Runnable, ServerMessageReceiverListener {

    public static void main(String[] args) {
        Server server = new Server(8081);
        server.run();
    }

    private Logger logger = Logger.getLogger(Server.class);
    private int port;
    private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
    private SMPPServerSession serverSession;
    private SMPPServerSessionListener sessionListener;
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();

    public Server(int port) {

        this.port = port;
    }

    public void run() {
        try {
            sessionListener = new SMPPServerSessionListener(port);

            while (true) {
                serverSession = sessionListener.accept();
                serverSession.setMessageReceiverListener(this);
                BindRequest request = serverSession.waitForBind(5000);
                if ("test".equals(request.getSystemId()) &&
                        "test".equals(request.getPassword())) {
                    request.accept("sys");

                } else if (!"test".equals(request.getPassword())) {

                    request.reject(SMPPConstant.STAT_ESME_RINVPASWD);
                } else if (!"test".equals(request.getSystemId())) {
                    request.reject(SMPPConstant.STAT_ESME_RINVSYSID);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            serverSession.unbindAndClose();
            try {
                sessionListener.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (PDUStringException e) {
            e.printStackTrace();
        }
    }


        public MessageId onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession smppServerSession) throws ProcessRequestException {
            MessageId messageId = messageIDGenerator.newMessageId();
            OptionalParameter.Message_payload payload = (OptionalParameter.Message_payload) submitSm.getOptionalParameter(OptionalParameter.Tag.MESSAGE_PAYLOAD);
        String valueAsString = payload.getValueAsString();

        logger.info("id  = " + messageId + " Length = " +valueAsString.length()  +" MSG = " + valueAsString );

        return messageId;
    }

    public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }
}


