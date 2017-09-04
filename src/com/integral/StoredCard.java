package com.integral;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StoredCard implements Serializable {

	@Id
	@Column(name = "ID",  nullable = false, length = 20) 
	private int id;
	
	//手机号
	private String mobile;
	
	//验证码
	private String verificationCode;
}
