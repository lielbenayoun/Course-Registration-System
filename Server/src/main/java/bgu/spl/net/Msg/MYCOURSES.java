package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class MYCOURSES implements IncomingMessages {
    public MYCOURSES() {}


    @Override
    public Message visit(MessageProtocol messageProtocol) {
       return messageProtocol.visit();
    }
}
