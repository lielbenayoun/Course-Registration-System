package bgu.spl.net.Msg;

import bgu.spl.net.srv.MessageProtocol;

public class ADMINREG implements IncomingMessages {
    private final String username;
    private final String password;

    public ADMINREG(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername(){ return username; }
    public String getPassword(){ return password; }

    @Override
    public Message visit(MessageProtocol messageProtocol) {
        return messageProtocol.visit(this);
    }
}
