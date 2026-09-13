package com.omarproject.storeapi.DAO;

import com.omarproject.storeapi.Entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesRepository extends JpaRepository<Sales, Integer> {
}
