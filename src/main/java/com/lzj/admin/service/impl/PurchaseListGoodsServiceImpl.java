package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.mapper.PurchaseListGoodsMapper;
import com.lzj.admin.pojo.User;
import com.lzj.admin.query.PurchaseListGoodsQuery;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.service.PurchaseListGoodsService;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

import javax.annotation.Resource;

/**
 * <p>
 * 进货单商品表 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class PurchaseListGoodsServiceImpl extends ServiceImpl<PurchaseListGoodsMapper, PurchaseListGoods> implements PurchaseListGoodsService {
	@Resource
    private PurchaseListGoodsMapper purchaseListGoodsMapper;

	@Override
    public Map<String, Object> purchaseListGoodsList(PurchaseListGoodsQuery purchaseListGoodsQuery) {
        IPage<PurchaseListGoods> page = new Page<>(purchaseListGoodsQuery.getPage(), purchaseListGoodsQuery.getLimit());
        QueryWrapper<PurchaseListGoods> wrapper = new QueryWrapper<>();
        if(null != purchaseListGoodsQuery.getPurchaseListId()){
            wrapper.eq("purchase_list_id", purchaseListGoodsQuery.getPurchaseListId());
        }
        page = this.baseMapper.selectPage(page, wrapper);
        return PageResultUtil.setResult(page.getTotal(), page.getRecords());
    }
}
