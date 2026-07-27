package com.lzj.admin.controller;


import com.lzj.admin.pojo.GoodsUnit;
import com.lzj.admin.service.GoodsUnitService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author TianTian
 * @date 2022/1/19 8:54
 */
@Controller
@RequestMapping("/goodsUnit")
public class GoodsUnitController {
	@GetMapping("/index")
    public String index(){
        // 返回ftl页面名称
        return "goods/goods_type";
    }
	@Resource
    private GoodsUnitService goodsUnitService;
	
	@PostMapping("/allGoodsUnits")
    @ResponseBody
    public List<GoodsUnit> allGoodsUnits(){
        return goodsUnitService.list();
    }

}
