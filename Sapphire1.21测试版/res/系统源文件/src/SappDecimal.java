import com.sapp.main.SappClass;


/**
 * Decimal����һ�����ص�С����
 * �ñ�������Double
 * */
public class SappDecimal {
	/**
	 * ���صĹ��췽��
	 * */
	public static Object create(String str){
		return Double.parseDouble(str);
	}
	
	/**
	 * ���صĲ���������
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
		}
		return (myself%param);
	}
	/**
	 * ת��Ϊ����
	 * @throws Exception
	 * */
	public static Object N_toInteger(SappClass self) throws Exception{
		return ((Double)(self.object)).longValue();
	}
	
	/**
	 * ==����
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
		}
	}
	
	/**
	 * !=����
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
	 * ��ϵ
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
		}
	}
	
}
