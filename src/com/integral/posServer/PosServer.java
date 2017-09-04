package com.integral.posServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.integral.IntegralDao;

@Controller
public class PosServer implements InitializingBean {
	static Log log = LogFactory.getLog("suguo");
	
	private String hostName="localhost";
	private Integer port=9922;
	
//	private String unicomHostName="211.0.0.195";
//	private Integer unicomPort=5555;
//	
//	private String agentCode="34b06q8";
	
	@Autowired
	public IntegralDao integralDao;
	
	public IntegralDao getIntegralDao() {
		return integralDao;
	}

	public void setIntegralDao(IntegralDao integralDao) {
		this.integralDao = integralDao;
	}

	public String getHostName() {
		return hostName;
	}

	public void afterPropertiesSet() throws Exception {
	
		startPosServer(hostName,port);
		
	//	startPosServer("172.168.1.50",6677);
	}

	private void startPosServer(String hostName,Integer port) throws IOException {
		final ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(hostName,port));
		log.info("Pos Server["+hostName+":"+port+"] 已启动......................");
		
		Thread t = new Thread(){
			public void run(){
				try {
					starServer(server);
				} catch (IOException e) {
					log.error(e,e);
					e.printStackTrace();
				}
			}
		};
		t.start();
	}


	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		PosServer server = new PosServer();
		try {
			
			server.afterPropertiesSet();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void starServer(ServerSocket server) throws IOException {
	
		
		
		while (true) {
			Socket socket = server.accept();
			
			log.info("已有客户端在连接"+socket.getInetAddress().toString()+ ":"+socket.getPort());
			PosServerThread st = new PosServerThread(socket,integralDao);
			new Thread(st).start();
		}
	}


}
