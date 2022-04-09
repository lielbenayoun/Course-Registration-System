package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class KDAMCHECK implements IncomingMessages {
    private final short courseNum;
    public KDAMCHECK(short courseNum) {
        this.courseNum = courseNum;
    }

    public short getCourseNum(){ return courseNum; }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return messageProtocol.visit(this);
    }
}
