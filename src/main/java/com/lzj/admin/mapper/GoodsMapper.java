package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.query.GoodsQuery;
import org.apache.ibatis.annotations.Param;

public interface GoodsMapper extends BaseMapper<Goods> {
	/**
         * 分页条件查询商品（关联类别名称）
     * @param page 分页对象
     * @param goodsQuery 查询条件
     * @return 分页数据
     */
	IPage<Goods> goodsList(IPage<Goods> page, @Param("goodsQuery") GoodsQuery goodsQuery);
}
