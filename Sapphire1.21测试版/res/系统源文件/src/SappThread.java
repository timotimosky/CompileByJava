import com.sapp.main.SappClass;
import com.sapp.main.SappMethod;


/**
 * 这个线程类
 * 使用java中Thread类
 * */
public class SappThread extends Thread{
	/**
	 * 执行的方法
	 * 默认是run方法。
	 * */
	private String runmethod = "";
	public void setRunMethod(String rmn){
		runmethod = rmn;
	}
	/**
	 * 执行的对象
	 * */
	private SappClass exeobj = null;
	public void setSappClass(SappClass sc){
		exeobj = sc;
	}
	public void run(){
		//找到这个类要执行的方法
		SappMethod met = exeobj.getMethodByName(exeobj.className, runmethod);
		
		try{
			if(met.isSynchronized){//执行的是一个同步的方法。
				synchronized(met){
					met = met.clone();

					met.setExeObject(exeobj);
					
					met.exeMethod();
				}
			}else{
				met = met.clone();

				met.setExeObject(exeobj);
				
				met.exeMethod();
			}
		}catch(Exception e){
			System.out.println("线程:"+this+",执行对象:"+exeobj+","+runmethod+"方法异常。");
		}
	}
	
	/**
	 * 主线程。
	 * */
	public void mainThread(SappClass self){
		self.object = Thread.currentThread();
	}
	
	/**
	 * 构造新的线程。
	 * */
	public Object createThread(SappClass parm){
		SappThread sapt = new SappThread();
		sapt.setSappClass(parm);
		return sapt;
	}
	
	/**
	 * start
	 * @throws Exception 
	 * */
	public static void N_start(SappClass self,SappClass parm) throws Exception{
		if(parm.className.equals("SappString")){
			SappThread thisthread = (SappThread)self.object;
			thisthread.setRunMethod((String)parm.object);
			thisthread.start();
		}else{
			throw new Exception("未确定线程执行的方法！");
		}
		
	}
	
	/**
	 * sleep
	 * */
	@SuppressWarnings("static-access")
	public static void N_sleep(SappClass self,SappClass parm){
		try {
			//System.out.println("睡眠了:"+((Long)parm.object).longValue());
			((Thread)self.object).sleep(((Long)parm.object).longValue());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * interrupt
	 * */
	public static void N_interrupt(SappClass self){
		((Thread)self.object).interrupt();
	}
	
	/**
	 * isAlive
	 * */
	public static Object N_isAlive(SappClass self){
		return new Boolean(((Thread)self.object).isAlive());
	}
	
	/**
	 * isInterrupted
	 * */
	public static Object N_isInterrupted(SappClass self){
		return new Boolean(((Thread)self.object).isInterrupted());
	}
	
	/**
	 * currentThread
	 * */
	public static Object N_currentThread(SappClass self) throws Exception{
		return (Thread)(self.object);
	}
}

