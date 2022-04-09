package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class ISREGISTERED implements IncomingMessages {
    private final short courseNum;
    public ISREGISTERED(short courseNum) {
        super();
        this.courseNum = courseNum;
    }

    public short getCourseNum(){ return courseNum; }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return messageProtocol.visit(this);
    }
}
