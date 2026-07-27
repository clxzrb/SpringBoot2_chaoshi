package com.lzj.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Supplier;
import com.lzj.admin.query.SupplierQuery;
import com.lzj.admin.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

	@Resource
    private SupplierService supplierService;

    /**
     * 供应商管理主页
     */
    @RequestMapping("index")
    public String index() {
        return "supplier/supplier";
    }

    /**
     * 供应商列表查询
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> supplierList(SupplierQuery supplierQuery) {
        return supplierService.supplierList(supplierQuery);
    }

    /**
     * 添加/更新供应商页面
     */
    @RequestMapping("addOrUpdateSupplierPage")
    public String addOrUpdateSupplierPage(Integer id, Model model) {
        if (null != id) {
            model.addAttribute("supplier", supplierService.getById(id));
        }
        return "supplier/add_update";
    }

    /**
     * 保存供应商
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveSupplier(Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return RespBean.success("记录添加成功");
    }

    /**
     * 更新供应商
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateSupplier(Supplier supplier) {
        supplierService.updateSupplier(supplier);
        return RespBean.success("记录更新成功");
    }

    /**
     * 删除供应商
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteSupplier(Integer[] ids) {
        supplierService.deleteSupplier(ids);
        return RespBean.success("供应商记录删除成功");
    }

    /**
     * 查询所有供应商
     */
    @RequestMapping("allSuppliers")
    @ResponseBody
    public List<Supplier> allSuppliers() {
        return supplierService.list(new QueryWrapper<Supplier>().eq("is_del", 0));
    }
}
