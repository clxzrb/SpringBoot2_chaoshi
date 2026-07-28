package com.lzj.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Menu;
import com.lzj.admin.utils.PageResultUtil;
import io.swagger.models.auth.In;

import javax.lang.model.type.IntersectionType;
import java.util.List;
import java.util.Map;
/**
 * 菜单表服务类
 * @author TianTian
 * @date 2022/1/19 13:57
 */
public interface MenuService extends IService<Menu> {
	/**
     * 查询所有菜单（树形结构）
     */
    List<Menu> queryAllMenus();

    /**
     * 保存菜单
     */
    void saveMenu(Menu menu);

    /**
     * 更新菜单
     */
    void updateMenu(Menu menu);

    /**
     * 删除菜单
     */
    void deleteMenu(Integer id);
}
