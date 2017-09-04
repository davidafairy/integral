package com.integral;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * �����������ɺ�����Ľ��� 
 */
public class CmdProcessor {
	
	static Log log = LogFactory.getLog(CmdProcessor.class);
	

	

	public byte[] parseCmd(String cmdBody){
			
		List<Byte> bytes = new ArrayList<Byte>();
		//4D 5A --'M' 'Z' ����ͷ 
		bytes.add((byte)0x4D);	
		bytes.add((byte)0x5A);
	
		ByteUtils.addShort2Bytes( cmdBody.getBytes().length, bytes );//�������ݳ���
		
		byte[] bs = cmdBody.getBytes();  //��������
		for (int i = 0; i < bs.length; i++) {
			bytes.add( bs[i] );
		}
		
		return toCmdBytes(bytes);// ��list ת������ ����ӡ����
	}
	
	
	private byte[] toCmdBytes(List<Byte> cmd) {
		byte[] byteArray = new byte[cmd.size()];
		
		for (int i=0;i<cmd.size();i++) {
			byteArray[i] = cmd.get(i);
			
			log.debug("HexString = "+ByteUtils.byteToHex(byteArray[i]));
			
		}
		log.debug("*******************************************");
		return byteArray;
	}
	
}
