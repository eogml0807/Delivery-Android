package com.kh.delivery_project.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;

public class AddressUtil {

    public static String getAddress(Context context, double order_lat, double order_lng) {
        Geocoder geocoder = new Geocoder(context);
        String order_addr = null;
        try {
            List<Address> addrList = geocoder.getFromLocation(order_lat, order_lng, 1);
            Address address = addrList.get(0);
            order_addr = address.getAddressLine(0);
        } catch (Exception e) {
            order_addr = "주소를 찾을 수 없습니다";
            e.printStackTrace();
        }
        return order_addr;
    }

}
