package bgu.spl.net.srv;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;

import java.util.Arrays;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.Msg.*;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {

    private int len = 0;
    private byte[] bytes = new byte[1 << 10];
    private short opcode = -1;
    private String username = null;
    private String password = null;
    private short coursenum = -1;
    HashMap<Short, Class> formatHashMap = new HashMap<>();
    HashMap<Class, Short> ClassToOp = new HashMap<>();
    private int index = 0;


    private void pushByte(byte nextByte) {
        if (len >= bytes.length)
            bytes = Arrays.copyOf(bytes, len * 2);
        bytes[len++] = nextByte;
    }

    public short bytesToShort(byte[] byteArr) {
        return (short) (((byteArr[0] & 0xFF) << 8) | (byteArr[1] & 0xFF));
    }

    public short decodeCourseNum(byte nextbyte) {
        if (index == 1) {
            pushByte(nextbyte);
            byte[] courseByte = new byte[2];
            courseByte[0] = bytes[0];
            courseByte[1] = bytes[1];
            len = 0;
            index = 0;
            return bytesToShort(courseByte);

        } else {
            pushByte(nextbyte);
            index++;
            return -1;
        }

    }

    private String decodeString(byte nextbyte) {
        String toReturn = null;
        if (nextbyte == '\0') {
            toReturn = new String(bytes, 0, len, StandardCharsets.UTF_8);
            len = 0;
        } else
            pushByte(nextbyte);
        return toReturn;
    }

    private boolean decodeMessage(byte nextbyte) {
        switch (opcode) {
            case 1:
            case 2:
            case 3:
                if (username == null)
                    username = decodeString(nextbyte);
                else if (password == null)
                    password = decodeString(nextbyte);
                return username != null && password != null;
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
                if (coursenum == -1)
                    coursenum = decodeCourseNum(nextbyte);
                return coursenum != -1;
            case 8:
                if (username == null)
                    username = decodeString(nextbyte);
                return username != null;
            case 4:
            case 11:
                return true;
        }
        return false;

    }

    public Message Format() {
        try {
            switch (opcode) {
                case 1:
                    return new ADMINREG(username, password);
                case 2:
                    return new STUDENTREG(username, password);
                case 3:
                    return new LOGIN(username, password);
                case 4:
                    return new LOGOUT();
                case 5:
                    return new COURSEREG(coursenum);
                case 6:
                    return new KDAMCHECK(coursenum);
                case 7:
                    return new COURSESTAT(coursenum);
                case 8:
                    return new STUDENTSTAT(username);
                case 9:
                    return new ISREGISTERED(coursenum);
                case 10:
                    return new UNREGISTER(coursenum);
                case 11:
                    return new MYCOURSES();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            len = 0;
            username = null;
            password = null;
            coursenum = -1;
            opcode = -1;
        }
        return null;
    }

    @Override
    public Message decodeNextByte(byte nextbyte) {
        Message message = null;
        if (opcode == -1) {
            pushByte(nextbyte);
            if (len == 2) {
                opcode = bytesToShort(Arrays.copyOfRange(bytes, 0, 2));
                len = 0;
            }
        } else {
            if (decodeMessage(nextbyte))
                message = Format();
        }
        if (opcode == 4 | opcode == 11)
            message = Format();
        return message;
    }

    @Override
    public byte[] encode(Message message) {
        Parser parser = new Parser();
        parser.encode(ClassToOp.get(message.getClass()));
        selector(parser, message);
        return parser.arr();
    }


    private void selector(Parser help, Message msg) {
        if (msg instanceof ADMINREG) {
            ADMINREG admin = (ADMINREG) msg;
            help.encode(admin.getUsername());
            help.encode(admin.getPassword());
        } else if (msg instanceof STUDENTREG) {
            STUDENTREG student = (STUDENTREG) msg;
            help.encode(student.getUserName());
            help.encode(student.getPassword());
        } else if (msg instanceof LOGIN) {
            LOGIN login = (LOGIN) msg;
            help.encode(login.getUserName());
            help.encode(login.getPassword());
        } else if (msg instanceof LOGOUT) ;
        else if (msg instanceof COURSEREG) {
            COURSEREG course = (COURSEREG) msg;
            help.encode(course.getCourseNum());
        } else if (msg instanceof KDAMCHECK) {
            KDAMCHECK kdam = (KDAMCHECK) msg;
            help.encode(kdam.getCourseNum());
        } else if (msg instanceof COURSESTAT) {
            COURSESTAT coursestat = (COURSESTAT) msg;
            help.encode(coursestat.getCourseNum());
        } else if (msg instanceof STUDENTSTAT) {
            STUDENTSTAT studentstat = (STUDENTSTAT) msg;
            help.encode(studentstat.getStudentName());
        } else if (msg instanceof ISREGISTERED) {
            ISREGISTERED isregistered = (ISREGISTERED) msg;
            help.encode(isregistered.getCourseNum());
        } else if (msg instanceof UNREGISTER) {
            UNREGISTER unregister = (UNREGISTER) msg;
            help.encode(unregister.getCourseNum());
        } else if (msg instanceof MYCOURSES) ;
        else if (msg instanceof ACK) {
            ACK ack = (ACK) msg;
            help.encode(ack.getOpCode());
            help.encode(ack.getData());
        } else if (msg instanceof ERR) {
            ERR err = (ERR) msg;
            help.encode(err.getOpCode());
        }
    }
    public MessageEncoderDecoderImpl() {
        formatHashMap.put((short) 1, ADMINREG.class);
        formatHashMap.put((short) 2, STUDENTREG.class);
        formatHashMap.put((short) 3, LOGIN.class);
        formatHashMap.put((short) 4, LOGOUT.class);
        formatHashMap.put((short) 5, COURSEREG.class);
        formatHashMap.put((short) 6, KDAMCHECK.class);
        formatHashMap.put((short) 7, COURSESTAT.class);
        formatHashMap.put((short) 8, STUDENTSTAT.class);
        formatHashMap.put((short) 9, ISREGISTERED.class);
        formatHashMap.put((short) 10, UNREGISTER.class);
        formatHashMap.put((short) 11, MYCOURSES.class);
        formatHashMap.put((short) 12, ACK.class);
        formatHashMap.put((short) 13, ERR.class);
        ClassToOp.put(ADMINREG.class, (short) 1);
        ClassToOp.put(STUDENTREG.class, (short) 2);
        ClassToOp.put(LOGIN.class, (short) 3);
        ClassToOp.put(LOGOUT.class, (short) 4);
        ClassToOp.put(COURSEREG.class, (short) 5);
        ClassToOp.put(KDAMCHECK.class, (short) 6);
        ClassToOp.put(COURSESTAT.class, (short) 7);
        ClassToOp.put(STUDENTSTAT.class, (short) 8);
        ClassToOp.put(ISREGISTERED.class, (short) 9);
        ClassToOp.put(UNREGISTER.class, (short) 10);
        ClassToOp.put(MYCOURSES.class, (short) 11);
        ClassToOp.put(ACK.class, (short) 12);
        ClassToOp.put(ERR.class, (short) 13);
    }
}