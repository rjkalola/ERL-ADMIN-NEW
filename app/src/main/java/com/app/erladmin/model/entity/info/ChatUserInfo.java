
package com.app.erladmin.model.entity.info;

public class ChatUserInfo {
    private int id, to_user, un_read_msg_count;
    private String message, image, name, created_date, eclapse_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public int getUn_read_msg_count() {
        return un_read_msg_count;
    }

    public void setUn_read_msg_count(int un_read_msg_count) {
        this.un_read_msg_count = un_read_msg_count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getEclapse_date() {
        return eclapse_date;
    }

    public void setEclapse_date(String eclapse_date) {
        this.eclapse_date = eclapse_date;
    }
}



