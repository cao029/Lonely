package com.leyou.mapper;

import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.test.context.jdbc.Sql;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

import javax.websocket.server.PathParam;
import java.util.List;

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long> {
    @Select("SELECT * FROM tb_category WHERE id IN (SELECT category_id FROM tb_category_brand WHERE brand_id = #{bid})")
    List<Category> queryBrandId(@Param("bid") Long bid);

    @Delete("delete from tb_category where id in (select category_id from tb_category_brand where brand_id = #{bid})")
    int deleleByBrandId(@Param("bid") Long bid);
}
