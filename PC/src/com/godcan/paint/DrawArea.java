package com.godcan.paint;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
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
import com.godcan.util.Util;

/**
 * ��ͼ���ࣨ����ͼ�εĻ��ƺ�����¼���
 * �ͻ���
 * @author Spirit
 *
 */
public class DrawArea extends JPanel {
	
	private static final long serialVersionUID = 3559220003292632288L;
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private DrawPad drawpad = null;
    private List<Drawing> itemList = new ArrayList<Drawing>(); //����ͼ����
    
	private int currentChoice = 1;//����Ĭ�ϻ���ͼ��״̬Ϊ��ʻ�
    private int index = 0;//��ǰ�Ѿ����Ƶ�ͼ����Ŀ
    private Color color = Color.black;//��ǰ���ʵ���ɫ
    private int R,G,B;//������ŵ�ǰ��ɫ�Ĳ�ֵ
    private float stroke = 1.0f;//���û��ʵĴ�ϸ ��Ĭ�ϵ��� 1.0
	public DrawArea(DrawPad dp) {
		drawpad = dp;
		//�����ͻ���
		try {
			socket = new Socket( Util.IP , Util.port );
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
		// ��������ó�ʮ����
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setBackground(Color.white);// ���û������ı����ǰ�ɫ
		addMouseListener(new MouseA());// �������¼�
		addMouseMotionListener(new MouseB());
		createNewitem();
		//�������ܷ�������Ϣ���߳�
		Thread t = new Thread(new ServerImageHandler());
		t.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);   //����
		Graphics2D g2d = (Graphics2D)g;//������ʻ�
		int j = 0;
		for(int i = 0; i < itemList.size(); i++) {
			if(itemList.get(i) == null) {
				itemList.remove(i);
				index--;
				i--;
			}
		}
		while(j <= index) {
			draw(g2d,itemList.get(j));
			j++;
	    }
	}
	
	public void draw(Graphics2D g2d , Drawing i) {
		i.draw(g2d);//�����ʴ���������������У�������ɸ��ԵĻ�ͼ
	}
	
	/**
	 * �½�һ��ͼ�εĻ�����Ԫ����ĳ����
	 */
	public void createNewitem(){
		switch(currentChoice){
			case 1:  itemList.add(new Pencil());break;
			case 2:  itemList.add(new Line());break;
			case 3:  itemList.add(new Rect());break;
			case 4:  itemList.add(new FillRect());break;
			case 5:  itemList.add(new Oval());break;
			case 6:  itemList.add(new FillOval());break;
			case 7:  itemList.add(new Circle());break;
			case 8:  itemList.add(new FillCircle());break;
			case 9:  itemList.add(new RoundRect());break;
			case 10: itemList.add(new FillRoundRect());break;
			case 11: itemList.add(new Rubber());break;
		}
		itemList.get(index).type = currentChoice;
		itemList.get(index).R = R;
	    itemList.get(index).G = G;
		itemList.get(index).B = B;
		itemList.get(index).stroke = stroke;
	}
	
	/**
	 * ��ȡ����˴�������ͼ����������
	 */
	public void acquireObject() {
		try {
			Object obj = null;
			Drawing d = null;
			if((obj = ois.readObject()) != null) {
				d = (Drawing)obj;
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
	
	/**
	 * �ڲ���
	 * ���ܷ�����ת���Ļ����߽����жϽ��
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
 
/**
 * �������������Ӧ�¼��Ĳ��������İ��¡��ͷš��������ƶ����϶�����ʱ����һ���������ʱ�˳�����ʱ���������� )
 * @author Spirit
 *
 */
class MouseA extends MouseAdapter {

	@Override
	public void mouseEntered(MouseEvent me) {
		drawpad.setStratBar("�������ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mouseExited(MouseEvent me) {
		drawpad.setStratBar("����˳��ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mousePressed(MouseEvent me) {
		drawpad.setStratBar("��갴���ڣ�["+me.getX()+" ,"+me.getY()+"]");//����״̬����ʾ
		
		itemList.get(index).x1 = itemList.get(index).x2 = me.getX();
		itemList.get(index).y1 = itemList.get(index).y2 = me.getY();
		itemList.get(index).isMove = false;
		
		//�����ǰѡ��Ϊ��ʻ�����Ƥ�� ,��ǰ�㴫��������
		if(currentChoice == 1 || currentChoice == 11){
			try {
				oos.writeObject(itemList.get(index));
			} catch (IOException e) {
				e.printStackTrace();
			}
			index++;
			createNewitem();
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		drawpad.setStratBar("����ɿ��ڣ�["+me.getX()+" ,"+me.getY()+"]");
		if(currentChoice == 1 || currentChoice == 11){
			itemList.get(index).x1 = me.getX();
			itemList.get(index).y1 = me.getY();
			for(int i = 0; i < 10; i++) {
				Drawing d = new Pencil();
				d.isMove = false;
				try {
					oos.writeObject(d);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		itemList.get(index).x2 = me.getX();
		itemList.get(index).y2 = me.getY();
		itemList.get(index).isMove = false;
		//��ǰ�㴫��������
		try {
			oos.writeObject(itemList.get(index));
		} catch (IOException e) {
			e.printStackTrace();
		}
		repaint();
		index++;
		createNewitem();
	}

}

/**
 * �����������Ĺ������϶�
 * @author Spirit
 *
 */
class MouseB extends MouseMotionAdapter {
	
	public void mouseDragged(MouseEvent me) {
		drawpad.setStratBar("����϶��ڣ�[" + me.getX() + " ," + me.getY() + "]");
		if(currentChoice == 1 || currentChoice == 11){
			itemList.get(index-1).x1 = itemList.get(index).x2 = itemList.get(index).x1 = me.getX();
			itemList.get(index-1).y1 = itemList.get(index).y2 = itemList.get(index).y1 = me.getY();
			itemList.get(index).isMove = true;
			//��ǰ�㴫��������
			try {
				oos.writeObject(itemList.get(index));
			} catch (IOException e) {
				e.printStackTrace();
			}
			index++;
			createNewitem();
		} else {
			itemList.get(index).x2 = me.getX();
			itemList.get(index).y2 = me.getY();
		}
		repaint();
	}
	
	public void mouseMoved(MouseEvent me) {
		drawpad.setStratBar("����ƶ��ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}
}


	
	public void setIndex(int x) {//����index�Ľӿ�
		index = x;
	}
	public int getIndex(){//����index�Ľӿ�
		return index ;
	}
	public void setColor(Color color) {   //������ɫ��ֵ
		this.color = color; 
	}
	public void setStroke(float f) {    //���û��ʴ�ϸ�Ľӿ�
		stroke = f;
	}
	public List<Drawing> getItemList() {   //����ͼ�ζ�������
		return itemList;
	}
	
	/**
	 * ѡ��ǰ��ɫ
	 */
	public void chooseColor() {    
		color = JColorChooser.showDialog(drawpad, "��ѡ����ɫ", color);
		try {
			R = color.getRed();
			G = color.getGreen();
			B = color.getBlue();
		} catch (Exception e) {
			R = 0;
			G = 0;
			B = 0;
		}
		itemList.get(index).R = R;
		itemList.get(index).G = G;
		itemList.get(index).B = B;
	}
	/**
	 * ���ʴ�ϸ�ĵ���
	 */
	public void setStroke() {
		String input ;
		input = JOptionPane.showInputDialog("�����뻭�ʵĴ�ϸ( >0 )");
		try {
			stroke = Float.parseFloat(input);
		} catch (Exception e) {
			stroke = 1.0f;
		}
		itemList.get(index).stroke = stroke;
	}
	
	public void setCurrentChoice(int i) {
		currentChoice = i;
	}


}
