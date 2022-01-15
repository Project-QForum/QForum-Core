package cn.jackuxl.qforum.entity;

public class User {
    private String userName;
    private String password;
    private int id;
    private String email;
    private String sessionId;
    private String salt;
    private String lastLoginIp;
    private Boolean admin;
    private String official;
    private String introduction = "这个人很懒，什么都没留下。";
    private String avatarUrl;
    

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getOfficial() {
        return official;
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

    public String getIntroduction(){
        return introduction;
    }

    public String getLastLoginIp(){
        return this.lastLoginIp;
    }

    public String getAvatarUrl(){
        return this.avatarUrl;
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

    public void setOfficial(String official) {
        this.official = official;
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

    public void setIntroduction(String introduction){this.introduction = introduction;}

    public void setAvatarUrl(String avatarUrl){this.avatarUrl = avatarUrl;}

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }


}
