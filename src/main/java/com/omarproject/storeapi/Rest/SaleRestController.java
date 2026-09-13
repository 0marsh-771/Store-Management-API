package com.omarproject.storeapi.Rest;

import com.omarproject.storeapi.Entity.Sales;
import com.omarproject.storeapi.Service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sale")
public class SaleRestController {

    private SalesService salesService;

    private JsonMapper jsonMapper;

    @Autowired
    public SaleRestController(SalesService theSalesService, JsonMapper theJsonMapper){
        salesService = theSalesService;
        jsonMapper = theJsonMapper;
    }

    @GetMapping("/sales")
    public List<Sales> findAll(){
        return salesService.findAll();
    }

    @GetMapping("/sales/{salesId}")
    public Sales getSale(@PathVariable int salesId){

        Sales theSale = salesService.findById(salesId);

        if(theSale == null){
            throw new RuntimeException("Sale with the id of " + salesId + " Could not be found");
        }

        return theSale;

    }

    @PostMapping("/sales")
    public Sales createSale(@RequestBody Sales theSale){

        theSale.setId(0);

        Sales dbSale = salesService.save(theSale);

        return dbSale;

    }

    @PutMapping("/sales")
    public Sales updateSale(@RequestBody Sales theSale){

        Sales dbSale = salesService.save(theSale);

        return dbSale;

    }

    @PatchMapping("/sales/{saleId}")
    public Sales patchSale(@PathVariable int saleId,
                           @RequestBody Map<String, Object> patchPayload){

        Sales tempSale = salesService.findById(saleId);

        if(tempSale == null){
            throw new RuntimeException("Sale with the id of " + saleId + " Could not be found");
        }

        if(patchPayload.containsKey("id")){
            throw new RuntimeException("Body cant contain the id key");
        }

        Sales patchSale = jsonMapper.updateValue(tempSale, patchPayload);

        Sales dbSale = salesService.save(patchSale);

        return dbSale;

    }

    @DeleteMapping("/sales/{saleId}")
    public String deleteSale(@PathVariable int saleId){

        Sales theSale = salesService.findById(saleId);

        if(theSale == null){
            throw new RuntimeException("Sale with the id of " + saleId + " Could not be found");
        }

        salesService.deleteById(saleId);

        return "Sale with the id of " + saleId + " was successfully deleted!";

    }

}
