package com.kwe.kweplus.modal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 表一
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JintieOutputSerial implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pid", type = IdType.AUTO)
    private Long pid;

    @TableField("MESSAGEHEAD")
    private String messagehead;

    @TableField("MESSAGETYPE")
    private String messagetype;

    @TableField("MESSAGEDATE")
    private String messagedate;

    @TableField("MESSAGETIME")
    private String messagetime;

    @TableField("UPDATEFLAG")
    private String updateflag;

    @TableField("DOCNO")
    private String docno;

    @TableField("STOREID")
    private String storeid;

    @TableField("SKU")
    private String sku;

    @TableField("SERIALNO")
    private String serialno;

    @TableField("SCANTIME")
    private LocalDateTime scantime;

    /**
     * 数据状态(-1待审核,0已审核,1已取)
     */
    private Integer status;

    /**
     * 取走日期
     */
    private LocalDateTime taketime;

    @TableField("wl_ywno")
    private String wlYwno;

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getMessagehead() {
		return messagehead;
	}

	public void setMessagehead(String messagehead) {
		this.messagehead = messagehead;
	}

	public String getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}

	public String getMessagedate() {
		return messagedate;
	}

	public void setMessagedate(String messagedate) {
		this.messagedate = messagedate;
	}

	public String getMessagetime() {
		return messagetime;
	}

	public void setMessagetime(String messagetime) {
		this.messagetime = messagetime;
	}

	public String getUpdateflag() {
		return updateflag;
	}

	public void setUpdateflag(String updateflag) {
		this.updateflag = updateflag;
	}

	public String getDocno() {
		return docno;
	}

	public void setDocno(String docno) {
		this.docno = docno;
	}

	public String getStoreid() {
		return storeid;
	}

	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public LocalDateTime getScantime() {
		return scantime;
	}

	public void setScantime(LocalDateTime scantime) {
		this.scantime = scantime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDateTime getTaketime() {
		return taketime;
	}

	public void setTaketime(LocalDateTime taketime) {
		this.taketime = taketime;
	}

	public String getWlYwno() {
		return wlYwno;
	}

	public void setWlYwno(String wlYwno) {
		this.wlYwno = wlYwno;
	}
    
    
}
