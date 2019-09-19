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
public class JintieOutputHead implements Serializable {

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

    @TableField("WAREHOUSEID")
    private String warehouseid;

    @TableField("ORDERTIME")
    private LocalDateTime ordertime;

    @TableField("STOREID")
    private String storeid;

    @TableField("DOCTYPE")
    private String doctype;

    @TableField("SOREFERENCE1")
    private String soreference1;

    @TableField("SOREFERENCE2")
    private String soreference2;

    @TableField("SOREFERENCE3")
    private String soreference3;

    @TableField("SOREFERENCE4")
    private String soreference4;

    @TableField("SOREFERENCE5")
    private String soreference5;

    @TableField("EXPECTEDSHIPMENTTIME")
    private LocalDateTime expectedshipmenttime;

    @TableField("CONSIGNEEID")
    private String consigneeid;

    @TableField("CONSIGNEEADDRESS1")
    private String consigneeaddress1;

    @TableField("CONSIGNEEADDRESS2")
    private String consigneeaddress2;

    @TableField("CONSIGNEEADDRESS3")
    private String consigneeaddress3;

    @TableField("CONSIGNEEADDRESS4")
    private String consigneeaddress4;

    @TableField("CONSIGNEECITY")
    private String consigneecity;

    @TableField("CONSIGNEEPROVINCE")
    private String consigneeprovince;

    @TableField("CONSIGNEEZIP")
    private String consigneezip;

    @TableField("CONSIGNEECONTACT1")
    private String consigneecontact1;

    @TableField("CONSIGNEECONTACT2")
    private String consigneecontact2;

    @TableField("CONSIGNEETEL1")
    private String consigneetel1;

    @TableField("CONSIGNEETEL2")
    private String consigneetel2;

    @TableField("CONSIGNEEFAX1")
    private String consigneefax1;

    @TableField("CONSIGNEEFAX2")
    private String consigneefax2;

    @TableField("BUYERPO")
    private String buyerpo;

    @TableField("COUNTRYOFORIGIN")
    private String countryoforigin;

    @TableField("COUNTRYOFDESTINATION")
    private String countryofdestination;

    @TableField("TRANSPORTATION")
    private String transportation;

    @TableField("VEHICLENO")
    private String vehicleno;

    @TableField("PLACEOFDISCHARGE")
    private String placeofdischarge;

    @TableField("PLACEOFDELIVERY")
    private String placeofdelivery;

    @TableField("SPOTSERVICE01")
    private String spotservice01;

    @TableField("SPOTSERVICE02")
    private String spotservice02;

    @TableField("SPOTSERVICE03")
    private String spotservice03;

    @TableField("SPOTSERVICE04")
    private String spotservice04;

    @TableField("SPOTSERVICE05")
    private String spotservice05;

    @TableField("CONTAINERTYPE")
    private String containertype;

    @TableField("CONTAINERQTY")
    private Integer containerqty;

    @TableField("NOTES")
    private String notes;

    @TableField("BILLTOID")
    private String billtoid;

    @TableField("CONSIGNEECOUNTRYCODE")
    private String consigneecountrycode;

    @TableField("OPENQTY")
    private Integer openqty;

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


    @TableField("BILLOFLADING")
    private String billoflading;


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


	public String getWarehouseid() {
		return warehouseid;
	}


	public void setWarehouseid(String warehouseid) {
		this.warehouseid = warehouseid;
	}


	public LocalDateTime getOrdertime() {
		return ordertime;
	}


	public void setOrdertime(LocalDateTime ordertime) {
		this.ordertime = ordertime;
	}


	public String getStoreid() {
		return storeid;
	}


	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}


	public String getDoctype() {
		return doctype;
	}


	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}


	public String getSoreference1() {
		return soreference1;
	}


	public void setSoreference1(String soreference1) {
		this.soreference1 = soreference1;
	}


	public String getSoreference2() {
		return soreference2;
	}


	public void setSoreference2(String soreference2) {
		this.soreference2 = soreference2;
	}


	public String getSoreference3() {
		return soreference3;
	}


	public void setSoreference3(String soreference3) {
		this.soreference3 = soreference3;
	}


	public String getSoreference4() {
		return soreference4;
	}


	public void setSoreference4(String soreference4) {
		this.soreference4 = soreference4;
	}


	public String getSoreference5() {
		return soreference5;
	}


	public void setSoreference5(String soreference5) {
		this.soreference5 = soreference5;
	}


	public LocalDateTime getExpectedshipmenttime() {
		return expectedshipmenttime;
	}


	public void setExpectedshipmenttime(LocalDateTime expectedshipmenttime) {
		this.expectedshipmenttime = expectedshipmenttime;
	}


	public String getConsigneeid() {
		return consigneeid;
	}


	public void setConsigneeid(String consigneeid) {
		this.consigneeid = consigneeid;
	}


	public String getConsigneeaddress1() {
		return consigneeaddress1;
	}


	public void setConsigneeaddress1(String consigneeaddress1) {
		this.consigneeaddress1 = consigneeaddress1;
	}


	public String getConsigneeaddress2() {
		return consigneeaddress2;
	}


	public void setConsigneeaddress2(String consigneeaddress2) {
		this.consigneeaddress2 = consigneeaddress2;
	}


	public String getConsigneeaddress3() {
		return consigneeaddress3;
	}


	public void setConsigneeaddress3(String consigneeaddress3) {
		this.consigneeaddress3 = consigneeaddress3;
	}


	public String getConsigneeaddress4() {
		return consigneeaddress4;
	}


	public void setConsigneeaddress4(String consigneeaddress4) {
		this.consigneeaddress4 = consigneeaddress4;
	}


	public String getConsigneecity() {
		return consigneecity;
	}


	public void setConsigneecity(String consigneecity) {
		this.consigneecity = consigneecity;
	}


	public String getConsigneeprovince() {
		return consigneeprovince;
	}


	public void setConsigneeprovince(String consigneeprovince) {
		this.consigneeprovince = consigneeprovince;
	}


	public String getConsigneezip() {
		return consigneezip;
	}


	public void setConsigneezip(String consigneezip) {
		this.consigneezip = consigneezip;
	}


	public String getConsigneecontact1() {
		return consigneecontact1;
	}


	public void setConsigneecontact1(String consigneecontact1) {
		this.consigneecontact1 = consigneecontact1;
	}


	public String getConsigneecontact2() {
		return consigneecontact2;
	}


	public void setConsigneecontact2(String consigneecontact2) {
		this.consigneecontact2 = consigneecontact2;
	}


	public String getConsigneetel1() {
		return consigneetel1;
	}


	public void setConsigneetel1(String consigneetel1) {
		this.consigneetel1 = consigneetel1;
	}


	public String getConsigneetel2() {
		return consigneetel2;
	}


	public void setConsigneetel2(String consigneetel2) {
		this.consigneetel2 = consigneetel2;
	}


	public String getConsigneefax1() {
		return consigneefax1;
	}


	public void setConsigneefax1(String consigneefax1) {
		this.consigneefax1 = consigneefax1;
	}


	public String getConsigneefax2() {
		return consigneefax2;
	}


	public void setConsigneefax2(String consigneefax2) {
		this.consigneefax2 = consigneefax2;
	}


	public String getBuyerpo() {
		return buyerpo;
	}


	public void setBuyerpo(String buyerpo) {
		this.buyerpo = buyerpo;
	}


	public String getCountryoforigin() {
		return countryoforigin;
	}


	public void setCountryoforigin(String countryoforigin) {
		this.countryoforigin = countryoforigin;
	}


	public String getCountryofdestination() {
		return countryofdestination;
	}


	public void setCountryofdestination(String countryofdestination) {
		this.countryofdestination = countryofdestination;
	}


	public String getTransportation() {
		return transportation;
	}


	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}


	public String getVehicleno() {
		return vehicleno;
	}


	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}


	public String getPlaceofdischarge() {
		return placeofdischarge;
	}


	public void setPlaceofdischarge(String placeofdischarge) {
		this.placeofdischarge = placeofdischarge;
	}


	public String getPlaceofdelivery() {
		return placeofdelivery;
	}


	public void setPlaceofdelivery(String placeofdelivery) {
		this.placeofdelivery = placeofdelivery;
	}


	public String getSpotservice01() {
		return spotservice01;
	}


	public void setSpotservice01(String spotservice01) {
		this.spotservice01 = spotservice01;
	}


	public String getSpotservice02() {
		return spotservice02;
	}


	public void setSpotservice02(String spotservice02) {
		this.spotservice02 = spotservice02;
	}


	public String getSpotservice03() {
		return spotservice03;
	}


	public void setSpotservice03(String spotservice03) {
		this.spotservice03 = spotservice03;
	}


	public String getSpotservice04() {
		return spotservice04;
	}


	public void setSpotservice04(String spotservice04) {
		this.spotservice04 = spotservice04;
	}


	public String getSpotservice05() {
		return spotservice05;
	}


	public void setSpotservice05(String spotservice05) {
		this.spotservice05 = spotservice05;
	}


	public String getContainertype() {
		return containertype;
	}


	public void setContainertype(String containertype) {
		this.containertype = containertype;
	}


	public Integer getContainerqty() {
		return containerqty;
	}


	public void setContainerqty(Integer containerqty) {
		this.containerqty = containerqty;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	public String getBilltoid() {
		return billtoid;
	}


	public void setBilltoid(String billtoid) {
		this.billtoid = billtoid;
	}


	public String getConsigneecountrycode() {
		return consigneecountrycode;
	}


	public void setConsigneecountrycode(String consigneecountrycode) {
		this.consigneecountrycode = consigneecountrycode;
	}


	public Integer getOpenqty() {
		return openqty;
	}


	public void setOpenqty(Integer openqty) {
		this.openqty = openqty;
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


	public String getBilloflading() {
		return billoflading;
	}


	public void setBilloflading(String billoflading) {
		this.billoflading = billoflading;
	}


}
