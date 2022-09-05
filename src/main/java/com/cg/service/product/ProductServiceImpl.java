package com.cg.service.product;


import com.cg.exception.DataInputException;
import com.cg.model.Product;
import com.cg.model.ProductMedia;
import com.cg.model.dto.IProductDTO;
import com.cg.model.dto.ProductDTO;
import com.cg.model.emuns.FileType;
import com.cg.repository.ProductMediaRepository;
import com.cg.repository.ProductRepository;
import com.cg.service.product.ProductService;
import com.cg.service.upload.UploadService;
import com.cg.util.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMediaRepository productMediaRepository;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private UploadUtils uploadUtils;


    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Iterable<IProductDTO> findAllIProductDTO() {
        return productRepository.findAllIProductDTO();
    }

    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Product create(ProductDTO productDTO) {

        String fileType = productDTO.getFile().getContentType();

        assert fileType != null;

        fileType = fileType.substring(0, 5);

        productDTO.setFileType(fileType);

        Product product = productRepository.save(productDTO.toProduct());

        ProductMedia productMedia = productMediaRepository.save(productDTO.toProductMedia());

        if (fileType.equals(FileType.IMAGE.getValue())) {
            uploadAndSaveProductImage(productDTO, product, productMedia);
        }

        if (fileType.equals(FileType.VIDEO.getValue())) {
            uploadAndSaveProductVideo(productDTO, product, productMedia);
        }

        return product;
    }

    @Override
    public Product edit(ProductDTO productDTO, String id) {
        List<ProductMedia> productMediaDTOList = productMediaRepository.findProductMediaByProduct(productDTO.toProduct());
        for(ProductMedia productMedia1 : productMediaDTOList) {
            productMediaRepository.deleteById(productMedia1.getId());
        }

        String fileType = productDTO.getFile().getContentType();

        assert fileType != null;

        fileType = fileType.substring(0, 5);

        productDTO.setFileType(fileType);

        Product product = productRepository.save(productDTO.toProduct());

//        ProductMedia productMedia = productMediaRepository.findByProduct(product).get();

//        ProductMedia productMedia = productDTO.toProductMedia();

        ProductMedia productMedia = productMediaRepository.save(productDTO.toProductMedia());

        if (fileType.equals(FileType.IMAGE.getValue())) {
            uploadAndSaveProductImage(productDTO, product, productMedia);
        }

        if (fileType.equals(FileType.VIDEO.getValue())) {
            uploadAndSaveProductVideo(productDTO, product, productMedia);
        }

        return product;
    }


    @Override
    public IProductDTO findIProductDTOById(String id) {
        return productRepository.findIProductDTOById(id);
    }

    private void uploadAndSaveProductImage(ProductDTO productDTO, Product product, ProductMedia productMedia) {
        try {
            Map uploadResult = uploadService.uploadImage(productDTO.getFile(), uploadUtils.buildImageUploadParams(productMedia));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productMedia.setFileName(productMedia.getId() + "." + fileFormat);
            productMedia.setFileUrl(fileUrl);
            productMedia.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
            productMedia.setCloudId(productMedia.getFileFolder() + "/" + productMedia.getId());
            productMedia.setProduct(product);
            productMediaRepository.save(productMedia);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }


    private void uploadAndSaveProductVideo(ProductDTO productDTO, Product product, ProductMedia productMedia) {
        try {
            Map uploadResult = uploadService.uploadVideo(productDTO.getFile(), uploadUtils.buildVideoUploadParams(productMedia));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productMedia.setFileName(productMedia.getId() + "." + fileFormat);
            productMedia.setFileUrl(fileUrl);
            productMedia.setFileFolder(UploadUtils.VIDEO_UPLOAD_FOLDER);
            productMedia.setCloudId(productMedia.getFileFolder() + "/" + productMedia.getId());
            productMedia.setProduct(product);
            productMediaRepository.save(productMedia);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload video thất bại");
        }
    }

    @Override
    public void delete(Product product) throws IOException {

        Optional<ProductMedia> productImageVideo = productMediaRepository.findByProduct(product);

        if (productImageVideo.isPresent()) {
            String publicId = productImageVideo.get().getCloudId();

            if (productImageVideo.get().getFileType().equals(FileType.IMAGE.getValue())) {
                uploadService.destroyImage(publicId, uploadUtils.buildImageDestroyParams(product, publicId));
            }

            if (productImageVideo.get().getFileType().equals(FileType.VIDEO.getValue())) {
                uploadService.destroyVideo(publicId, uploadUtils.buildVideoDestroyParams(product, publicId));
            }

            productMediaRepository.delete(productImageVideo.get());
        }

        productRepository.delete(product);

    }

    @Override
    public Iterable<IProductDTO> findProductByProductName(String search) {
        return productRepository.findProductByProductName(search);
    }


}
