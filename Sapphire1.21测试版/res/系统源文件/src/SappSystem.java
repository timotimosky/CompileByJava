
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import com.sapp.main.SappClass;
import com.sapp.main.SappEngine;
import com.sapp.tools.SappClassLoader;

/**
 * ϵͳ��
 * */
public class SappSystem {
	
	//Ĭ�Ϲ�����
	
	/**
	 * ���������
	 * */
	private SappEngine eng = null;
	//����������档
	public void setEngine(SappEngine e){
		eng = e;
		if(e.in!=null){
			scanner = new Scanner(e.in);
		}
		if(e.out!=null){
			pw = new PrintWriter(e.out);
		}
	}
	
	/**
	 * ��ȡ��
	 * */
	private Scanner scanner = null;
	/**
	 * ��ӡ��
	 * */
	private PrintWriter pw = null;
 	
	/**
	 * ����java��
	 * @throws Exception 
	 * */
	public void loadJavaClass(SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("û���������͵Ĳ�������:"+param.className);
		}else{
			String classn = (String)(param.object);
			
			Class<?> cls =new SappClassLoader().loadClassFile(new File(classn));
			
			eng.javaObjs.put(classn, cls.newInstance());
		}
	}
	
	/**
	 * ����һ���ļ��е���
	 * @throws Exception 
	 * */
	public void load(SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("û���������͵Ĳ�������:"+param.className);
		}else{
			eng.load((java.lang.String)(param.object));
		}
	}
	/**
	 * ��ӡ
	 * @throws Exception 
	 * */
	public void printString(SappClass str) throws Exception {
		if(!str.className.equals("SappString")){
			throw new Exception("û���������͵Ĳ�������:"+str.className);
		}else{
			pw.print((String)(str.object));
			pw.flush();
		}
	}
	
	/**
	 * ��ȡ
	 * @throws Exception 
	 * */
	public Object readLine() throws Exception{	
		return scanner.nextLine();
	}
	
	/**
	 * �˳�
	 * @throws Exception 
	 * */
	public void exit(SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("��ҪSappInteger���͵Ĳ������ҵ�:"+param.className);
		}else{
			eng.runstate = ((Long)(param.object)).intValue();
		}
	}
	
}
