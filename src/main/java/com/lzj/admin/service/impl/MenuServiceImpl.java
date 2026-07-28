package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.mapper.MenuMapper;
import com.lzj.admin.pojo.Menu;
import com.lzj.admin.service.MenuService;
import com.lzj.admin.service.RoleMenuService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
	@Resource
    private MenuMapper menuMapper;

    @Override
    public List<Menu> queryAllMenus() {
        return menuMapper.queryAllMenus();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveMenu(Menu menu) {
        //参数校验
        AssertUtil.isTrue(StringUtil.isEmpty(menu.getName()), "菜单名称不能为空！");
        AssertUtil.isTrue(menu.getGrade() == null, "菜单层级不能为空！");
        AssertUtil.isTrue(menu.getpId() == null, "父级菜单不能为空！");

        //菜单名称不能重复
        Menu temp = menuMapper.findMenuByName(menu.getName());
        AssertUtil.isTrue(temp != null, "菜单名称已存在！");

        //设置默认值
        menu.setIsDel(0);
        if (menu.getState() == null) {
            menu.setState(0);
        }

        //保存
        AssertUtil.isTrue(!this.save(menu), "菜单添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateMenu(Menu menu) {
        //参数校验
        AssertUtil.isTrue(menu.getId() == null, "菜单ID不能为空！");
        AssertUtil.isTrue(StringUtil.isEmpty(menu.getName()), "菜单名称不能为空！");

        //检查是否存在
        Menu oldMenu = this.getById(menu.getId());
        AssertUtil.isTrue(oldMenu == null, "菜单不存在！");

        //菜单名称不能重复
        Menu temp = menuMapper.findMenuByNameExcludeSelf(menu.getName(), menu.getId());
        AssertUtil.isTrue(temp != null, "菜单名称已存在！");

        //更新
        AssertUtil.isTrue(!this.updateById(menu), "菜单更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteMenu(Integer id) {
        //参数校验
        AssertUtil.isTrue(id == null, "菜单ID不能为空！");

        //检查是否存在
        Menu menu = this.getById(id);
        AssertUtil.isTrue(menu == null, "菜单不存在！");
        Long childCount = menuMapper.countChildren(id);
        AssertUtil.isTrue(childCount > 0, "该菜单下还有子菜单，不能删除！");

        //逻辑删除
        menu.setIsDel(1);
        AssertUtil.isTrue(!this.updateById(menu), "菜单删除失败！");
    }
}

