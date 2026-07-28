package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.mapper.CustomerMapper;
import com.lzj.admin.query.CustomerQuery;
import com.lzj.admin.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 客户表实现类
 * @author TianTian
 * @date 2022/1/19 14:50
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Map<String, Object> customerList(CustomerQuery customerQuery) {
        IPage<Customer> page = new Page<>(customerQuery.getPage(), customerQuery.getLimit());
        page = this.baseMapper.customerPageList(page, customerQuery);
        Long total = this.baseMapper.countCustomer(customerQuery);
        page.setTotal(total);
        return PageResultUtil.setResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveCustomer(Customer customer) {
        checkParams(customer.getName(), customer.getContact(), customer.getNumber());
        AssertUtil.isTrue(null != this.findCustomerByName(customer.getName()), "客户已存在!");
        customer.setIsDel(0);
        AssertUtil.isTrue(!(this.save(customer)), "记录添加失败!");
    }

    private void checkParams(String name, String contact, String number) {
        AssertUtil.isTrue(StringUtils.isBlank(name), "请输入客户名称!");
        AssertUtil.isTrue(StringUtils.isBlank(contact), "请输入联系人!");
        AssertUtil.isTrue(StringUtils.isBlank(number), "请输入联系电话!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateCustomer(Customer customer) {
        AssertUtil.isTrue(null == this.getById(customer.getId()), "请选择客户记录!");
        checkParams(customer.getName(), customer.getContact(), customer.getNumber());
        Customer temp = this.findCustomerByName(customer.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(customer.getId())), "客户已存在!");
        AssertUtil.isTrue(!(this.updateById(customer)), "记录更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteCustomer(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除记录id");
        List<Customer> customerList = new ArrayList<Customer>();
        for (Integer id : ids) {
            Customer temp = this.getById(id);
            AssertUtil.isTrue(null == temp, "待删除客户不存在");
            temp.setIsDel(1);
            customerList.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(customerList)), "记录删除失败!");
    }

    @Override
    public Customer findCustomerByName(String name) {
    	List<Customer> list = this.list(new QueryWrapper<Customer>()
                .eq("is_del", 0)
                .eq("name", name)
                .last("AND ROWNUM = 1"));
        return list.isEmpty() ? null : list.get(0);
    }
    
    @Override
    public List<Customer> findAllCustomers() {
        return customerMapper.findAllCustomers();
    }
}
