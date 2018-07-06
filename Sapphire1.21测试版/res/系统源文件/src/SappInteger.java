import com.sapp.main.SappClass;


/**
 * ������ʹ��Long
 * */
public class SappInteger {
	/**
	 * ���صĹ��췽��
	 * */
	public static Object create(String str){
		return Long.parseLong(str);
	}
	
	/**
	 * == ����������С����������
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
		}
	}
	/**
	 * != ����������С����������
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
		}
	}
	/**
	 * ��ϵ
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
		}
	}
	
	/**
	 * ת��Decimal
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
	 * ���صĲ���������
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
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
			throw new Exception("û���������͵Ĳ�������:"+param1.className);
		}
		return myself % param;
	}
}
