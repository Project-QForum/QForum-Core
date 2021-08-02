package cn.jackuxl.qforum.model;

public class User {
    public String userName;
    public String password;
    public int id;
    public String email;
    public String session;
    public String salt;

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public Integer getId(){
        return id;
    }

    public String getSession(){
        return session;
    }

    public String getSalt(){
        return salt;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setSession(String session){
        this.session = session;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }
}
