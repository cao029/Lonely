package com.leyou.mapper;

import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id,brand_id) VALUES (#{cid},#{bid})")
    int saveCategoryBrand(@Param("cid") Long cid,@Param("bid") Long bid);

//    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = #{cid})")
    @Select("SELECT b.id, b.`name`, b.letter, b.image FROM tb_category_brand cb RIGHT JOIN tb_brand b ON cb.brand_id = b.id WHERE cb.category_id = #{cid}")
    List<Brand> queryBrandById(@Param("cid") Long cid);


}
