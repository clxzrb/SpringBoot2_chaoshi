package com.lzj.admin.controller;


import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.query.PurchaseListGoodsQuery;
import com.lzj.admin.query.PurchaseListQuery;
import com.lzj.admin.service.PurchaseListGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * 进货单商品表
 * @author TianTian
 * @date 2022/1/19 12:32
 */
@Controller
@RequestMapping("/purchaseListGoods")
public class PurchaseListGoodsController {
	@Resource
	private PurchaseListGoodsService purchaseListGoodsService;

	/**
	 * 加载进货单商品明细
	 */
	@RequestMapping("list")
	@ResponseBody
    public Map<String,Object> purchaseListGoodsList(PurchaseListGoodsQuery purchaseListGoodsQuery){
        return purchaseListGoodsService.purchaseListGoodsList(purchaseListGoodsQuery);
    }
}
