package com.miniAmazon;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.*;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByProductName (String productName);
}
