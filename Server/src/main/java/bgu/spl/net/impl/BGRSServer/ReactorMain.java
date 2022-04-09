package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Msg.Message;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.MessageEncoderDecoderImpl;
import bgu.spl.net.srv.MessageProtocol;
import bgu.spl.net.srv.Reactor;

import java.util.function.Supplier;

public class ReactorMain {
    public static void main(String[] args) throws Exception {
        Supplier<MessagingProtocol<Message>> protocol = MessageProtocol::new;
        Supplier<MessageEncoderDecoder<Message>> supplier = MessageEncoderDecoderImpl::new;
        if (args.length < 2) {
            throw new Exception("illegal argument");
        }
        int portNum = 0;
        int numOfThreads = 0;
        try {
            portNum = Integer.parseInt(args[0]);
            numOfThreads = Integer.parseInt(args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Reactor<Message> reactor = new Reactor<>(numOfThreads, portNum, protocol, supplier);
        reactor.serve();
    }
}
