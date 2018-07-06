import com.sapp.main.SappClass;


public class SappMath {
	/**
	 * abs
	 * @throws Exception 
	 * */
	public static Object abs(SappClass param) throws Exception{
		SappClass news = param.clone();
		if(param.className.equals("SappDecimal")){
			double parm = (Double)(param.object);
			news.object = Math.abs(parm);
			return news;
		}else if(param.className.equals("SappInteger")){
			long parm = ((Long)(param.object));
			news.object = new Long((long)Math.abs(parm));
			return news;
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
	}
	
	/**
	 * acos
	 * @throws Exception 
	 * */
	public static Object acos(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.acos(parm));
	}
	
	/**
	 * asin
	 * @throws Exception 
	 * */
	public static Object asin(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.asin(parm));
	}
	
	/**
	 * atan
	 * @throws Exception 
	 * */
	public static Object atan(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.atan(parm));
	}

	/**
	 * cos
	 * @throws Exception 
	 * */
	public static Object cos(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.cos(parm));
	}
	
	/**
	 * sin
	 * @throws Exception 
	 * */
	public static Object sin(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.sin(parm));
	}
	
	/**
	 * tan
	 * @throws Exception 
	 * */
	public static Object tan(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.tan(parm));
	}
	
	/**
	 * cbrt
	 * @throws Exception 
	 * */
	public static Object cbrt(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.cbrt(parm));
	}
	
	/**
	 * ceil
	 * @throws Exception 
	 * */
	public static Object ceil(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.ceil(parm));
	}
	
	/**
	 * cosh
	 * @throws Exception 
	 * */
	public static Object cosh(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.cosh(parm));
	}
	
	/**
	 * exp
	 * @throws Exception 
	 * */
	public static Object exp(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.exp(parm));
	}
	
	/**
	 * expm1
	 * @throws Exception 
	 * */
	public static Object expm1(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.expm1(parm));
	}
	
	/**
	 * floor
	 * @throws Exception 
	 * */
	public static Object floor(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.floor(parm));
	}
	
	/**
	 * hypot
	 * @throws Exception 
	 * */
	public static Object hypot(SappClass param1,SappClass param2) throws Exception{
		double parm1,parm2;
		if(param1.className.equals("SappDecimal")){
			parm1 = (Double)(param1.object);
		}else if(param1.className.equals("SappInteger")){
			parm1 = ((Long)(param1.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param1.className);
		}
		if(param2.className.equals("SappDecimal")){
			parm2 = (Double)(param2.object);
		}else if(param2.className.equals("SappInteger")){
			parm2 = ((Long)(param2.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param2.className);
		}
		return (Double)(Math.hypot(parm1,parm2));
	}
	
	/**
	 * IEEEremainder
	 * @throws Exception 
	 * */
	public static Object IEEEremainder(SappClass param1,SappClass param2) throws Exception{
		double parm1,parm2;
		if(param1.className.equals("SappDecimal")){
			parm1 = (Double)(param1.object);
		}else if(param1.className.equals("SappInteger")){
			parm1 = ((Long)(param1.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param1.className);
		}
		if(param2.className.equals("SappDecimal")){
			parm2 = (Double)(param2.object);
		}else if(param2.className.equals("SappInteger")){
			parm2 = ((Long)(param2.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param2.className);
		}
		return (Double)(Math.IEEEremainder(parm1,parm2));
	}
	
	/**
	 * log
	 * @throws Exception 
	 * */
	public static Object log(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.log(parm));
	}
	
	/**
	 * log10
	 * @throws Exception 
	 * */
	public static Object log10(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.log10(parm));
	}
	
	/**
	 * log1p
	 * @throws Exception 
	 * */
	public static Object log1p(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.log1p(parm));
	}
	
	/**
	 * pow
	 * @throws Exception 
	 * */
	public static Object pow(SappClass param1,SappClass param2) throws Exception{
		double parm1,parm2;
		if(param1.className.equals("SappDecimal")){
			parm1 = (Double)(param1.object);
		}else if(param1.className.equals("SappInteger")){
			parm1 = ((Long)(param1.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param1.className);
		}
		if(param2.className.equals("SappDecimal")){
			parm2 = (Double)(param2.object);
		}else if(param2.className.equals("SappInteger")){
			parm2 = ((Long)(param2.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param2.className);
		}
		return (Double)(Math.pow(parm1,parm2));
	}
	
	/**
	 * random
	 * @throws Exception 
	 * */
	public static Object random(SappClass self) throws Exception{
		return (Double)(Math.random());
	}
	
	/**
	 * rint
	 * @throws Exception 
	 * */
	public static Object rint(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.rint(parm));
	}
	
	/**
	 * signum
	 * @throws Exception 
	 * */
	public static Object signum(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.signum(parm));
	}
	
	/**
	 * sinh
	 * @throws Exception 
	 * */
	public static Object sinh(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.sinh(parm));
	}
	
	/**
	 * tanh
	 * @throws Exception 
	 * */
	public static Object tanh(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.tanh(parm));
	}
	
	/**
	 * sqrt
	 * @throws Exception 
	 * */
	public static Object sqrt(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.sqrt(parm));
	}
	
	/**
	 * toDegrees
	 * @throws Exception 
	 * */
	public static Object toDegrees(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.toDegrees(parm));
	}
	
	/**
	 * toRadians
	 * @throws Exception 
	 * */
	public static Object toRadians(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.toRadians(parm));
	}
	
	/**
	 * ulp
	 * @throws Exception 
	 * */
	public static Object ulp(SappClass param) throws Exception{
		double parm;
		if(param.className.equals("SappDecimal")){
			parm = (Double)(param.object);
		}else if(param.className.equals("SappInteger")){
			parm = ((Long)(param.object)).doubleValue();
		}else{
			throw new Exception("acos方法接收了未知的参数类型:"+param.className);
		}
		return (Double)(Math.ulp(parm));
	}
	
	/**
	 * getPI
	 * @throws Exception 
	 * */
	public static Object getPI(){
		return (Double)(Math.PI);
	}
	
	/**
	 * getE
	 * @throws Exception 
	 * */
	public static Object getE(){
		return (Double)(Math.E);
	}
}
