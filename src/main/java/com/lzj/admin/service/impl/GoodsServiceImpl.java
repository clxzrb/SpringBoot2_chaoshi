package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.mapper.GoodsMapper;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.CustomerReturnListGoodsService;
import com.lzj.admin.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.service.GoodsTypeService;
import com.lzj.admin.service.SaleListGoodsService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商品表实现类
 * @author TianTian
 * @date 2022/1/19 14:51
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
	@Resource
    private GoodsMapper goodsMapper;

    @Override
    public Map<String, Object> goodsList(GoodsQuery goodsQuery) {
        // 分页对象 page/limit 取自父类BaseQuery，和进货单保持一致
        IPage<Goods> page = new Page<>(goodsQuery.getPage(), goodsQuery.getLimit());
        page = this.baseMapper.goodsList(page, goodsQuery);
        return PageResultUtil.setResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveGoods(Goods goods) {
        // 参数校验
        AssertUtil.isTrue(null == goods.getTypeId(), "商品类别不能为空");
        AssertUtil.isTrue(null == goods.getSellingPrice() || goods.getSellingPrice() <= 0, "售价必须大于0");
        AssertUtil.isTrue(null == goods.getPurchasingPrice() || goods.getPurchasingPrice() <= 0, "采购价必须大于0");

        if (null == goods.getId()) {
            goods.setIsDel(0);
            goods.setState(0);
            goods.setLastPurchasingPrice(goods.getPurchasingPrice());
            goods.setInventoryQuantity(0);
            this.save(goods);
        } else {
            //修改商品
            Goods oldGoods = this.getById(goods.getId());
            AssertUtil.isTrue(null == oldGoods, "商品不存在！");
            goods.setLastPurchasingPrice(oldGoods.getPurchasingPrice());
            goods.setInventoryQuantity(oldGoods.getInventoryQuantity());
            this.updateById(goods);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteGoods(Integer id) {
        AssertUtil.isTrue(null == id, "商品ID不能为空");
        Goods goods = this.getById(id);
        AssertUtil.isTrue(null == goods, "待删除商品不存在");
        // 逻辑删除，修改is_del=1
        goods.setIsDel(1);
        AssertUtil.isTrue(!this.updateById(goods), "商品删除失败");
    }

}
