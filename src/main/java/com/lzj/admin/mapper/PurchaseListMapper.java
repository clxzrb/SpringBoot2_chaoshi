package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.model.CountResultModel;
import com.lzj.admin.pojo.PurchaseList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzj.admin.query.PurchaseListQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 进货单接口
 * @author TianTian
 * @date 2022/1/21 18:27
 */
@Mapper
public interface PurchaseListMapper extends BaseMapper<PurchaseList> {
	/**
         * 分页查询进货单（关联供应商、用户）
     */
    IPage<PurchaseList> purchaseList(IPage<PurchaseList> page, @Param("purchaseListQuery") PurchaseListQuery purchaseListQuery);
    
    /**
     * 商品采购统计 - 获取总记录数
     */
    Long countPurchaseTotal(@Param("purchaseListQuery") PurchaseListQuery purchaseListQuery);

    /**
     * 商品采购统计 - 获取分页数据
     */
    List<CountResultModel> purchaseListQueryList(@Param("purchaseListQuery") PurchaseListQuery purchaseListQuery);
}
