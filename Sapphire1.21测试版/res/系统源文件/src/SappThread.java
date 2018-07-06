import com.sapp.main.SappClass;
import com.sapp.main.SappMethod;


/**
 * ����߳���
 * ʹ��java��Thread��
 * */
public class SappThread extends Thread{
	/**
	 * ִ�еķ���
	 * Ĭ����run������
	 * */
	private String runmethod = "";
	public void setRunMethod(String rmn){
		runmethod = rmn;
	}
	/**
	 * ִ�еĶ���
	 * */
	private SappClass exeobj = null;
	public void setSappClass(SappClass sc){
		exeobj = sc;
	}
	public void run(){
		//�ҵ������Ҫִ�еķ���
		SappMethod met = exeobj.getMethodByName(exeobj.className, runmethod);
		
		try{
			if(met.isSynchronized){//ִ�е���һ��ͬ���ķ�����
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
			System.out.println("�߳�:"+this+",ִ�ж���:"+exeobj+","+runmethod+"�����쳣��");
		}
	}
	
	/**
	 * ���̡߳�
	 * */
	public void mainThread(SappClass self){
		self.object = Thread.currentThread();
	}
	
	/**
	 * �����µ��̡߳�
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
			throw new Exception("δȷ���߳�ִ�еķ�����");
		}
		
	}
	
	/**
	 * sleep
	 * */
	@SuppressWarnings("static-access")
	public static void N_sleep(SappClass self,SappClass parm){
		try {
			//System.out.println("˯����:"+((Long)parm.object).longValue());
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

