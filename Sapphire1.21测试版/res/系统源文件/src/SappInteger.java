import com.sapp.main.SappClass;


/**
 * 整数类使用Long
 * */
public class SappInteger {
	/**
	 * 本地的构造方法
	 * */
	public static Object create(String str){
		return Long.parseLong(str);
	}
	
	/**
	 * == 参数可以是小数或者整数
	 * @throws Exception 
	 * */
	public static Object equal(SappClass self,SappClass param1) throws Exception{	
		long myself = ((Long)self.object).longValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself == param;
		}else if(param1.className.equals("Decimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself == param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	/**
	 * != 参数可以是小数或者整数
	 * @throws Exception 
	 * */
	public static Object unequal(SappClass self,SappClass param1) throws Exception{
		long myself = ((Long)self.object).longValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself != param;
		}else if(param1.className.equals("Decimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself != param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	/**
	 * 关系
	 * */
	//< n
	public static Object less(SappClass self,SappClass param1) throws Exception{//%
		
		long myself = ((Long)self.object).longValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself < param;
		}else if(param1.className.equals("Decimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself < param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		
	}
	//<= nc
	public static Object lessequal(SappClass self,SappClass param1) throws Exception{//%
		long myself = ((Long)self.object).longValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself <= param;
		}else if(param1.className.equals("Decimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself <= param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	//> m
	public static Object more(SappClass self,SappClass param1) throws Exception{//%
		long myself = ((Long)self.object).longValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself > param;
		}else if(param1.className.equals("Decimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself > param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	//>= mc
	public static Object moreequal(SappClass self,SappClass param1) throws Exception{//%
		long myself = ((Long)self.object).longValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself >= param;
		}else if(param1.className.equals("Decimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself >= param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	
	/**
	 * 转换Decimal
	 * @throws Exception 
	 * */
	public static Object N_toDecimal(SappClass self) throws Exception{
		return ((Long)self.object).doubleValue();
	}
	
	/**
	 * toString
	 * @throws Exception 
	 * */
	public static Object N_toString(SappClass self) throws Exception{	
		return ""+((Long)(self.object)).longValue();	
	}
	
	/**
	 * 本地的操作符方法
	 * @throws Exception
	 * */
	//+
	public static Object addition(SappClass self,SappClass param1) throws Exception{//+
		
		long myself = ((Long)self.object).longValue();
		long param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).longValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).longValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return myself + param;
	}
	//-
	public static Object subtraction(SappClass self,SappClass param1) throws Exception{//-
		long myself = ((Long)self.object).longValue();
		long param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).longValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).longValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return myself - param;
	}
	//*
	public static Object multiplication(SappClass self,SappClass param1) throws Exception{//*
		long myself = ((Long)self.object).longValue();
		long param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).longValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).longValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return myself * param;
	}
	///
	public static Object division(SappClass self,SappClass param1) throws Exception{///
		long myself = ((Long)self.object).longValue();
		long param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).longValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).longValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return myself / param;
	}
	//%
	public static Object complementation(SappClass self,SappClass param1) throws Exception{//%
		long myself = ((Long)self.object).longValue();
		long param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).longValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).longValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return myself % param;
	}
}
