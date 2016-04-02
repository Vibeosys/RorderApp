package com.vibeosys.rorderapp.printutils;

import com.vibeosys.rorderapp.data.OrderDetailsDTO;

import java.util.HashMap;

/**
 * Created by akshay on 21-03-2016.
 */
public class PrintBody {

    HashMap<String, OrderDetailsDTO> menus;

    public HashMap<String, OrderDetailsDTO> getMenus() {
        return menus;
    }

    public void setMenus(HashMap<String, OrderDetailsDTO> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "PrintBody{" +
                "menus=" + menus +
                '}';
    }
}
