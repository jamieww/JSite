package com.cyclone.jsite.common.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jame on 2014/9/5.
 */
@MappedSuperclass
public abstract class CDataEntity<T> extends BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    //protected String id;		// 编号
    protected User createBy;	// 创建者
    protected Date createDate;// 创建日期
    protected User updateBy;	// 更新者
    protected Date updateDate;// 更新日期

    protected Date createDateStart;
    protected Date createDateEnd;
    protected Date updateDateStart;
    protected Date updateDateEnd;

    public CDataEntity() {
        super();
    }

    @PrePersist
    public void prePersist(){
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())){
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
        //this.id = IdGen.uuid();
    }

    @PreUpdate
    public void preUpdate(){
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId())){
            this.updateBy = user;
        }
        this.updateDate = new Date();
    }

//    @Id
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    @JsonIgnore
    @ManyToOne(fetch= FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    public User getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(User updateBy) {
        this.updateBy = updateBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(index= Index.YES, analyze= Analyze.NO, store= Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Temporal(TemporalType.DATE)
    @Transient
    public Date getCreateDateStart() {
        return DateUtils.getDateStart(createDateStart);
    }

    public void setCreateDateStart(Date createDateStart) {
        this.createDateStart = createDateStart;
    }

    @Temporal(TemporalType.DATE)
    @Transient
    public Date getCreateDateEnd() {
        return DateUtils.getDateEnd(createDateEnd);
    }

    public void setCreateDateEnd(Date createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    @Temporal(TemporalType.DATE)
    @Transient
    public Date getUpdateDateStart() {
        return DateUtils.getDateStart(updateDateStart);
    }

    public void setUpdateDateStart(Date updateDateStart) {
        this.updateDateStart = updateDateStart;
    }

    @Temporal(TemporalType.DATE)
    @Transient
    public Date getUpdateDateEnd() {
        return DateUtils.getDateEnd(updateDateEnd);
    }

    public void setUpdateDateEnd(Date updateDateEnd) {
        this.updateDateEnd = updateDateEnd;
    }
}
