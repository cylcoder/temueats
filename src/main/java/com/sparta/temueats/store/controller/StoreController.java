package com.sparta.temueats.store.controller;

import com.sparta.temueats.menu.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class StoreController {

    @GetMapping("/stores/req")
    public String getStoreRequestForm(Model model) {
        List<Category> categories = Arrays.stream(Category.values()).toList();
        model.addAttribute("categories", categories);
        return "store/store-req";
    }

}