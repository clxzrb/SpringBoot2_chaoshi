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
	@Resource
    private GoodsService goodsService;

    @Override
    public Map<String, Object> purchaseList(PurchaseListQuery purchaseListQuery) {
        IPage<PurchaseList> page = new Page<>(purchaseListQuery.getPage(), purchaseListQuery.getLimit());
        page = this.baseMapper.purchaseList(page, purchaseListQuery);
        return PageResultUtil.setResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePurchaseList(Integer id) {
        //先删除进货单商品明细
        AssertUtil.isTrue(!purchaseListGoodsService.remove(new QueryWrapper<PurchaseListGoods>().eq("purchase_list_id", id)),
                "商品明细删除失败");
        //删除主进货单记录
        AssertUtil.isTrue(!this.removeById(id), "进货单删除失败");
    }

    @Override
    public Map<String, Object> countPurchase(PurchaseListQuery purchaseListQuery) {
        // 计算分页参数
        int page = purchaseListQuery.getPage() != null ? purchaseListQuery.getPage() : 1;
        int limit = purchaseListQuery.getLimit() != null ? purchaseListQuery.getLimit() : 10;
        int start = (page - 1) * limit;
        int end = page * limit;

        purchaseListQuery.setIndex(start);
        purchaseListQuery.setStartRow(start);
        purchaseListQuery.setEndRow(end);

        // 查询总数
        Long count = this.baseMapper.countPurchaseTotal(purchaseListQuery);
        // 查询分页数据
        List<CountResultModel> list = this.baseMapper.purchaseListQueryList(purchaseListQuery);
        return PageResultUtil.setResult(count, list);
    }
    
    //生成单号
    @Override
    public String createPurchaseNumber() {
        // 获取当前日期 yyyyMMdd
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 查询当天已有多少进货单
        QueryWrapper<PurchaseList> wrapper = new QueryWrapper<>();
        wrapper.likeRight("purchase_number", "JH" + date);
        int count = this.count(wrapper);
        // 生成三位流水号
        String serialNumber = String.format("%03d", count + 1);
        return "JH" + date + serialNumber;
    }

    
    //保存进货单
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void savePurchaseList(PurchaseList purchaseList, List<PurchaseListGoods> plgList) {
        //参数校验
        AssertUtil.isTrue(purchaseList.getSupplierId() == null || purchaseList.getSupplierId() == 0, "请选择供应商");
        AssertUtil.isTrue(purchaseList.getAmountPayable() == null, "应付金额为空");
        AssertUtil.isTrue(purchaseList.getAmountPaid() == null, "实付金额为空");
        AssertUtil.isTrue(purchaseList.getPurchaseDate() == null, "请选择进货日期");
        AssertUtil.isTrue(plgList == null || plgList.isEmpty(), "请选择商品");

        //保存进货单主表（MyBatis-Plus 自动回填 ID）
        AssertUtil.isTrue(!this.save(purchaseList), "进货单保存失败!");

        //保存商品明细 + 更新库存（核心逻辑）
        for (PurchaseListGoods plg : plgList) {
            //设置进货单ID
            plg.setPurchaseListId(purchaseList.getId());

            //更新商品库存（进货 = 库存增加）
            Goods goods = goodsService.getById(plg.getGoodsId());
            if (goods != null) {
                //原库存 + 进货数量 = 新库存
                int newQuantity = goods.getInventoryQuantity() + plg.getNum();
                goods.setInventoryQuantity(newQuantity);
                goods.setState(2);
                goods.setLastPurchasingPrice(plg.getPrice());
                AssertUtil.isTrue(!goodsService.updateById(goods), "商品库存更新失败!");
                
                System.out.println("商品ID: " + goods.getId() + 
                    ", 原库存: " + (goods.getInventoryQuantity() - plg.getNum()) + 
                    ", 进货: " + plg.getNum() + 
                    ", 新库存: " + goods.getInventoryQuantity());
            }

            //保存进货商品明细
            AssertUtil.isTrue(!purchaseListGoodsService.save(plg), "进货商品记录添加失败!");
        }
    }
}
