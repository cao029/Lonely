package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOND(400,"没有列表信息"),
    BRANDS_INSERT_CID_FILE(400,"新增品牌和分类失败"),
    IMAGE_TYPE_NOT_PERVIDE(400,"不支持的图片类型"),
    INVALID_FILE_TYPE(400,"图片内容不能为空"),
    FILE_UPLOAD_ERROR(400,"文件加载失败"),
    SPEC_GROUP_NOT_FOUND(400,"分组信息没有找到"),
    SPEC_PARAM_NOT_FOUND(400,"参数信息没有找到"),
    BRANDS_INSERT_NOT_SUCCESS(400,"添加品牌信息失败"),
    GOODS_SELECT_NOT_FOND(400,"查询商品信息失败，没有数据"),
    CATEGORY_BYIDS_NOT_FOOUND(400,"通过id的分类信息没有查询到"),
    BRAND_NOT_FOUND(400,"品牌没有查询到"),
    BRANDS_NOT_DATA(400,"没有查询到相关的信息"),
    SKU_INSERT_FILE(400,"SKU添加失败，请稍后再试"),
    SKU_SELECT_NOT_FOUND(400,"sku查询失败"),
    GOOD_SAVE_ERROR(400,"商品添加失败"),
    STOCK_SELECT_FILE(400,"stock查询失败"),
    SPU_DETAIL_NOT_FOUND(400,"SPU没有查到数据"),
    TYPE_NOT_EXITED(400,"查询的类型不存在"),
    USER_NOT_EXIST(400,"用户不存在"),
    PHONE_NOT_MATCHES(400,"手机格式不符合要求"),
    MSG_IS_NULL(400,"监听的信息为空"),
    REDIS_DATA_NOT_FOUND(400,"redis没有查到数据"),
    DATA_NOT_TOGETHER(400,"数据不相同"),
    DATA_NOT_NULL(400,"数据不能为空"),
    LOGIN_NOT_FILE(400,"登录失败，用户名或者密码错误"),
    USER_IS_NOT_EXIST(400,"用户不存在"),
    TOKEN_MAKE_FILE(400,"TOKEN生成失败"),
    INIT_PUBLIC_KEY_FILE(400,"初始化公钥失败！"),
    TOKEN_SHOW_EXCEPTION(403,"JwtUtils校验token出现异常"),
    CART_NOT_HAVE_DATE(400,"redis服务器灭有数据"),
    PHONE_NOT_CURRENT(400,"手机格式不正确"),
    GOOD_INSERT_FILE(400,"商品添加失败");
    ;
    private int value;
    private String msg;

    public int status(){
        return this.value;
    }

    public String msg(){
        return this.msg;
    }


}
