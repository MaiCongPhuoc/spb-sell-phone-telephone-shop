package com.cg.model.dto;

import com.cg.model.Product;
import com.cg.model.ProductMedia;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ProductDTO implements Serializable {

    private String id;

    @NotBlank(message = "Name is required")
    private String productName;

    private BigDecimal price;

    private int quantity;

    @NotBlank(message = "Description is required")
    private String description;

    private String fileName;

    private String fileFolder;

    private String fileUrl;

    private String cloudId;

    private String fileProductId;

    private MultipartFile file;

    private String fileType;

    public ProductDTO() {
    }

    public ProductDTO(String productName, BigDecimal price, int quantity, String description, MultipartFile file) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.file = file;
    }

    public Product toProduct() {
        return new Product()
                .setId(id)
                .setProductName(productName)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description);
    }

    public ProductMedia toProductMedia() {
        return new ProductMedia()
                .setId(fileProductId)
                .setFileName(fileName)
                .setFileFolder(fileFolder)
                .setFileUrl(fileUrl)
                .setCloudId(cloudId)
                .setFileType(fileType);
    }
}