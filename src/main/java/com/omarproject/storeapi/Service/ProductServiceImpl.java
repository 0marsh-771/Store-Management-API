package com.omarproject.storeapi.Service;
import com.omarproject.storeapi.DAO.ProductRepository;
import com.omarproject.storeapi.Entity.Product;
import com.omarproject.storeapi.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository theProductRepository){
        productRepository = theProductRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int theId) {
        return productRepository.findById(theId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", theId));
    }

    @Override
    public Product save(Product theProduct) {
        return productRepository.save(theProduct);
    }

    @Override
    public void deleteById(int theId) {
        if (!productRepository.existsById(theId)) {
            throw new ResourceNotFoundException("Product", theId);
        }

        productRepository.deleteById(theId);
    }
}
