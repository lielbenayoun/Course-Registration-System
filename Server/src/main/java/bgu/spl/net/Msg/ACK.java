package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class ACK implements OutcomingMessages {
    private final String data;
    private final short opCode;

    public ACK(short msgOpCode, String data) {
        this.data = data;
        this.opCode = msgOpCode;
    }

    public String getData(){return this.data; }
    public short getOpCode(){ return this.opCode; }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return null;
    }
}
