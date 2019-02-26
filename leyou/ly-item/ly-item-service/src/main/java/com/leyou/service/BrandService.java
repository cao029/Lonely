package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.awt.*;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryPageData(Integer page, Integer rows, String key, String sortBy, Boolean desc) {
//        分页
        PageHelper.startPage(page,rows);
//        过滤条件
        Example example = new Example(Brand.class);
        if (StringUtils.isNoneBlank(key)) {
            example.createCriteria().orLike("name","%" + key + "%").orEqualTo("letter",key.toUpperCase());
        }
//        排序
        if (StringUtils.isNoneBlank(sortBy)) {
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
//        查询
        List<Brand> brands = brandMapper.selectByExample(example);

//        判断是否为空
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRANDS_NOT_DATA);
        }
//        解析分页结果
        PageInfo<Brand> info = new PageInfo<>(brands);

        return new PageResult<>(info.getTotal(),info.getList());
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
//        新增品牌
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if (count == 0) {
            throw new LyException(ExceptionEnum.BRANDS_INSERT_NOT_SUCCESS);
        }

        for (Long cid : cids) {
            int cNum = brandMapper.saveCategoryBrand(cid,brand.getId());
            if (cNum == 0) {
                throw new LyException(ExceptionEnum.BRANDS_INSERT_CID_FILE);
            }
        }
    }


    public Brand queryById(Long brandId) {
        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> brands = brandMapper.queryBrandById(cid);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }
}
