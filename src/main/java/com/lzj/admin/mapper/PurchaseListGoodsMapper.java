package com.lzj.admin.mapper;

import com.lzj.admin.pojo.PurchaseListGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 进货单商品表 Mapper 接口
 * </p>
 *
 * @author 老李
 * @since 2021-03-27
 */
@Mapper
public interface PurchaseListGoodsMapper extends BaseMapper<PurchaseListGoods> {
	/**
         * 根据进货单ID查询商品明细
     */
    List<PurchaseListGoods> listPurchaseGoodsByPurchaseId(@Param("purchaseListId") Integer purchaseListId);
}
