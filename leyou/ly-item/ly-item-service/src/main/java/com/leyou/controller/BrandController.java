package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> getPageData(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",defaultValue = "false")Boolean desc
    ){
        return ResponseEntity.ok(brandService.queryPageData(page,rows,key,sortBy,desc));
    }
    /**
     * 新增品牌
     * @param brand
     * @param cidList
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids")List<Long> cidList){
        brandService.saveBrand(brand, cidList);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


//    http://api.leyou.com/api/item/brand/cid/76'
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> getBrandListByCid(@PathVariable("cid")Long cid){
        return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }


}
