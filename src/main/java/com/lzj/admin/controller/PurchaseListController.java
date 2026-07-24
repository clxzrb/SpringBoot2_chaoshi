package com.lzj.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.PurchaseList;
import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.query.PurchaseListQuery;
import com.lzj.admin.service.PurchaseListService;

import com.lzj.admin.service.UserService;
import com.lzj.admin.utils.AssertUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * 进货单控制器
 * @author TianTian
 * @date 2022/1/19 12:32
 */
@Controller
@RequestMapping("/purchase")
public class PurchaseListController {
	@GetMapping("/index")
    public String index(){
        // 返回ftl页面名称
        return "purchase/purchase";
    }
	
	@GetMapping("/searchPage")
    public String searchPage(){
        // 返回ftl页面名称
        return "purchase/purchase_search";
    }
	
	@Resource
    private PurchaseListService purchaseListService;
	/**
         * 分页列表数据接口
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> purchaseList(PurchaseListQuery purchaseListQuery){
        return purchaseListService.purchaseList(purchaseListQuery);
    }

    /**
         * 删除进货单
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id){
        purchaseListService.deletePurchaseList(id);
        return RespBean.success("删除成功");
    }
}
