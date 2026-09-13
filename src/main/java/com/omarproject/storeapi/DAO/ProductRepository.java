package com.omarproject.storeapi.DAO;

import com.omarproject.storeapi.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
