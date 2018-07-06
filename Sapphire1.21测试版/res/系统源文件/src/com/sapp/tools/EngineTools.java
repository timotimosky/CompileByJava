package com.sapp.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Engine�����ࡣ
 * */
public class EngineTools {
	/**
	 * �������ȥ���ļ��е�ע�͡�
	 * */
	public static String dealCode(File file) throws Exception{

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);//������
		
		//�����Ĵ��뻺����
		StringBuilder code = new StringBuilder();
		
		//��ǰ�����״̬
		boolean codestate = true;//����״̬����һ��Ϊע��״̬ ע��Ϊ #һ��
		
		boolean isstring = false;//�Ƿ������ַ���״̬����Ҫ�����һЩ�����ַ�
		boolean ischar = false;//�ַ�״̬
		boolean iszy = false;//ת��״̬
		
		int linenum = 0;//��ǰ������
		String nowdel = null;
		while((nowdel=br.readLine())!=null){
			linenum++;
			//����ÿһ��
			for(int i = 0;i<nowdel.length();i++){
				//��õ�ǰ���ַ�
				char check = nowdel.charAt(i);
				
				//����״̬
				if(codestate){
					if(!isstring&&!ischar){//���ַ���״̬
						if(check=='/'){//���ܵ���ע��״̬��
							if(i+1<nowdel.length()){
								if(nowdel.charAt(i+1)=='*'){//��������: /*
									i++;
									codestate = false;//��ǰ�����Ѿ����Ǵ���״̬��
									continue;
								}
							}
						}else if(check=='#'){
							codestate = true;
							break;//��һ���Ѿ���ע����
						}else if(check=='\n'||check=='\t'){//�����������ַ�
							continue;
						}
						
						//����������Ĵ����ж�����
						if(check=='\"'){//��������� ���ַ����ж��Ƿ����������ַ���״̬
							isstring = true;
						}else if(check=='\''){//�����ַ�״̬�ж���
							ischar = true;
						}
						
					}else{//���ַ��������ַ���
						if(!iszy){//����ת���״̬��������
							if(check=='\"'){//��������� ���ַ����ж��Ƿ����������ַ���״̬
								isstring = false;
							}else if(check=='\''){//�����ַ�״̬�ж���
								ischar = false;
							}else if(check=='\\'){//������ת��״̬
								iszy = true;
							}
						}else{//��ת��״̬
							iszy = false;
						}
					}
					//����
					code.append(check);
				}else{//�Ǵ���״̬
					if(check=='*'){//���ܵ��⿪ע��״̬
						if(i+1<nowdel.length()){
							if(nowdel.charAt(i+1)=='/'){
								i++;
								codestate = true;//��ԭ����״̬
								continue;
							}
						}
					}
				}
			}
		}
		//���봦����ϣ�������ڵ�״̬
		if(!codestate){//���Ǳ��״̬
			throw new Exception("����:\nע��δ����������\n"+
					"����:"+linenum+"\n");
		}
		if(isstring){
			throw new Exception("����:\n�ַ���δ����������\n"+
					"����:"+linenum+"\n");
		}
		if(ischar){
			throw new Exception("����:\n�ַ�δ����������\n"+
					"����:"+linenum+"\n");
		}
		//ȥ������Ŀո�
		return code+"";
	}
}
