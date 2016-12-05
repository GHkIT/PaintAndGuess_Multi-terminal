package com.godcan.filesave;

import java.io.*;

import javax.swing.*;

import com.godcan.entity.Drawing;
import com.godcan.paint.DrawArea;

//�ļ��� ���ļ��Ĵ򿪡��½������棩
public class FileSave {
	
    private DrawArea drawArea;
    
    public FileSave(DrawArea drawPanel) {
    	this.drawArea = drawPanel;
	}
    
    /**
     * ����ͼ��
     */
	public void saveFile() {
		
		//JFileChooser Ϊ�û�ѡ���ļ��ṩ��һ�ּ򵥵Ļ���
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//setFileSelectionMode()���� JFileChooser���������û�ֻѡ���ļ���ֻѡ��Ŀ¼�����߿�ѡ���ļ���Ŀ¼��
		int result = filechooser.showSaveDialog(drawArea);
		if(result == JFileChooser.CANCEL_OPTION){
        	return ;
        }
        
        File fileName = filechooser.getSelectedFile();//getSelectedFile()����ѡ�е��ļ�
	    fileName.canWrite();//����Ӧ�ó����Ƿ�����޸Ĵ˳���·������ʾ���ļ�
	    if(fileName == null || fileName.getName().equals(""))//�ļ���������ʱ
	    {
	    	JOptionPane.showMessageDialog(filechooser,"�ļ���","�������ļ�����",JOptionPane.ERROR_MESSAGE);
	    } else {
	    	try {
				fileName.delete();//ɾ���˳���·������ʾ���ļ���Ŀ¼
				FileOutputStream fos = new FileOutputStream(fileName+".xxh");//�ļ���������ֽڵķ�ʽ���
				//���������
				ObjectOutputStream output = new ObjectOutputStream(fos);
				//Drawing record;
				
				output.writeInt(drawArea.getIndex());
				
				for(int i = 0;i < drawArea.getIndex(); i++) {
					Drawing p = drawArea.getItemList().get(i);
					output.writeObject(p);
					output.flush();//ˢ�¸����Ļ��塣�˲�����д�������ѻ��������ֽڣ���������ˢ�µ��ײ����С�
					               //�����е�ͼ����Ϣǿ�Ƶ�ת���ɸ������Ի��洢���ļ���    
				}
				output.close();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
}
