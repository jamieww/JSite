/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.cyclone.jsite.modules.ifolder.service;

import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.cyclone.jsite.modules.ifolder.entity.File;
import com.cyclone.jsite.modules.ifolder.dao.FileDao;

import java.util.List;

/**
 * 智慧夹Service
 * @author Jame
 * @version 2014-09-05
 */
@Component
@Transactional(readOnly = true)
public class FileService extends BaseService {

    public static final String CACHE_FILE = "file";

	@Autowired
	private FileDao fileDao;
	
	public File get(String id) {
		return fileDao.get(id);
	}
	
	public Page<File> find(Page<File> page, File file) {
		DetachedCriteria dc = fileDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(file.getName())){
			dc.add(Restrictions.like("name", "%"+file.getName()+"%"));
		}
		//dc.add(Restrictions.eq(File.FIELD_DEL_FLAG, File.DEL_FLAG_NORMAL));
		dc.addOrder(Order.desc("id"));
		return fileDao.find(page, dc);
	}

    public List<File> findDirectory() {
        @SuppressWarnings("unchecked")
        List<File> list = (List <File>) UserUtils.getCache(CACHE_FILE);

        if (list == null){
            User user = UserUtils.getUser();
            DetachedCriteria dc = fileDao.createDetachedCriteria();
            dc.add(Restrictions.eq("user",user));
            dc.add(Restrictions.eq("flag",File.FILE_FLAG_NORMAL));
            list = fileDao.find(dc);

            UserUtils.putCache(CACHE_FILE,list);
        }
        return list;
    }
	
	@Transactional(readOnly = false)
	public void save(File file) {
		file.setUser(UserUtils.getUser());
        //创建一级目录
        File parent = file.getParent();
        if(parent.getId() == null) {
            parent.setId(0L);
        }
        fileDao.save(file);
        UserUtils.removeCache(CACHE_FILE);
	}
	
	@Transactional(readOnly = false)
	public void delete(String id) {
        File file = get(id);
        if(file != null) {
            fileDao.deleteById(id);
            UserUtils.removeCache(CACHE_FILE);
        }
	}
	
}
