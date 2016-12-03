package com.godcan.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * �ͻ���
 * @author Spirit
 *
 */
public class Client {
	
	//�ͻ��˵�Socket,�������ӷ���˵�ServerSocket���ڷ����ͨѶ�� 
	private Socket socket;
	
	/**
	 * ���췽��,������ʼ���ͻ���
	 */
	public Client() {
		try {
			socket = new Socket("localhost",9999);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �ͻ��˿�ʼ�����ķ���
	 */
	public void start() {
		try{
			/*
			 *�������������˷��ͻ������Ϣ���߳� 
			 */
			ServerMessageHandler handler = 
					          new ServerMessageHandler();
			Thread t = new Thread(handler);
			t.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������˷��Ϳͻ��˲²����Ϣ
	 */
	public void sendGuessMessage() {
		try {
			PrintWriter pw = new PrintWriter(
			          socket.getOutputStream(), true);
			pw.println("sss");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ʾ�µĽ��
	 */
	public void getGuessResult() {
		try {
			BufferedReader br = new BufferedReader(
			           new InputStreamReader(
			        		   socket.getInputStream()));
			String response = br.readLine();
			System.out.println(response+"x");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Client c = new Client();
		c.start();
	}
	
	/**
	 * �ڲ���
	 * ����1:�ͻ��˵Ļ����ߴ𰸷���������
	 * ����2:���ܷ�����ת���Ļ����߽����жϽ��
	 * @author Spirit
	 */
	private class ServerMessageHandler implements Runnable {
		
		@Override
		public void run() {
			try {
				while(true) {
					sendGuessMessage();
					getGuessResult();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
