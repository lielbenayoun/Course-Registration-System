package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class LOGOUT implements IncomingMessages {
    public LOGOUT() {
    }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return messageProtocol.visit(this);
    }
}
