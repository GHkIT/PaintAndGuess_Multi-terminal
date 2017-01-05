package com.godcan.panel;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.godcan.entity.Circle;
import com.godcan.entity.Drawing;
import com.godcan.entity.FillCircle;
import com.godcan.entity.FillOval;
import com.godcan.entity.FillRect;
import com.godcan.entity.FillRoundRect;
import com.godcan.entity.Line;
import com.godcan.entity.Oval;
import com.godcan.entity.Pencil;
import com.godcan.entity.Rect;
import com.godcan.entity.RoundRect;
import com.godcan.entity.Rubber;
import com.godcan.jframe.GuessJFrame;
import com.godcan.util.Util;

import net.sf.json.JSONObject;

/**
 * ��ͼ���ࣨ����ͼ�εĻ��ƺ�����¼���
 * �ͻ���
 * @author Spirit
 *
 */
public class GuessPanel extends JPanel {
	
	private static final long serialVersionUID = 3559220003292632288L;
	
	private Socket socket;
	private PrintWriter pw;
	private BufferedReader br;
	private JSONObject json;
	
	private GuessJFrame guessJFrame = null;
    private List<Drawing> itemList = new ArrayList<Drawing>(); //����ͼ����
    
//    private String message = null;   //��һ�ַ��ͲµĽ��ʹ�õı���
    private int index = 0;//��ǰ�Ѿ����Ƶ�ͼ����Ŀ
	public GuessPanel(GuessJFrame gj) {
		guessJFrame = gj;
		//�����ͻ���
		try {
			socket = new Socket( Util.IP , Util.port );
			pw = new PrintWriter(socket.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw.println("���ǲ�");
		} catch(Exception e) {
			e.printStackTrace();
		}
		// ��������ó�ʮ����
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setBackground(Color.white);// ���û������ı����ǰ�ɫ
		addMouseListener(new MouseA());// �������¼�
		addMouseMotionListener(new MouseB());
		//�������ܷ�������Ϣ���߳�
		Thread t1 = new Thread(new ServerImageHandler());
		t1.start();
//		//�����������������Ϣ���߳�
//		Thread t2 = new Thread(new ServerMessageHandler());
//		t2.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);   //����
		Graphics2D g2d = (Graphics2D)g;//������ʻ�
		int j = 0;
		while(j < index) {
			draw(g2d,itemList.get(j));
			j++;
	    }
	}
	
	public void draw(Graphics2D g2d , Drawing i) {
		i.draw(g2d);//�����ʴ���������������У�������ɸ��ԵĻ�ͼ
	}

	/**
	 * �Ѵ�������JSONת���ɶ���
	 */
	public Drawing swiftItem(String str){
		json = JSONObject.fromObject(str);
		int currentChoice = (int) json.get("type");
		switch(currentChoice){
			case 1:  return (Drawing) JSONObject.toBean( json , Pencil.class );
			case 2:  return (Drawing) JSONObject.toBean( json , Line.class );
			case 3:  return (Drawing) JSONObject.toBean( json , Rect.class );
			case 4:  return (Drawing) JSONObject.toBean( json , FillRect.class );
			case 5:  return (Drawing) JSONObject.toBean( json , Oval.class );
			case 6:  return (Drawing) JSONObject.toBean( json , FillOval.class );
			case 7:  return (Drawing) JSONObject.toBean( json , Circle.class );
			case 8:  return (Drawing) JSONObject.toBean( json , FillCircle.class );
			case 9:  return (Drawing) JSONObject.toBean( json , RoundRect.class ); 
			case 10: return (Drawing) JSONObject.toBean( json , FillRoundRect.class );
			case 11: return (Drawing) JSONObject.toBean( json , Rubber.class );
			default:return new Drawing();
		}
	}
	
	/**
	 * ��ȡ����˴�������ͼ����������
	 */
	private void acquireObject() {
		try {
			String str = null;
			Drawing d = null;
			if((str = br.readLine()) != null) {
				//�ж��Ƿ��ǲ²���
				if("��ϲ��,�¶���,����¸�ͼ��".equals(str) || "�������".equals(str)) {
					guessJFrame.setTextValue(str);
					return;
				}
				//�ж��Ƿ�����ջ���
				if("��ջ���".equals(str)) {
					itemList.clear();
					index = 0;
					System.out.println("aaa");
					repaint();
					return;
				}
				//����תΪJSON�����������ת��
				d = swiftItem(str);
				if(d.type == 1 || d.type == 11) {
					if(index > 0 && itemList.get(index-1).isMove) {   //����ǻ��ʻ����ĵ�,Ӧ��ǰ��ĵ������˵���
						itemList.get(index-1).x1 = d.x1;
						itemList.get(index-1).y1 = d.y1;
					}
				}
				itemList.add(d);
				index++;
				repaint();
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * ��������˷��Ϳͻ��˲µ���Ϣ
//	 */
//	private void sendMessage() {
//		if(message != null) {
//			pw.println(message);//���Ͳ²�Ľ��
//			message = null; //ʹ��������Ϊnull
//		}
//		
//	}
	
	/**
	 * �ڲ���
	 * ���ܷ�����ת���Ļ�
	 * @author Spirit
	 */
	private class ServerImageHandler implements Runnable {
		
		@Override
		public void run() {
			try {
				while(true) {
					acquireObject();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
//	/**
//	 * �ڲ���
//	 * ������������жϽ��
//	 * @author Spirit
//	 */
//	private class ServerMessageHandler implements Runnable {
//		
//		@Override
//		public void run() {
//			try {
//				while(true) {
//					sendMessage();
//				}
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
 
/**
 * �������������Ӧ�¼��Ĳ��������İ��¡��ͷš��������ƶ����϶�����ʱ����һ���������ʱ�˳�����ʱ���������� )
 * @author Spirit
 *
 */
class MouseA extends MouseAdapter {

	@Override
	public void mouseEntered(MouseEvent me) {
		guessJFrame.setStratBar("�������ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mouseExited(MouseEvent me) {
		guessJFrame.setStratBar("����˳��ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mousePressed(MouseEvent me) {
		guessJFrame.setStratBar("��갴���ڣ�["+me.getX()+" ,"+me.getY()+"]");//����״̬����ʾ
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		guessJFrame.setStratBar("����ɿ��ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}

}

/**
 * �����������Ĺ������϶�
 * @author Spirit
 *
 */
class MouseB extends MouseMotionAdapter {
	
	public void mouseDragged(MouseEvent me) {
		guessJFrame.setStratBar("����϶��ڣ�[" + me.getX() + " ," + me.getY() + "]");
	}
	
	public void mouseMoved(MouseEvent me) {
		guessJFrame.setStratBar("����ƶ��ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}
}

	
	public void setIndex(int x) {//����index�Ľӿ�
		index = x;
	}
	public int getIndex(){//����index�Ľӿ�
		return index ;
	}

//	public void setMessage(String message) {//��һ�ַ��͵���Ϣֵ���÷�����Ϣ
//		this.message = message;
//	}

	/**
	 * ���ͽ���ĵڶ��ְ취
	 */
	public void sendGuessResult(String result) {
		pw.println(result);
	}
	

}
