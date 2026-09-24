package com.omarproject.storeapi.Service;

import com.omarproject.storeapi.DAO.SalesRepository;
import com.omarproject.storeapi.Entity.Sales;
import com.omarproject.storeapi.Exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements SalesService{

    private SalesRepository salesRepository;

    @Autowired
    public SalesServiceImpl(SalesRepository theSalesRepository){
        salesRepository = theSalesRepository;
    }

    @Override
    public List<Sales> findAll() {
        return salesRepository.findAll();
    }

    @Override
    public Sales findById(int theId) {
        return salesRepository.findById(theId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", theId));
    }

    @Override
    public Sales save(Sales theSale) {
        return salesRepository.save(theSale);
    }

    @Override
    public void deleteById(int theId) {
        if (!salesRepository.existsById(theId)) {
            throw new ResourceNotFoundException("Sale", theId);
        }

        salesRepository.deleteById(theId);
    }
}
