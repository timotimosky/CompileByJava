import com.sapp.main.SappClass;


/**
 * Decimal类是一个本地的小数类
 * 用本地类型Double
 * */
public class SappDecimal {
	/**
	 * 本地的构造方法
	 * */
	public static Object create(String str){
		return Double.parseDouble(str);
	}
	
	/**
	 * 本地的操作符方法
	 * @throws Exception
	 * */
	//+
	public static Object addition(SappClass self,SappClass param1) throws Exception{//+
	
		double myself = ((Double)self.object).doubleValue();
		double param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).doubleValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).doubleValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return (myself+param);
	
	}
	//-
	public static Object subtraction(SappClass self,SappClass param1) throws Exception{//-
		
		double myself = ((Double)self.object).doubleValue();
		double param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).doubleValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).doubleValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return (myself-param);
		
	}
	//*
	public static Object multiplication(SappClass self,SappClass param1) throws Exception{//*
		double myself = ((Double)self.object).doubleValue();
		double param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).doubleValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).doubleValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return (myself*param);
	}
	///
	public static Object division(SappClass self,SappClass param1) throws Exception{///
		double myself = ((Double)self.object).doubleValue();
		double param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).doubleValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).doubleValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return (myself/param);
	}
	//%
	public static Object complementation(SappClass self,SappClass param1) throws Exception{//%
		double myself = ((Double)self.object).doubleValue();
		double param;
		if(param1.className.equals("SappInteger")){
			param = ((Long)param1.object).doubleValue();		
		}else if(param1.className.equals("SappDecimal")){
			param = ((Double)param1.object).doubleValue();
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
		return (myself%param);
	}
	/**
	 * 转换为整数
	 * @throws Exception
	 * */
	public static Object N_toInteger(SappClass self) throws Exception{
		return ((Double)(self.object)).longValue();
	}
	
	/**
	 * ==方法
	 * @throws Exception
	 * */
	public static Object equal(SappClass self,SappClass param1) throws Exception{//%
		double myself = ((Double)self.object).doubleValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself == param;
		}else if(param1.className.equals("SappDecimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself == param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	
	/**
	 * !=方法
	 * @throws Exception
	 * */
	public static Object unequal(SappClass self,SappClass param1) throws Exception{//%
		double myself = ((Double)self.object).doubleValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself != param;
		}else if(param1.className.equals("SappDecimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself != param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	
	/**
	 * toString
	 * @throws Exception 
	 * */
	public static Object N_toString(SappClass self) throws Exception{
		return ""+((Double)(self.object)).doubleValue();
	}
	
	/**
	 * 关系
	 * */
	//< n
	public static Object less(SappClass self,SappClass param1) throws Exception{//%
		double myself = ((Double)self.object).doubleValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself < param;
		}else if(param1.className.equals("SappDecimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself < param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	
	//<= nc
	public static Object lessequal(SappClass self,SappClass param1) throws Exception{//%
		double myself = ((Double)self.object).doubleValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself <= param;
		}else if(param1.className.equals("SappDecimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself <= param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	//> m
	public static Object more(SappClass self,SappClass param1) throws Exception{//%
		double myself = ((Double)self.object).doubleValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself > param;
		}else if(param1.className.equals("SappDecimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself > param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	//>= mc
	public static Object moreequal(SappClass self,SappClass param1) throws Exception{//%
		double myself = ((Double)self.object).doubleValue();
		if(param1.className.equals("SappInteger")){
			long param = ((Long)param1.object).longValue();
			return myself >= param;
		}else if(param1.className.equals("SappDecimal")){
			double param = ((Double)param1.object).doubleValue();
			return myself >= param;
		}else{
			throw new Exception("没有这种类型的操作参数:"+param1.className);
		}
	}
	
}
