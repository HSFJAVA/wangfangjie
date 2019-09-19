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
 * 
 * </p>
 *
 * @author jobob
 * @since 2019-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class JintieInputHead implements Serializable {

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

    @TableField("ASNCREATIONTIME")
    private String asncreationtime;

    @TableField("ASNGROUP")
    private String asngroup;

    @TableField("STOREID")
    private String storeid;

    @TableField("DOCTYPE")
    private String doctype;

    @TableField("ASNREFERENCE1")
    private String asnreference1;

    @TableField("ASNREFERENCE2")
    private String asnreference2;

    @TableField("ASNREFERENCE3")
    private String asnreference3;

    @TableField("ASNREFERENCE4")
    private String asnreference4;

    @TableField("ASNREFERENCE5")
    private String asnreference5;

    @TableField("BILLOFLADING")
    private String billoflading;

    @TableField("EXPECTARRIVETIME")
    private LocalDateTime expectarrivetime;

    @TableField("ACTUALARRIVETIME")
    private String actualarrivetime;

    @TableField("EFFECTIVEDATE")
    private String effectivedate;

    @TableField("PONO")
    private String pono;

    @TableField("CARRIERID")
    private String carrierid;

    @TableField("CARRIERNAME")
    private String carriername;

    @TableField("CARRIERADDRESS1")
    private String carrieraddress1;

    @TableField("CARRIERADDRESS2")
    private String carrieraddress2;

    @TableField("CARRIERCITY")
    private String carriercity;

    @TableField("CARRIERPROVINCE")
    private String carrierprovince;

    @TableField("CARRIERZIP")
    private String carrierzip;

    @TableField("SHIPPERID")
    private String shipperid;

    @TableField("SHIPPERNAME")
    private String shippername;

    @TableField("SHIPPERADDRESS1")
    private String shipperaddress1;

    @TableField("SHIPPERADDRESS2")
    private String shipperaddress2;

    @TableField("SHIPPERCITY")
    private String shippercity;

    @TableField("SHIPPERPROVINCE")
    private String shipperprovince;

    @TableField("SHIPPERZIP")
    private String shipperzip;

    @TableField("VEHICLENO")
    private String vehicleno;

    @TableField("VEHICLEDATE")
    private String vehicledate;

    @TableField("VEHICLEDRIVER")
    private String vehicledriver;

    @TableField("COUNTRYOFORIGIN")
    private String countryoforigin;

    @TableField("COUNTRYOFDESTINATION")
    private String countryofdestination;

    @TableField("PLACEOFLOADING")
    private String placeofloading;

    @TableField("PLACEOFDISCHARGE")
    private String placeofdischarge;

    @TableField("PLACEOFDELIVERY")
    private String placeofdelivery;

    @TableField("DELIVERYTERMS")
    private String deliveryterms;

    @TableField("DELIVERYTERMSDESCR")
    private String deliverytermsdescr;

    @TableField("USERDEFINE1")
    private String userdefine1;

    @TableField("USERDEFINE2")
    private String userdefine2;

    @TableField("TRANSITMODE")
    private String transitmode;

    @TableField("VESSEL")
    private String vessel;

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
    private String containerqty;

    @TableField("SEALNO1")
    private String sealno1;

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
	/**
	 * 总净重
	 */
	@TableField("TOTALNW")
    private String TOTALNW;


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
	public String getAsncreationtime() {
		return asncreationtime;
	}
	public void setAsncreationtime(String asncreationtime) {
		this.asncreationtime = asncreationtime;
	}
	public String getAsngroup() {
		return asngroup;
	}
	public void setAsngroup(String asngroup) {
		this.asngroup = asngroup;
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
	public String getAsnreference1() {
		return asnreference1;
	}
	public void setAsnreference1(String asnreference1) {
		this.asnreference1 = asnreference1;
	}
	public String getAsnreference2() {
		return asnreference2;
	}
	public void setAsnreference2(String asnreference2) {
		this.asnreference2 = asnreference2;
	}
	public String getAsnreference3() {
		return asnreference3;
	}
	public void setAsnreference3(String asnreference3) {
		this.asnreference3 = asnreference3;
	}
	public String getAsnreference4() {
		return asnreference4;
	}
	public void setAsnreference4(String asnreference4) {
		this.asnreference4 = asnreference4;
	}
	public String getAsnreference5() {
		return asnreference5;
	}
	public void setAsnreference5(String asnreference5) {
		this.asnreference5 = asnreference5;
	}
	public String getBilloflading() {
		return billoflading;
	}
	public void setBilloflading(String billoflading) {
		this.billoflading = billoflading;
	}
	public LocalDateTime getExpectarrivetime() {
		return expectarrivetime;
	}
	public void setExpectarrivetime(LocalDateTime expectarrivetime) {
		this.expectarrivetime = expectarrivetime;
	}
	public String getActualarrivetime() {
		return actualarrivetime;
	}
	public void setActualarrivetime(String actualarrivetime) {
		this.actualarrivetime = actualarrivetime;
	}
	public String getEffectivedate() {
		return effectivedate;
	}
	public void setEffectivedate(String effectivedate) {
		this.effectivedate = effectivedate;
	}
	public String getPono() {
		return pono;
	}
	public void setPono(String pono) {
		this.pono = pono;
	}
	public String getCarrierid() {
		return carrierid;
	}
	public void setCarrierid(String carrierid) {
		this.carrierid = carrierid;
	}
	public String getCarriername() {
		return carriername;
	}
	public void setCarriername(String carriername) {
		this.carriername = carriername;
	}
	public String getCarrieraddress1() {
		return carrieraddress1;
	}
	public void setCarrieraddress1(String carrieraddress1) {
		this.carrieraddress1 = carrieraddress1;
	}
	public String getCarrieraddress2() {
		return carrieraddress2;
	}
	public void setCarrieraddress2(String carrieraddress2) {
		this.carrieraddress2 = carrieraddress2;
	}
	public String getCarriercity() {
		return carriercity;
	}
	public void setCarriercity(String carriercity) {
		this.carriercity = carriercity;
	}
	public String getCarrierprovince() {
		return carrierprovince;
	}
	public void setCarrierprovince(String carrierprovince) {
		this.carrierprovince = carrierprovince;
	}
	public String getCarrierzip() {
		return carrierzip;
	}
	public void setCarrierzip(String carrierzip) {
		this.carrierzip = carrierzip;
	}
	public String getShipperid() {
		return shipperid;
	}
	public void setShipperid(String shipperid) {
		this.shipperid = shipperid;
	}
	public String getShippername() {
		return shippername;
	}
	public void setShippername(String shippername) {
		this.shippername = shippername;
	}
	public String getShipperaddress1() {
		return shipperaddress1;
	}
	public void setShipperaddress1(String shipperaddress1) {
		this.shipperaddress1 = shipperaddress1;
	}
	public String getShipperaddress2() {
		return shipperaddress2;
	}
	public void setShipperaddress2(String shipperaddress2) {
		this.shipperaddress2 = shipperaddress2;
	}
	public String getShippercity() {
		return shippercity;
	}
	public void setShippercity(String shippercity) {
		this.shippercity = shippercity;
	}
	public String getShipperprovince() {
		return shipperprovince;
	}
	public void setShipperprovince(String shipperprovince) {
		this.shipperprovince = shipperprovince;
	}
	public String getShipperzip() {
		return shipperzip;
	}
	public void setShipperzip(String shipperzip) {
		this.shipperzip = shipperzip;
	}
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getVehicledate() {
		return vehicledate;
	}
	public void setVehicledate(String vehicledate) {
		this.vehicledate = vehicledate;
	}
	public String getVehicledriver() {
		return vehicledriver;
	}
	public void setVehicledriver(String vehicledriver) {
		this.vehicledriver = vehicledriver;
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
	public String getPlaceofloading() {
		return placeofloading;
	}
	public void setPlaceofloading(String placeofloading) {
		this.placeofloading = placeofloading;
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
	public String getDeliveryterms() {
		return deliveryterms;
	}
	public void setDeliveryterms(String deliveryterms) {
		this.deliveryterms = deliveryterms;
	}
	public String getDeliverytermsdescr() {
		return deliverytermsdescr;
	}
	public void setDeliverytermsdescr(String deliverytermsdescr) {
		this.deliverytermsdescr = deliverytermsdescr;
	}
	public String getUserdefine1() {
		return userdefine1;
	}
	public void setUserdefine1(String userdefine1) {
		this.userdefine1 = userdefine1;
	}
	public String getUserdefine2() {
		return userdefine2;
	}
	public void setUserdefine2(String userdefine2) {
		this.userdefine2 = userdefine2;
	}
	public String getTransitmode() {
		return transitmode;
	}
	public void setTransitmode(String transitmode) {
		this.transitmode = transitmode;
	}
	public String getVessel() {
		return vessel;
	}
	public void setVessel(String vessel) {
		this.vessel = vessel;
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
	public String getContainerqty() {
		return containerqty;
	}
	public void setContainerqty(String containerqty) {
		this.containerqty = containerqty;
	}
	public String getSealno1() {
		return sealno1;
	}
	public void setSealno1(String sealno1) {
		this.sealno1 = sealno1;
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

	public String getTOTALNW() {
		return TOTALNW;
	}

	public void setTOTALNW(String TOTALNW) {
		this.TOTALNW = TOTALNW;
	}
}
