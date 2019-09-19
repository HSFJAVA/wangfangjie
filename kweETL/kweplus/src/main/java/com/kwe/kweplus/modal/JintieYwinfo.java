package com.kwe.kweplus.modal;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JintieYwinfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 业务编号
     */
    private String ywNo;

    /**
     * 客户业务编号
     */
    private String customNo;

    /**
     * 结算客户名称
     */
    private String clearingCustomerName;

    private String status;

    /**
     * 单证类型(0进仓1出仓)
     */
    private Integer type;

    /**
     * 接单备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 修改人
     */
    private String updateUser;

	/**
	 * 审核人
	 */
	private String auditUser;

	/**
	 * 审核日期
	 */
	private LocalDateTime auditTime;

    /**
     * 修改日期
     */
    private LocalDateTime updateDate;

    /**
     * 取走日期
     */
    private LocalDateTime taketime;

    private String bringUser;

    private String bringDate;

    private String remark1;

    private String remark2;

    private String remark3;

    private String remark4;

    private String remark5;

    private String remark6;

    private String remark7;

    private String remark8;

    private String remark9;

	private String remark10;

	@TableField(exist = false)
	private String templateName;
	@TableField(exist = false)
	private String modelName;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@TableField(exist = false)
    private String dateStr;

    private LocalDateTime createDateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getYwNo() {
		return ywNo;
	}

	public void setYwNo(String ywNo) {
		this.ywNo = ywNo;
	}

	public String getCustomNo() {
		return customNo;
	}

	public void setCustomNo(String customNo) {
		this.customNo = customNo;
	}

	public String getClearingCustomerName() {
		return clearingCustomerName;
	}

	public void setClearingCustomerName(String clearingCustomerName) {
		this.clearingCustomerName = clearingCustomerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getType() { return type; }

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public LocalDateTime getTaketime() {
		return taketime;
	}

	public void setTaketime(LocalDateTime taketime) {
		this.taketime = taketime;
	}

	public String getBringUser() {
		return bringUser;
	}

	public void setBringUser(String bringUser) {
		this.bringUser = bringUser;
	}

	public String getBringDate() {
		return bringDate;
	}

	public void setBringDate(String bringDate) {
		this.bringDate = bringDate;
	}

	public String getRemark1() {
		return remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getRemark4() {
		return remark4;
	}

	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}

	public String getRemark5() {
		return remark5;
	}

	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}

	public String getRemark6() {
		return remark6;
	}

	public void setRemark6(String remark6) {
		this.remark6 = remark6;
	}

	public String getRemark7() {
		return remark7;
	}

	public void setRemark7(String remark7) {
		this.remark7 = remark7;
	}

	public String getRemark8() {
		return remark8;
	}

	public void setRemark8(String remark8) {
		this.remark8 = remark8;
	}

	public String getRemark9() {
		return remark9;
	}

	public void setRemark9(String remark9) {
		this.remark9 = remark9;
	}

	public String getRemark10() {
		return remark10;
	}

	public void setRemark10(String remark10) {
		this.remark10 = remark10;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}


	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}

	public LocalDateTime getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(LocalDateTime auditTime) {
		this.auditTime = auditTime;
	}

	public String getCreateUserEmp() {
		return createUserEmp;
	}

	public void setCreateUserEmp(String createUserEmp) {
		this.createUserEmp = createUserEmp;
	}

	public String getUpdateUserEmp() {
		return updateUserEmp;
	}

	public void setUpdateUserEmp(String updateUserEmp) {
		this.updateUserEmp = updateUserEmp;
	}

	public String getAuditUserEmp() {
		return auditUserEmp;
	}

	public void setAuditUserEmp(String auditUserEmp) {
		this.auditUserEmp = auditUserEmp;
	}


	private String createUserEmp;


	private String updateUserEmp;


	private String auditUserEmp;

	@Override
	public String toString() {
		return "JintieYwinfo{" +
				"id=" + id +
				", ywNo='" + ywNo + '\'' +
				", customNo='" + customNo + '\'' +
				", clearingCustomerName='" + clearingCustomerName + '\'' +
				", status='" + status + '\'' +
				", type=" + type +
				", remark='" + remark + '\'' +
				", createUser='" + createUser + '\'' +
				", createDate='" + createDate + '\'' +
				", updateUser='" + updateUser + '\'' +
				", auditUser='" + auditUser + '\'' +
				", auditTime=" + auditTime +
				", updateDate=" + updateDate +
				", taketime=" + taketime +
				", bringUser='" + bringUser + '\'' +
				", bringDate='" + bringDate + '\'' +
				", remark1='" + remark1 + '\'' +
				", remark2='" + remark2 + '\'' +
				", remark3='" + remark3 + '\'' +
				", remark4='" + remark4 + '\'' +
				", remark5='" + remark5 + '\'' +
				", remark6='" + remark6 + '\'' +
				", remark7='" + remark7 + '\'' +
				", remark8='" + remark8 + '\'' +
				", remark9='" + remark9 + '\'' +
				", remark10='" + remark10 + '\'' +
				", dateStr='" + dateStr + '\'' +
				", createDateTime=" + createDateTime +
				", createUserEmp='" + createUserEmp + '\'' +
				", updateUserEmp='" + updateUserEmp + '\'' +
				", auditUserEmp='" + auditUserEmp + '\'' +
				'}';
	}
}
