package com.sapp.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import java.util.Vector;

import com.sapp.main.ParamsInterface;
import com.sapp.main.SappClass;
import com.sapp.main.SappEngine;
import com.sapp.main.SappMethod;
import com.sapp.tools.InterpreterTools;

/**
 * 这是一个解释器,解析翻译的代码ParamsInterface
 * */
public class Interpreter {
	
	//------引擎
	private SappEngine eng = null;
	//------字符串字面值Map
	private Map<String,String> fcmap = null;
	//获得对应字符串字面值的类对象名
	public String getZFZMZclassname(String str){
		for(String s:fcmap.keySet()){
			if(str.matches(s)){
				return fcmap.get(s);
			}
		}
		return null;
	}
	public String getBDclass(String str){
		return fcmap.get(str);
	}
	
	//------所在的类,如果是全局就为null
	/**
	 * 如果执行的是一个类的方法
	 * 就是这个类
	 * */
	private SappClass classo = null;
	
	/**
	 * 如果执行的是一个方法的话
	 * */
	private SappMethod sappmet = null;
	//-----------------变量区
	/**
	 * 全局变量区
	 * */
	private Map<String,SappClass> qjBLQ = null;//全局
	/**
	 * 实例变量区
	 * */
	private Map<String,SappClass> slBLQ = null;//实例
	/**
	 * 随机变量区
	 * */
	private Map<String,SappClass> sjBLQ = null;//随机
	/**
	 * 类对象空间
	 * */
	private Map<String,SappClass> ldxKJ = null;//类对象空间
	//-----------------
	/**
	 * 对应的类对象变量
	 * */
	private Map<String,SappClass> dyldxBL = null;//对应的类对象变量
	
	//----------------------------------
	/**
	 * 临时堆栈
	 * */
	public Stack<SappClass> stackspace = new Stack<SappClass>();
	//太复杂了用方法空值存储
	public SappClass stackPop(){
	//	System.out.println(this+"弹栈->"+stackspace.peek().className);
		return stackspace.pop();
	}
	public void stackPush(SappClass sc){
	//	System.out.println(this+"压栈-<"+sc.className);
		stackspace.push(sc);
	}
	/**
	 * 延时对象堆栈，需要手动弹栈。
	 * */
	public Stack<SappClass> repstack = new Stack<SappClass>();
	//----------------------------------
	
	/**
	 * 构造
	 * */
	public Interpreter(ParamsInterface pif){
		eng = pif.getRunEngine();
		fcmap = eng.getZFCZMZMap();
		qjBLQ = pif.getQJvars();
		slBLQ = pif.getInstancevars();
		sjBLQ = pif.getSJvars();
		ldxKJ = pif.getClassObject();
		classo = pif.getRunClass();
		sappmet = pif.getRunMethod();
		//如果有类名
		if(classo!=null){//不是主类
			dyldxBL = ldxKJ.get(classo.className).slParams;
		}
	}

	/**
	 * 返回值类型
	 * */
	public static final int RUNOK = -1;
	public static final int RETURN = 0;
	public static final int BREAK = 1;
	public static final int CONTINUE = 2;
	public static final int THROW = 3;
	public static final int RUNSTOP = 4;
	
	/**
	 * 执行
	 * @return int 
	 * RUNOK 正常执行;
	 * RETURN 遇到return;
	 * BREAK 遇到break;
	 * CONTINUE 遇到continue;
	 * THROW 遇到throw;
	 * RUNSTOP 遇到错误或者引擎退出信号。
	 * @throws  
	 * */
	public int exe(Vector<String> vcmds) {
		//整个语句解析到的位置
		int explposi = 0;
		//等待整体执行的临时命令集
		Vector<String> exestrs = null;
		//整个语句分类
		while(explposi<vcmds.size()){
			//每一步的运行状态
			int runstate = RUNOK;
			
			if(vcmds.elementAt(explposi).equals("[IF]")){//找到了一个[IF段]
				//获得一个段
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//执行这一个段
				runstate = toExeIF(exestrs);//遇到停止
			}else if(vcmds.elementAt(explposi).equals("[TRY]")){
				//获得一个段
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//执行这一个段
				runstate = toExeTRY(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[DO]")){
				//获得一个段
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//执行这一个段
				runstate = toExeDO(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[FOR]")){
				//获得一个段
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//执行这一个段
				runstate = toExeFOR(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[WHILE]")){
				//获得一个段
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//执行这一个段
				runstate = toExeWHILE(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[SWITCH]")){
				//获得一个段
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//执行这一个段
				runstate = toExeSWITCH(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[SYNCHRONIZED]")){
				//获得一个段
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//执行这一个段
				runstate = toExeSYNCHRONIZED(exestrs);
			}else {
				if(vcmds.elementAt(explposi).equals("[POP]")){
				//	stackspace.pop();//弹出延时对象。
					stackPop();
					explposi++;
				}else if(vcmds.elementAt(explposi).equals("[POPSTACK]")){
					repstack.pop();//弹出延时对象。
					explposi++;
				}else if(vcmds.elementAt(explposi).equals("[RETURN]")){
					runstate =  RETURN;
				}else if(vcmds.elementAt(explposi).equals("[BREAK]")){
					runstate =  BREAK;
				}else if(vcmds.elementAt(explposi).equals("[CONTINUE]")){
					runstate =  CONTINUE;
				}else if(vcmds.elementAt(explposi).equals("[THROW]")){
					runstate =  THROW;
				}else{
					runstate =  toExeState(vcmds.elementAt(explposi));
					explposi++;
				}
			}
			if(runstate!=RUNOK){//只要运行后的状态不为正常运行就返回
				return runstate;
			}
		}

		return RUNOK;
	}
	
	/**
	 * 执行while语句
	 * @throws Exception 
	 * */
	private int toExeWHILE(Vector<String> exestrs){
		
		//找到[PD]标签
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		//找到[LP]标签
		int lpindex = 0;
		for(;lpindex<exestrs.size();lpindex++){
			if(exestrs.elementAt(lpindex).equals("[LP]")){
				break;
			}
		}
		//判断前执行的语句
		Vector<String> pdqv = new Vector<String>();
		for(int i=1;i<pdindex;i++){
			pdqv.add(exestrs.elementAt(i));
		}
		//判断语句
		String pdyj = exestrs.elementAt(pdindex);
		
		//循环语句
		Vector<String> xhtyj = new Vector<String>();
		for(int i=lpindex+1;i<exestrs.size()-1;i++){
			xhtyj.add(exestrs.elementAt(i));
		}
		//执行
		do{
			//执行判断前的语句
			int state1 = exe(pdqv);
			if(state1!=RUNOK){//非正常执行
				return state1;
			}
			
			//判断
			Boolean boo = toExePD(pdyj);
			if(boo == null){//判断出现异常
				return RUNSTOP;
			}else if(boo){//true
				int state = exe(xhtyj);//执行循环体的状态
				if(state == BREAK){//遇到break
					break;
				}else if(state == CONTINUE){//遇到continue
					continue;
				}else if(state != RUNOK){//遇到分正常状态
					return state;
				}
			}else{//false
				break;
			}
		}while(true);
		
		return RUNOK;//正常执行
	}
	/**
	 * 执行do语句
	 * @throws Exception 
	 * */
	private int toExeDO(Vector<String> exestrs) {

		//找到[PD]标签
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		//找到[LP]标签
		int lpindex = 0;
		for(;lpindex<exestrs.size();lpindex++){
			if(exestrs.elementAt(lpindex).equals("[LP]")){
				break;
			}
		}
		//判断前执行的语句
		Vector<String> pdqv = new Vector<String>();
		for(int i=1;i<pdindex;i++){
			pdqv.add(exestrs.elementAt(i));
		}
		//判断语句
		String pdyj = exestrs.elementAt(pdindex);
		
		//循环语句
		Vector<String> xhtyj = new Vector<String>();
		for(int i=lpindex+1;i<exestrs.size()-1;i++){
			xhtyj.add(exestrs.elementAt(i));
		}
		//执行
		do{
			//开始执行循环体
			int state = exe(xhtyj);//执行循环体的状态
			if(state == BREAK){//遇到break
				break;
			}else if(state == CONTINUE){//遇到continue
				continue;
			}else if(state != RUNOK){
				return state;
			}
			
			//执行判断前的语句
			state = exe(pdqv);
			if(state != RUNOK){
				return state;
			}
			
			//判断
			Boolean boo = toExePD(pdyj);
			if(boo == null){//判断出现异常
				return RUNSTOP;
			}else if(boo){//true
				continue;
			}else{//false
				break;
			}
		}while(true);
		
		return RUNOK;//正常执行
		
	}
	/**
	 * 执行FOR循环
	 * @throws Exception 
	 * */
	private int toExeFOR(Vector<String> exestrs){

		//找到[PD]标签
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		//找到[LP]标签
		int lpindex = 0;
		for(;lpindex<exestrs.size();lpindex++){
			if(exestrs.elementAt(lpindex).equals("[LP]")){
				break;
			}
		}
		//判断前执行的语句
		Vector<String> pdqv = new Vector<String>();
		for(int i=1;i<pdindex;i++){
			pdqv.add(exestrs.elementAt(i));
		}
		//判断语句
		String pdyj = exestrs.elementAt(pdindex);
		//第三参数语句
		Vector<String> dscsyj = new Vector<String>();
		for(int i=pdindex+1;i<lpindex;i++){
			dscsyj.add(exestrs.elementAt(i));
		}
		//循环语句
		Vector<String> xhtyj = new Vector<String>();
		for(int i=lpindex+1;i<exestrs.size()-1;i++){
			xhtyj.add(exestrs.elementAt(i));
		}
		//执行
		do{
			//执行判断前的语句
			int state = exe(pdqv);
			if(state != RUNOK){
				return state;
			}
			//判断
			Boolean boo = toExePD(pdyj);
			if(boo == null){//判断出现异常
				return RUNSTOP;
			}else if(!boo){//false
				break;
			}
			
			//开始执行循环体
			state = exe(xhtyj);//执行循环体的状态
			if(state == BREAK){//遇到break
				break;
			}else if(state != RUNOK&&state != CONTINUE){//,遇到continue后继续向下执行.
				return state;
			}
			
			//循环后面
			state = exe(dscsyj);
			if(state != RUNOK){
				return state;
			}
			
		}while(true);
		
		return RUNOK;
	}
	
	/**
	 * 执行TRY语句
	 * */
	private int toExeTRY(Vector<String> exestrs){
		//找到配套的[CATCH]标签。
		int caindex = 0;
		int state = 0;
		for(;caindex<exestrs.size();caindex++){
			String str = exestrs.elementAt(caindex);
			if(str.equals("[TRY]")){
				state++;
			}else if(str.equals("[CATCH]")){
				state--;
				if(state == 0){
					break;
				}
			}
		}
		
		//执行体
		Vector<String> exepart = new Vector<String>();
		for(int i=1;i<caindex;i++){
			exepart.add(exestrs.elementAt(i));
		}
		//所有catch分支
		Vector<Vector<String>> catchstr = new Vector<Vector<String>>();
		//一个catch分支
		Vector<String> cathcs = new Vector<String>();
		//寻找catch分支
		
		int state12 = 0;//标志
		while(true){
			caindex ++;//越过上一个catch语句
			String str = exestrs.elementAt(caindex);
			if(str.equals("[IF]")||str.equals("[DO]")||str.equals("[WHILE]")
					||str.equals("[TRY]")||str.equals("[FOR]")||str.equals("[SWITCH]")
					||str.equals("[SYNCHRONIZED]")){
				state12++;
			}else if(str.equals("[END]")){
				state12 --;
				if(state12 == -1){//遇到最后的end了
					catchstr.add(cathcs);
					break;
				}
			}else if(str.equals("[CATCH]")){
				if(state12 == 0){//遇到这一层的catch了。
					catchstr.add(cathcs);
					cathcs = new Vector<String>();
					continue;
				}
			}
			cathcs.add(str);
		}
		
		//执行
		int state1 = exe(exepart);
		if(state1 == THROW){//抛出了异常
			//是否能捕捉
			boolean cancatch = false;
			for(Vector<String> vstr:catchstr){
				int state2 = toExeFZ(vstr);
				if(state2 == IFTRUE){
					cancatch = true;//捕捉成功
					break;
				}else if(state2 == IFFALSE){//如果是false就执行下一个部分
					continue;
				}else if(state2 != RUNOK){
					return state2;
				}
			}
			if(!cancatch){//没有捕捉成功就继续抛出。
				return THROW;
			}
		}else if(state1 != RUNSTOP){
			return state1;
		}
		
		return RUNOK;
	}
	/**
	 * 执行IF语句
	 * */
	private int toExeIF(Vector<String> exestrs) {
		//所有的分子
		Vector<Vector<String>> fenzhis = new Vector<Vector<String>>();
		//分离这个if语句的分子
		Vector<String> onefz = new Vector<String>();//一个分支
		int state = 0;
		for(int i = 1;i<exestrs.size()-1;i++){
			String strs = exestrs.elementAt(i);
			if(strs.equals("[ELSE]")){
				if(state == 0){
					fenzhis.add(onefz);
					onefz = new Vector<String>();
				}else{
					onefz.add(strs);
				}
			}else{
				onefz.add(strs);
				if(strs.equals("[IF]")||strs.equals("[DO]")||strs.equals("[WHILE]")
						||strs.equals("[TRY]")||strs.equals("[FOR]")||strs.equals("[SWITCH]")
						||strs.equals("[SYNCHRONIZED]")){
					state++;
				}else if(strs.equals("[END]")){
					state --;
				}
			}
		}
		fenzhis.add(onefz);
		//开始执行每一个分子
		for(Vector<String> vstrs:fenzhis){
			int bzf = toExeFZ(vstrs);//返回
			if(bzf == IFTRUE){
				break;
			}else if(bzf == IFFALSE){//如果是false就执行下一个部分
				continue;
			}else if(bzf != RUNOK){
				return bzf;
			}
		}
		
		return RUNOK;
	}
	/**
	 * 执行一个分支，可以是if或者catch
	 * */
	private int toExeFZ(Vector<String> exestrs){
		//找到[PD]标签
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		if(pdindex == exestrs.size()){//没找到[PD]
			//执行整个语句
			int state = exe(exestrs);
			if(state!=RUNOK){
				return state;
			}
			return IFTRUE;
		}else{
			//找到了
			//判断前执行的语句
			Vector<String> pdqv = new Vector<String>();
			for(int i=0;i<pdindex;i++){
				pdqv.add(exestrs.elementAt(i));
			}
			//判断语句
			String pdyj = exestrs.elementAt(pdindex);
			
			//执行体
			Vector<String> exepart = new Vector<String>();
			for(int i=pdindex+1;i<exestrs.size();i++){
				exepart.add(exestrs.elementAt(i));
			}
			
			//执行
			//判断前
			int state = exe(pdqv);
			if(state!=RUNOK){
				return state;
			}
			//判断
			Boolean boo = toExePD(pdyj);
			if(boo == null){
				return RUNSTOP;
			}else if(boo){//执行
				state = exe(exepart);
				if(state!=RUNOK){
					return state;
				}
				return IFTRUE;
			}else{//false
				return IFFALSE;
			}
		}

	}
	//if语句为一个false
	public static final int IFFALSE = 10;
	public static final int IFTRUE = 11;
	/**
	 * 执行一个switch块
	 * */
	private int toExeSWITCH(Vector<String> exestrs) {
		//找到switch括号中的对象，从[SWITCH]到第一个[CASE]
		//找到[PD]标签
		int fkindex = 0;
		for(;fkindex<exestrs.size();fkindex++){
			if(exestrs.elementAt(fkindex).startsWith("[CASE]")){
				break;
			}
		}
		//找到主要对象
		Vector<String> zydx = new Vector<String>();
		for(int i=1;i<fkindex;i++){
			zydx.add(exestrs.elementAt(i));
		}
		
		//找到CASE或者DEFAULT
		boolean havedefault = false;//是否有默认值
		Vector<String> defals = new Vector<String>();
		//所有CASE分支
		Vector<Vector<String>> casestr = new Vector<Vector<String>>();
		//一个CASE分支
		Vector<String> cases = new Vector<String>();
		//寻找CASE分支
		int state12 = 0;//标志
		while(true){
			fkindex ++;//越过上一个case语句
			String str = exestrs.elementAt(fkindex);
			if(str.equals("[IF]")||str.equals("[DO]")||str.equals("[WHILE]")
					||str.equals("[TRY]")||str.equals("[FOR]")||str.equals("[SWITCH]")
					||str.equals("[SYNCHRONIZED]")){
				state12++;
			}else if(str.equals("[END]")){
				state12 --;
				if(state12 == -1){//遇到最后的end了
					casestr.add(cases);
					break;
				}
			}else if(str.equals("[CASE]")||str.equals("[DEFAULT]")){
				if(state12 == 0){//遇到这一层的catch了。
					casestr.add(cases);
					if(str.equals("[DEFAULT]")){
						havedefault = true;
						break;
					}
					cases = new Vector<String>();
					continue;
				}
			}
			cases.add(str);
		}
		//查看是否有default
		state12 = 0;
		if(havedefault){//有
			while(true){
				fkindex ++;//越过上一个case，default语句
				String str = exestrs.elementAt(fkindex);
				if(str.equals("[IF]")||str.equals("[DO]")||str.equals("[WHILE]")
						||str.equals("[TRY]")||str.equals("[FOR]")||str.equals("[SWITCH]")
						||str.equals("[SYNCHRONIZED]")){
					state12++;
				}else if(str.equals("[END]")){
					state12 --;
					if(state12 == -1){//遇到最后的end了
						break;
					}
				}
				defals.add(str);
			}
		}
		//把case语句的判断部分和执行部分分开，为一个表
		HashMap<Integer,Integer> swindex = new HashMap<Integer,Integer>();//判断代码与语句执行部分位置
		Vector<Vector<String>> fenzipd = new Vector<Vector<String>>();//分支判断前代码
		Vector<String> pdcode = new Vector<String>();//判断代码。
		Vector<String> allcode = new Vector<String>();//总共的执行代码。
		for(Vector<String> vstr:casestr){
			//找到[PD]标签
			int pdindex = 0;
			for(;pdindex<vstr.size();pdindex++){
				if(vstr.elementAt(pdindex).startsWith("[PD]")){
					break;
				}
			}
			swindex.put(pdcode.size(), allcode.size());//定位
			
			Vector<String> pdfz = new Vector<String>();//判断前
			for(int i=0;i<pdindex;i++){
				pdfz.add(vstr.elementAt(i));
			}
			fenzipd.add(pdfz);//添加进判断前语句集
			
			pdcode.add(vstr.elementAt(pdindex));//判断
			
			for(int i=pdindex+1;i<vstr.size();i++){ //执行语句
				allcode.add(vstr.elementAt(i));
			}
		}
		//default语句加到总集合中
		if(havedefault){
			for(String code:defals){
				allcode.add(code);
			}
		}
		
		//执行switch语句
		int state = exe(zydx);//获得主要的比较对象
		if(state!=RUNOK){
			return state;
		}
		
		//执行case语句了.
		boolean cancase = false;
		//判断分支了
		for(int i = 0;i<pdcode.size();i++){
			//判断前语句
			state = exe(fenzipd.elementAt(i));
			if(state!=RUNOK){
				return state;
			}
			//判断
			Boolean boo = toExePD(pdcode.elementAt(i));
			if(boo == null){
				return RUNSTOP;
			}else if(boo){//执行就是从这里开始执行
				cancase = true;
				
				//截取从这里执行的段
				Vector<String> execode = new Vector<String>();
				for(int j = swindex.get(i);j<allcode.size();j++){
					execode.add(allcode.elementAt(j));
				}
				state = exe(execode);
				if(state!=RUNOK&&state!=BREAK){
					return state;
				}
				break;
			}
		}
		if(!cancase&&havedefault){//没有成功执行case，但是有default语句。
			state = exe(defals);//defalut;
			if(state!=RUNOK&&state!=BREAK){
				return state;
			}
		}
		return RUNOK;
	}
	

	private int toExeSYNCHRONIZED(Vector<String> exestrs) {
		//获得同步对象。
		String sycpco = exestrs.elementAt(1);
		String sycpa = sycpco.substring(sycpco.indexOf(']')+1, sycpco.length());
		
		Vector<String> sycvcode = new Vector<String>();
		for(int i = 2;i<exestrs.size()-1;i++){
			sycvcode.add(exestrs.elementAt(i));
		}
		
		//执行
		//获得同步对象
		SappClass sycobj = null;
		try {
			sycobj = getObject(sycpa);
		}catch(RunStateException e){
			return e.state;//异常的代码
		} catch (Exception e) {
			eng.printError("异常:"+e.getMessage());
			return RUNSTOP;
		}
		//执行同步。
		synchronized(sycobj){
			//System.out.println("锁住对象:"+sycobj);
			int state = exe(sycvcode);
			if(state!=RUNOK){
				return state;
			}
		}
		//System.out.println("解锁对象:"+sycobj);
		return RUNOK;
	}
	/**
	 * 获得一个段的代码
	 * */
	private int getPartCode(Vector<String> returns,Vector<String> allcodes,int startindex){
		//状态标志
		int state = 0;
		for(;startindex<allcodes.size();startindex++){
			
			String nowcode = allcodes.elementAt(startindex);//获得当前的代码
			
			returns.add(nowcode);//添加这一个标签
			//判断添加的这一个标签
			if(nowcode.equals("[IF]")||nowcode.equals("[DO]")||nowcode.equals("[WHILE]")
					||nowcode.equals("[TRY]")||nowcode.equals("[FOR]")||nowcode.equals("[SWITCH]")
					||nowcode.equals("[SYNCHRONIZED]")){
				state ++;
			}else if(nowcode.equals("[END]")){
				state --;
				if(state == 0){//完成了
					//跳过这一个标签
					startindex++;
					break;
				}
			}
		}
		return startindex;
	}
	
	//执行判断语句.[PD]
	private Boolean toExePD(String cmd){
		//获取判断的随机字符串对象
		String pdcmd = cmd.substring("[PD]".length(), cmd.length());
		
		SappClass lsobj = null;
		try {
			lsobj = getObject(pdcmd);
		} catch (Exception e) {
			System.out.println("没有找到对象:"+pdcmd);
			e.printStackTrace();
		}
		
		if(!lsobj.className.equals(fcmap.get("true"))){//检查是不是可以判断的类
			eng.printError("不能产生结果的判断:"+lsobj.className);
			return null;
		}
		
		return (Boolean)lsobj.object;
	}
	/**
	 * 执行普通语句，非流程控制语句
	 * @throws Exception 
	 * */
	private int toExeState(String cmd){
		/**
		 * 检查系统运行状态
		 * */
		if(eng.runstate == SappEngine.STOP){//检查到了停止状态
			return RUNSTOP;
		}
		
		//从PT语句跳到特殊语句的情况
		//System.out.println("\nexe->"+cmd);
		//一般流程的语句,只有两种[DY][FZ]
		//获得前面的操作数
		int czf = cmd.indexOf("<=");
		String qian = cmd.substring(0, czf);
		String hou = cmd.substring(czf+2, cmd.length());
	
		//解释后面的对象。
		SappClass houobj = null;
		try{
			houobj = getObject(hou);
		}catch(RunStateException e){
			return e.state;//异常的代码
		}catch(Exception e){
			e.printStackTrace();
			eng.printError("异常:"+e.getMessage());
			return RUNSTOP;
		}
		
		//System.out.println(qian+":"+hou+"="+houobj.className);
		
		//根据前面的参数确定操作
		//解析前面的对象
		if(qian.equals("PUSH")){
			stackPush(houobj);
			//stackspace.push(houobj);//压栈
			//System.out.println("压栈了"+houobj.className);
		}else if(qian.equals("PDOBJ")){//临时对象
			//System.out.println("压入延时堆栈:"+houobj);
			repstack.push(houobj);
		}else if(qian.equals("this")){
			classo.transClass(houobj);
		}else if(qian.indexOf('.')!=-1){
			
			String []objs = qian.split("[.]");
			
			//前面的对象
			SappClass lsobj = null;
			try{
				lsobj = getObject(objs[0]);
			}catch(RunStateException e){
				return e.state;//异常的代码
			}catch(Exception e){
				e.printStackTrace();
				eng.printError("异常:"+e.getMessage());
				return RUNSTOP;
			}
			
			//后面的参数。
			if(objs[1].equals("this")){
				lsobj.transClass(houobj);
			}else{
				if(objs[1].matches("[$]\\p{Alnum}+")){//在全局定义
					qjBLQ.put(objs[1], houobj);
				}else if(objs[1].matches("@@\\p{Alnum}+")){//在类对象中定义
					//在引擎中找到这个类
					eng.thisClass.get(lsobj.className).slParams.put(objs[1], houobj);
				}else if(objs[1].matches("@\\p{Alnum}+")){//在实例对象中定义
					lsobj.slParams.put(objs[1], houobj);
				}
			}
			
		}else if(qian.indexOf("->")!=-1){//下标操作 []
			
			//分解参数。
			String[] pars = qian.split("->");
			
			//前临时对象。
			SappClass qianlsobj = null;
			SappClass houlsobj = null;
			try{
				qianlsobj = getObject(pars[0]);
				houlsobj = getObject(pars[1]);
			}catch(RunStateException e){
				return e.state;//异常的代码
			}catch(Exception e){
				e.printStackTrace();
				eng.printError("异常:"+e.getMessage());
				return RUNSTOP;
			}
			
			//获得前面对象的put方法。
			SappMethod metob = qianlsobj.getMethodByName(qianlsobj.className,"set");
			if(metob==null){
				eng.printError("对象:"+qianlsobj+"没有找到方法:\n类"
						+qianlsobj.className+",方法:[]");
				return RUNSTOP;
			}
			
			//执行这个方法
			try {
				exeMethod(qianlsobj,metob,houlsobj,houobj);
			} catch (RunStateException e) {
				return e.state;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			if(qian.matches("[$]\\p{Alnum}+")){//原始对象。
				qjBLQ.put(qian, houobj);
			}else if(qian.matches("@@\\p{Alnum}+")){
				dyldxBL.put(qian, houobj);
			}else if(qian.matches("@\\p{Alnum}+")){
				slBLQ.put(qian, houobj);
			}else if(qian.matches("\\p{Alpha}\\p{Alnum}*")){//随机变量
				sjBLQ.put(qian, houobj);
			}else{
				eng.printError("非法的变量名:"+qian);
				return RUNSTOP;
			}
		}
		
		return RUNOK;
	}
	/**
	 * 执行一个方法
	 * 类方法是共享的
	 * @throws Exception 
	 * */
	private SappClass exeMethod(SappClass obj,SappMethod met,SappClass ... pacl) throws Exception{	
		if(met.isSynchronized){
			synchronized(met){
				return exeDefMethod(obj, met,pacl);
			}
		}else{
			return exeDefMethod(obj, met,pacl);
		}
	}
	
	private SappClass exeDefMethod(SappClass obj,SappMethod met,SappClass ... pacl) throws Exception{
		
		//方法简化名不要求前面的类名
		String allname = met.methodName;
		//获得简化名
		String metjhname = allname.substring(allname.indexOf('.')+1);
		
		if(!met.isNative){
			met = met.clone();
		}
		//设置执行对象
		met.setExeObject(obj);
		
		if(metjhname.equals("new")){
			
			SappClass retobj = obj.clone();
			
			SappMethod mets = retobj.getMethodByName(retobj.className,"new").clone();
			
			mets.setExeObject(retobj);
			
			mets.exeMethod(pacl);
			
			return retobj;
			
		}else{
			return met.exeMethod(pacl);//执行获得返回值
		}
	}
	/**
	 * 获得后面这个对象
	 * @throws Exception 
	 * @throws Exception 传递错误参数。
	 * */
	private SappClass getObject(String houstr) throws Exception {
		
		SappClass houobj = null;
		//1.检查后面这个字符串是不是 字符串字面值,Hash表或者数组
		
		String lin = null;//临时字符串，在检查完是否是字符串字面值时，把类对象名记住。
		if(houstr.equals("POP")){
			houobj = stackPop();	
		}else if(houstr.equals("PDOBJ")){//临时对象
			houobj = repstack.peek();
			//System.out.println("获得延时堆栈变量:"+houobj);
		}else if(houstr.equals("this")){
			houobj = classo;
		}else if((lin = getZFZMZclassname(houstr))!=null){
			//后面是字面值如果是字符串的话就要解释转义字符
			if(houstr.matches("(\"|')(.|\n|\r)*(\"|')")){//字符串或者字符
				houstr = InterpreterTools.toExplainString(houstr);
			}
			//根据字符串字面值与本地类创建一个对象.
			//System.out.println("字面值:"+houstr);
			houobj = toCreateObject(lin,houstr);
			
			if(houstr.matches("\\[[0-9]*\\]")){//数组
				
				//获得数组中的对象个数
				int objnum = Integer.parseInt(houstr.substring(1, houstr.length()-1));
				SappClass [] objs = new SappClass[objnum];
				for(int i = objnum-1;i>=0;i--){
					objs[i] = stackPop();//弹出参数依照顺序
				}
				
				//获得这个对象的put方法。是native方法。
				SappMethod metp = houobj.getMethodByName(houobj.className,"put");
				if(metp==null){
					eng.printError("对象:"+houobj+"没有找到方法:\n类"
							+houobj.className+",方法:[]");
					eng.printError("put方法也是一个比较特殊的方法。数组中初始化时添加数组对象。" +
							"put(对象)，根据初始化序列依次添加。");
					throw new RunStateException(RUNSTOP);
				}
				
				//执行添加元素方法
				for(SappClass param:objs){//这里的元素都变成了随机变量。
					exeMethod(houobj,metp,param);
				}
			}else if(houstr.matches("\\{[0-9]*\\}")){//Hash表
				
				//获得数组中的对象个数
				int objnum = Integer.parseInt(houstr.substring(1, houstr.length()-1))*2;//两倍
				SappClass [] objs = new SappClass[objnum];
				for(int i = objnum-1;i>=0;i--){
					objs[i] = stackPop();//弹出参数依照顺序
				}
				
				//获得put方法。
				SappMethod metp = houobj.getMethodByName(houobj.className,"put");
				if(metp==null){
					eng.printError("对象:"+houobj+"没有找到方法:\n类"
							+houobj.className+",方法:[]");
					eng.printError("put方法也是一个比较特殊的方法。Hash表中初始化时添加表对象。" +
							"put(对象)，根据初始化序列依次添加。");
					throw new RunStateException(RUNSTOP);
				}
				
				//执行方法。
				for(int i = 0;i<objnum;i+=2){
					exeMethod(houobj,metp,objs[i],objs[i+1]);
				}
			}
		}else if(houstr.matches("[|]{1}.+[|]{1}")){//是否是特殊的操作
			//去掉特殊操作符号||
			String opers = houstr.substring(1, houstr.length()-1);
			//分为两类:一是访问方法。二是判断实例
			if(opers.startsWith("Call")){
				//获取开始位置
				int dys = opers.indexOf('>');
				//获取对象名
				int nind = opers.indexOf(':');
				String objname = opers.substring(dys+1, nind).trim();
				//获取方法名
				int find = opers.indexOf(':',nind+1);
				String metname = opers.substring(nind+1, find).trim();
				//获得参数
				String pars = opers.substring(find+1, opers.length()).trim();
				
				//最先解析参数。
				SappClass [] pacl = null;
				if(pars.length()>0){
					if(pars.matches("[0-9]+")){//参数是一个数字
						int nums = Integer.parseInt(pars);
						pacl = new SappClass[nums];
						for(int i = nums-1;i>=0;i--){
							pacl[i] = stackPop();
						}
					}else{
						pacl = new SappClass[1];
						pacl[0] = getObject(pars);
					}
				}else{
					pacl = new SappClass[0];
				}
				
				//再找到对象。
				SappClass lsobj = null;//临时对象,操作的对象
				//获得方法
				SappMethod met = null;
				if(objname.equals("super")){
					lsobj = classo;//执行的对象为当前的对象
					//获得这个对象的方法
					met = lsobj.getMethodByName(lsobj.className, metname);
					
					//获得这个方法的覆盖方法
					met = met.getOverrided();
				}else{//sappmet
					lsobj = getObject(objname);	
					if(lsobj==classo){//执行的是this对象。
						met = lsobj.getMethodByName(sappmet.classname, metname);
					}else{
						met = lsobj.getMethodByName(lsobj.className, metname);
					}
				}				
				//获得方法
				if(met==null){
					eng.printError("对象:"+lsobj+"没有找到方法:\n类"
							+lsobj.className+",方法:"+metname);
					throw new RunStateException(RUNSTOP);
				}
				
				//根据方法参数类型修改参数
				
				Vector<String> pastr = met.paramsVstr;
				for(int i = 0;i<pastr.size()&&i<pacl.length;i++){
					if(pastr.elementAt(i).startsWith("*")){//不是引用参数就要克隆
						pacl[i] = pacl[i].clone();
					}
				}
				houobj = exeMethod(lsobj,met,pacl);
				
			}else if(opers.startsWith("Instance")){
				//获得对象索引
				int dys = opers.indexOf('>');
				int fgh = opers.indexOf(':');
				//对象
				String dxsy = opers.substring(dys+1, fgh).trim();
				//类名
				String lname = opers.substring(fgh+1, opers.length()).trim();

				SappClass lsobj = getObject(dxsy);//弹出对象
				
				//获得真假的字符串字面值
				String zmz = eng.isInstance(lsobj.className,lname)?"true":"false";
				//根据字符串字面值获得克隆对象。
				houobj = toCreateObject(getZFZMZclassname(zmz),zmz);
			}else if(opers.startsWith("Get")){//|Get> : |
				//获得对象索引
				int dys = opers.indexOf('>');
				int fgh = opers.indexOf(':');
				//对象
				String dxsy = opers.substring(dys+1, fgh).trim();
				//变量
				String lname = opers.substring(fgh+1, opers.length()).trim();
				
				SappClass lsobj = getObject(dxsy);//获得对象
				
				if(lname.matches("@@\\p{Alnum}+")){
					houobj = eng.thisClass.get(lsobj.className).slParams.get(houstr);
				}else if(lname.matches("@\\p{Alnum}+")){
					houobj = lsobj.slParams.get(houstr);
				}
				
			}
			
		}else{//不是字符串字面值,是一个变量
			if(houstr.matches("[$]\\p{Alnum}+")){
				houobj = qjBLQ.get(houstr);
			}else if(houstr.matches("@@\\p{Alnum}+")){
				houobj = dyldxBL.get(houstr);
			}else if(houstr.matches("@\\p{Alnum}+")){
				houobj = slBLQ.get(houstr);
			}else if(houstr.matches("\\p{Alpha}\\p{Alnum}*")){//随机变量
				if(ldxKJ.containsKey(houstr)){
					houobj = ldxKJ.get(houstr);
				}else{
					houobj = sjBLQ.get(houstr);
				}
			}else{
				eng.printError("非法的变量名:"+houstr);
				throw new RunStateException(RUNSTOP);
			}
			/*if(houstr.equals("name")){
				System.out.println(houobj.className);
			}*/
			
		}
		//如果后面这个对象是空对象的话
		if(houobj == null){
			houobj = eng.thisClass.get(fcmap.get("null")).clone();
		}
		
		return houobj;
	}
	
	//根据字符串字面值和本地类创建对象
	/**
	 * @param clan 本地对应字符串字面值的类对象名。
	 * @param hou 字面值
	 * @throws Exception 
	 * */
	private SappClass toCreateObject(String clan,String hou) throws Exception{
		
		//java对象类
		String jacl = eng.nativeJava;
		//System.out.println("本地转换类:"+jacl);
		//复制一个java对象类。
		SappClass paramobj = eng.thisClass.get(jacl).clone();
		paramobj.object = hou;
		
		//执行对象。
		SappClass obj = eng.thisClass.get(clan);
		
		//获得方法
		SappMethod met = obj.getMethodByName(obj.className, "create");
		
		return exeMethod(obj, met, paramobj);
	}
}
