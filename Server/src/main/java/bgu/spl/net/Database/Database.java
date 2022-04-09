package bgu.spl.net.Database;

import java.io.File;
import java.util.*;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */

public class Database {

    private final List<Course> courses;
    private final List<DatabaseUser> users;

    private static class SingletonHolder {
        private static final Database instance = new Database();
    }

    private Database() {
        courses = Collections.synchronizedList(new LinkedList<>());
        users = Collections.synchronizedList(new LinkedList<>());
        initialize();
    }
    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return SingletonHolder.instance;
    }
    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    void initialize() {
        try {
            File file = new File("Courses.txt");
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                courses.add(course(data));
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Course course(String courseLine) throws Exception {
        List<Integer> kdamcourses = new LinkedList<>();
        for (String str : courseLine.split("\\|")[2].split(",")) {
            if (str.startsWith("["))
                str = str.substring(1);
            if (str.endsWith("]"))
                str = str.substring(0,str.length() - 1);
            if(str.length() > 0)
                kdamcourses.add(Integer.parseInt(str));
        }
        if (Integer.parseInt(courseLine.split("\\|")[0]) < 0 || courseLine.split("\\|")[1].length() == 0 || Integer.parseInt(courseLine.split("\\|")[3]) < 5) {
            throw new Exception("illegal parameters");
        }
        return new Course(Integer.parseInt(courseLine.split("\\|")[0]), courseLine.split("\\|")[1], kdamcourses, Integer.parseInt(courseLine.split("\\|")[3]));
    }
    public boolean registerAdmin(String userName, String password) {
        synchronized (users) {
            if (getuser(userName) != null) return false;
            else {
                users.add(new DatabaseUser(userName, password, true));
                return true;
            }
        }
    }

    public boolean registerStudent(String userName, String password) {
        synchronized (users) {
            if (getuser(userName) != null)
                return false;
            else {
                users.add(new DatabaseUser(userName, password, false));
                return true;
            }
        }
    }

    public boolean Login(String userName, String password) {
        DatabaseUser dbu = getuser(userName);
        if (dbu == null || dbu.isLoggedIn() || dbu.getPassWord().compareTo(password) != 0)
            return false;
        dbu.setLoggedIn(true);
        return true;
    }


    public void logout(String userName) {
        Objects.requireNonNull(getuser(userName)).setLoggedIn(false);
    }


    public boolean registertocourse(int courseNum, String userName) {
        DatabaseUser dbu = getuser(userName);
        assert dbu != null;
        if(!dbu.isAdmin()) {
            Course course = getcoursebynum(courseNum);
            return course != null && course.registerToCourse(dbu);
        }
        return false;
    }


    public List<Integer> getkdam(int courseNum) {
        if(!courses.contains(getcoursebynum(courseNum)))
            return null;

        else{
            return Objects.requireNonNull(getcoursebynum(courseNum)).getKdamCourses();
        }
    }


    public String getCourseStats(int courseNum) {
        if (!courses.contains(getcoursebynum(courseNum))) return null;


        assert getcoursebynum(courseNum) != null;
        List<String> students = Objects.requireNonNull(getcoursebynum(courseNum)).getStudents();
        Collections.sort(students);
        int courseCount = Objects.requireNonNull(getcoursebynum(courseNum)).countStudents();
        String ans = "Course: (" + Objects.requireNonNull(getcoursebynum(courseNum)).getCourseNum() + ") " + Objects.requireNonNull(getcoursebynum(courseNum)).getCourseName() + "\n";
        ans += "Seats Available: " + (Objects.requireNonNull(getcoursebynum(courseNum)).getMaxCourses() - courseCount) + "/" + Objects.requireNonNull(getcoursebynum(courseNum)).getMaxCourses() + "\n";
        ans += "Students Registered: " + students.toString().replaceAll(" ", "");
        return ans;
    }

    public boolean isRegistered(int courseNum, String userName) {
        return getuser(userName) != null && !getuser(userName).isAdmin() && getuser(userName).getCourses().contains(courseNum);
    }

    public boolean unregister(int courseNum, String userName) {
        assert getuser(userName) != null;
        return getuser(userName).getCourses().contains(courseNum) && getcoursebynum(courseNum) != null && Objects.requireNonNull(getcoursebynum(courseNum)).unRegisterFromCourse(getuser(userName));
    }

    public List<Integer> getcourses(String userName){
        assert  getuser(userName) != null;
        if( getuser(userName).isAdmin())
            return null;
        return  getuser(userName).getCourses();
    }

    private Course getcoursebynum(int courseNum) {
        for(Course course : courses) {
            if(course.getCourseNum() == courseNum) {
                return course;
            }
        }
        return null;
    }

    public DatabaseUser getuser(String userName) {
        for (DatabaseUser user : users) {
            if (user.getUserName().compareTo(userName) == 0) {
                return user;
            }
        }
        return null;
    }

    public boolean isAdmin(String userName) {
        return !Objects.requireNonNull(getuser(userName)).isAdmin();
    }
}
