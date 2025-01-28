package com.tfg.terranostra.repositories;

import com.tfg.terranostra.models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository <ProductModel, Long> {
}
