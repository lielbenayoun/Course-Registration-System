package bgu.spl.net.srv;

import bgu.spl.net.Database.Database;
import bgu.spl.net.Msg.*;
import bgu.spl.net.api.MessagingProtocol;

import java.util.List;

public class MessageProtocol implements MessagingProtocol<Message> {

    private final User user;
    private final Database database;

    public MessageProtocol() {
        user = new User();
        database = Database.getInstance();
    }

    @Override
    public Message process(Message msg) {
        return msg.visit(this);
    }

    @Override
    public boolean shouldTerminate() {
        return user.getShouldTerminate();
    }

    public Message visit(ADMINREG adminreg) {
        if(!user.getLoggedIn()) {
            if (database.registerAdmin(adminreg.getUsername(), adminreg.getPassword())) {
                return new ACK((short) 1, "");
            }
        }
        return new ERR((short)1);
    }

    public Message visit(STUDENTREG studentreg){
        if(!user.getLoggedIn()) {
            if (database.registerStudent(studentreg.getUserName(), studentreg.getPassword())) {
                return new ACK((short) 2, "");
            }
        }
        return new ERR((short)2);
    }

    public Message visit(LOGIN login) {
        if (user.getLoggedIn()) {
            return new ERR((short)3);
        }
        if (database.Login(login.getUserName(), login.getPassword())) {
            user.setLoggedIn(true);
            user.setUserName(login.getUserName());
            user.setShouldTerminate(false);
            return new ACK((short)3,"");
        }
        return new ERR((short)3);
    }

    public Message visit(LOGOUT logout) {
        if (!user.getLoggedIn()) {
            return new ERR((short)4);
        }
        database.logout(user.getUserName());
        user.setLoggedIn(false);
        user.setUserName(null);
        user.setShouldTerminate(true);
        return new ACK((short)4, "");
    }

    public Message visit(COURSEREG coursereg) {
        if (!user.getLoggedIn()) {
            return new ERR((short)5);
        }
        if (database.registertocourse(coursereg.getCourseNum(), user.getUserName())){
            return new ACK((short)5, "");
        }
        return new ERR((short)5);
    }

    public Message visit(KDAMCHECK kdamcheck) {
        if (!user.getLoggedIn()) {
            return new ERR((short)6);
        }
        List<Integer> kdams = database.getkdam(kdamcheck.getCourseNum());
        if(kdams != null) {
            String s = kdams.toString();
            s = s.replaceAll(" ", "");
            return new ACK((short)6, s);
        }
        return new ERR((short)6);
    }

    public Message visit(COURSESTAT coursestat) {
        if (!user.getLoggedIn() || database.isAdmin(user.getUserName())){
            return new ERR((short)7);
        }
       String msg = database.getCourseStats(coursestat.getCourseNum());
        if (msg == null) {
            return new ERR((short)7);
        }
        return new ACK((short)7, msg);
    }

    public Message visit(STUDENTSTAT studentstat){
        if(!user.getLoggedIn() || database.isAdmin(user.getUserName())) {
            return new ERR((short)8);
        }
        String userMsg = "Student: " + studentstat.getStudentName() + "\n";
        userMsg = userMsg + "Courses: " + database.getcourses(studentstat.getStudentName()).toString().replaceAll(" ", "");
        return new ACK((short)8, userMsg);
    }

    public Message visit(ISREGISTERED isregistered) {
        if (!user.getLoggedIn()) {
            return new ERR((short)9);
        }
        if(database.isRegistered(isregistered.getCourseNum(), user.getUserName())){
            return new ACK((short)9, "REGISTERED");
        }
        return new ACK((short)9, "NOT REGISTERED");
    }

    public Message visit(UNREGISTER unregister) {
        if (!user.getLoggedIn()) {
            return new ERR((short)10);
        }
        if(database.unregister(unregister.getCourseNum(), user.getUserName())){
            return new ACK((short)10, "");
        }
        return new ERR((short)10);
    }

    public Message visit() {
        if (!user.getLoggedIn()) {
            return new ERR((short)11);
        }
        List<Integer> courses = database.getcourses(user.getUserName());
        if (courses == null) {
            return new ERR((short)11);
        }
        String cor= courses.toString();
        cor = cor.replaceAll(" ", "");
        return new ACK((short)11, cor);
    }

}
