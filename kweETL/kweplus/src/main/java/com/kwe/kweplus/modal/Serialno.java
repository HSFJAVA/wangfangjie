package com.kwe.kweplus.modal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2019-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Serialno implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号名称
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("serialNO_name")
    private String serialnoName;

    @TableField("currNum")
    private String currNum;

    @TableField("currMonth")
    private String currMonth;

    @TableField("currYear")
    private String currYear;

    @TableField("currDay")
    private String currDay;

    @TableField("serialNO_length")
    private Integer serialnoLength;

    @TableField("serialNO_type")
    private String serialnoType;

    @TableField("serialNO_rule")
    private String serialnoRule;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerialnoName() {
		return serialnoName;
	}

	public void setSerialnoName(String serialnoName) {
		this.serialnoName = serialnoName;
	}

	public String getCurrNum() {
		return currNum;
	}

	public void setCurrNum(String currNum) {
		this.currNum = currNum;
	}

	public String getCurrMonth() {
		return currMonth;
	}

	public void setCurrMonth(String currMonth) {
		this.currMonth = currMonth;
	}

	public String getCurrYear() {
		return currYear;
	}

	public void setCurrYear(String currYear) {
		this.currYear = currYear;
	}

	public String getCurrDay() {
		return currDay;
	}

	public void setCurrDay(String currDay) {
		this.currDay = currDay;
	}

	public Integer getSerialnoLength() {
		return serialnoLength;
	}

	public void setSerialnoLength(Integer serialnoLength) {
		this.serialnoLength = serialnoLength;
	}

	public String getSerialnoType() {
		return serialnoType;
	}

	public void setSerialnoType(String serialnoType) {
		this.serialnoType = serialnoType;
	}

	public String getSerialnoRule() {
		return serialnoRule;
	}

	public void setSerialnoRule(String serialnoRule) {
		this.serialnoRule = serialnoRule;
	}


}
