package com.leyou.controller;

import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    /category/list?pid=0
    @GetMapping("list")
    public ResponseEntity<List<Category>> getCategory(@RequestParam(value = "pid",defaultValue = "0")Long pid){
        return ResponseEntity.ok(categoryService.getCategoryList(pid));
    }

//    /item/category/bid/" + oldBrand.id
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> setCategoryById(@PathVariable("bid")Long bid){
        return ResponseEntity.ok(categoryService.updateCategoryById(bid));
    }

    @GetMapping("delBid/{bid}")
    public ResponseEntity<Void> delCategory(@PathVariable("bid")Long bid){
        categoryService.delCategoryById(bid);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
