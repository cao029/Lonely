package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpuController {

    @Autowired
    private SpuService spuService;

//    http://api.leyou.com/api/item/spu/page?key=&saleable=true&page=1&rows=5
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> getGoodsPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false) Boolean saleable
    ){
        return ResponseEntity.ok(spuService.getGoodsPage(page,rows,key,saleable));
    }

//    http://api.leyou.com/api/item/spu/detail/6
//    http://api.leyou.com/api/item/brand/cid/76'
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        this.spuService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id")Long id){
        return ResponseEntity.ok(this.spuService.querySpuDetailById(id));
    }

    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuById(@RequestParam("id")Long id){
        return ResponseEntity.ok(this.spuService.querySkuById(id));
    }



}
