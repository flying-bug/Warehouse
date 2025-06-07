package model;

public class Accounts {
    private int account_id;
    private String account_name;
    private String password;
    private String email;
    private String phone;
    private String full_name;
    private String profile_image;
    private int role_id;

    public Accounts() {

    }

    public Accounts(int account_id, String account_name, String password, String email, String phone, String full_name, String profile_image, int role_id) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.full_name = full_name;
        this.profile_image = profile_image;
        this.role_id = role_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
