package model;

import java.sql.Timestamp;

public class Accounts {
    private int account_id;
    private String account_name;
    private String password;
    private String full_name;
    private String email;
    private String phone;
    private int role_id;
    private String profile_image;
    private int status;
    private Timestamp created_at;

    public Accounts() {
    }

    // Full constructor
    public Accounts(int account_id, String account_name, String password, String full_name, String email,
                    String phone, int role_id, String profile_image, int status, Timestamp created_at) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.password = password;
        this.full_name = full_name;
        this.email = email;
        this.phone = phone;
        this.role_id = role_id;
        this.profile_image = profile_image;
        this.status = status;
        this.created_at = created_at;
    }

    // Getters and Setters

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
