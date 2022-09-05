package com.cg.service.productMedia;

import com.cg.model.ProductMedia;

import java.nio.file.OpenOption;
import java.util.Optional;

public interface ProductMediaService {

    Optional<ProductMedia> findById(String id);

    Iterable<ProductMedia> findAll();

    ProductMedia create(ProductMedia productMedia);

    void delete(ProductMedia productMedia);

    void save(ProductMedia productMedia);
}
