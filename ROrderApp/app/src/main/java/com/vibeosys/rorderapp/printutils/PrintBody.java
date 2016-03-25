package com.vibeosys.rorderapp.printutils;

import com.vibeosys.rorderapp.data.OrderDetailsDTO;

import java.util.HashMap;

/**
 * Created by akshay on 21-03-2016.
 */
public class PrintBody {

    HashMap<Integer, OrderDetailsDTO> menus;

    public HashMap<Integer, OrderDetailsDTO> getMenus() {
        return menus;
    }

    public void setMenus(HashMap<Integer, OrderDetailsDTO> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "PrintBody{" +
                "menus=" + menus +
                '}';
    }
}
