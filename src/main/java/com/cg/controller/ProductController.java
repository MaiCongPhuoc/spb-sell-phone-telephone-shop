package com.cg.controller;

import com.cg.model.Product;
import com.cg.model.dto.ProductDTO;
import com.cg.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("product/listProduct");
        Iterable<Product> productDTOList = productService.findAll();
        modelAndView.addObject("productDTOList", productDTOList);
        return modelAndView;
    }
}
