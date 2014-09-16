/**
 * There are <a href="https://github.com/jamieww/jsite">JSite</a> code generation
 */
package com.cyclone.jsite.modules.ifolder.entity;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cyclone.jsite.common.persistence.CDataEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 智慧夹Entity
 * @author Jame
 * @version 2014-09-05
 */
@Entity
@Table(name = "file",catalog="ifolder")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class File extends CDataEntity<File> {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
    private User user;      // 文件或目录的拥有者
    private String name; 	// 文件或目录名称
    private File parent;    //父级目录
    private Integer flag;       //文件标志（0：目录；1：文件）
    private Date modified;  //文件上次修改时间
    private String address; //文件的下载地址

    //文件标志（0：目录；1：文件）
    public static final Integer FILE_FLAG_NORMAL = 0;

	public File() {
		super();
	}

	public File(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="seqid")
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ifolder_file")
	//@SequenceGenerator(name = "seq_ifolder_file", sequenceName = "seq_ifolder_file")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	@Length(min=1, max=255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @NotNull
    public File getParent() {
        return parent;
    }

    public void setParent(File parent) {
        this.parent = parent;
    }

    @NotNull
    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
	
}


