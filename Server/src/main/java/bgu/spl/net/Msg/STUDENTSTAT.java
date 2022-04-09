package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class STUDENTSTAT implements IncomingMessages {
    public String studentName;
    public STUDENTSTAT(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentName(){ return studentName; }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return messageProtocol.visit(this);
    }
}
