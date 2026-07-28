package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
	/**
     * 查询所有未删除菜单
     */
    List<Menu> queryAllMenus();

    /**
     * 根据名称查询未删除菜单
     */
    Menu findMenuByName(@Param("name") String name);

    /**
     * 根据名称查询未删除菜单（排除自身ID）
     */
    Menu findMenuByNameExcludeSelf(@Param("name") String name, @Param("id") Integer id);

    /**
     * 统计子菜单数量
     */
    Long countChildren(@Param("pId") Integer pId);

    /**
     * 根据用户ID查询菜单权限
     */
    List<Menu> findMenusByUserId(@Param("userId") Integer userId);
}
