package bgu.spl.net.Database;

import java.util.LinkedList;
import java.util.List;

public class DatabaseUser {
    private final String userName;
    private final String passWord;
    private final boolean isAdmin;
    private final List<Integer> courses;
    private boolean loggedIn;

    public DatabaseUser(String username, String password, boolean isAdmin) {
        this.userName = username;
        this.passWord = password;
        this.isAdmin = isAdmin;
        courses = new LinkedList<>();
        loggedIn = false;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getPassWord() {
        return passWord;
    }


    public void addCourse(int courseNum) {
        if(courses.contains(courseNum))
            return;
        courses.add(courseNum);
    }

    public void removeCourse(int courseNum) {
        for (int i = 0; i < courses.size(); i++) {
            if(courses.get(i) == courseNum) {
                courses.remove(i);
            }
        }
    }

    public List<Integer> getCourses() {
        return courses;
    }
}
