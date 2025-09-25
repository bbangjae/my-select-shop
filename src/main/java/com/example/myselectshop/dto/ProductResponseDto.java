package com.example.myselectshop.dto;

import com.example.myselectshop.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String link;
    private String image;
    private int lprice;
    private int myprice;

    private List<FolderResponseDto> productFolderList = new ArrayList<>();

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();
        this.productFolderList = product.getProductFolderList().stream()
                .map(folder -> new FolderResponseDto(folder.getFolder()))
                .toList();
    }

    public static ProductResponseDto of(Product product) {
        return new ProductResponseDto(product);
    }
}