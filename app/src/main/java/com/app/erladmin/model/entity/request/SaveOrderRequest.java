package com.app.erladmin.model.entity.request;


import com.app.erladmin.model.entity.info.ServiceItemInfo;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class SaveOrderRequest {
    int pickup_hour_id, deliver_hour_id, address_id, lu_service_hour_type_id, type, promo_amount, wallet_balance,client_id;
    String pickup_date, deliver_date, pickup_hour, deliver_hour, delivery_note, promo_code, address;
    boolean deduct_wallet;
    List<ServiceItemInfo> order = new ArrayList<>();

    public int getPickup_hour_id() {
        return pickup_hour_id;
    }

    public void setPickup_hour_id(int pickup_hour_id) {
        this.pickup_hour_id = pickup_hour_id;
    }

    public int getDeliver_hour_id() {
        return deliver_hour_id;
    }

    public void setDeliver_hour_id(int deliver_hour_id) {
        this.deliver_hour_id = deliver_hour_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public int getLu_service_hour_type_id() {
        return lu_service_hour_type_id;
    }

    public void setLu_service_hour_type_id(int lu_service_hour_type_id) {
        this.lu_service_hour_type_id = lu_service_hour_type_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getDeliver_date() {
        return deliver_date;
    }

    public void setDeliver_date(String deliver_date) {
        this.deliver_date = deliver_date;
    }

    public String getDelivery_note() {
        return delivery_note;
    }

    public void setDelivery_note(String delivery_note) {
        this.delivery_note = delivery_note;
    }

    public boolean isDeduct_wallet() {
        return deduct_wallet;
    }

    public void setDeduct_wallet(boolean deduct_wallet) {
        this.deduct_wallet = deduct_wallet;
    }

    public List<ServiceItemInfo> getOrder() {
        return order;
    }

    public void setOrder(List<ServiceItemInfo> order) {
        this.order = order;
    }

    public String getPromo_code() {
        return promo_code;
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public int getPromo_amount() {
        return promo_amount;
    }

    public void setPromo_amount(int promo_amount) {
        this.promo_amount = promo_amount;
    }

    public int getWallet_balance() {
        return wallet_balance;
    }

    public void setWallet_balance(int wallet_balance) {
        this.wallet_balance = wallet_balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPickup_hour() {
        return pickup_hour;
    }

    public void setPickup_hour(String pickup_hour) {
        this.pickup_hour = pickup_hour;
    }

    public String getDeliver_hour() {
        return deliver_hour;
    }

    public void setDeliver_hour(String deliver_hour) {
        this.deliver_hour = deliver_hour;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }
}
