package com.godcan.jframe;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.*;

import com.godcan.filesave.FileSave;
import com.godcan.panel.GuessPanel;

/**
 * ������
 * @author Spirit
 *
 */
public class GuessJFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6659463605220271263L;
	private JLabel startbar;//״̬��
	private GuessPanel guessPanel;//������Ķ���
	private JPanel pan;  //����µĻ���
	private JTextField jtf;//�µ��ı���
	private JButton submit;//�ύ��ť
	
	public GuessJFrame(String string) {
		super(string);
	   //״̬���ĳ�ʼ��
	    startbar = new JLabel("�ҵ�СС��ͼ��");
	    //�滭���ĳ�ʼ��
	    guessPanel = new GuessPanel(this);
	    //�µĳ�ʼ��
	    pan = new JPanel();
	    jtf = new JTextField(10);
	    submit = new JButton("�ύ��");
	    submit.addActionListener(this);
	    pan.add(jtf);
	    pan.add(submit);
	    
	    Container con = getContentPane();//�õ��������
	    con.add(guessPanel,BorderLayout.CENTER);
	    con.add(startbar,BorderLayout.SOUTH);
	    con.add(pan,BorderLayout.EAST);
	    Toolkit tool = getToolkit();//�õ�һ��Tolkit��Ķ�����Ҫ���ڵõ���Ļ�Ĵ�С��
	    Dimension dim = tool.getScreenSize();//�õ���Ļ�Ĵ�С ������Dimension����
	    setBounds(40,40,dim.width-370,dim.height-300);
	    setVisible(true);
	    validate();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * ����״̬����ʾ���ַ�
	 * @param s
	 */
	public void setStratBar(String s) {
		startbar.setText(s);
	}
	
	/**
	 * �����ı����ֵ
	 * @param s
	 */
	public void setTextValue(String s) {
		jtf.setText(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == submit) {  //��ť�������Ϣ��ֵ
//			guessPanel.setMessage(jtf.getText());
			guessPanel.sendGuessResult(jtf.getText());
		}
	}
	
}
