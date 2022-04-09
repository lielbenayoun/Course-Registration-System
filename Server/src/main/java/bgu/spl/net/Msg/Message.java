package bgu.spl.net.Msg;
import bgu.spl.net.srv.MessageProtocol;


public interface Message {
    Message visit(MessageProtocol messageProtocol);
}
