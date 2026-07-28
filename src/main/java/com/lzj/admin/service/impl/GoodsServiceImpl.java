package com.lzj.admin.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.mapper.GoodsMapper;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.GoodsType;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.GoodsService;
import com.lzj.admin.service.GoodsTypeService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
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
    @Resource
    private GoodsTypeService goodsTypeService;

    @Override
    public Map<String, Object> goodsList(GoodsQuery goodsQuery) {
        IPage<Goods> page = new Page<>(goodsQuery.getPage(), goodsQuery.getLimit());
        page = this.baseMapper.goodsList(page, goodsQuery);
        return PageResultUtil.setResult(page.getTotal(), page.getRecords());
    }

    /**
     * 根据商品名称查询未删除商品
     */
    @Override
    public Goods findGoodsByName(String name) {
        List<Goods> goodsList = this.list(new QueryWrapper<Goods>()
                .eq("is_del", 0)
                .eq("name", name)
                .last("AND ROWNUM = 1"));
        return goodsList.isEmpty() ? null : goodsList.get(0);
    }
    
    /**
     * 根据商品编码查询未删除商品
     */
    @Override
    public Goods findGoodsByCode(String code) {
        List<Goods> goodsList = this.list(new QueryWrapper<Goods>()
                .eq("is_del", 0)
                .eq("code", code)
                .last("AND ROWNUM = 1"));
        return goodsList.isEmpty() ? null : goodsList.get(0);
    }

    /**
     * 新增商品
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveGoods(Goods goods) {
        checkParams(goods.getTypeId(), goods.getName(), goods.getSellingPrice(), goods.getPurchasingPrice());
        // 商品名称唯一校验
        AssertUtil.isTrue(null != this.findGoodsByName(goods.getName()), "该商品名称已存在!");
        // 初始化默认值
        goods.setIsDel(0);
        goods.setState(0);
        goods.setLastPurchasingPrice(goods.getPurchasingPrice());
        goods.setInventoryQuantity(0);
        AssertUtil.isTrue(!this.save(goods), "商品添加失败!");
    }

    /**
     * 更新商品
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateGoods(Goods goods) {
        AssertUtil.isTrue(null == this.getById(goods.getId()), "请选择商品记录!");
        checkParams(goods.getTypeId(), goods.getName(), goods.getSellingPrice(), goods.getPurchasingPrice());
        // 重名校验：排除自身ID
        Goods temp = this.findGoodsByName(goods.getName());
        AssertUtil.isTrue(null != temp && !temp.getId().equals(goods.getId()), "该商品名称已存在!");
        // 保留原有库存、上次采购价
        Goods oldGoods = this.getById(goods.getId());
        goods.setLastPurchasingPrice(oldGoods.getPurchasingPrice());
        goods.setInventoryQuantity(oldGoods.getInventoryQuantity());
        AssertUtil.isTrue(!this.updateById(goods), "商品更新失败!");
    }
    
    /**
     * 逻辑删除商品
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteGoods(Integer id) {
        // 参数校验id不能为空
        AssertUtil.isTrue(null == id, "请选择待删除记录id");
        Goods temp = this.getById(id);
        AssertUtil.isTrue(null == temp, "商品不存在！");
        // 逻辑删除
        temp.setIsDel(1);
        AssertUtil.isTrue(!this.updateById(temp), "商品删除失败!");
    }

    /**
     * 统一参数校验（参照Supplier抽取私有方法）
     */
    private void checkParams(Integer typeId, String goodsName, Float sellingPrice, Float purchasingPrice) {
        AssertUtil.isTrue(null == typeId, "商品类别不能为空!");
        AssertUtil.isTrue(StringUtil.isEmpty(goodsName), "请输入商品名称!");
        AssertUtil.isTrue(null == sellingPrice || sellingPrice <= 0, "售价必须大于0!");
        AssertUtil.isTrue(null == purchasingPrice || purchasingPrice <= 0, "采购价必须大于0!");
    }
    
    
    
    
    /**
     * 期初库存更新商品库存、采购成本
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateStock(Goods goods) {
        // 1.参数校验
    	AssertUtil.isTrue(null == goods.getId(), "商品ID不能为空！");
        AssertUtil.isTrue(null == goods.getInventoryQuantity() || goods.getInventoryQuantity() < 0, "库存数量不能为负数！");
        AssertUtil.isTrue(null == goods.getPurchasingPrice() || goods.getPurchasingPrice() <= 0, "成本价必须大于0！");

        // 2.查询原商品
        Goods oldGoods = this.getById(goods.getId());
        AssertUtil.isTrue(null == oldGoods, "商品不存在！");

        // 3.更新库存、采购价，同步上次采购价，状态改为已入库1
        oldGoods.setInventoryQuantity(goods.getInventoryQuantity());
        oldGoods.setPurchasingPrice(goods.getPurchasingPrice());
        oldGoods.setLastPurchasingPrice(goods.getPurchasingPrice());
        oldGoods.setState(1); // 期初库存录入完成
        AssertUtil.isTrue(!this.updateById(oldGoods), "期初库存更新失败！");
    }

    /**
     * 期初库存页面删除商品（逻辑删除）
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteStock(Integer id) {
        AssertUtil.isTrue(null == id, "商品ID不能为空");
        Goods goods = this.getById(id);
        AssertUtil.isTrue(null == goods, "待删除商品不存在");
        goods.setIsDel(1);
        AssertUtil.isTrue(!this.updateById(goods), "期初商品删除失败");
    }
    
 // ✅ 新增：查询所有商品类别（树状图）
    @Override
    public List<TreeDto> queryAllGoodsTypes(Integer typeId) {
        // 查询所有商品类别数据
        QueryWrapper<GoodsType> queryWrapper = new QueryWrapper<>();
        List<GoodsType> goodsTypeList = this.goodsTypeService.list(queryWrapper);

        // 创建TreeDto集合
        List<TreeDto> treeDtoList = new ArrayList<>();

        // 将数据库实体GoodsType转换为前端需要的TreeDto
        for (GoodsType goodsType : goodsTypeList) {
            TreeDto treeDto = new TreeDto();
            treeDto.setId(goodsType.getId());
            treeDto.setpId(goodsType.getpId());
            treeDto.setName(goodsType.getName());
            treeDtoList.add(treeDto);
        }

        return treeDtoList;
    }
}