package com.example.myselectshop.repository;

import com.example.myselectshop.entity.Folder;
import com.example.myselectshop.entity.Product;
import com.example.myselectshop.entity.ProductFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductFoldRepository extends JpaRepository<ProductFolder, Long> {
    Optional<ProductFolder> findByProductAndFolder(Product product, Folder folder);
}
