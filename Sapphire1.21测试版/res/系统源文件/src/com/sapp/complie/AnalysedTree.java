package com.sapp.complie;

import java.util.Vector;

import com.sapp.tools.CompliesTools;

/**
 * 语法分析树
 * */
public class AnalysedTree {
	/**
	 * 这个分析树的操作符
	 * */
	public String OPERATOR = "";
	/**
	 * 父节点
	 * */
	public AnalysedTree fatherBranch = null;
	/**
	 * 左树枝
	 * */
	public AnalysedTree leftBranch = null;
	/**
	 * 右树枝
	 * */
	public AnalysedTree rightBranch = null;
	
	//构造方法
	public AnalysedTree(AnalysedTree parent){//父节点
		fatherBranch = parent;
	}
	
	/**
	 * 字面值正则式
	 * */
	public static final String[] zzRegex = new String[]{
			"null","true","false","[-,+]?[0-9]+","[-,+]?[0-9]+[.][0-9]*",
			"\"(.|\n|\r)*\"","'(.|\n|\r)*'"
	};
	/**
	 * 字面值类
	 * */
	public static final String[] zzObj = new String[]{//这些本地类必须由create方法
			"SappNull","SappBoolean","SappBoolean","SappInteger","SappDecimal",
			"SappString","SappChar"
	};
	/**
	 * 是否是字面值
	 * */
	public static String isZMvalue(String str){
		for(int i = 0;i<zzRegex.length;i++){
			if(str.matches(zzRegex[i])){
				return zzObj[i];
			}
		}
		return null;
	}
	/**
	 * 特殊结构正则式
	 * */
	public static final String[] spRegex  = new String[]{"\\[.*\\]","\\{.*\\}"};
	/**
	 * 特殊结构类
	 * */
	public static final String[] spObj  = new String[]{"SappArray","SappHash"};
	/**
	 * 是否是特殊结构
	 * */
	public static String isSPvalue(String str){
		for(int i = 0;i<spRegex.length;i++){
			if(str.matches(spRegex[i])){
				return spObj[i];
			}
		}
		return null;
	}
	/**
	 * 分析一段代码。
	 * @throws Exception 
	 * */
	public void analysedCode(String code) throws Exception{
		//这段代码不能再最外面存在括号，要保证可分析，或者达到最简。
		code = code.trim();//去除两边的空格
		
		if(code.length()==0){//没有代码。
			return;
		}
		//System.out.println("解释代码:"+code);
		//从左至右找出第一个操作符。
		Analysed anal = new Analysed();
		//分析代码
		while(true){
			dividePart(code,anal);
			//System.out.println("!!!!!!!!!!!!!!!!!!!");
			//System.out.println("分解得到的左边:"+anal.leftpart);
			//System.out.println("分解得到的操作符:"+anal.operator);
			//System.out.println("分解得到的右边:"+anal.rightpart);
			//System.out.println("-------------------");
			//如果没有找到操作符了
			if(anal.operator.equals("")){//说明分解为了一个整体
				//如果分析的这个整体两边是一对括号。
				if(code.charAt(0)=='('&&code.charAt(code.length()-1)==')'){
					code = code.substring(1, code.length()-1).trim();
					anal.clear();
					anal.allpart = true;
					//重新分析
				}else{
					OPERATOR = code;
					return;
				}
			}else{//不为空就退出
				break;
			}
		}
		
		//生成新的树
		if(fatherBranch!=null){//有父节点，那么就要比较父节点与当前的操作符的优先级了。
			int thisgrade = CompliesTools.getOperatorGrade(anal.operator);
			int paregrade = CompliesTools.getOperatorGrade(fatherBranch.OPERATOR);
			//这里要先没有括号。
			if(!anal.allpart&&(thisgrade<paregrade||
					(thisgrade==paregrade&&!CompliesTools.isRightOperator(anal.operator)))){
				//System.out.println("后运算:"+anal.operator);
				//构造新的枝,父枝是当前父枝的父枝。
				//System.out.println("这个操作符:"+anal.operator+"优先级低。");
				this.analysedCode(anal.leftpart);//当前树分析左边的枝
				//构造一个新的节点
				AnalysedTree parenew = new AnalysedTree(fatherBranch.fatherBranch);
				if(fatherBranch.fatherBranch!=null){//
					fatherBranch.fatherBranch.rightBranch = parenew;//父节点父节点的有节点为新的对象
				}
				fatherBranch.fatherBranch = parenew;
				
				parenew.OPERATOR = anal.operator;
				//新的节点的左枝为父枝。
				parenew.leftBranch = fatherBranch;
				//新的节点有一个新的右枝
				parenew.rightBranch = new AnalysedTree(parenew);
				parenew.rightBranch.analysedCode(anal.rightpart);
				return;
			}
		}
		
		//没有父节点
		OPERATOR = anal.operator;
		
		//左边
		leftBranch = new AnalysedTree(this);
		leftBranch.analysedCode(anal.leftpart);
		
		//右边
		rightBranch = new AnalysedTree(this);
		rightBranch.analysedCode(anal.rightpart);
		
	}
	/**
	 * 一个分析的内部类
	 * */
	private class Analysed{
		public boolean allpart = false;//是否有括号
		public String operator = null;//操作符
		public String leftpart = null;//左边的部分
		public String rightpart = null;//右边的部分
		public void clear(){
			allpart = false;
			operator = null;
			leftpart = null;
			rightpart = null;
		}
	}
	//分析
	private void dividePart(String code,Analysed ana) throws Exception{
		
		//上一个字符
		char last = ' ';
		//状态标志
		int state = 0;
		//左边的字符串
		StringBuilder leftstr = new StringBuilder();
		//操作符
		StringBuffer operstr = new StringBuffer();

		for(int index = 0;index<code.length();index++){
			char ch = code.charAt(index);
			
			//分析
			if(CompliesTools.isOperator(last)){//上一个是操作符
				if(CompliesTools.isOperator(ch)){//这一个是操作符
					if(CompliesTools.isOperator(""+operstr+ch)){
						operstr.append(ch);
					}else{
						ana.operator = operstr+"";
						ana.leftpart = leftstr+"";
						ana.rightpart = 
							code.substring(leftstr.length()+operstr.length(),
									code.length());
						return;
					}
				}else{//这一个是操作数
					//解决小数的问题
					if(last=='.'&&Character.isDigit(ch)){//上一个是.这一个是数字
						operstr = new StringBuffer();
						leftstr.append("."+ch);
						last = ch;
						continue;
					}
					
					ana.operator = operstr+"";
					ana.leftpart = leftstr+"";
					ana.rightpart = 
						code.substring(leftstr.length()+operstr.length(),
								code.length());
					//System.out.println("LJ操作符:"+operstr);
					//System.out.println("LJ左操作数:"+leftstr);
					//System.out.println("LJ右操作数:"+ana.rightpart);
					return;
				}
			}else{//上一个是操作数
				if(CompliesTools.isOperator(ch)){//这一个是操作符
					//System.out.println("操作符:"+ch);
					operstr.append(ch);
				}else{//这一个是操作数
					//字符串
					if(ch=='\"'||ch=='\''){
						leftstr.append(ch);//添加第一个字符
						boolean iszy = false;//这里不忙解析转义
						while(index<code.length()){
							char now = code.charAt(++index);
							leftstr.append(now);
							if(iszy){//转义字符
								iszy = false;
							}else{
								if(now=='\"'||now=='\''){
									last = '\"';
									break;
								}else if(ch == '\\'){
									iszy = true;
								}
							}
						}
						continue;
					}
					
					
					//取整个括号
					if(ch == '('||ch == '['||ch == '{'){//遇到了括号了
						while(index<code.length()){
							char now = code.charAt(index);
							//System.out.println(state+"--add:"+now);
							leftstr.append(now);
							if(now == '('||now == '['||now == '{'){
								state ++;
							}else if(now == ')'||now == ']'||now == '}'){
								state --;
								if(state == 0){
									break;
								}
							}
							index++;
						}
						//System.out.println("->"+leftstr);
						last = ')';
						continue;
					}
					leftstr.append(ch);	
				}
			}
			last = ch;
		}
		//能运行到这个地方说明符号在最后的。
		ana.operator = operstr+"";
		ana.leftpart = leftstr+"";
		ana.rightpart = "";
	}
	
	/**
	 * 获得这个树的根
	 * */
	public AnalysedTree getRoot(){
		if(fatherBranch==null){
			//show(this);
			return this;
		}else{
			return fatherBranch.getRoot();
		}
	}
	
	/**
	 * 显示这个树
	 * */
	public void show(AnalysedTree tree){
			System.out.println("||||||||打印树||||||||");
			if(tree==null){
				System.out.println("空");
			}else{
				System.out.println("树根:"+tree.OPERATOR);
				System.out.println("<-");
				show(tree.leftBranch);
				System.out.println("->");
				show(tree.rightBranch);
			}
			System.out.println("----------------------");
	}
	/**
	 * 编译预期获得的类型。
	 * */
	public static final int COMOBJECT = 0;
	public static final int COMPARAM = 1;
	
	/**
	 * 编译这个分析树
	 * @return String 返回的一个字符串。
	 * @param type 这个树预期的类型
	 * @throws Exception 
	 * */
	public String complieTree(Vector<String> vstr,int comtype) throws Exception{
		//这里本身不是操作符，说明这个是最简的式子。
		if(CompliesTools.getOperatorGrade(OPERATOR)==-1){
			//字面值，不可能是参数
			String zmzstr = isZMvalue(OPERATOR);
			if(zmzstr!=null){
				if(comtype==COMOBJECT){
					vstr.add("PUSH<="+OPERATOR);
					return "POP";
				}else{
					return OPERATOR;
				}
			}
			
			//特殊操作符，不可能时参数。
			String spstr = isSPvalue(OPERATOR);
			if(spstr!=null){
				//类型
				int type = OPERATOR.charAt(0)=='['?0:1;
				//参数
				String parms = OPERATOR.substring(1, OPERATOR.length()-1);
				//分解参数
				Vector<String> elements = new Vector<String>();//所有元素的集合
				StringBuilder oneelem = new StringBuilder();//一个元素
				int stateli = 0;//标志
				for(char ch:parms.toCharArray()){
					if(ch=='('||ch=='['||ch=='{'){
						stateli++;
					}else if(ch==')'||ch==']'||ch=='}'){
						stateli--;
					}else if(ch==','){//元素结束标志
						if(stateli == 0){
							elements.add(oneelem+"");
							oneelem = new StringBuilder();
							continue;
						}
					}
					oneelem.append(ch);
				}
				if(oneelem.length()>0){
					elements.add(oneelem+"");//最后一个
				}
	
				//处理参数。
				for(String pstr:elements){
					if(type==0){//数组
						if(pstr.length()==0){//没有元素就为null
							pstr = "null";
						}
						AnalysedTree nes = new AnalysedTree(null);
						
						nes.analysedCode("PUSH="+pstr);
						nes.getRoot().complieTree(vstr,comtype);
					}else{
						if(pstr.length()==0){//没有元素就为null
							throw new Exception("Hash表中不允许存在空。");
						}
						//找到正确的=>
						int aindex = 0;
						int statte = 0;
						for(;aindex<pstr.length();aindex++){
							char ch = pstr.charAt(aindex);
							if(ch=='('||ch=='['||ch=='{'){
								statte++;
							}else if(ch==')'||ch==']'||ch=='}'){
								statte--;
							}else if(ch=='='){//元素结束标志
								if(statte == 0){
									if(pstr.charAt(aindex+1)=='>'){
										break;
									}
								}
							}
						}
						String keystr = pstr.substring(0, aindex).trim();
						String valuestr = pstr.substring(aindex+2).trim();
						
						AnalysedTree keytree = new AnalysedTree(null);
						keytree.analysedCode("PUSH="+keystr);
						keytree.getRoot().complieTree(vstr,comtype);
						
						AnalysedTree valutree = new AnalysedTree(null);
						valutree.analysedCode("PUSH="+valuestr);
						valutree.getRoot().complieTree(vstr,comtype);
					}
				}
				
				if(type == 0){
					vstr.add("PUSH<=["+elements.size()+"]");
				}else{
					vstr.add("PUSH<={"+elements.size()+"}");
				}
				
				return "POP";
			}
			
			//方法，其中操作符方法只能隐式调用。
			if(OPERATOR.matches("[_,\\p{Alpha}][_,\\p{Alnum}]*\\(.*\\)")){//方法
				
				//找出参数
				String pamstr = OPERATOR.substring(OPERATOR.indexOf('(')+1,
						OPERATOR.lastIndexOf(')')).trim();
				
				//分解参数。
				Vector<String> cspas = new Vector<String>();//参数的集合
				StringBuilder lins = new StringBuilder();
				//分割参数
				boolean isstring = false;//字符串
				boolean iszy = false;//转义
				int statet = 0;//这个状态
				for(char ch:pamstr.toCharArray()){
					if(isstring){//字符串状态
						if(iszy){//转义状态
							iszy = false;
						}else{
							if(ch == '\''||ch == '"'){
								isstring = false;
							}else if(ch == '\\'){
								iszy = true;
							}
						}
					}else{//非字符串状态
						if(ch == '('||ch == '{'||ch == '['){
							statet++;
						}else if(ch == ')'||ch == '}'||ch == ']'){
							statet--;
						}else if(ch == '\''||ch == '"'){
							isstring = true;
						}else if(ch == ','){
							if(statet == 0){
								cspas.add((lins+"").trim());
								lins = new StringBuilder();
								continue;
							}
						}
					}
					lins.append(ch);
				}
				if(lins.length()>0){
					cspas.add((lins+"").trim());
				}
				//处理每一个参数,压入每一个参数
				for(String pars:cspas){
					AnalysedTree nes = new AnalysedTree(null);
					nes.analysedCode("PUSH="+pars);
					nes.getRoot().complieTree(vstr,comtype);
				}
				
				//可能这个方法是一个隐藏了this指针的方法。
				//方法名。
				String metna = OPERATOR.substring(0, OPERATOR.indexOf('(')).trim();
				if(comtype==COMOBJECT){//编译类型为对象那么就隐藏this指针的方法。
					vstr.add("PUSH<=|Call>this:"+metna+":"+cspas.size()+"|");
					return "POP";
				}else{//是参数那么就是一个方法的后边部分。
					return metna+"-"+cspas.size();
				}
			}else if(OPERATOR.matches("[_,\\p{Alpha}][_,\\p{Alnum}]*(\\[.*\\])+")){//a[20];
				
				//分析字符串
				Vector<String> parms = new Vector<String>();
				int index = 0;
				while(index<OPERATOR.length()){
					index = OPERATOR.indexOf('[',index);
					if(index!=-1){
						int start = index;
						index = OPERATOR.indexOf(']',start);
						if(index!=-1){
							parms.add(OPERATOR.substring(start+1, index).trim());
						}else{
							throw new Exception("不能解析的符号:"+OPERATOR);
						}
					}else{
						break;
					}
					index++;
				}
				String objname = OPERATOR.substring(0, OPERATOR.indexOf('[')).trim();//对象
				
				vstr.add("PUSH<="+objname);//压入原来的对象
				for(int i=0;i<parms.size()-1;i++){
					vstr.add("PUSH<="+parms.elementAt(i));//压入参数。
					vstr.add("PUSH<=|Call>POP:[]:POP|");
				}
				if(comtype == COMOBJECT){
					
					vstr.add("PUSH<="+parms.elementAt(parms.size()-1));
					vstr.add("PUSH<=|Call>POP:[]:POP|");
					return "POP";
				}else{
					return "POP->"+parms.elementAt(parms.size()-1);
				}
				
			}else{//应该是一些变量	
				if(OPERATOR.equals("super")){//这个变量很特殊
					return OPERATOR;
				}
				
				if(!OPERATOR.equals("")&&comtype==COMOBJECT){//空的也不用压栈
					vstr.add("PUSH<="+OPERATOR);
					return "POP";
				}else{
					return OPERATOR;
				}
			}
		}else{//是操作符的话就要运算了。
			
			//获得左,右边的字符串
			String leftstr = "";
			String rightstr = "";
			
			//System.out.println("左:"+leftstr);
			//System.out.println("右:"+rightstr);
			//根据编译信号编译。
			//15
			if(OPERATOR.equals(".")){
				
				if(leftBranch!=null){//左边要求是对象。
					leftstr = leftBranch.complieTree(vstr,COMOBJECT);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMPARAM);
				}
				
				return toDealOperator15(vstr,leftstr,rightstr,comtype);
			}
			//14
			else if(OPERATOR.equals("<?")){

				if(leftBranch!=null){//左边要求是对象。
					leftstr = leftBranch.complieTree(vstr,COMOBJECT);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMPARAM);
				}
				
				return toDealOperator14(vstr,leftstr,rightstr);
			}
			//13
			else if(OPERATOR.equals("!")||OPERATOR.equals("~")||OPERATOR.equals("++")||OPERATOR.equals("--")){//同一样的操作
				if(leftBranch!=null){//左边要求是对象。
					leftstr = leftBranch.complieTree(vstr,COMOBJECT);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMOBJECT);
				}
				return toDealOperator13(vstr,leftstr,rightstr);
			}
			//上面是比较特殊的
			else if(OPERATOR.equals("+")||OPERATOR.equals("-")||OPERATOR.equals("*")||OPERATOR.equals("/")
					||OPERATOR.equals("%")||OPERATOR.equals("<<")||OPERATOR.equals(">>")||OPERATOR.equals(">")
					||OPERATOR.equals("<")||OPERATOR.equals(">=")||OPERATOR.equals("<=")||OPERATOR.equals("==")
					||OPERATOR.equals("!=")||OPERATOR.equals("&")||OPERATOR.equals("^")||OPERATOR.equals("|")
					||OPERATOR.equals("&&")||OPERATOR.equals("||")){
				if(leftBranch!=null){//左边要求是对象。
					leftstr = leftBranch.complieTree(vstr,COMOBJECT);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMOBJECT);
				}
				return toDealOperator12to3(vstr,leftstr,rightstr);
			}
			//1
			else if(OPERATOR.equals("=")||OPERATOR.equals("+=")||OPERATOR.equals("-=")||OPERATOR.equals("*=")
					||OPERATOR.equals("/=")||OPERATOR.equals("|=")||OPERATOR.equals("&=")||OPERATOR.equals("^=")
					||OPERATOR.equals(">>=")||OPERATOR.equals("<<=")||OPERATOR.equals("%=")){
				if(leftBranch!=null){//左边要求是参数。
					leftstr = leftBranch.complieTree(vstr,COMPARAM);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMOBJECT);
				}
				return toDealOperator1(vstr,leftstr,rightstr);
			}else{
				throw new Exception("不能解析的操作符:"+OPERATOR);
			}
		}
		
	}
	
	//-----------------------语法树处理操作符
	//15
	/**
	 * '.'操作符有两个用途。
	 * 1.为属性操作符。
	 * 2.为方法操作符。
	 * */
	private String toDealOperator15(Vector<String> vstr,String left,
			String right,int comtype) {
		if(right.matches("[_,\\p{Alpha}][_,\\p{Alnum}]*-[0-9]+")){//函数名-参数个数。参数都在栈中。
			String [] parms = right.split("-");
			vstr.add("PUSH<=|Call>"+left+":"+parms[0]+":"+parms[1]+"|");//N表示
			return "POP";
		}else{
			if(comtype == COMOBJECT ){
				vstr.add("PUSH<=|Get>"+left+":"+right+"|");
				return "POP";
			}else{
				return left+"."+right;
			}
		}
	}
	//14
	private String toDealOperator14(Vector<String> vstr, String leftstr,
			String rightstr) {
		vstr.add("PUSH<=|Instance>"+leftstr+":"+rightstr+"|");
		return "POP";
	}
	//13
	private String toDealOperator13(Vector<String> vstr, String leftstr,
			String rightstr) {
		if(OPERATOR.equals("++")||OPERATOR.equals("--")){//分为前置和后置
			int type = leftstr.equals("")?1:0;
			vstr.add("PUSH<="+type);
			if(type==1){				
				vstr.add("PUSH<=|Call>"+rightstr+":"+OPERATOR+":POP|");
			}else{
				vstr.add("PUSH<=|Call>"+leftstr+":"+OPERATOR+":POP|");
			}
			return "POP";
		}else{
			vstr.add("PUSH<=|Call>"+rightstr+":"+OPERATOR+":|");
			
			return "POP";
		}
	}
	//12-2
	private String toDealOperator12to3(Vector<String> vstr, String leftstr,
			String rightstr) {
		if(leftstr.equals("")&&(OPERATOR.equals("+")||OPERATOR.equals("-"))){
			if(rightstr.matches("[0-9]+[.]?[0-9]*")){//是数字
				return OPERATOR+rightstr;
			}else{//是操作符
				vstr.add("PUSH<=|Call>0:"+OPERATOR+":"+rightstr+"|");
				return "POP";
			}
		}else{
			vstr.add("PUSH<=|Call>"+leftstr+":"+OPERATOR+":"+rightstr+"|");
			return "POP";
		}
	}
	//1
	private String toDealOperator1(Vector<String> vstr, String leftstr,
			String rightstr) {
		if(leftstr.equals("PUSH")){//左边要求压栈
			if(!rightstr.equals("POP")){
				//右边不是弹栈。
				vstr.add("PUSH<="+rightstr);
				
			}
		}else{//左边不是压栈
			
			if(OPERATOR.equals("=")){
				vstr.add("PDOBJ<="+rightstr);
			}else{
				vstr.add("PDOBJ<=|Call>"+leftstr+":"+OPERATOR+":"+rightstr+"|");
			}
			vstr.add(leftstr+"<=PDOBJ");
			vstr.add("PUSH<=PDOBJ");
			vstr.add("[POPSTACK]");
		}
		return "POP";
	}
}
