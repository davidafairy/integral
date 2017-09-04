package com.integral;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
//@Table(name="biz_integral",schema="gj") 
//@SequenceGenerator(name = "biz_integral_seq", sequenceName = "biz_integral_seq", allocationSize = 1, initialValue = 100)
public class Integral implements Serializable {

	private static final long serialVersionUID = -163653507825578157L;

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "biz_integral_seq")
	@Column(name = "ID",  nullable = false, length = 20) 
	private int id;
	
	///////////////////////////////联通/////////////////////////////////
	//手机号
	private String mobile;
	
	//用户名称
	private String userName;
	
	//验证码
	private String verificationCode;
	
	//兑换时间（联通获取验证码时间）
	private Date exchangeTime;
	
	//兑换流水号（对联通）
	private String exchangeFlowNo;
	
	//兑换金额
	private long exchangeMoney;
	
	//区域编号
	private String areaCode;
	
	//营业厅编号
	private String businessHallCode;
	
	//兑换方式(1.积分兑换2.充值回馈9.其他)
	private int exchangeType;
		
	//有效期
	private Date validity;
	
	//兑换状态，1.有效;2失效;3.已使用
	private int status;
	
	/////////////////////////////////苏果////////////////////////////////
	
	//支付流水号（对苏果）
	private String paymentFlowNo;
	
	//部门编号
	private String deptNo;
	
	//柜员编号
	private String salesman;
	
	//POS机编号
	private String posNo;

	//支付时间（用户在苏果消费时间）
	private Date paymentTime;
	
	//支付金额
	private long paymentMoney;
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Date getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(Date exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public String getExchangeFlowNo() {
		return exchangeFlowNo;
	}

	public void setExchangeFlowNo(String exchangeFlowNo) {
		this.exchangeFlowNo = exchangeFlowNo;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getBusinessHallCode() {
		return businessHallCode;
	}

	public void setBusinessHallCode(String businessHallCode) {
		this.businessHallCode = businessHallCode;
	}

	public int getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(int exchangeType) {
		this.exchangeType = exchangeType;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPaymentFlowNo() {
		return paymentFlowNo;
	}

	public void setPaymentFlowNo(String paymentFlowNo) {
		this.paymentFlowNo = paymentFlowNo;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getSalesman() {
		return salesman;
	}

	public void setSalesman(String salesman) {
		this.salesman = salesman;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public long getExchangeMoney() {
		return exchangeMoney;
	}

	public void setExchangeMoney(long exchangeMoney) {
		this.exchangeMoney = exchangeMoney;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public long getPaymentMoney() {
		return paymentMoney;
	}

	public void setPaymentMoney(long paymentMoney) {
		this.paymentMoney = paymentMoney;
	}
	
}
