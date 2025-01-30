package com.tfg.terranostra.repositories;

import com.tfg.terranostra.models.ProductoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository <ProductoModel, Long> {
}
