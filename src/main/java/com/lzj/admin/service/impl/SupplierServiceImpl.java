package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.SupplierMapper;
import com.lzj.admin.pojo.Supplier;
import com.lzj.admin.query.SupplierQuery;
import com.lzj.admin.service.SupplierService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 供应商服务类
 * @author TianTian
 * @date 2022/1/19 14:43
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
	@Resource
    private SupplierMapper supplierMapper;

    @Override
    public Map<String, Object> supplierList(SupplierQuery supplierQuery) {
        IPage<Supplier> page = new Page<>(supplierQuery.getPage(), supplierQuery.getLimit());
        // 执行分页查询
        page = this.baseMapper.supplierPageList(page, supplierQuery);
        // 查询总条数
        Long total = this.baseMapper.countSupplier(supplierQuery);
        page.setTotal(total);
        return PageResultUtil.setResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveSupplier(Supplier supplier) {
        // 参数校验
        checkParams(supplier.getName(), supplier.getContact(), supplier.getNumber());
        AssertUtil.isTrue(null != this.findSupplierByName(supplier.getName()), "供应商已存在!");
        supplier.setIsDel(0);
        AssertUtil.isTrue(!(this.save(supplier)), "记录添加失败!");
    }

    /**
     * 参数校验
     */
    private void checkParams(String name, String contact, String number) {
        AssertUtil.isTrue(StringUtils.isBlank(name), "请输入供应商名称!");
        AssertUtil.isTrue(StringUtils.isBlank(contact), "请输入联系人!");
        AssertUtil.isTrue(StringUtils.isBlank(number), "请输入联系电话!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateSupplier(Supplier supplier) {
        AssertUtil.isTrue(null == this.getById(supplier.getId()), "请选择供应商记录!");
        checkParams(supplier.getName(), supplier.getContact(), supplier.getNumber());
        Supplier temp = this.findSupplierByName(supplier.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(supplier.getId())), "供应商已存在!");
        AssertUtil.isTrue(!(this.updateById(supplier)), "记录更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteSupplier(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除记录id");
        List<Supplier> supplierList = new ArrayList<>();
        for (Integer id : ids) {
            Supplier temp = this.getById(id);
            AssertUtil.isTrue(null == temp, "待删除供应商不存在");
            temp.setIsDel(1);
            supplierList.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(supplierList)), "记录删除失败!");
    }

    @Override
    public Supplier findSupplierByName(String name) {
        return this.baseMapper.findSupplierByName(name);
    }
}
