package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Msg.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.MessageEncoderDecoderImpl;
import bgu.spl.net.srv.MessageProtocol;
import bgu.spl.net.srv.Server;

import java.util.function.Supplier;

public class TPCMain {
    public static void main(String[] args) throws Exception {

        Supplier<MessagingProtocol<Message>>  protocol = MessageProtocol::new;
        Supplier<MessageEncoderDecoder<Message>>  supplier = MessageEncoderDecoderImpl::new;
        if(args.length < 1) {
            throw new Exception("illegal argument");
        }
        int portNum = 0;
        try {
            portNum = Integer.parseInt(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server.threadPerClient(portNum, protocol, supplier).serve();
    }
}
