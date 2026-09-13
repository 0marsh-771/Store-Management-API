package com.omarproject.storeapi.Service;

import com.omarproject.storeapi.Entity.Sales;

import java.util.List;

public interface SalesService {

    List<Sales> findAll();

    Sales findById(int theId);

    Sales save(Sales theSale);

    void deleteById(int theId);

}
