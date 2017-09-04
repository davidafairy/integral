package com.integral.posServer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.integral.ByteUtils;
import com.integral.Integral;
import com.integral.IntegralDao;

public class PosServerThread implements Runnable {

	static Log log = LogFactory.getLog("suguo");

	Socket socket;

	OutputStream socketOut;

	InputStream inputStream;


	volatile boolean runing = true;
	
	IntegralDao integralDao;
	

	public PosServerThread(Socket socket,IntegralDao integralDao) {
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

		log.info("��ʼ������Ϣ.....");
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
			log.info("pos client "+pos+" ��ʱ0.5����ѹر�.....");
		} catch (Exception e) {
			log.error(e,e);
		}
		
	}

	boolean receive(DataInputStream in) {
		try {
			if (in.available() > 0) {

				byte[] head = new byte[2];// ����ͷ 'M' 'Z'
				in.readFully(head);

				byte[] lengthBytes = new byte[2];// ����
				in.readFully(lengthBytes);
				int length = ByteUtils.byte2short(lengthBytes);
				length = length - 255;
				
				byte[] contentBytes = new byte[length];// ����
				in.readFully(contentBytes);

				processResult(new String(head), contentBytes);
				return true;
				
			}
		} catch (Throwable e) {

			log.error("��������ʱ���ִ��� " + e, e);
		}
		return false;
	}

	private void sendCmd(String cmdBody) throws IOException{
		
		List<Byte> bytes = new ArrayList<Byte>();
		//4D 5A --'M' 'Z' ����ͷ 
		bytes.add((byte)0x4D);	
		bytes.add((byte)0x5A);
	
		ByteUtils.addShort2Bytes( cmdBody.getBytes().length+255, bytes );//�������ݳ���
		
		byte[] bs = cmdBody.getBytes();  //��������
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
		
		log.info("#################Server �յ�["+socket.toString()+"]����������["+cmdContent+"]###################");
		if(cmdContent.startsWith("0010")){//���ֶһ�
			processIntegral(cmdContent);
		}
		if(cmdContent.startsWith("0011")){//���ֶһ�
			undoIntegral(cmdContent);
		}
		if(cmdContent.startsWith("0050")){//�û����ֲ�ѯ
			queryUserIntegral(cmdContent);
		}
	}
	
	void queryUserIntegral(String cmdBody){
//		sendCmd("0051|0|�췲");
//		Salesman			��Ա���
//		PosNo               ���������
//		verificationCode	��֤��
		String[] cmds = StringUtils.split(cmdBody, "|");
		String verificationCode=cmds[3];

		String suguoResult = "0050|1|��ѯʧ��,û�и���֤��";

		Integral integral = integralDao.searchIntegral(verificationCode);
//		Integral integral = new Integral();
//		integral.setStatus(3);
		if(integral != null) {
			if (integral.getStatus() == 1) {
				suguoResult = "0050|0|"+integral.getMobile()+"|"+integral.getUserName()+"|"+integral.getExchangeMoney();
			}
			if (integral.getStatus() == 2) {
				suguoResult = "0050|1|��ѯʧ��,����֤����ʧЧ";
			}
			
			if (integral.getStatus() == 3) {
				suguoResult = "0050|1|��ѯʧ��,����֤����ʹ��";
			}
		} else {
			suguoResult = "0050|1|��ѯʧ��,û�и���֤��";
		}
		
		
		try {
			sendCmd(suguoResult);
			log.info("���չ�pos�� ["+socket.toString()+"]�ظ���ѯ����ɹ�");
		} catch (Exception e) {
			log.error("���չ��ظ���ѯ���ʱ����ʱ����"+e,e);
		}
	}
	
	void processIntegral(String cmdBody){
		String suguoResult = "0010|1|֧��ʧ��,����֤����ʹ��";
		
		String[] cmds = StringUtils.split(cmdBody, "|");
		String flowNo=cmds[1];
		String deptNo=cmds[2];
		String salesman=cmds[3];
		String posNo=cmds[4];
		String money=cmds[5];
		String verificationCode=cmds[6];
		
		Integral integral = integralDao.searchIntegral(verificationCode);
		if(integral != null) {
			if (integral.getExchangeMoney() <= Integer.valueOf(money)) {
				suguoResult = "0010|1|����֧��ʧ��,���ý���";
			} else {
				if (integral.getStatus() == 1) {
					integral.setPaymentFlowNo(flowNo);
					integral.setDeptNo(deptNo);
					integral.setSalesman(salesman);
					integral.setPosNo(posNo);
					integral.setPaymentMoney(Long.valueOf(money));
					integral.setPaymentTime(Calendar.getInstance().getTime());
					integral.setStatus(3);
					try {
						integralDao.updateIntegral(integral);
						suguoResult = "0010|0|����֧���ɹ�";
					} catch (Exception e) {
						suguoResult = "0010|1|����֧��ʧ��,ϵͳ�쳣";
					}
					
				}else if (integral.getStatus() == 2) {
					suguoResult = "0010|1|����֧��ʧ��,����֤����ʧЧ";
				}else if (integral.getStatus() == 3) {
					suguoResult = "0010|1|����֧��ʧ��,����֤����ʹ��";
				}
			}
			
		} else {
			suguoResult = "0010|1|����֧��ʧ��,û�и���֤��";
		}
		try {
			sendCmd(suguoResult);
			log.info("���չ�pos�� ["+socket.toString()+"]�ظ���ѯ����ɹ�");
		} catch (Exception e) {
			log.error("���չ��ظ���ѯ���ʱ����ʱ����"+e,e);
		}
	}
	
	void undoIntegral(String cmdBody){
		String suguoResult = "0011|1|֧������ʧ��";
		
		String[] cmds = StringUtils.split(cmdBody, "|");
		String flowNo=cmds[1];
		
		Integral integral = integralDao.searchIntegralByPaymentFlowNo(flowNo);
		if(integral != null) {
			integral.setStatus(1);
			integralDao.updateIntegral(integral);
			suguoResult = "0011|0|֧�������ɹ�";
		}else {
			suguoResult = "0011|1|֧������ʧ��,û�в鵽�ñʶ���";
		}
		try {
			sendCmd(suguoResult);
			log.info("���չ�pos�� ["+socket.toString()+"]�ظ���ѯ����ɹ�");
		} catch (Exception e) {
			log.error("���չ��ظ���ѯ���ʱ����ʱ����"+e,e);
		}
	}

}
