package com.godcan.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.Serializable;
//ͼ�λ����� ���ڻ��Ƹ���ͼ��
//���࣬����ͼ�ε�Ԫ���õ����еĽӿڣ�����ʹ�õ�
//���������Էŵ������У�������Ա����ظ�����

/*��ͨ��ʵ�� java.io.Serializable �ӿ������������л����ܡ�
δʵ�ִ˽ӿڵ��ཫ�޷�ʹ���κ�״̬���л������л���
�����л�������������ͱ����ǿ����л��ġ����л��ӿ�û�з������ֶΣ�
�����ڱ�ʶ�����л������塣*/


public class Drawing implements Serializable {


	private static final long serialVersionUID = 5154297285845966496L;
	public int x1,x2,y1,y2;   	    //������������
	public int  R,G,B;				//����ɫ������
	public float stroke ;			//����������ϸ������
	public int type;				//���廭������
	public boolean isMove;              //�Ƿ�Ϊ�ƶ�����

	//�����ͼ����
	public void draw(Graphics2D g2d ){
		
	}
}















