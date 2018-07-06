package com.sapp.complie;
import java.util.Vector;

/**
 * 这是一个编译的类。
 * 主要把语句翻译为命令式代码。
 * */
public class Complies implements Cloneable{
	
	/**
	 * 代码分类。
	 * */
	public static final int FENLEI = 0;
	/**
	 * 语句处理。
	 * */
	public static final int YJCD = 1;
	/**
	 * if块处理。
	 * */
	public static final int IFCD = 2;
	/**
	 * do块处理。
	 * */
	public static final int DOCD = 3;
	/**
	 * for块处理。
	 * */
	public static final int FORCD = 4;
	/**
	 * while块处理。
	 * */
	public static final int WHHILECD = 5;
	/**
	 * try块处理。
	 * */
	public static final int TRYCD = 6;
	/**
	 * switch块处理。
	 * */
	public static final int SWITCHCD = 8;
	/**
	 * break,continue，return,throw处理。
	 * */
	public static final int BRKCONRE = 7;
	/**
	 * synchronized块处理。
	 * */
	public static final int SYNCHRONCD = 9;
	
	/***
	 * @param vcmds 编译后的语句的容器.
	 * @param code 要编译的代码.
	 * @param dealtype 处理的类型.
	 * */
	public void complies(Vector<String> vcmds,String code,int dealtype) throws Exception{
		code = code.trim();//每次去除两边的空
		//分类处理代码
		switch(dealtype){
		case FENLEI://代码分类
			toDealFENLEI(vcmds,code);
		break;
		case YJCD://语句代码，一段语句代码可以去除不必要的空格
			
			toDealYJCD(vcmds,code);
		break;
		case IFCD://if块
			toDealIFCD(vcmds,code);
		break;
		case DOCD://do块
			toDealDOCD(vcmds,code);
		break;
		case FORCD://for块
			toDealFORCD(vcmds,code);
		break;
		case WHHILECD://while块
			toDealWHHILECD(vcmds,code);
		break;
		case TRYCD://try块
			toDealTRYCD(vcmds,code);
		break;
		case SWITCHCD://switch块
			toDealSWITCHCD(vcmds,code);
		break;
		case BRKCONRE://break,continue，return，throw  
			toDealBRKCONRE(vcmds,code);
		break;
		case SYNCHRONCD://synchronized块
			toDealSYNCHRONCD(vcmds,code);
		break;
		default:
			throw new Exception("未知的代码类型。");
		}
	}
	
	/***************细节方法
	 * @throws Exception 
	 * ******************/
	//----代码分类
	private void toDealFENLEI(Vector<String> vcmds, String code) throws Exception {
		Vector<String> cmdstrs = new Codedivide(code.trim()).divideCmdBody();//至少是没有其他多余的字符
		for(String cmd:cmdstrs){//循环编译
			
			if(cmd.startsWith("do")){
				complies(vcmds,cmd,DOCD);
			}else if(cmd.startsWith("for")){
				complies(vcmds,cmd,FORCD);
			}else if(cmd.startsWith("while")){
				complies(vcmds,cmd,WHHILECD);
			}else if(cmd.startsWith("if")){
				complies(vcmds,cmd,IFCD);
			}else if(cmd.startsWith("switch")){
				complies(vcmds,cmd,SWITCHCD);
			}else if(cmd.startsWith("synchronized")){//同步
				complies(vcmds,cmd,SYNCHRONCD);
			}else if(cmd.startsWith("try")){//
				complies(vcmds,cmd,TRYCD);
				//关键字
			}else if(cmd.startsWith("break")||cmd.startsWith("continue")
					||cmd.startsWith("return")||cmd.startsWith("throw")){
				complies(vcmds,cmd,BRKCONRE);
			}else{
				complies(vcmds,cmd,YJCD);
			}
		}
	}

	/**
	 * 处理语句，使用语法树。
	 * @throws Exception 
	 * */
	private void toDealYJCD(Vector<String> vcmds, String code) throws Exception {
		AnalysedTree nantre = new AnalysedTree(null);//无根语法树
		nantre.analysedCode(code.substring(0, code.length()-1));//分析语句
		nantre.getRoot().complieTree(vcmds, AnalysedTree.COMOBJECT);//编译语句
	}
	
	//一些关键字break,continue,和return.
	private void toDealBRKCONRE(Vector<String> vcmds, String code) throws Exception {
		//System.out.println(code);
		code = code.substring(0, code.length()-1).trim();
		
		if(code.equals("break")){
			vcmds.add("[BREAK]");
		}else if(code.equals("continue")){
			vcmds.add("[CONTINUE]");
		}else if(code.startsWith("return")){
			if(code.trim().length()!=6){//不只是return
				
				String ayj = "PUSH="+code.substring(6).trim()+";";
				complies(vcmds,ayj,YJCD);
			}
			vcmds.add("[RETURN]");
		}else if(code.startsWith("throw")){//抛出异常
			String excname = "$exception";
			String excobj = code.substring(5).trim();
			if(code.matches("throw[ ]*[(](.|\n|\t)*")){//更新了异常名。
				excname = code.substring(code.indexOf('(')+1, code.indexOf(')')).trim();
				excobj = code.substring(code.indexOf(')')+1).trim();
			}
			String nowcode = excname+"="+excobj+";";//这个表达式返回一个对象给随机变量
			complies(vcmds,nowcode,YJCD);
			vcmds.add("[THROW]");
		}else{
			throw new Exception("不能解析的符号:"+code);
		}
	}
	//----------处理try catch段
	private void toDealTRYCD(Vector<String> vcmds, String code) throws Exception {
		
		//进行代码依次解析
		//解析位置
		int index = 0;
		
		while(index<code.length()){
			//移动到非空格
			while(index<code.length()&&code.charAt(index)==' '){
				index++;
			}
			
			if(code.startsWith("try", index)){
				vcmds.add("[TRY]");
				index += 3;
			}else if(code.startsWith("catch", index)){
				vcmds.add("[CATCH]");
				index += 5;
			}else if(code.startsWith("(", index)){
				//获取catch中的语句
				int state = 0;
				int staz = index;
				do{
					char ch = code.charAt(index);
					if(ch == '('){
						state ++;
					}else if(ch == ')'){
						state --;
						if(state == 0){//找到了搭配的了
							break;
						}
					}
					index++;
				}while(index<code.length());
				
				String pjstr = code.substring(staz+1, index++);//这最多是一条语句
				
				complies(vcmds, "PUSH="+pjstr+";", YJCD);
				
				vcmds.add("[PD]POP");//[PD]好了.
				
			}else if(code.startsWith("{", index)){
				//包含的代码块
				int bhs = index;
				
				//找到下一个搭配的}
				int state = 0;
				do{
					char ch = code.charAt(index);
					if(ch == '{'){
						state ++;
					}else if(ch == '}'){
						state --;
						if(state == 0){//找到了搭配的了
							break;
						}
					}
					index++;
				}while(index<code.length());
				
				String bhstr = code.substring(bhs+1, index++);
				
				complies(vcmds, bhstr, FENLEI);//编译了包含的代码。
			}
		}
		vcmds.add("[END]");
	}
	private void toDealWHHILECD(Vector<String> vcmds, String code) throws Exception {
	
		int index = 0;
		
		int xkhs = index = code.indexOf('(', index);
		//寻找配套)
		int state = 0;
		do{
			char ch = code.charAt(index);
			if(ch == '('){
				state ++;
			}else if(ch == ')'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<code.length());
		
		String bhstr = code.substring(xkhs+1,index++);
		
		/*******以上为while的执行条件不在循环中********/
		//while循环开始标志
		vcmds.add("[WHILE]");
		
		complies(vcmds, "PUSH="+bhstr+";", YJCD);//执行要判断的语句
		
		vcmds.add("[PD]POP");
		
		vcmds.add("[LP]");
		
		//语句块
		int dkhs = index = code.indexOf('{', index);
		//寻找配套}
		do{
			char ch = code.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<code.length());
		String dhstr = code.substring(dkhs+1, index++);
		
		complies(vcmds, dhstr, FENLEI);
		//
		vcmds.add("[END]");
	}
	private void toDealFORCD(Vector<String> vcmds, String code) throws Exception {
		//处理for循环第一个参数
		//获取for循环中的()中的参数
		int index = 0;
		
		int xkhs = index = code.indexOf('(', index);
		//寻找配套)
		int state = 0;
		do{
			char ch = code.charAt(index);
			if(ch == '('){
				state ++;
			}else if(ch == ')'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<code.length());
		
		String sparams = code.substring(xkhs, ++index);
		sparams = sparams.substring(1, sparams.length()-1).replaceAll(";", " ; ");
		
		String [] parts = sparams.split(";");

		if(parts[0].trim().length()!=0){
			String cmdstr = parts[0].trim()+";";
			//执行第一个条件
			complies(vcmds, cmdstr, YJCD);
		}
		
		//for循环开始
		vcmds.add("[FOR]");
		
		//第二个参数
		//申请一个判断的随机变量
		if(parts[1].trim().length()!=0){//有第二个
			complies(vcmds, "PUSH="+parts[1].trim()+";", YJCD);
		}else{
			complies(vcmds, "PUSH=true;", YJCD);
			
		}
		
		//PD语句
		vcmds.add("[PD]POP");

		//第三个参数
		if(parts[2].trim().length()!=0){
			String zj = parts[2].trim()+";";
			//执行条件变化
			complies(vcmds, zj, YJCD);
		}
		
		vcmds.add("[LP]");
		
		//循环体
		int dkhs = index = code.indexOf('{', index);
		//寻找配套}
		do{
			char ch = code.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<code.length());
		
		String dhstr = code.substring(dkhs+1, index);
		
		complies(vcmds, dhstr, FENLEI);
		
		vcmds.add("[END]");
	}
	private void toDealDOCD(Vector<String> vcmds, String code) throws Exception {
		
		//判断标记
		int state = 0;
		
		int xkhj = code.lastIndexOf(')');//获得最后这个)的位置
		
		int laststart = xkhj;//记录最后的位置
		
		//向前寻找配套(
		do{
			char ch = code.charAt(xkhj);
			if(ch == ')'){
				state ++;
			}else if(ch == '('){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			xkhj --;
		}while(xkhj>0);
		
		String xhstr = code.substring(xkhj+1, laststart);
		
		//do循环开始标志
		vcmds.add("[DO]");
		
		complies(vcmds, "PUSH="+xhstr+";", YJCD);
		vcmds.add("[PD]POP");
		
		//循环体
		vcmds.add("[LP]");
		
		int dkhj = code.lastIndexOf('}');
		
		laststart = dkhj;
		//向前寻找配套{
		do{
			char ch = code.charAt(dkhj);
			if(ch == '}'){
				state ++;
			}else if(ch == '{'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			dkhj --;
		}while(dkhj>0);
		
		String dhstr = code.substring(dkhj+1, laststart);
		
		complies(vcmds, dhstr, FENLEI);
		
		vcmds.add("[END]");
	}
	private void toDealIFCD(Vector<String> vcmds, String code) throws Exception {
		
		boolean isadd = false;
		//这个标记用字符串解析
		int index = 0;
		
		while(index<code.length()){
			//移动到非空格
			while(index<code.length()&&code.charAt(index)==' '){
				index++;
			}
			if(code.startsWith("if", index)){
				if(!isadd){
					vcmds.add("[IF]");//只加载一次
					isadd = true;
				}
				index += 2;
			}else if(code.startsWith("else", index)){
				vcmds.add("[ELSE]");
				index += 4;
			}else if(code.startsWith("(", index)){
				//解析一段判断
				int xkhs = index;
				//寻找配套)
				int state = 0;
				do{
					char ch = code.charAt(index);
					if(ch == '('){
						state ++;
					}else if(ch == ')'){
						state --;
						if(state == 0){//找到了搭配的了
							break;
						}
					}
					index++;
				}while(index<code.length());
				
				String bhstr = code.substring(xkhs+1, index++);
				//System.out.println("PUSH="+bhstr+";");
				
				complies(vcmds, bhstr+";", YJCD);
				vcmds.add("[PD]POP");
				
			}else if(code.startsWith("{", index)){
				
				int dkhs = index;
				//寻找配套}
				int state = 0;
				do{
					char ch = code.charAt(index);
					if(ch == '{'){
						state ++;
					}else if(ch == '}'){
						state --;
						if(state == 0){//找到了搭配的了
							break;
						}
					}
					index++;
				}while(index<code.length());
				
				String dhstr = code.substring(dkhs+1, index++);
				
				complies(vcmds, dhstr, FENLEI);
				
			}else{
				throw new Exception("未能解析的if语句："+code.substring(index));
			}
		}
		vcmds.add("[END]");
	}
	private void toDealSWITCHCD(Vector<String> vcmds, String code) throws Exception {//switch块
		
		vcmds.add("[SWITCH]");
	
		//找到分支开始语句
		int startin = code.indexOf('(');
		int state = 0;
		int index = startin;
		do{
			char ch = code.charAt(index);
			if(ch == '('){
				state ++;
			}else if(ch == ')'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<code.length());
		String pddx = code.substring(startin+1, index++).trim();
		
		complies(vcmds, "PDOBJ="+pddx+";", YJCD);
		
		//找到大括号中的{}编译
		index = code.indexOf('{',index);
		index++;
		while(index<code.length()){
			//移动到非空格
			while(index<code.length()&&code.charAt(index)==' '){
				index++;
			}
			
			if(code.startsWith("case", index)){//一个case分支
				
				vcmds.add("[CASE]");
				
				int cast = index;
				
				index = code.indexOf(':',index);
				
				String bjdu = code.substring(cast+4, index++).trim();//索引增加了
				
				complies(vcmds, "PUSH="+bjdu+";", YJCD);
				
				complies(vcmds, "PUSH=PDOBJ==POP;", YJCD);
				
				vcmds.add("[PD]POP");
				
				//具体的代码 找到下一个case,default或者}
				int start = index;//开始的语句
				int statet = 0;//  ; 标志 
				
				while(index<code.length()){
					char ch = code.charAt(index);					
					if(ch == '{'){//语句块中只有{}中可能有case
						statet ++;
					}else if(ch == '}'){//}						
						if(statet == 0){
							index++;
							break;
						}
						statet --;
					}else if(statet ==0&&ch == 'c'){//case的开头
						if(code.startsWith("case", index)){
							break;
						}
					}else if(statet ==0&&ch == 'd'){//default的开头
						if(code.startsWith("default", index)){
							break;
						}
					}
					index++;
				}
				
				String partcode = code.substring(start, index).trim();
				
				complies(vcmds, partcode, FENLEI);
				
			}else if(code.startsWith("default", index)){
				vcmds.add("[DEFAULT]");
				index = code.indexOf(':', index);
				//找到后面的语句
				int start = ++index;//开始的语句
				int statet = 0;//  ; 标志 
				while(index<code.length()){
					char ch = code.charAt(index);
					if(ch == '{'){//语句块中只有{}中可能有case
						statet ++;
					}else if(ch == '}'){//}
						if(statet == 0){
							index ++;
							break;
						}
						statet --;
					}else if(statet ==0&&ch == 'c'){//不可能遇到
						if(code.startsWith("case", index)){
							throw new Exception("default应该是最后的分子，但是意外遇到了case。");
						}
					}else if(statet ==0&&ch == 'd'){//不可能遇到
						if(code.startsWith("default", index)){
							throw new Exception("switch中只能有一个default分支。");
						}
					}
					index ++;
				}
				
				String 	partcode = code.substring(start, index).trim();
				
				complies(vcmds, partcode, FENLEI);
				
			}else if(code.startsWith("}", index)){
				break;
			}
		}
		vcmds.add("[END]");
		vcmds.add("[POPSTACK]");
	}
	private void toDealSYNCHRONCD(Vector<String> vcmds, String code) throws Exception {//同步块，
		//获得同步的变量名。
		String sycparam = code.substring(code.indexOf('(')+1, code.indexOf(')'));
		String syccode = code.substring(code.indexOf('{')+1, code.length()-1);
		
		complies(vcmds, "PUSH="+sycparam+";", YJCD);
		vcmds.add("[SYNCHRONIZED]");
		vcmds.add("[SYNCHR]POP");
		complies(vcmds, syccode, FENLEI);
		vcmds.add("[END]");
	}
}


/**
 * 一个代码处理的类
 * */
class Codedivide{
	
	//构造
	Codedivide(String code){
		cmdbody = code;
	}
	//----------------------处理原始代码段
	/**
	 * 解析到的位置
	 * */
	int index = 0;//当前解析到的位置
	/**
	 * 应该解析的字符串
	 * */
	private String cmdbody = null;
	/**
	 * 把所有的语句和块分解开来
	 * */
	public Vector<String> divideCmdBody() {
		Vector<String> cmdstrs = new Vector<String>();
		
		while(index<cmdbody.length()){
			if(cmdbody.startsWith("if", index)){
				cmdstrs.addElement(cutToIF());
			}else if(cmdbody.startsWith("do", index)){
				cmdstrs.addElement(cutToDO());
			}else if(cmdbody.startsWith("for", index)){
				cmdstrs.addElement(cutToFORorWHILE());
			}else if(cmdbody.startsWith("while", index)){
				cmdstrs.addElement(cutToFORorWHILE());
			}else if(cmdbody.startsWith("try", index)){
				cmdstrs.addElement(cutToTRY());
			}else if(cmdbody.startsWith("switch", index)){
				cmdstrs.addElement(cutToSWITCH());
			}else if(cmdbody.startsWith("synchronized", index)){
				cmdstrs.addElement(cutToSYNCHRON());
			}else{
				cmdstrs.addElement(cutToFENHAO());
			}
			//移动到下一个不是空格的位置
			removeSpace();
		}
		return cmdstrs;
	}

	/**
	 * 移除当前的空格
	 * */
	private void removeSpace(){
		while(index<cmdbody.length()&&cmdbody.charAt(index)==' '){
			index ++;
		}
	}
	/**
	 * 解析switch块
	 * */
	private String cutToSWITCH() {
		int startindex = index;
		
		int state = 0;
		do{
			char ch = cmdbody.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		
		return cmdbody.substring(startindex, ++index);
	}
	/**
	 * 解析到一个if语句
	 * */
	private String cutToIF(){
		int startindex = index;
		int state = 0;
		
		boolean boo = true;//循环判断变量
		do{
			//找到外层的一个}
			do{
				char ch = cmdbody.charAt(index);
				if(ch == '{'){
					state ++;
				}else if(ch == '}'){
					state --;
					if(state == 0){//找到了搭配的了
						//判断后面有没有else
						//去除空格
						
						index++;
						
						removeSpace();
						
						if(!cmdbody.startsWith("else", index)){//完了
							boo = false;
							break ;
						}
					}
				}
				index++;
			}while(index<cmdbody.length());
			
		}while(boo);
		
		return cmdbody.substring(startindex, index);
	}
	/**
	 * 解析到一个for或者while
	 * */
	private String cutToFORorWHILE(){
		int startindex = index;
		
		int state = 0;
		do{
			char ch = cmdbody.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		
		return cmdbody.substring(startindex, ++index);
	}
	/**
	 * 解析到一个do
	 * */
	private String cutToDO(){
		//找到最外面的‘{’的位置
		int startindex = index;
		
		int state = 0;
		do{
			char ch = cmdbody.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		//现在的index就是最后一个大括号的位置
		
		return cmdbody.substring(startindex, index = cmdbody.indexOf(';', index)+1);
	}
	/**
	 * 利用index解析一个语句
	 * */
	private String cutToFENHAO(){
		StringBuilder lins = new StringBuilder();
		char ch;
		int state = 0;//标志位
		do{
			ch = cmdbody.charAt(index);
			lins.append(ch);
			if(ch == '('||ch == '['||ch == '{'){
				state ++;
			}else if(ch == ')'||ch == ']'||ch == '}'){
				state --;
			}else if(ch==';'){
				if(state == 0){
					index ++;
					break;
				}
			}
			index ++;
		}while(index<cmdbody.length());
		return lins+"";
	}
   /**
    * 解析完一个try
    * */
	private String cutToTRY(){
		
		int startindex = index;
		do{//第一次找到try语句完成，第二次就判断catch.
			int state = 0;
			do{
				char ch = cmdbody.charAt(index);
				if(ch == '{'){
					state ++;
				}else if(ch == '}'){
					state --;
					if(state == 0){//找到了搭配的了
						index++;
						break;
					}
				}
				index++;
			}while(index<cmdbody.length());
			
			removeSpace();//移动到下一个不是空格的地方
			
			if(cmdbody.startsWith("catch", index)){
				continue;
			}else{
				break;
			}
		}while(true);
		return cmdbody.substring(startindex, index);
	}
	
	/**
	 * 一个同步块
	 * */
	private String cutToSYNCHRON() {
		int startindex = index;
		
		int state = 0;
		do{
			char ch = cmdbody.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//找到了搭配的了
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		
		return cmdbody.substring(startindex, ++index);
	}
}