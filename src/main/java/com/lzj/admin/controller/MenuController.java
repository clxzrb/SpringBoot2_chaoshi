package com.lzj.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Menu;
import com.lzj.admin.service.MenuService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 菜单控制器
 * @author TianTian
 * @date 2022/1/14 15:40
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
	@Resource
    private MenuService menuService;

    /**
     * 菜单管理主页
     */
    @RequestMapping("index")
    public String index() {
        return "menu/menu";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<Menu> menuList = menuService.queryAllMenus();
        result.put("code", 0);
        result.put("msg", "");
        result.put("data", menuList);
        return result;
    }

    /**
     * 添加菜单页面
     */
    @RequestMapping("addMenuPage")
    public String addMenuPage(Integer grade, Integer pId, Model model) {
        model.addAttribute("grade", grade);
        model.addAttribute("pId", pId);
        return "menu/add_update";
    }

    /**
     * 更新菜单页面
     */
    @RequestMapping("updateMenuPage")
    public String updateMenuPage(Integer id, Model model) {
        model.addAttribute("menu", menuService.getById(id));
        return "menu/add_update";
    }

    /**
     * 保存菜单
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveMenu(Menu menu) {
        menuService.saveMenu(menu);
        return RespBean.success("菜单添加成功！");
    }

    /**
     * 更新菜单
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateMenu(Menu menu) {
        menuService.updateMenu(menu);
        return RespBean.success("菜单更新成功！");
    }

    /**
     * 删除菜单
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteMenu(Integer id) {
        menuService.deleteMenu(id);
        return RespBean.success("菜单删除成功！");
    }
}
