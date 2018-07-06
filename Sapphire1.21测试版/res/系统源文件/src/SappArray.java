import java.util.Vector;

import com.sapp.main.SappClass;

/**
 * 数组类使用集合
 * Vector<SappClass>
 * */
public class SappArray {
	
	/**
	 * 本地的构造方法
	 * */
	public static Object create(String str){
		return new Vector<SappClass>();
	}
	
	/**
	 * 构造方法一定空间的数组
	 * @throws Exception 
	 * */
	public static Object createArray(SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("需要SappInteger类型参数:找到"+param.className);
		}else{
			return	new Vector<SappClass>(((Long)(param.object)).intValue());
		}
	}
	
	/**
	 * 计算大小
	 * @throws Exception 
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_size(SappClass self) throws Exception{
		return new Long(((Vector<SappClass>)(self.object)).size());
	}
	
	/**
	 * 添加元素
	 * */
	@SuppressWarnings("unchecked")
	public static void N_put(SappClass self,SappClass param) throws Exception{	
		((Vector<SappClass>)(self.object)).add(param);
	}
	
	/**
	 * 设置元素
	 * */
	@SuppressWarnings("unchecked")
	public static void N_set(SappClass self,SappClass param1,SappClass param2) throws Exception{
		if(!param1.className.equals("SappInteger")){
			throw new Exception("需要SappInteger类型参数:找到"+param1.className);
		}else{
			//获得索引
			int index = ((Long)(param1.object)).intValue();
			((Vector<SappClass>)(self.object)).set(index, param2);
			
		}
	}
	
	/**
	 * 获得数组元素
	 * */
	@SuppressWarnings("unchecked")
	public static Object N_get(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("需要SappInteger类型参数:找到"+param.className);
		}else{
			//获得索引
			int index = ((Long)(param.object)).intValue();
			return ((Vector<SappClass>)(self.object)).elementAt(index);
		}
	}

	/**
	 * 给定位置添加元素
	 * */
	@SuppressWarnings("unchecked")
	public static void N_putByIndex(SappClass self,SappClass param1,SappClass param2) throws Exception{
		if(!param1.className.equals("SappInteger")){
			throw new Exception("需要SappInteger类型参数:找到"+param1.className);
		}else{
			//获得索引
			int index = ((Long)(param1.object)).intValue();
			((Vector<SappClass>)(self.object)).add(index, param2);
		}
	}
	/**
	 * 设置新的大小
	 * */
	@SuppressWarnings("unchecked")
	public static void N_setSize(SappClass self,SappClass param) throws Exception{
		if(!param.className.equals("SappInteger")){
			throw new Exception("需要SappInteger类型参数:找到"+param.className);
		}
		int size = ((Long)(param.object)).intValue();
		((Vector<SappClass>)(self.object)).setSize(size);
	}
}
