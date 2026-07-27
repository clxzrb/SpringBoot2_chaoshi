package com.lzj.admin.controller;


import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.GoodsType;
import com.lzj.admin.service.GoodsTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author TianTian
 * @date 2022/1/19 8:36
 */
@Controller
@RequestMapping("/goodsType")
public class GoodsTypeController {
	@Resource
    private GoodsTypeService goodsTypeService;

	@GetMapping("/index")
    public String index(){
        // 返回ftl页面名称
        return "goods/goods_type";
    }

    /**
     * 查询所有商品类别zTree树形数据，返回Map数组
     */
    @PostMapping("/queryAllGoodsTypes")
    @ResponseBody
    public List<Map<String,Object>> queryAllGoodsTypes(){
        return goodsTypeService.queryAllGoodsTypes();
    }
}
