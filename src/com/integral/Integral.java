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
	
	///////////////////////////////��ͨ/////////////////////////////////
	//�ֻ���
	private String mobile;
	
	//�û�����
	private String userName;
	
	//��֤��
	private String verificationCode;
	
	//�һ�ʱ�䣨��ͨ��ȡ��֤��ʱ�䣩
	private Date exchangeTime;
	
	//�һ���ˮ�ţ�����ͨ��
	private String exchangeFlowNo;
	
	//�һ����
	private long exchangeMoney;
	
	//������
	private String areaCode;
	
	//Ӫҵ�����
	private String businessHallCode;
	
	//�һ���ʽ(1.���ֶһ�2.��ֵ����9.����)
	private int exchangeType;
		
	//��Ч��
	private Date validity;
	
	//�һ�״̬��1.��Ч;2ʧЧ;3.��ʹ��
	private int status;
	
	/////////////////////////////////�չ�////////////////////////////////
	
	//֧����ˮ�ţ����չ���
	private String paymentFlowNo;
	
	//���ű��
	private String deptNo;
	
	//��Ա���
	private String salesman;
	
	//POS�����
	private String posNo;

	//֧��ʱ�䣨�û����չ�����ʱ�䣩
	private Date paymentTime;
	
	//֧�����
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
