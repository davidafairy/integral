package com.integral.unicomServer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.integral.AESUtil;
import com.integral.ByteUtils;
import com.integral.Integral;
import com.integral.IntegralDao;

public class UnicomServerThread implements Runnable {

	static Log log = LogFactory.getLog("unicom");

	Socket socket;

	OutputStream socketOut;

	InputStream inputStream;

	static private Random random = new Random();

	volatile boolean runing = true;
	
	IntegralDao integralDao;
	

	public UnicomServerThread(Socket socket,IntegralDao integralDao) {
		this.socket = socket;
		this.integralDao = integralDao;
	}

	public void run() {

		try {
			socketOut = socket.getOutputStream();
			inputStream = socket.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));

		log.info("开始收听消息.....");
		while (runing) {
			boolean relValue = receive(in);
			if(relValue == true){
				break;
			}
		}
		
		
		try {
			Thread.currentThread().sleep(500);
			String pos = socket.toString();
			socket.close();
			log.info("pos client "+pos+" 延时0.5秒后已关闭.....");
		} catch (Exception e) {
			log.error(e,e);
		}
		
	}

	boolean receive(DataInputStream in) {
		try {
			if (in.available() > 0) {

				byte[] head = new byte[2];// 命令头 'M' 'Z'
				in.readFully(head);

				byte[] lengthBytes = new byte[2];// 包长
				in.readFully(lengthBytes);
				int length = ByteUtils.byte2short(lengthBytes);
				length = length - 255;
				
				byte[] contentBytes = new byte[length];// 内容
				in.readFully(contentBytes);

				processResult(new String(head), contentBytes);
				return true;
				
			}
		} catch (Throwable e) {

			log.error("接受命令时出现错误！ " + e, e);
		}
		return false;
	}

	private void sendCmd(String cmdBody) throws IOException{
		
		List<Byte> bytes = new ArrayList<Byte>();
		//4D 5A --'M' 'Z' 命令头 
		bytes.add((byte)0x4D);	
		bytes.add((byte)0x5A);
	
		ByteUtils.addShort2Bytes( cmdBody.getBytes().length+255, bytes );//命令内容长度
		
		byte[] bs = cmdBody.getBytes();  //命令内容
		for (int i = 0; i < bs.length; i++) {
			bytes.add( bs[i] );
		}
		
		byte[] cmdBytes = new byte[bytes.size()];
		
		for (int i=0;i<bytes.size();i++) {
			cmdBytes[i] = bytes.get(i);
		}
		
		socketOut.write(cmdBytes);
		socketOut.flush();
	}
	
	private void processResult( String head, byte[] contentBytes)throws Exception {
		String cmdContent = new String(contentBytes);
		
		log.info("#################Server 收到["+socket.toString()+"]发来的命令["+cmdContent+"]###################");
		if(cmdContent.startsWith("0010")){//积分兑换
			exchange(cmdContent);
		}
	}
	
	void exchange(String cmdBody){
//		sendCmd("0051|0|朱凡");
//		Salesman			柜员编号
//		PosNo               收银机编号
//		verificationCode	验证码
		String[] cmds = StringUtils.split(cmdBody, "|");
		String flowNo = cmds[1];
		String areaCode = cmds[2];
		String businessHallCode = cmds[3];
		String phoneNo = cmds[4];
		String money = cmds[5];
		String customerName = cmds[6];
		String type = cmds[7];

		String unicomResult = "0010|1|获取验证码失败";

//		Integral integral = integralDao.searchIntegral(verificationCode);
		Integral integral = new Integral();
		integral.setExchangeFlowNo(flowNo);
		integral.setAreaCode(areaCode);
		integral.setBusinessHallCode(businessHallCode);
		integral.setMobile(phoneNo);
		integral.setExchangeMoney(Long.valueOf(money));
		integral.setUserName(customerName);
		integral.setExchangeType(Integer.valueOf(type));
		integral.setExchangeTime(Calendar.getInstance().getTime());
		integral.setStatus(1);
		
		String verificationCode = generateVerificationCode();
		integral.setVerificationCode(verificationCode);
		try {
			try {
				integralDao.saveIntegral(integral);
				unicomResult = "0010|0|"+AESUtil.encrypt(verificationCode, "Abc123abc");
			} catch (Exception e) {
				unicomResult = "0010|1|获取验证码失败";
				log.error("保存积分兑换信息失败："+e,e);
			}
			
			sendCmd(unicomResult);
			log.info("向联通 ["+socket.toString()+"]回复查询命令成功");
		} catch (Exception e) {
			log.error("向联通回复查询结果时命令时出错"+e,e);
			System.out.println("向联通回复查询结果时命令时出错");
		}
	}
	
	//生成验证码
	private static String generateVerificationCode() {
		String verificationCode="";
		for (int i=0;i<8;i++) {
			int t = random.nextInt(10);
			verificationCode += String.valueOf(t);
		}
		return verificationCode;
	}
	
	public static void main(String[] args) {
		System.out.println(generateVerificationCode());
	}

}
