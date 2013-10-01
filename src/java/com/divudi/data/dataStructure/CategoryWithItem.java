/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.data.dataStructure;

import com.divudi.entity.Category;
import com.divudi.entity.Item;
import java.util.List;

/**
 *
 * @author safrin
 */
public class CategoryWithItem {

    private Category category;
    //private List<Item> item;
    private List<ItemWithFee> itemWithFees;
    private double subTotal;
    private double subHosTotal;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ItemWithFee> getItemWithFees() {
        return itemWithFees;
    }

    public void setItemWithFees(List<ItemWithFee> itemWithFees) {
        this.itemWithFees = itemWithFees;
    }

    public double getSubTotal() {
        subTotal = 0.0;
        for (ItemWithFee it : itemWithFees) {
            subTotal += it.getTotal();
        }
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getSubHosTotal() {
        subHosTotal = 0.0;
        for (ItemWithFee it : itemWithFees) {
            subHosTotal += it.getHospitalFee();
        }
        return subHosTotal;
    }

    public void setSubHosTotal(double subHosTotal) {
        this.subHosTotal = subHosTotal;
    }
}
