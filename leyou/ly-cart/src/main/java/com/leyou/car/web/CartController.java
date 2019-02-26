package com.leyou.car.web;

import com.leyou.car.pojo.Cart;
import com.leyou.car.service.CartService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    private ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    private ResponseEntity<List<Cart>> getCart(){
        return ResponseEntity.ok(cartService.getCart());
    }


    @PutMapping
    private ResponseEntity<Void> changeCart(@RequestParam("skuId") Long skuId,@RequestParam("num")Integer num){
        cartService.changeCart(skuId,num);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    private ResponseEntity<Void> deleteCart(@RequestParam("skuId")Long skuId){
        cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }

}
