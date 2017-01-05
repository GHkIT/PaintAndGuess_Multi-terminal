package com.godcan.panel;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import com.godcan.jframe.DrawJFrame;
import com.godcan.util.Util;

import net.sf.json.JSONObject;

/**
 * 
 * �滭�ͻ���
 * @author Spirit
 *
 */
public class DrawPanel extends JPanel {
	
	private static final long serialVersionUID = 1509156167257348083L;
	
	private Socket socket;
	private PrintWriter pw;
	private BufferedReader br;
	
	private DrawJFrame drawJFrame = null;
    private List<Drawing> itemList = new ArrayList<Drawing>(); //����ͼ����
    
	private int currentChoice = 1;//����Ĭ�ϻ���ͼ��״̬Ϊ��ʻ�
    private int index = 0;//��ǰ�Ѿ����Ƶ�ͼ����Ŀ
    private Color color = Color.black;//��ǰ���ʵ���ɫ
    private int R,G,B;//������ŵ�ǰ��ɫ�Ĳ�ֵ
    private float stroke = 1.0f;//���û��ʵĴ�ϸ ��Ĭ�ϵ��� 1.0
    private JSONObject json;
    private String guessSthing;
	public DrawPanel(DrawJFrame dj) {
		drawJFrame = dj;
		//�����ͻ���
		try {
			socket = new Socket( Util.IP , Util.port );
			pw = new PrintWriter(socket.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw.println("���ǻ�");
			if((guessSthing = (String)br.readLine()) != null) {
				drawJFrame.setTextValue(guessSthing);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		// ��������ó�ʮ����
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setBackground(Color.white);// ���û������ı����ǰ�ɫ
		addMouseListener(new MouseA());// �������¼�
		addMouseMotionListener(new MouseB());
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
	 * �½�һ��ͼ�εĻ�����Ԫ����ĳ����
	 */
	public Drawing createNewitem(){
		Drawing d = null;
		switch(currentChoice){
			case 1:  d = new Pencil();break;
			case 2:  d = new Line();break;
			case 3:  d = new Rect();break;
			case 4:  d = new FillRect();break;
			case 5:  d = new Oval();break;
			case 6:  d = new FillOval();break;
			case 7:  d = new Circle();break;
			case 8:  d = new FillCircle();break;
			case 9:  d = new RoundRect();break;
			case 10: d = new FillRoundRect();break;
			case 11: d = new Rubber();break;
		}
		d.type = currentChoice;
		d.R = R;
	    d.G = G;
		d.B = B;
		d.stroke = stroke;
		return d;
	}
	
	/**
	 * ������Ʒ
	 */
	public void sendFlush() {
		try {
			pw.println("������Ʒ");
			if((guessSthing = (String)br.readLine()) != null) {
				drawJFrame.setTextValue(guessSthing);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		drawJFrame.setStratBar("�������ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mouseExited(MouseEvent me) {
		drawJFrame.setStratBar("����˳��ڣ�["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mousePressed(MouseEvent me) {
		drawJFrame.setStratBar("��갴���ڣ�["+me.getX()+" ,"+me.getY()+"]");//����״̬����ʾ
		
		itemList.add(createNewitem());
		
		itemList.get(index).x1 = itemList.get(index).x2 = me.getX();
		itemList.get(index).y1 = itemList.get(index).y2 = me.getY();
		itemList.get(index).isMove = false;
		
		//�����ǰѡ��Ϊ��ʻ�����Ƥ�� ,��ǰ�㴫��������
		if(currentChoice == 1 || currentChoice == 11){
			json = JSONObject.fromObject(itemList.get(index));
			pw.println(json.toString());
		}
		index++;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		drawJFrame.setStratBar("����ɿ��ڣ�["+me.getX()+" ,"+me.getY()+"]");
		if(currentChoice == 1 ||currentChoice == 11) {
			itemList.add(createNewitem());
			itemList.get(index-1).x1 = itemList.get(index).x2 = itemList.get(index).x1 = me.getX();
			itemList.get(index-1).y1 = itemList.get(index).y2 = itemList.get(index).y1 = me.getY();
			itemList.get(index).isMove = false;
		} else {
			index--;
			itemList.get(index).x2 = me.getX();
			itemList.get(index).y2 = me.getY();
		}
		//��ǰ�㴫��������
		json = JSONObject.fromObject(itemList.get(index));
		pw.println(json.toString());
		index++;
		repaint();
	}

}

/**
 * �����������Ĺ������϶�
 * @author Spirit
 *
 */
class MouseB extends MouseMotionAdapter {
	
	public void mouseDragged(MouseEvent me) {
		drawJFrame.setStratBar("����϶��ڣ�[" + me.getX() + " ," + me.getY() + "]");
		if(currentChoice == 1 || currentChoice == 11) {
			itemList.add(createNewitem());
			itemList.get(index-1).x1 = itemList.get(index).x2 = itemList.get(index).x1 = me.getX();
			itemList.get(index-1).y1 = itemList.get(index).y2 = itemList.get(index).y1 = me.getY();
			itemList.get(index).isMove = true;
			//��ǰ�㴫��������
			json = JSONObject.fromObject(itemList.get(index));
			pw.println(json.toString());
			index++;
		} else {
			itemList.get(index-1).x2 = me.getX();
			itemList.get(index-1).y2 = me.getY();
		}
		repaint();
	}
	
	public void mouseMoved(MouseEvent me) {
		drawJFrame.setStratBar("����ƶ��ڣ�["+me.getX()+" ,"+me.getY()+"]");
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
		color = JColorChooser.showDialog(drawJFrame, "��ѡ����ɫ", color);
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

	/**
	 * ��ջ��岢�����������������Ϣ
	 */
	public void clear() {
		itemList.clear();
		System.out.println("xxx");
		index = 0;
		repaint();
		pw.println("��ջ���");
	}

}
