package com.lzj.admin.controller;


import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.GoodsType;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.GoodsService;
import com.lzj.admin.service.GoodsTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * 商品控制器
 * @author TianTian
 * @date 2022/1/18 22:50
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
	@GetMapping("/index")
    public String index(){
        // 返回ftl页面名称
        return "goods/goods";
    }
	@GetMapping("/addOrUpdateGoodsPage")
	public String addOrUpdateGoodsPage(Integer id, Model model){
	    if(null != id){
	        Goods goods = goodsService.getById(id);
	        model.addAttribute("goods", goods);
	        if(goods.getTypeId() != null){
	            GoodsType goodsType = goodsTypeService.getById(goods.getTypeId());
	            model.addAttribute("goodsType", goodsType);
	        }
	    }
	    return "goods/add_update";
	}
	@GetMapping("/toGoodsTypePage")
	public String toGoodsTypePage(Integer typeId, Model model){
	    model.addAttribute("typeId", typeId);
	    return "goods/goods_type";
	}
	
	@Resource
    private GoodsService goodsService;
	
	@Resource
    private GoodsTypeService goodsTypeService;

    /**
     * 分页列表数据接口
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> goodsList(GoodsQuery goodsQuery){
        return goodsService.goodsList(goodsQuery);
    }

    /**
     * 新增商品提交
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(Goods goods){
        goodsService.saveGoods(goods);
        return RespBean.success("操作成功");
    }
    
    /** 编辑更新商品接口 */
    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Goods goods){
        goodsService.updateGoods(goods);
        return RespBean.success("修改成功");
    }

    /**
     * 批量删除商品
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteSupplier(Integer id){
        goodsService.deleteGoods(id);
        return RespBean.success("商品记录删除成功");
    }
    
    //查询所有商品类别（树状图）
    @RequestMapping("queryAllGoodsTypes")
    @ResponseBody
    public List<TreeDto> queryAllGoodsTypes() {
        return goodsService.queryAllGoodsTypes(null);
    }
}
