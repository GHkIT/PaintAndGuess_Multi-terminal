package com.godcan.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.godcan.entity.Drawing;
import com.godcan.util.Util;

public class Server {
	
    private ServerSocket server;
    //�ü��������������пͻ��˵������,���ڹ㲥��
    private List<ObjectOutputStream> allOut;
    //������Ŀͻ���
    private List<ObjectOutputStream> printStream;
    //�����ͼ�����
    private Object obj;

    /**
     * ����������������빲�����С�
     * @param out
     */
    private synchronized void addOut(ObjectOutputStream out) {
    	allOut.add(out);
    }
    
    /**
     * ��������������ӹ�������ɾ����
     * @param out
     */
    private synchronized void removeOut(ObjectOutputStream out) {
    	allOut.remove(out);
    }
    
    /**
     * ����������Ϣ���͸������ͻ��ˡ�
     * @param message
     */
    private void sendMessageToAllClient(Object obj,ObjectOutputStream out) {
    	for(ObjectOutputStream o:allOut) {
    		if(o != out) {
    			try {
    				o.writeObject(obj);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }

    public Server() {
    	try {
    		server = new ServerSocket(Util.port);
    		allOut = new ArrayList<ObjectOutputStream>();
    		printStream = new ArrayList<ObjectOutputStream>();
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
    		InetAddress address = socket.getInetAddress();
    		host = address.getHostAddress();
    	}
		@Override
		public void run() {
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			try{
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				//������ͻ��˵���������빲����
				addOut(oos);
				
				while(allOut.size() < 2) {
					System.out.println("��Ϣ");
					Thread.sleep(5000);
				}
				while((obj = ois.readObject()) != null) {
					sendMessageToAllClient(obj,oos);
				}
			} catch(Exception e) {
				
			} finally{
				removeOut(oos);
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
