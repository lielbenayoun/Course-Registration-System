package bgu.spl.net.Database;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Course {
    private final int courseNum;
    private final String courseName;
    private final List<Integer> kdamCourses;
    private final List<String> currentStudents;
    private final int maxCourses;


    public Course(int courseNum, String courseName, List<Integer> kdamCourses, int maxCourses) {
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.kdamCourses = kdamCourses;
        this.maxCourses = maxCourses;
        currentStudents = Collections.synchronizedList(new LinkedList<>());
    }

    public int getCourseNum() {
        return courseNum;
    }

    public int getMaxCourses() {
        return maxCourses;
    }

    public List<Integer> getKdamCourses() {
        return kdamCourses;
    }
    public String getCourseName() {
        return courseName;
    }

    public boolean registerToCourse(DatabaseUser dbu) {
        synchronized (this){
            if (currentStudents.size() < maxCourses && !currentStudents.contains(dbu.getUserName()))
                if (dbu.getCourses().containsAll(kdamCourses)) {
                    currentStudents.add(dbu.getUserName());
                    dbu.addCourse(courseNum);
                    return true;
            }
        }
        return false;
    }
    public boolean unRegisterFromCourse(DatabaseUser dbu) {
        synchronized (this) {
            if(currentStudents.contains(dbu.getUserName())) {
                currentStudents.remove(dbu.getUserName());
                dbu.removeCourse(courseNum);
                return true;
            }
        }
        return false;
    }

    public int countStudents() {
        return currentStudents.size();
    }

    public List<String> getStudents() {
        return currentStudents;
    }
}
