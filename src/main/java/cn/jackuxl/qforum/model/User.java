package cn.jackuxl.qforum.model;

public class User {
    public String userName;
    public String password;
    public int id;
    public String email;
    public String sessionId;
    public String salt;
    public String lastLoginIp;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Integer getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSalt() {
        return salt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginIp(){
        return this.lastLoginIp;
    }
}
