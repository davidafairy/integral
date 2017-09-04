package com.integral;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestPosServerThread implements Runnable {

	static Log log = LogFactory.getLog(TestPosServerThread.class);

	Socket socket;

	OutputStream socketOut;

	InputStream inputStream;


	volatile boolean runing = true;
	
	String unicomHostName;
	Integer unicomPort;
	String agentCode;
	

	public TestPosServerThread(Socket socket,String unicomHostName, Integer unicomPort, String agentCode) {
		this.socket = socket;
		this.unicomHostName = unicomHostName;
		this.unicomPort=unicomPort;
		this.agentCode=agentCode;
	}

	public void run() {

		try {
			socketOut = socket.getOutputStream();
			inputStream = socket.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		DataInputStream in = new DataInputStream(new BufferedInputStream(inputStream));

		log.debug("开始收听消息.....");
		
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
			log.debug("pos client "+pos+" 延时0.5秒后已关闭.....");
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
		
		log.debug("#################Server 收到["+socket.toString()+"]发来的命令["+cmdContent+"]###################");
		

	}
	


}
