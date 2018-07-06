import com.sapp.main.SappClass;


/**
 * String类
 * 主要使用Java中的String类
 * */
public class SappString {
	/**
	 * 本地的构造方法
	 * */
	public static Object create(String str){
		return str.substring(1, str.length()-1);
	}
	
	/**
	 * mate方法
	 * @throws Exception 
	 * */
	public static Object mate(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappString")){
			throw new Exception("需要SappString类型的参数，找到了:"+param.className);
		}
		return (java.lang.String)self.object+(java.lang.String)param.object;
	}
	
	/**
	 * charAt方法
	 * @throws Exception 
	 * */
	public static Object N_charAt(SappClass self,SappClass param) throws Exception{		
		int index = 0;
		if(param.className.equals("SappDecimal")){
			index = ((Double)(param.object)).intValue();
		}else if(param.className.equals("SappInteger")){
			index = ((Long)(param.object)).intValue();
		}
		String selfstr = (String)(self.object);
		if(index<0&&index>selfstr.length()){
			throw new Exception("字符串数组越界异常。");
		}
		return selfstr.charAt(index);		
	}
	
	/**
	 * equalsString方法
	 * @throws Exception 
	 * */
	public static Object equalsString(SappClass self,SappClass param) throws Exception{	
		if(!param.className.equals("SappString")){
			throw new Exception("需要SappString类型的参数，找到了:"+param.className);
		}
		return new Boolean(((String)self.object).equals((param.object)));
	}
}
