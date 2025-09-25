package com.example.myselectshop.service;

import com.example.myselectshop.dto.ProductMypriceRequestDto;
import com.example.myselectshop.dto.ProductRequestDto;
import com.example.myselectshop.dto.ProductResponseDto;
import com.example.myselectshop.entity.*;
import com.example.myselectshop.naver.dto.ItemDto;
import com.example.myselectshop.repository.FolderRepository;
import com.example.myselectshop.repository.ProductFoldRepository;
import com.example.myselectshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    public static final int MIN_MY_PRICE = 100;

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFoldRepository productFoldRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto, User user) {
        Product product = productRepository.save(new Product(requestDto, user));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();

        if (myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정 해주세요.");
        }

        Product product = productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다.")
        );

        product.updateMyprice(requestDto.getMyprice());

        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRoleEnum = user.getRole();
        Page<Product> productList;

        if (userRoleEnum.equals(UserRoleEnum.USER)) {
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);
        }

        return productList.map(ProductResponseDto::of);
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품은 존재하지 않습니다.")
        );
        product.updateByItemDto(itemDto.getLprice());
    }

    public void addFolder(Long productId, Long folderId, User user) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NullPointerException("해당 상품은 존재하지 않습니다.")
        );

        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NullPointerException("해당 폴더가 존재하지 않습니다.")
        );

        if (!product.getUser().getId().equals(user.getId())
        || !folder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다.");
        }

        if (productFoldRepository.findByProductAndFolder(product, folder).isPresent()) {
            throw new IllegalArgumentException("중복된 폴더 입니다.");
        }

        productFoldRepository.save(new ProductFolder(product, folder));

    }
}
