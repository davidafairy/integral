package test;

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

public class TestPosServerThread implements Runnable {

	static Log log = LogFactory.getLog("suguo");

	Socket socket;

	OutputStream socketOut;

	InputStream inputStream;


	volatile boolean runing = true;
	
	

	public TestPosServerThread(Socket socket,IntegralDao integralDao) {
		this.socket = socket;
	}

	public void run() {

		try {
			socketOut = socket.getOutputStream();
			inputStream = socket.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));

		System.out.println("开始收听消息.....");
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
			System.out.println("pos client "+pos+" 延时0.5秒后已关闭.....");
		} catch (Exception e) {
			e.printStackTrace();
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

			e.printStackTrace();
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
		
		System.out.println("#################Server 收到["+socket.toString()+"]发来的命令["+cmdContent+"]###################");
		if(cmdContent.startsWith("0010")){//积分兑换
			processIntegral(cmdContent);
		}
		if(cmdContent.startsWith("0050")){//用户积分查询
			queryUserIntegral(cmdContent);
		}
	}
	
	void queryUserIntegral(String cmdBody){
//		sendCmd("0051|0|朱凡");
//		Salesman			柜员编号
//		PosNo               收银机编号
//		verificationCode	验证码
		String[] cmds = StringUtils.split(cmdBody, "|");
		String verificationCode=cmds[3];

		String suguoResult = "0050|1|查询失败,没有该验证码";

		suguoResult = "0050|0|18652005099|达伟|10000";
		
		
		try {
			sendCmd(suguoResult);
			System.out.println("向苏果pos机 ["+socket.toString()+"]回复查询命令成功");
		} catch (Exception e) {
			System.out.println("向苏果回复查询结果时命令时出错"+e);
		}
	}
	
	void processIntegral(String cmdBody){
		String suguoResult = "0010|1|支付失败,该验证码已使用";
		
		String[] cmds = StringUtils.split(cmdBody, "|");
		String flowNo=cmds[1];
		String deptNo=cmds[2];
		String salesman=cmds[3];
		String posNo=cmds[4];
		String money=cmds[5];
		String verificationCode=cmds[6];
		
		suguoResult = "0010|0|积分支付成功";
		try {
			sendCmd(suguoResult);
			System.out.println("向苏果pos机 ["+socket.toString()+"]回复查询命令成功");
		} catch (Exception e) {
			System.out.println("向苏果回复查询结果时命令时出错"+e);
		}
	}

}
