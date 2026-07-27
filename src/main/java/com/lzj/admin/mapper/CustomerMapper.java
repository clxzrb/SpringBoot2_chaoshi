package com.lzj.admin.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.query.CustomerQuery;

public interface CustomerMapper extends BaseMapper<Customer> {
	IPage<Customer> customerPageList(IPage<Customer> page, @Param("customerQuery") CustomerQuery customerQuery);
	Long countCustomer(@Param("customerQuery") CustomerQuery customerQuery);
	Customer findCustomerByName(@Param("name") String name);
}
