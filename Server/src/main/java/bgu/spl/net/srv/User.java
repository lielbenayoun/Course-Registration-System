package bgu.spl.net.srv;

public class User {
    private String userName;
    private boolean isLoggedIn;
    private boolean shouldTerminate;

    public User() {
        userName = null;
        isLoggedIn = false;
        shouldTerminate = false;
    }

    public void setShouldTerminate(boolean shouldTerminate) {
        this.shouldTerminate = shouldTerminate;
    }

    public boolean getShouldTerminate() {
        return shouldTerminate;
    }

    public boolean getLoggedIn() {
        return isLoggedIn;
    }

    public String getUserName() {
        return userName;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
