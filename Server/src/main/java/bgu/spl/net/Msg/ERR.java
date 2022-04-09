package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;


public class ERR implements OutcomingMessages {
    private final short opcode;
    public ERR(short opcode) {
        this.opcode = opcode;
    }

    public short getOpCode(){ return opcode; }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return null;
    }
}
