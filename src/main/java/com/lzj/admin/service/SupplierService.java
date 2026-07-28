package com.lzj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.Supplier;
import com.lzj.admin.query.SupplierQuery;

import java.util.List;
import java.util.Map;

/**
 * 供应商服务类
 * @author TianTian
 * @date 2022/1/19 13:59
 */
public interface SupplierService extends IService<Supplier> {
	/**
     * 供应商列表查询（分页）
     */
    Map<String, Object> supplierList(SupplierQuery supplierQuery);

    /**
     * 保存供应商
     */
    void saveSupplier(Supplier supplier);

    /**
     * 更新供应商
     */
    void updateSupplier(Supplier supplier);

    /**
     * 删除供应商
     */
    void deleteSupplier(Integer[] ids);

    /**
     * 根据名称查询供应商
     */
    Supplier findSupplierByName(String name);
    
    List<Supplier> listAllSuppliers();
}
