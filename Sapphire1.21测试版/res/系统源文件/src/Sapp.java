

import java.io.File;
import java.util.Scanner;

import com.sapp.main.SappEngine;

public class Sapp {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String []args){

		SappEngine re = new SappEngine();
		try {
			//����Ĭ�ϵı���class·����
			re.setNativeClassPath("res/native");
			//�������Լ��
			re.load("res/lang/SappBasicConfigration.sph");
			//���������ļ�
			re.load("res/Test/Hash/hash1.sph");
			//Tools.printVector(re.commnds);
			re.exeBegin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/*Scanner input = new Scanner(System.in); 
		SappEngine re = new SappEngine();
		//����Ĭ�ϵı���class·����
		re.setNativeClassPath("res/native");
		//�������Լ��
		try {
			//re.setCompCodePath("res/complied");
			
			re.load("res/lang/SappBasicConfigration.sph");
			
		} catch (Exception e1) {
			System.out.println("�������Լ���ļ�ʧ��!");
			e1.printStackTrace();
		}
		while(true){
			System.out.println("������Ҫִ�еĲ���:lf-�����ļ�,ri-���뽻������,et-�˳�����");
			String str = input.nextLine().trim();
			if(str.equals("lf")){
				System.out.println("������Ҫ���ص��ļ�(·��):������������Sapp�е�begin���ϡ�");
				str = input.nextLine().trim();
				File file = new File(str);
				if(!file.exists()){
					System.out.println("�Բ���,�ļ�������!");
					System.out.print("���س�������...");
					input.nextLine();
				}else{
					try {
						//���begin
						re.clearBegin();
						//�����ļ�
						re.load(str);
						//ִ��begin
						re.exeBegin();
						System.out.print("���س�������...");
						input.nextLine();
					} catch (Exception e) {
						e.printStackTrace();
						System.out.print("���س�������...");
						input.nextLine();
					}
				}
			}else if(str.equals("ri")){
				StringBuffer cjml = new StringBuffer();
				System.out.println("�����˽���ģʽ:��������Ϊ���ӵ�ǰ����(�����-c ��ͷ��Ϊ����ִ��),exe-ִ�д���" +
						"�õ�����(����մ�����),cr-������Ӻõ�����,et-�˳�����ģʽ");
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
					}else if(str.equals("cr")){
						cjml = new StringBuffer();
					}else if(str.equals("et")){
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
			}else if(str.equals("et")){
				break;
			}else{
				System.out.println("δ֪������!");
			}
		}*/
		
	}

}
