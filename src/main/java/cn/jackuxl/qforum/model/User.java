package cn.jackuxl.qforum.model;

public class User {
    public String userName;
    public String password;
    public int id;
    public String email;

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

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
