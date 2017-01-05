package com.godcan.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
	
    private ServerSocket server;
    //�ü��������������пͻ��˵������,���ڹ㲥��
    private List<PrintWriter> allOut;

    /**
     * ����������������빲�����С�
     * @param out
     */
    private synchronized void addOut(PrintWriter out) {
    	allOut.add(out);
    }
    
    /**
     * ��������������ӹ�������ɾ����
     * @param out
     */
    private synchronized void removeOut(PrintWriter out) {
    	allOut.remove(out);
    }
    
    /**
     * ����������Ϣ���͸����пͻ��ˡ�
     * @param message
     */
    private void sendMessageToAllClient(String message) {
    	for(PrintWriter out:allOut) {
    		out.println(message);
    	}
    }

    public Server() {
    	try {
    		server = new ServerSocket(8888);
    		allOut = new ArrayList<PrintWriter>();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * ����˿�ʼ�����ķ���
     */
    public void start() {
    	try {	
			while(true) {
	    		System.out.println("�ȴ��ͻ�������...");
				Socket socket = server.accept();
				System.out.println("һ���ͻ�������!");
				//����һ���߳�,������ͻ��˽��н�����
				ClientHandler handler = new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
		Server s = new Server();
		s.start();
	}
    
    /**
     * ����˴�����ĳ���ͻ��˵Ľ���������
     * @author Spirit
     */
    private class ClientHandler implements Runnable {

    	private Socket socket;
    	//��ǰ�ͻ��˵ĵ�ַ
    	private String host;
    	
    	public ClientHandler(Socket socket) {
    		this.socket = socket;
    		//��ȡԶ�̼������ַ��Ϣ
    		InetAddress address = socket.getInetAddress();
    		//���IP
    		host = address.getHostAddress();
    		System.out.println("["+host+"]������!");
    	}
		@Override
		public void run() {
			PrintWriter pw = null;
			try{
				BufferedReader br = new BufferedReader(
						               new InputStreamReader(
								           socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(),true);
				//������ͻ��˵���������빲����
				addOut(pw);
				String message = null;
				
				while(allOut.size() < 2) {
					System.out.println("��Ϣ");
					Thread.sleep(5000);
				}
				sendMessageToAllClient("�����ѹ�,���Կ�ʼ��!");
				while((message = br.readLine()) != null) {
					System.out.println("["+host+"]˵:"+message);
					sendMessageToAllClient("["+host+"]˵:"+message);
					Thread.sleep(5000);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally{
				removeOut(pw);
				try {
					socket.close();
					System.out.println("["+host+"]������!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
}
