package com.godcan.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import com.godcan.filesave.FileSave;
import com.godcan.panel.DrawPanel;

/**
 * ������
 * @author Spirit
 *
 */
public class DrawJFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6659463605220271263L;
	private JToolBar buttonpanel;//���尴ť���
	private JMenuBar bar ;//����˵���
	private JMenu file,color,stroke;//����˵�
	private JMenuItem savefile,exit;//file �˵��еĲ˵���
	private JMenuItem colorchoice,strokeitem;//help �˵��еĲ˵���
	private Icon sf;//�ļ��˵����ͼ�����
	private JLabel startbar;//״̬��
	private DrawPanel drawPanel;//������Ķ���
	private FileSave fileclass ;//�ļ�����
	private JPanel pan;
	private JTextField jtf;//�����ı���
	private JButton flush;//ˢ�°�ť
	private JButton renew;//���»���ť
	String[] fontName; 
	//���幤����ͼ�������
	private String names[] = {"savefile","pen","line"
			,"rect","frect","oval","foval","circle","fcircle"
			,"roundrect","froundrect","rubber","color"
			,"stroke"};//���幤����ͼ�������
	private Icon icons[];//����ͼ������
	
	private String tiptext[] = {//����������Ƶ���Ӧ�İ�ť�ϸ�����Ӧ����ʾ
			"����ͼƬ","��ʻ�","��ֱ��","�����ĵľ���",
			"������","�����ĵ���Բ","�����Բ"
			,"�����ĵ�Բ","���Բ","��Բ�Ǿ���","���Բ�Ǿ���"
			,"��Ƥ��","��ɫ","ѡ�������Ĵ�ϸ"};
	JButton button[];//���幤�����еİ�ť��
	public DrawJFrame(String string) {
		// TODO ������Ĺ��캯��
		super(string);
	    //�˵��ĳ�ʼ��
	    file = new JMenu("�ļ�");
	    color = new JMenu("��ɫ");
	    stroke = new JMenu("����");
	    //�˵����ĳ�ʼ��
	    bar = new JMenuBar();
	    
	    //�˵�����Ӳ˵�
	    bar.add(file);
	    bar.add(color);
	    bar.add(stroke);
	    
	    //��������Ӳ˵���
	    setJMenuBar(bar);
	    
	    //�˵�����ӿ�ݼ�
	    file.setMnemonic('F');//����ALT+��F��
	    color.setMnemonic('C');//����ALT+��C��
	    stroke.setMnemonic('S');//����ALT+��S��
	   
//	    //File �˵���ĳ�ʼ��
//	    try {
//			Reader reader = new InputStreamReader(getClass().getResourceAsStream("/com/godcan/icon"));//��ȡ�ļ�����·��Ϊ��׼
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(this,"ͼƬ��ȡ����","����",JOptionPane.ERROR_MESSAGE);
//		}
	    sf = new ImageIcon(this.getClass().getResource("images/savefile.jpg"));
	    savefile = new JMenuItem("����",sf);
	    exit = new JMenuItem("�˳�");
	    
	    //File �˵�����Ӳ˵���
	    file.add(savefile);
	    file.add(exit);
	    
	    //File �˵�����ӿ�ݼ�
	    savefile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
	    exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.CTRL_MASK));
	   
	    //File �˵����ע�����
	    savefile.addActionListener(this);
	    exit.addActionListener(this);
	    
	    //Color �˵���ĳ�ʼ��
	    colorchoice = new JMenuItem("��ɫ��");
	    colorchoice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
	    colorchoice.addActionListener(this);
	    color.add(colorchoice);
	    
	    //Stroke �˵���ĳ�ʼ��
	    strokeitem = new JMenuItem("���û���");
	    strokeitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_MASK));
	    stroke.add(strokeitem);
	    strokeitem.addActionListener(this);
	    
	    //�������ĳ�ʼ��
	    buttonpanel = new JToolBar(JToolBar.HORIZONTAL);
	    icons = new ImageIcon[names.length];
	    button = new JButton[names.length];
	    for(int i = 0; i < names.length; i++) {
	        icons[i] = new ImageIcon(this.getClass().getResource("images/"+names[i]+".jpg"));//���ͼƬ������·��Ϊ��׼��
	    	button[i] = new JButton("",icons[i]);//�����������еİ�ť
	    	button[i].setToolTipText(tiptext[i]);//����������Ƶ���Ӧ�İ�ť�ϸ�����Ӧ����ʾ
	    	buttonpanel.add(button[i]);
	    	button[i].setBackground(Color.red);
	    	if(i<1){
	    		button[i].addActionListener(this);
	    	} else if(i<=13) {
	        	button[i].addActionListener(this);	        
	        }
	    }
	   //״̬���ĳ�ʼ��
	    startbar = new JLabel("�ҵ�СС��ͼ��");
	    
	    fileclass = new FileSave(drawPanel);
	    //���ĳ�ʼ��
	    pan = new JPanel();
	    jtf = new JTextField(10);
	    jtf.setEditable(false);
	    //��ʼ��ˢ�°�ť
	    flush = new JButton("������Ʒ");
	    flush.addActionListener(this);
	    pan.add(jtf);
	    pan.add(flush);
	    //�滭���ĳ�ʼ��
	    drawPanel = new DrawPanel(this);
	    renew = new JButton("ˢ�»���");
	    renew.addActionListener(this);
	    buttonpanel.add(renew);
	   
	    Container con = getContentPane();//�õ��������
	    con.add(buttonpanel, BorderLayout.NORTH);
	    con.add(drawPanel,BorderLayout.CENTER);
	    con.add(startbar,BorderLayout.SOUTH);
	    con.add(pan,BorderLayout.EAST);
	    Toolkit tool = getToolkit();//�õ�һ��Tolkit��Ķ�����Ҫ���ڵõ���Ļ�Ĵ�С��
	    Dimension dim = tool.getScreenSize();//�õ���Ļ�Ĵ�С ������Dimension����
	    setBounds(40,40,dim.width-370,dim.height-300);
	    setVisible(true);
	    validate();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//����״̬����ʾ���ַ�
	public void setStratBar(String s) {
		startbar.setText(s);
	}
	public void actionPerformed(ActionEvent e) {
		for(int i = 1; i <= 11; i++) {
			if(e.getSource() == button[i]) {
				drawPanel.setCurrentChoice(i);
				drawPanel.createNewitem();
				drawPanel.repaint();
		    }
		}
		if(e.getSource() == savefile|| e.getSource() == button[0]){   //���� 
			fileclass.saveFile();
		} else if(e.getSource() == flush) {  //ˢ���ı���
			drawPanel.sendFlush();
		} else if(e.getSource() == renew) {  //ˢ�»���
			drawPanel.clear();
		} else if(e.getSource() == exit) {  //�˳����� 
				System.exit(0);
		} else if(e.getSource() == colorchoice|| e.getSource() == button[12]) {    //������ɫ�Ի���
			drawPanel.chooseColor();//��ɫ��ѡ��
	    } else if(e.getSource() == button[13]|| e.getSource()==strokeitem) {    //���ʴ�ϸ
	    	drawPanel.setStroke();//���ʴ�ϸ�ĵ���
		}
	}
	
	/**
	 * ���ı������û�����Ʒ
	 * @param s
	 */
	public void setTextValue(String s) {
		jtf.setText(s);
	}
	
}
