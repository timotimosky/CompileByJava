package com.sapp.main;

import java.io.File;
import java.util.Scanner;

public class Sapp {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String []args){

		/*SappEngine re = new SappEngine();
		try {
			//����Ĭ�ϵı���class·����
			re.setNativeClassPath("res/native");
			//�������Լ��
			re.load("res/lang/SappBasicConfigration.sph");
			//���������ļ�
			re.load("res/test.sph");
			//Tools.printVector(re.commnds);
			re.exeBegin();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		Scanner input = new Scanner(System.in); 
		SappEngine re = new SappEngine();
		//����Ĭ�ϵı���class·����
		re.setNativeClassPath("res/native");
		//�������Լ��
		try {
			re.load("res/lang/SappBasicConfigration.sph");
		} catch (Exception e1) {
			System.out.println("�������Լ���ļ�ʧ��!");
			e1.printStackTrace();
		}
		while(true){
			System.out.println("������Ҫִ�еĲ���:loadfile-�����ļ�,exebegin-ִ��begin��,enter-���뽻������,exit-�˳�����");
			String str = input.nextLine().trim();
			if(str.equals("loadfile")){
				System.out.println("������Ҫ���ص��ļ�:(·��)");
				str = input.nextLine().trim();
				File file = new File(str);
				if(!file.exists()){
					System.out.println("�Բ���,�ļ�������!");
					System.out.print("���س�������...");
					input.nextLine();
				}else{
					try {
						re.load(str);
						//re.exeBegin();
						System.out.print("�����ļ�"+str+"�ɹ�.\n");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else if(str.equals("exebegin")){//���п�ʼ��
				System.out.println("������Ҫִ�е�begin��:(����ֱ�ӻس�ִ�����е�begin�Ρ�)");
				String beginstr = input.nextLine().trim();
				try {
					if(beginstr.equals("")){
						re.exeBegin();
					}else{
						re.exeBegin(beginstr);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(str.equals("enter")){
				StringBuffer cjml = new StringBuffer();
				System.out.println("�����˽���ģʽ:��������Ϊ���ӵ�ǰ����(�����-c ��ͷ��Ϊ����ִ��),exe-ִ�д���" +
						"�õ�����(����մ�����),clear-������Ӻõ�����,exit-�˳�����ģʽ");
				while(true){
					System.out.print(">");
					str = input.nextLine().trim();
					if(str.equals("exe")){
						try {
							re.exeCode(cjml+"");
							System.out.println();
							cjml = new StringBuffer();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else if(str.equals("clear")){
						cjml = new StringBuffer();
					}else if(str.equals("exit")){
						break;
					}else{
						if(str.startsWith("-c")){
							if(str.length()>2){
								str = str.substring(2).trim();
								try {
									re.exeCode(str);
									System.out.println();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								System.out.println("��Ч������:"+str);
							}
						}else{
							cjml.append(str);
						}
					}
				}
			}else if(str.equals("exit")){
				break;
			}else{
				System.out.println("δ֪������!");
			}
		}
		
	}

}
