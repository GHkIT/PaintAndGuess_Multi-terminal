package com.godcan.client;

import javax.swing.UIManager;

import com.godcan.paint.DrawPad;

/**
 * �������
 * @author Spirit
 *
 */
public class MiniDrawPad {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		DrawPad drawpad = new DrawPad("�㻭�Ҳ�С��Ϸ");
	}

}
