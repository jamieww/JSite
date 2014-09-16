/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.cyclone.jsite.modules.ifolder.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.cyclone.jsite.modules.ifolder.entity.File;
import com.cyclone.jsite.modules.ifolder.service.FileService;

import java.util.List;
import java.util.Map;

/**
 * 智慧夹Controller
 * @author Jame
 * @version 2014-09-05
 */
@Controller
@RequestMapping(value = "${adminPath}/ifolder/file")
public class FileController extends BaseController {

	@Autowired
	private FileService fileService;
	
	@ModelAttribute
	public File get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return fileService.get(id);
		}else{
			return new File();
		}
	}
	
	@RequiresPermissions("ifolder:file:view")
	@RequestMapping(value = {"list", ""})
	public String list(File file, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			file.setCreateBy(user);
		}
        file.setUser(user);
        Page<File> page = fileService.find(new Page<File>(request, response), file); 
        model.addAttribute("page", page);
		return "modules/ifolder/fileList";
	}

	@RequiresPermissions("ifolder:file:view")
	@RequestMapping(value = "form")
	public String form(File file, Model model) {
		model.addAttribute("file", file);
		return "modules/ifolder/fileForm";
	}

	@RequiresPermissions("ifolder:file:edit")
	@RequestMapping(value = "save")
	public String save(File file, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, file)){
			return form(file, model);
		}
		fileService.save(file);
		addMessage(redirectAttributes, "保存智慧夹'" + file.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/ifolder/file/?repage";
	}
	
	@RequiresPermissions("ifolder:file:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, RedirectAttributes redirectAttributes) {
		fileService.delete(id);
		addMessage(redirectAttributes, "删除智慧夹成功");
		return "redirect:"+Global.getAdminPath()+"/ifolder/file/?repage";
	}

    @RequiresUser
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<File> list = fileService.findDirectory();

        for(File e : list){
            if (extId == null || !extId.equals(e.getId()) ){
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParent()!=null?e.getParent().getId():0);
                map.put("name", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }

}
