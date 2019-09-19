package com.kwe.kweplus.modal;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class JintieOutputDetail implements Serializable {

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

    @TableField("DOCLINENO")
    private String doclineno;

    @TableField("CUSTOMERLINENO")
    private String customerlineno;

    @TableField("STOREID")
    private String storeid;

    @TableField("SKU")
    private String sku;

    @TableField("MANUFACTURERSKU")
    private String manufacturersku;

    @TableField("BRAND")
    private String brand;

    @TableField("MODEL")
    private String model;

    @TableField("HSCODE")
    private String hscode;

    @TableField("CHINESENAME")
    private String chinesename;

    @TableField("SPECIFICATIONS")
    private String specifications;

    @TableField("DECLARATIONELEMENT")
    private String declarationelement;

    @TableField("QTYORDERED")
    private Integer qtyordered;

    @TableField("LOTTABLE01")
    private String lottable01;

    @TableField("LOTTABLE02")
    private String lottable02;

    @TableField("LOTTABLE03")
    private String lottable03;

    @TableField("LOTTABLE04")
    private String lottable04;

    @TableField("LOTTABLE05")
    private String lottable05;

    @TableField("LOTTABLE06")
    private String lottable06;

    @TableField("LOTTABLE07")
    private String lottable07;

    @TableField("LOTTABLE08")
    private String lottable08;

    @TableField("LOTTABLE09")
    private String lottable09;

    @TableField("LOTTABLE10")
    private String lottable10;

    @TableField("LOTTABLE11")
    private String lottable11;

    @TableField("LOTTABLE12")
    private String lottable12;

    @TableField("VOLUMN")
    private BigDecimal volumn;

    @TableField("VOLUMNUOM")
    private String volumnuom;

    @TableField("CONTAINERNUM")
    private String containernum;

    @TableField("SEALNUM")
    private String sealnum;

    @TableField("SPECINSTRTXT")
    private String specinstrtxt;

    @TableField("HAZARDMATERIALTXT")
    private String hazardmaterialtxt;

    @TableField("DAMAGEIND")
    private String damageind;

    @TableField("DAMAGECD")
    private String damagecd;

    @TableField("OPENQTY")
    private Integer openqty;

    @TableField("ID")
    private String id;

    @TableField("DECLARATIONUNIT")
    private String declarationunit;

    @TableField("COUNTRYOFORIGIN")
    private String countryoforigin;

    @TableField("CURRENCY")
    private String currency;

    @TableField("STATUTORYUNIT1")
    private String statutoryunit1;

    @TableField("STATUTORYUNIT2")
    private String statutoryunit2;

    @TableField("STATUTORYUNIT2QTY")
    private Integer statutoryunit2qty;

    @TableField("CUBIC")
    private BigDecimal cubic;

    @TableField("TOTALCUBIC")
    private BigDecimal totalcubic;

    @TableField("GROSSWEIGHT")
    private BigDecimal grossweight;

    @TableField("TOTALGROSSWEIGHT")
    private BigDecimal totalgrossweight;

    @TableField("NETWEIGHT")
    private BigDecimal netweight;

    @TableField("TOTALNETWEIGHT")
    private BigDecimal totalnetweight;

    @TableField("PRICE")
    private BigDecimal price;

    @TableField("TOTALPRICE")
    private BigDecimal totalprice;

    /**
     * 数据状态(-1待审核,0已审核,1已取)
     */
    private Integer status;

    /**
     * 取走日期
     */
    private LocalDateTime taketime;

    @TableField(exist = false)
    private List<Map> remark1 = new ArrayList<>();

	@TableField(exist = false)
	private Map remark;

	public Map getRemark() {
		return remark;
	}

	public void setRemark(Map remark) {
		this.remark = remark;
	}

    /**
     * DN号
     */
    @TableField("SOREFERENCE2")
    private String soreference2;


    @TableField("wl_ywno")
    private String wlYwno;

    /**
     * DN号
     */
    @TableField("DNCODE")
    private String dnCode;

	/**
	 * 模糊标记
	 */
	@TableField("dimflag")
	private String dimFlag;


	public String getDimFlag() {
		return dimFlag;
	}


	public void setDimFlag(String dimFlag) {
		this.dimFlag = dimFlag;
	}


	/**
	 * 区分是否分行
	 */
	@TableField("COPYLINENO")

	private String copyLineNo;

	public String getCopyLineNo() {
		return copyLineNo;
	}

	public void setCopyLineNo(String copyLoneNo) {
		this.copyLineNo = copyLoneNo;
	}

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

	public String getDoclineno() {
		return doclineno;
	}

	public void setDoclineno(String doclineno) {
		this.doclineno = doclineno;
	}

	public String getCustomerlineno() {
		return customerlineno;
	}

	public void setCustomerlineno(String customerlineno) {
		this.customerlineno = customerlineno;
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

	public String getManufacturersku() {
		return manufacturersku;
	}

	public void setManufacturersku(String manufacturersku) {
		this.manufacturersku = manufacturersku;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}

	public String getChinesename() {
		return chinesename;
	}

	public void setChinesename(String chinesename) {
		this.chinesename = chinesename;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getDeclarationelement() {
		return declarationelement;
	}

	public void setDeclarationelement(String declarationelement) {
		this.declarationelement = declarationelement;
	}

	public Integer getQtyordered() {
		return qtyordered;
	}

	public void setQtyordered(Integer qtyordered) {
		this.qtyordered = qtyordered;
	}

	public String getLottable01() {
		return lottable01;
	}

	public void setLottable01(String lottable01) {
		this.lottable01 = lottable01;
	}

	public String getLottable02() {
		return lottable02;
	}

	public void setLottable02(String lottable02) {
		this.lottable02 = lottable02;
	}

	public String getLottable03() {
		return lottable03;
	}

	public void setLottable03(String lottable03) {
		this.lottable03 = lottable03;
	}

	public String getLottable04() {
		return lottable04;
	}

	public void setLottable04(String lottable04) {
		this.lottable04 = lottable04;
	}

	public String getLottable05() {
		return lottable05;
	}

	public void setLottable05(String lottable05) {
		this.lottable05 = lottable05;
	}

	public String getLottable06() {
		return lottable06;
	}

	public void setLottable06(String lottable06) {
		this.lottable06 = lottable06;
	}

	public String getLottable07() {
		return lottable07;
	}

	public void setLottable07(String lottable07) {
		this.lottable07 = lottable07;
	}

	public String getLottable08() {
		return lottable08;
	}

	public void setLottable08(String lottable08) {
		this.lottable08 = lottable08;
	}

	public String getLottable09() {
		return lottable09;
	}

	public void setLottable09(String lottable09) {
		this.lottable09 = lottable09;
	}

	public String getLottable10() {
		return lottable10;
	}

	public void setLottable10(String lottable10) {
		this.lottable10 = lottable10;
	}

	public String getLottable11() {
		return lottable11;
	}

	public void setLottable11(String lottable11) {
		this.lottable11 = lottable11;
	}

	public String getLottable12() {
		return lottable12;
	}

	public void setLottable12(String lottable12) {
		this.lottable12 = lottable12;
	}

	public BigDecimal getVolumn() {
		return volumn;
	}

	public void setVolumn(BigDecimal volumn) {
		this.volumn = volumn;
	}

	public String getVolumnuom() {
		return volumnuom;
	}

	public void setVolumnuom(String volumnuom) {
		this.volumnuom = volumnuom;
	}

	public String getContainernum() {
		return containernum;
	}

	public void setContainernum(String containernum) {
		this.containernum = containernum;
	}

	public String getSealnum() {
		return sealnum;
	}

	public void setSealnum(String sealnum) {
		this.sealnum = sealnum;
	}

	public String getSpecinstrtxt() {
		return specinstrtxt;
	}

	public void setSpecinstrtxt(String specinstrtxt) {
		this.specinstrtxt = specinstrtxt;
	}

	public String getHazardmaterialtxt() {
		return hazardmaterialtxt;
	}

	public void setHazardmaterialtxt(String hazardmaterialtxt) {
		this.hazardmaterialtxt = hazardmaterialtxt;
	}

	public String getDamageind() {
		return damageind;
	}

	public void setDamageind(String damageind) {
		this.damageind = damageind;
	}

	public String getDamagecd() {
		return damagecd;
	}

	public void setDamagecd(String damagecd) {
		this.damagecd = damagecd;
	}

	public Integer getOpenqty() {
		return openqty;
	}

	public void setOpenqty(Integer openqty) {
		this.openqty = openqty;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeclarationunit() {
		return declarationunit;
	}

	public void setDeclarationunit(String declarationunit) {
		this.declarationunit = declarationunit;
	}

	public String getCountryoforigin() {
		return countryoforigin;
	}

	public void setCountryoforigin(String countryoforigin) {
		this.countryoforigin = countryoforigin;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getStatutoryunit1() {
		return statutoryunit1;
	}

	public void setStatutoryunit1(String statutoryunit1) {
		this.statutoryunit1 = statutoryunit1;
	}

	public String getStatutoryunit2() {
		return statutoryunit2;
	}

	public void setStatutoryunit2(String statutoryunit2) {
		this.statutoryunit2 = statutoryunit2;
	}

	public Integer getStatutoryunit2qty() {
		return statutoryunit2qty;
	}

	public void setStatutoryunit2qty(Integer statutoryunit2qty) {
		this.statutoryunit2qty = statutoryunit2qty;
	}

	public BigDecimal getCubic() {
		return cubic;
	}

	public void setCubic(BigDecimal cubic) {
		this.cubic = cubic;
	}

	public BigDecimal getTotalcubic() {
		return totalcubic;
	}

	public void setTotalcubic(BigDecimal totalcubic) {
		this.totalcubic = totalcubic;
	}

	public BigDecimal getGrossweight() {
		return grossweight;
	}

	public void setGrossweight(BigDecimal grossweight) {
		this.grossweight = grossweight;
	}

	public BigDecimal getTotalgrossweight() {
		return totalgrossweight;
	}

	public void setTotalgrossweight(BigDecimal totalgrossweight) {
		this.totalgrossweight = totalgrossweight;
	}

	public BigDecimal getNetweight() {
		return netweight;
	}

	public void setNetweight(BigDecimal netweight) {
		this.netweight = netweight;
	}

	public BigDecimal getTotalnetweight() {
		return totalnetweight;
	}

	public void setTotalnetweight(BigDecimal totalnetweight) {
		this.totalnetweight = totalnetweight;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(BigDecimal totalprice) {
		this.totalprice = totalprice;
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

	public List<Map> getRemark1() {
		return remark1;
	}

	public void setRemark1(List<Map> remark1) {
		this.remark1 = remark1;
	}

	public String getSoreference2() {
		return soreference2;
	}

	public void setSoreference2(String soreference2) {
		this.soreference2 = soreference2;
	}

	public String getWlYwno() {
		return wlYwno;
	}

	public void setWlYwno(String wlYwno) {
		this.wlYwno = wlYwno;
	}

	public String getDnCode() {
		return dnCode;
	}

	public void setDnCode(String dnCode) {
		this.dnCode = dnCode;
	}
    
    
}
