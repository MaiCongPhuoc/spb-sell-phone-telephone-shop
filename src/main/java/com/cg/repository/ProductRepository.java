package com.cg.repository;

import com.cg.model.Product;
import com.cg.model.ProductMedia;
import com.cg.model.dto.IProductDTO;
import com.cg.model.dto.ProductDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {


    @Query("SELECT " +
            "pm.product.id AS id, " +
            "pm.product.productName AS productName, " +
            "pm.product.price AS price, " +
            "pm.product.quantity AS quantity, " +
            "pm.product.description AS description, " +
            "pm.id AS fileId, " +
            "pm.fileName AS fileName, " +
            "pm.fileFolder AS fileFolder, " +
            "pm.fileUrl AS fileUrl, " +
            "pm.fileType AS fileType " +
            "FROM ProductMedia pm " +
            "ORDER BY pm.product.productName ASC"
    )
    Iterable<IProductDTO> findAllIProductDTO();


    @Query("SELECT " +
            "pm.product.id AS id, " +
            "pm.product.productName AS productName, " +
            "pm.product.price AS price, " +
            "pm.product.quantity AS quantity," +
            "pm.product.description AS description, " +
            "pm.id AS fileId, " +
            "pm.fileName AS fileName, " +
            "pm.fileFolder AS fileFolder, " +
            "pm.fileUrl AS fileUrl, " +
            "pm.fileType AS fileType " +
            "FROM ProductMedia pm " +
            "WHERE pm.product.id = :id"
    )
    IProductDTO findIProductDTOById(@Param("id") String id);

    @Query("SELECT " +
            "pm.product.id AS id, " +
            "pm.product.productName AS productName, " +
            "pm.product.price AS price, " +
            "pm.product.quantity AS quantity, " +
            "pm.product.description AS description, " +
            "pm.id AS fileId, " +
            "pm.fileName AS fileName, " +
            "pm.fileFolder AS fileFolder, " +
            "pm.fileUrl AS fileUrl, " +
            "pm.fileType AS fileType " +
            "FROM ProductMedia pm " +
            "ORDER BY pm.product.productName ASC"
    )
    List<ProductMedia> findAllProductMedia();

    @Query("SELECT " +
            "pm.product.id AS id, " +
            "pm.product.productName AS productName, " +
            "pm.product.price AS price, " +
            "pm.product.quantity AS quantity, " +
            "pm.product.description AS description, " +
            "pm.id AS fileId, " +
            "pm.fileName AS fileName, " +
            "pm.fileFolder AS fileFolder, " +
            "pm.fileUrl AS fileUrl, " +
            "pm.fileType AS fileType " +
            "FROM ProductMedia pm " +
            "WHERE pm.product.productName LIKE %?1%"
    )
    Iterable<IProductDTO> findProductByProductName(String search);

}
