package com.gonzalojerez.dev.permissionsapp.products.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @PreAuthorize("hasAuthority('CREATE_PRODUCTS')")
    @PostMapping
    public String create(){
        return "Producto creado";
    }
}
