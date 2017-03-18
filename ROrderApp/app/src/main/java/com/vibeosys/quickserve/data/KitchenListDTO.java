package com.vibeosys.quickserve.data;

import java.util.List;

/**
 * Created by akshay on 18-03-2016.
 */
public class KitchenListDTO {

    private List<OrderDetailsDTO> orderList;

    public List<OrderDetailsDTO> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderDetailsDTO> orderList) {
        this.orderList = orderList;
    }
}
