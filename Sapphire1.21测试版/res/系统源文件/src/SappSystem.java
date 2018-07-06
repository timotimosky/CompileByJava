
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import com.sapp.main.SappClass;
import com.sapp.main.SappEngine;
import com.sapp.tools.SappClassLoader;

/**
 * 系统类
 * */
public class SappSystem {
	
	//默认构造器
	
	/**
	 * 这个引擎类
	 * */
	private SappEngine eng = null;
	//设置这个引擎。
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
	 * 读取类
	 * */
	private Scanner scanner = null;
	/**
	 * 打印类
	 * */
	private PrintWriter pw = null;
 	
	/**
	 * 加载java类
	 * @throws Exception 
	 * */
	public void loadJavaClass(SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("没有这种类型的操作参数:"+param.className);
		}else{
			String classn = (String)(param.object);
			
			Class<?> cls =new SappClassLoader().loadClassFile(new File(classn));
			
			eng.javaObjs.put(classn, cls.newInstance());
		}
	}
	
	/**
	 * 加载一个文件中的类
	 * @throws Exception 
	 * */
	public void load(SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("没有这种类型的操作参数:"+param.className);
		}else{
			eng.load((java.lang.String)(param.object));
		}
	}
	/**
	 * 打印
	 * @throws Exception 
	 * */
	public void printString(SappClass str) throws Exception {
		if(!str.className.equals("SappString")){
			throw new Exception("没有这种类型的操作参数:"+str.className);
		}else{
			pw.print((String)(str.object));
			pw.flush();
		}
	}
	
	/**
	 * 读取
	 * @throws Exception 
	 * */
	public Object readLine() throws Exception{	
		return scanner.nextLine();
	}
	
	/**
	 * 退出
	 * @throws Exception 
	 * */
	public void exit(SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("需要SappInteger类型的参数，找到:"+param.className);
		}else{
			eng.runstate = ((Long)(param.object)).intValue();
		}
	}
	
}
