package com.lzj.admin.service;

import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.GoodsQuery;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 商品表服务
 * @author TianTian
 * @date 2022/1/19 13:55
 */
public interface GoodsService extends IService<Goods> {
	/**
     * 分页查询商品列表
     */
    Map<String, Object> goodsList(GoodsQuery goodsQuery);

    /**
     * 新增商品
     */
    void saveGoods(Goods goods);
    
    /**
     * 批量逻辑删除商品
     */
    void deleteGoods(Integer id);

   /**
    * 根据商品名称查询未删除商品（查重）
    */
   Goods findGoodsByName(String name);

   void updateGoods(Goods goods);
   
   /**
    * 更新期初库存（修改库存、成本价）
    */
   void updateStock(Goods goods);
   /**
    * 期初库存删除商品（逻辑删除）
    */
   void deleteStock(Integer id);
   
   //查询所有商品类别（树状图）
   List<TreeDto> queryAllGoodsTypes(Integer typeId);
   
   Goods findGoodsByCode(String code);
}
