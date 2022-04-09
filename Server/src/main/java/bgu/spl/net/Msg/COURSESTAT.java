package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class COURSESTAT implements IncomingMessages {
    private final short courseNum;
    public COURSESTAT(short courseNum) {
        this.courseNum = courseNum;
    }

    public short getCourseNum(){ return courseNum; }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return messageProtocol.visit(this);
    }
}
