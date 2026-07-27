package com.lzj.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.Supplier;
import com.lzj.admin.query.SupplierQuery;

import sun.print.BackgroundServiceLookup;

public interface SupplierMapper extends BaseMapper<Supplier>{
	/**
     * 分页查询供应商列表
     */
    IPage<Supplier> supplierPageList(IPage<Supplier> page, @Param("supplierQuery") SupplierQuery supplierQuery);

    /**
     * 统计供应商总数
     */
    Long countSupplier(@Param("supplierQuery") SupplierQuery supplierQuery);

    /**
     * 根据名称查询供应商
     */
    Supplier findSupplierByName(@Param("name") String name);
}
