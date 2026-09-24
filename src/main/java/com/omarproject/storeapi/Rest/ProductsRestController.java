package com.omarproject.storeapi.Rest;

import com.omarproject.storeapi.Entity.Product;
import com.omarproject.storeapi.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductsRestController {

    private ProductService productService;

    private JsonMapper jsonMapper;

    @Autowired
    public ProductsRestController(ProductService theProductService, JsonMapper theJsonMapper){
        productService = theProductService;
        jsonMapper = theJsonMapper;
    }

    @GetMapping("/products")
    public List<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/products/{productId}")
    public Product getProduct(@PathVariable int productId){
        return productService.findById(productId);
    }

    @PostMapping("/products")
    public Product createProduct(@RequestBody Product theProduct){

        theProduct.setId(0);

        Product dbProduct = productService.save(theProduct);

        return dbProduct;

    }

    @PutMapping("/products")
    public Product updateProduct(@RequestBody Product theProduct){

        Product dbProduct = productService.save(theProduct);

        return dbProduct;

    }

    @PatchMapping("/products/{productId}")
    public Product patchProduct(@PathVariable int productId,
                                @RequestBody Map<String, Object> patchPayload){

        Product tempProduct = productService.findById(productId);

        if(patchPayload.containsKey("id")){
            throw new RuntimeException("Body cant contain the id key");
        }

        Product patchProduct = jsonMapper.updateValue(tempProduct, patchPayload);

        Product dbProduct = productService.save(patchProduct);

        return dbProduct;

    }

    @DeleteMapping("/products/{productId}")
    public String deleteProduct(@PathVariable int productId){

        productService.deleteById(productId);

        return "Product with the id of " + productId + " was successfully deleted!";

    }

}
