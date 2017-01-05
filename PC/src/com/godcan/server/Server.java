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

import com.godcan.util.Util;

public class Server {
	
    private ServerSocket server;
    //�ü��������������пͻ��˵������,���ڹ㲥��
    private List<PrintWriter> guessStream;
    private PrintWriter drawStream;
    //��Ʒ
    private String []something = {"��","��","����"};
    private String nowResult;
    
    /**
     * ����������������뼯���С�
     * @param out
     */
    private synchronized void addOut(PrintWriter pw) {
    	guessStream.add(pw);
    }
    
    /**
     * ��������������Ӽ�����ɾ����
     * @param out
     */
    private synchronized void removeOut(PrintWriter pw) {
    	guessStream.remove(pw);
    }
    
    /**
     * ��ͼ�������͸��µĿͻ��ˡ�
     * @param message
     */
    private synchronized void sendMessageToAllClient(String str) {
    	for(PrintWriter p:guessStream) {
			p.println(str);
			p.flush();
    	}
    }
    
    /**
     * ���ô�ʱ������Ʒ��
     * @return
     */
    private void setThing() {
    	Random random = new Random();
    	nowResult = something[random.nextInt(something.length)];
    }

    public Server() {
    	try {
    		server = new ServerSocket(Util.port);
    		guessStream = new ArrayList<PrintWriter>();
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
    	
    	private boolean isDraw;
    	
    	public ClientHandler(Socket socket) {
    		this.socket = socket;
    		InetAddress address = socket.getInetAddress();
    		host = address.getHostAddress();
    	}
		@Override
		public void run() {
			BufferedReader br = null;
			PrintWriter pw = null;
			String str = null;
			try{
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(),true);
				if((str = (String) br.readLine()) != null) {
					if("���ǻ�".equals(str)) {
						System.out.println("��������");
						drawStream = pw;
						isDraw = true;
						setThing();
						pw.println(nowResult);
					} else if("���ǲ�".equals(str)) {
						System.out.println("�¼�����");
						addOut(pw);
					}
				}
				
				while(guessStream.size() < 1) {
					System.out.println("��Ϣ");
					Thread.sleep(5000);
				}
				while((str = br.readLine()) != null) {
					if(isDraw) {
						if("������Ʒ".equals(str)) {
							setThing();
							pw.println(nowResult);
						}else {
							sendMessageToAllClient(str);
						}
					} else {
						System.out.println(str);
						if(nowResult.equals(str)) {
							pw.println("��ϲ��,�¶���,����¸�ͼ��");
						}else {
							pw.println("�������");
						}
					}
				}
			} catch(Exception e) {
				
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
