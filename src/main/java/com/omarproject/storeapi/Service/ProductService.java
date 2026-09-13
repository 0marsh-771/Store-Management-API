package com.omarproject.storeapi.Service;

import com.omarproject.storeapi.Entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(int theId);

    Product save(Product theProduct);

    void deleteById(int theId);

}
