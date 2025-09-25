package com.example.myselectshop.repository;

import com.example.myselectshop.entity.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductFoldRepository extends JpaRepository<ProductFolder, Long> {
}
