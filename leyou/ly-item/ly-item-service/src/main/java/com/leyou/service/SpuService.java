package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.mapper.DetailMapper;
import com.leyou.mapper.SkuMapper;
import com.leyou.mapper.SpuMapper;
import com.leyou.mapper.StockMapper;
import com.leyou.pojo.*;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private DetailMapper detailMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    public PageResult<Spu> getGoodsPage(Integer page, Integer rows, String key, Boolean saleable) {
//        分页
        PageHelper.startPage(page,rows);
//        查数据
//        1.过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
//        模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title","%"+key+"%");
        }
//        上下架过滤
        if (saleable != null) {
            criteria.andEqualTo("saleable",saleable);
        }
//        逻辑删除过滤
        criteria.andEqualTo("valid",true);
//       默认查询
        example.setOrderByClause("last_update_time DESC");
//        查询
        List<Spu> spus = spuMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(spus)) {
            throw new LyException(ExceptionEnum.GOODS_SELECT_NOT_FOND);
        }
        handleCategoryAndBrandName(spus);
//        返回
        PageInfo<Spu> info = new PageInfo<>(spus);
        return new PageResult<>(info.getTotal(),spus);
    }

    private void handleCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
//            处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());

//            处理分类名称
            String names = categoryService.queryByIds(Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.joining("/"));

            spu.setCname(names);
        }
    }

    @Transactional
    public void saveGoods(Spu spu) {
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        if (count != 1) {
            throw new LyException(ExceptionEnum.GOOD_INSERT_FILE);
        }
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        detailMapper.insert(detail);
//        新增sku和库存
        saveSkuAndStcock(spu);
    }

    private void saveSkuAndStcock(Spu spu) {
        int count;
        ArrayList<Stock> stockList = new ArrayList<>();

        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());

            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new LyException(ExceptionEnum.SKU_INSERT_FILE);
            }

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            stockList.add(stock);
        }
        count = stockMapper.insertList(stockList);

        if (count != stockList.size()) {
            throw new LyException(ExceptionEnum.BRANDS_INSERT_CID_FILE);
        }
    }

    public SpuDetail querySpuDetailById(Long id) {
        SpuDetail detail = detailMapper.selectByPrimaryKey(id);
        if (detail == null) {
            throw new LyException(ExceptionEnum.SPU_DETAIL_NOT_FOUND);
        }
        return detail;
    }

    public List<Sku> querySkuById(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(skuList)) {
            throw new LyException(ExceptionEnum.SKU_SELECT_NOT_FOUND);
        }
//        查询库存
        List<Long> ids = skuList.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stockList = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stockList)) {
            throw new LyException(ExceptionEnum.STOCK_SELECT_FILE);
        }
        Map<Long, Integer> stockMap = stockList.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skuList.forEach(s -> s.setStock(stockMap.get(s.getId())));
        return skuList;
    }
}
