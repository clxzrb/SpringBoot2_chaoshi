package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.model.CountResultModel;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.PurchaseList;
import com.lzj.admin.mapper.PurchaseListMapper;
import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.query.PurchaseListQuery;
import com.lzj.admin.service.GoodsService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.service.GoodsTypeService;
import com.lzj.admin.service.PurchaseListGoodsService;
import com.lzj.admin.service.PurchaseListService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.DateUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进货单 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class PurchaseListServiceImpl extends ServiceImpl<PurchaseListMapper, PurchaseList> implements PurchaseListService {
	@Resource
    private PurchaseListGoodsService purchaseListGoodsService;

    @Override
    public Map<String, Object> purchaseList(PurchaseListQuery purchaseListQuery) {
        IPage<PurchaseList> page = new Page<>(purchaseListQuery.getPage(), purchaseListQuery.getLimit());
        page = this.baseMapper.purchaseList(page, purchaseListQuery);
        return PageResultUtil.setResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePurchaseList(Integer id) {
        // 1.先删除进货单商品明细
        AssertUtil.isTrue(!purchaseListGoodsService.remove(new QueryWrapper<PurchaseListGoods>().eq("purchase_list_id", id)),
                "商品明细删除失败");
        // 2.删除主进货单记录
        AssertUtil.isTrue(!this.removeById(id), "进货单删除失败");
    }


}
