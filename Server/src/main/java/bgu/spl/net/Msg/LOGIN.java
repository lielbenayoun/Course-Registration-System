package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class LOGIN implements IncomingMessages {
    private final String userName;
    private final String password;

    public LOGIN(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName(){ return userName; }
    public String getPassword(){ return password; }


    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return messageProtocol.visit(this);
    }
}
