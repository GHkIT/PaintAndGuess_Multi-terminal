package com.godcan.client;

import javax.swing.UIManager;

import com.godcan.jframe.DrawJFrame;

/**
 * �������
 * @author Spirit
 *
 */
public class DrawClient {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		DrawJFrame drawJFrame = new DrawJFrame("�㻭�Ҳ�С��Ϸ");
		drawJFrame.setSize(1200, 600);
	}

}
