package com.godcan.client;

import javax.swing.UIManager;

import com.godcan.jframe.GuessJFrame;

/**
 * �������
 * @author Spirit
 *
 */
public class GuessClient {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		GuessJFrame guessJFrame = new GuessJFrame("�㻭�Ҳ�С��Ϸ");
		guessJFrame.setSize(1000, 485);
	}

}
