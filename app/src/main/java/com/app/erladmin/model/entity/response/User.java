
package com.app.erladmin.model.entity.response;


import org.parceler.Parcel;

@Parcel
public class User {
    int id,selected_language,user_type_id,company_id,phonecode_id,timezone_id;
    String name,first_name, last_name,email,api_token,image,company_name,phone_number,phonecode_name,date_of_birth;
    boolean is_image_valid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSelected_language() {
        return selected_language;
    }

    public void setSelected_language(int selected_language) {
        this.selected_language = selected_language;
    }

    public int getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(int user_type_id) {
        this.user_type_id = user_type_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getPhonecode_id() {
        return phonecode_id;
    }

    public void setPhonecode_id(int phonecode_id) {
        this.phonecode_id = phonecode_id;
    }

    public String getPhonecode_name() {
        return phonecode_name;
    }

    public void setPhonecode_name(String phonecode_name) {
        this.phonecode_name = phonecode_name;
    }

    public int getTimezone_id() {
        return timezone_id;
    }

    public void setTimezone_id(int timezone_id) {
        this.timezone_id = timezone_id;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public boolean isIs_image_valid() {
        return is_image_valid;
    }

    public void setIs_image_valid(boolean is_image_valid) {
        this.is_image_valid = is_image_valid;
    }
}


