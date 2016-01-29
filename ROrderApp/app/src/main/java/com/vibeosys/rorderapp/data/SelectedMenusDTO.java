package com.vibeosys.rorderapp.data;

import java.util.ArrayList;

/**
 * Created by akshay on 29-01-2016.
 */
public class SelectedMenusDTO {

    private ArrayList<OrderMenuDTO> selectedMenus;

    public SelectedMenusDTO(ArrayList<OrderMenuDTO> selectedMenus) {
        this.selectedMenus = selectedMenus;
    }

    public ArrayList<OrderMenuDTO> getSelectedMenus() {
        return selectedMenus;
    }

    public void setSelectedMenus(ArrayList<OrderMenuDTO> selectedMenus) {
        this.selectedMenus = selectedMenus;
    }

    public double getAmount(int qty,double price)
    {
        return qty*price;
    }

    public double getTotalBillAmount()
    {
        double sum=0;
        for (int i=0;i<selectedMenus.size();i++)
        {
            OrderMenuDTO menu=selectedMenus.get(i);
            sum=sum+getAmount(menu.getmQuantity(),menu.getmPrice());
        }
        return sum;
    }

    public int getTotalItems()
    {
        return this.selectedMenus.size();
    }
}
