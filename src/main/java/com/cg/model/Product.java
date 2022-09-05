package com.cg.model;

import com.cg.model.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "products")
public class Product  extends BaseEntity{

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "product_name")
    private String productName;

    @Column(precision = 9, scale = 0)
    private BigDecimal price;

    private int quantity;

    private String description;

    @Column(columnDefinition = "BIGINT(20) DEFAULT 0")
    private Long ts = new Date().getTime();

    @OneToMany(mappedBy = "product")
    private List<ProductMedia> productMedia;

    @Override
    public String toString() {
        return "Product{" +
                "id= '" + id + '\'' +
                ", productName= '" + productName + '\'' +
                ", price= " + price +
                ", quantity= " + quantity +
                ", description= '" + description + '\'' +
                ", productMedia= " + productMedia +
                '}';
    }

    public ProductDTO toProductDTO() {
        return new ProductDTO()
                .setId(id)
                .setProductName(productName)
                .setPrice(price)
                .setQuantity(quantity)
                .setDescription(description);
    }
}
