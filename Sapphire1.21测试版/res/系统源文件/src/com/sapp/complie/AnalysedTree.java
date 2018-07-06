package com.sapp.complie;

import java.util.Vector;

import com.sapp.tools.CompliesTools;

/**
 * �﷨������
 * */
public class AnalysedTree {
	/**
	 * ����������Ĳ�����
	 * */
	public String OPERATOR = "";
	/**
	 * ���ڵ�
	 * */
	public AnalysedTree fatherBranch = null;
	/**
	 * ����֦
	 * */
	public AnalysedTree leftBranch = null;
	/**
	 * ����֦
	 * */
	public AnalysedTree rightBranch = null;
	
	//���췽��
	public AnalysedTree(AnalysedTree parent){//���ڵ�
		fatherBranch = parent;
	}
	
	/**
	 * ����ֵ����ʽ
	 * */
	public static final String[] zzRegex = new String[]{
			"null","true","false","[-,+]?[0-9]+","[-,+]?[0-9]+[.][0-9]*",
			"\"(.|\n|\r)*\"","'(.|\n|\r)*'"
	};
	/**
	 * ����ֵ��
	 * */
	public static final String[] zzObj = new String[]{//��Щ�����������create����
			"SappNull","SappBoolean","SappBoolean","SappInteger","SappDecimal",
			"SappString","SappChar"
	};
	/**
	 * �Ƿ�������ֵ
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
	 * ����ṹ����ʽ
	 * */
	public static final String[] spRegex  = new String[]{"\\[.*\\]","\\{.*\\}"};
	/**
	 * ����ṹ��
	 * */
	public static final String[] spObj  = new String[]{"SappArray","SappHash"};
	/**
	 * �Ƿ�������ṹ
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
	 * ����һ�δ��롣
	 * @throws Exception 
	 * */
	public void analysedCode(String code) throws Exception{
		//��δ��벻����������������ţ�Ҫ��֤�ɷ��������ߴﵽ���
		code = code.trim();//ȥ�����ߵĿո�
		
		if(code.length()==0){//û�д��롣
			return;
		}
		//System.out.println("���ʹ���:"+code);
		//���������ҳ���һ����������
		Analysed anal = new Analysed();
		//��������
		while(true){
			dividePart(code,anal);
			//System.out.println("!!!!!!!!!!!!!!!!!!!");
			//System.out.println("�ֽ�õ������:"+anal.leftpart);
			//System.out.println("�ֽ�õ��Ĳ�����:"+anal.operator);
			//System.out.println("�ֽ�õ����ұ�:"+anal.rightpart);
			//System.out.println("-------------------");
			//���û���ҵ���������
			if(anal.operator.equals("")){//˵���ֽ�Ϊ��һ������
				//����������������������һ�����š�
				if(code.charAt(0)=='('&&code.charAt(code.length()-1)==')'){
					code = code.substring(1, code.length()-1).trim();
					anal.clear();
					anal.allpart = true;
					//���·���
				}else{
					OPERATOR = code;
					return;
				}
			}else{//��Ϊ�վ��˳�
				break;
			}
		}
		
		//�����µ���
		if(fatherBranch!=null){//�и��ڵ㣬��ô��Ҫ�Ƚϸ��ڵ��뵱ǰ�Ĳ����������ȼ��ˡ�
			int thisgrade = CompliesTools.getOperatorGrade(anal.operator);
			int paregrade = CompliesTools.getOperatorGrade(fatherBranch.OPERATOR);
			//����Ҫ��û�����š�
			if(!anal.allpart&&(thisgrade<paregrade||
					(thisgrade==paregrade&&!CompliesTools.isRightOperator(anal.operator)))){
				//System.out.println("������:"+anal.operator);
				//�����µ�֦,��֦�ǵ�ǰ��֦�ĸ�֦��
				//System.out.println("���������:"+anal.operator+"���ȼ��͡�");
				this.analysedCode(anal.leftpart);//��ǰ��������ߵ�֦
				//����һ���µĽڵ�
				AnalysedTree parenew = new AnalysedTree(fatherBranch.fatherBranch);
				if(fatherBranch.fatherBranch!=null){//
					fatherBranch.fatherBranch.rightBranch = parenew;//���ڵ㸸�ڵ���нڵ�Ϊ�µĶ���
				}
				fatherBranch.fatherBranch = parenew;
				
				parenew.OPERATOR = anal.operator;
				//�µĽڵ����֦Ϊ��֦��
				parenew.leftBranch = fatherBranch;
				//�µĽڵ���һ���µ���֦
				parenew.rightBranch = new AnalysedTree(parenew);
				parenew.rightBranch.analysedCode(anal.rightpart);
				return;
			}
		}
		
		//û�и��ڵ�
		OPERATOR = anal.operator;
		
		//���
		leftBranch = new AnalysedTree(this);
		leftBranch.analysedCode(anal.leftpart);
		
		//�ұ�
		rightBranch = new AnalysedTree(this);
		rightBranch.analysedCode(anal.rightpart);
		
	}
	/**
	 * һ���������ڲ���
	 * */
	private class Analysed{
		public boolean allpart = false;//�Ƿ�������
		public String operator = null;//������
		public String leftpart = null;//��ߵĲ���
		public String rightpart = null;//�ұߵĲ���
		public void clear(){
			allpart = false;
			operator = null;
			leftpart = null;
			rightpart = null;
		}
	}
	//����
	private void dividePart(String code,Analysed ana) throws Exception{
		
		//��һ���ַ�
		char last = ' ';
		//״̬��־
		int state = 0;
		//��ߵ��ַ���
		StringBuilder leftstr = new StringBuilder();
		//������
		StringBuffer operstr = new StringBuffer();

		for(int index = 0;index<code.length();index++){
			char ch = code.charAt(index);
			
			//����
			if(CompliesTools.isOperator(last)){//��һ���ǲ�����
				if(CompliesTools.isOperator(ch)){//��һ���ǲ�����
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
				}else{//��һ���ǲ�����
					//���С��������
					if(last=='.'&&Character.isDigit(ch)){//��һ����.��һ��������
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
					//System.out.println("LJ������:"+operstr);
					//System.out.println("LJ�������:"+leftstr);
					//System.out.println("LJ�Ҳ�����:"+ana.rightpart);
					return;
				}
			}else{//��һ���ǲ�����
				if(CompliesTools.isOperator(ch)){//��һ���ǲ�����
					//System.out.println("������:"+ch);
					operstr.append(ch);
				}else{//��һ���ǲ�����
					//�ַ���
					if(ch=='\"'||ch=='\''){
						leftstr.append(ch);//��ӵ�һ���ַ�
						boolean iszy = false;//���ﲻæ����ת��
						while(index<code.length()){
							char now = code.charAt(++index);
							leftstr.append(now);
							if(iszy){//ת���ַ�
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
					
					
					//ȡ��������
					if(ch == '('||ch == '['||ch == '{'){//������������
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
		//�����е�����ط�˵�����������ġ�
		ana.operator = operstr+"";
		ana.leftpart = leftstr+"";
		ana.rightpart = "";
	}
	
	/**
	 * ���������ĸ�
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
	 * ��ʾ�����
	 * */
	public void show(AnalysedTree tree){
			System.out.println("||||||||��ӡ��||||||||");
			if(tree==null){
				System.out.println("��");
			}else{
				System.out.println("����:"+tree.OPERATOR);
				System.out.println("<-");
				show(tree.leftBranch);
				System.out.println("->");
				show(tree.rightBranch);
			}
			System.out.println("----------------------");
	}
	/**
	 * ����Ԥ�ڻ�õ����͡�
	 * */
	public static final int COMOBJECT = 0;
	public static final int COMPARAM = 1;
	
	/**
	 * �������������
	 * @return String ���ص�һ���ַ�����
	 * @param type �����Ԥ�ڵ�����
	 * @throws Exception 
	 * */
	public String complieTree(Vector<String> vstr,int comtype) throws Exception{
		//���ﱾ���ǲ�������˵�����������ʽ�ӡ�
		if(CompliesTools.getOperatorGrade(OPERATOR)==-1){
			//����ֵ���������ǲ���
			String zmzstr = isZMvalue(OPERATOR);
			if(zmzstr!=null){
				if(comtype==COMOBJECT){
					vstr.add("PUSH<="+OPERATOR);
					return "POP";
				}else{
					return OPERATOR;
				}
			}
			
			//�����������������ʱ������
			String spstr = isSPvalue(OPERATOR);
			if(spstr!=null){
				//����
				int type = OPERATOR.charAt(0)=='['?0:1;
				//����
				String parms = OPERATOR.substring(1, OPERATOR.length()-1);
				//�ֽ����
				Vector<String> elements = new Vector<String>();//����Ԫ�صļ���
				StringBuilder oneelem = new StringBuilder();//һ��Ԫ��
				int stateli = 0;//��־
				for(char ch:parms.toCharArray()){
					if(ch=='('||ch=='['||ch=='{'){
						stateli++;
					}else if(ch==')'||ch==']'||ch=='}'){
						stateli--;
					}else if(ch==','){//Ԫ�ؽ�����־
						if(stateli == 0){
							elements.add(oneelem+"");
							oneelem = new StringBuilder();
							continue;
						}
					}
					oneelem.append(ch);
				}
				if(oneelem.length()>0){
					elements.add(oneelem+"");//���һ��
				}
	
				//���������
				for(String pstr:elements){
					if(type==0){//����
						if(pstr.length()==0){//û��Ԫ�ؾ�Ϊnull
							pstr = "null";
						}
						AnalysedTree nes = new AnalysedTree(null);
						
						nes.analysedCode("PUSH="+pstr);
						nes.getRoot().complieTree(vstr,comtype);
					}else{
						if(pstr.length()==0){//û��Ԫ�ؾ�Ϊnull
							throw new Exception("Hash���в�������ڿա�");
						}
						//�ҵ���ȷ��=>
						int aindex = 0;
						int statte = 0;
						for(;aindex<pstr.length();aindex++){
							char ch = pstr.charAt(aindex);
							if(ch=='('||ch=='['||ch=='{'){
								statte++;
							}else if(ch==')'||ch==']'||ch=='}'){
								statte--;
							}else if(ch=='='){//Ԫ�ؽ�����־
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
			
			//���������в���������ֻ����ʽ���á�
			if(OPERATOR.matches("[_,\\p{Alpha}][_,\\p{Alnum}]*\\(.*\\)")){//����
				
				//�ҳ�����
				String pamstr = OPERATOR.substring(OPERATOR.indexOf('(')+1,
						OPERATOR.lastIndexOf(')')).trim();
				
				//�ֽ������
				Vector<String> cspas = new Vector<String>();//�����ļ���
				StringBuilder lins = new StringBuilder();
				//�ָ����
				boolean isstring = false;//�ַ���
				boolean iszy = false;//ת��
				int statet = 0;//���״̬
				for(char ch:pamstr.toCharArray()){
					if(isstring){//�ַ���״̬
						if(iszy){//ת��״̬
							iszy = false;
						}else{
							if(ch == '\''||ch == '"'){
								isstring = false;
							}else if(ch == '\\'){
								iszy = true;
							}
						}
					}else{//���ַ���״̬
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
				//����ÿһ������,ѹ��ÿһ������
				for(String pars:cspas){
					AnalysedTree nes = new AnalysedTree(null);
					nes.analysedCode("PUSH="+pars);
					nes.getRoot().complieTree(vstr,comtype);
				}
				
				//�������������һ��������thisָ��ķ�����
				//��������
				String metna = OPERATOR.substring(0, OPERATOR.indexOf('(')).trim();
				if(comtype==COMOBJECT){//��������Ϊ������ô������thisָ��ķ�����
					vstr.add("PUSH<=|Call>this:"+metna+":"+cspas.size()+"|");
					return "POP";
				}else{//�ǲ�����ô����һ�������ĺ�߲��֡�
					return metna+"-"+cspas.size();
				}
			}else if(OPERATOR.matches("[_,\\p{Alpha}][_,\\p{Alnum}]*(\\[.*\\])+")){//a[20];
				
				//�����ַ���
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
							throw new Exception("���ܽ����ķ���:"+OPERATOR);
						}
					}else{
						break;
					}
					index++;
				}
				String objname = OPERATOR.substring(0, OPERATOR.indexOf('[')).trim();//����
				
				vstr.add("PUSH<="+objname);//ѹ��ԭ���Ķ���
				for(int i=0;i<parms.size()-1;i++){
					vstr.add("PUSH<="+parms.elementAt(i));//ѹ�������
					vstr.add("PUSH<=|Call>POP:[]:POP|");
				}
				if(comtype == COMOBJECT){
					
					vstr.add("PUSH<="+parms.elementAt(parms.size()-1));
					vstr.add("PUSH<=|Call>POP:[]:POP|");
					return "POP";
				}else{
					return "POP->"+parms.elementAt(parms.size()-1);
				}
				
			}else{//Ӧ����һЩ����	
				if(OPERATOR.equals("super")){//�������������
					return OPERATOR;
				}
				
				if(!OPERATOR.equals("")&&comtype==COMOBJECT){//�յ�Ҳ����ѹջ
					vstr.add("PUSH<="+OPERATOR);
					return "POP";
				}else{
					return OPERATOR;
				}
			}
		}else{//�ǲ������Ļ���Ҫ�����ˡ�
			
			//�����,�ұߵ��ַ���
			String leftstr = "";
			String rightstr = "";
			
			//System.out.println("��:"+leftstr);
			//System.out.println("��:"+rightstr);
			//���ݱ����źű��롣
			//15
			if(OPERATOR.equals(".")){
				
				if(leftBranch!=null){//���Ҫ���Ƕ���
					leftstr = leftBranch.complieTree(vstr,COMOBJECT);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMPARAM);
				}
				
				return toDealOperator15(vstr,leftstr,rightstr,comtype);
			}
			//14
			else if(OPERATOR.equals("<?")){

				if(leftBranch!=null){//���Ҫ���Ƕ���
					leftstr = leftBranch.complieTree(vstr,COMOBJECT);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMPARAM);
				}
				
				return toDealOperator14(vstr,leftstr,rightstr);
			}
			//13
			else if(OPERATOR.equals("!")||OPERATOR.equals("~")||OPERATOR.equals("++")||OPERATOR.equals("--")){//ͬһ���Ĳ���
				if(leftBranch!=null){//���Ҫ���Ƕ���
					leftstr = leftBranch.complieTree(vstr,COMOBJECT);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMOBJECT);
				}
				return toDealOperator13(vstr,leftstr,rightstr);
			}
			//�����ǱȽ������
			else if(OPERATOR.equals("+")||OPERATOR.equals("-")||OPERATOR.equals("*")||OPERATOR.equals("/")
					||OPERATOR.equals("%")||OPERATOR.equals("<<")||OPERATOR.equals(">>")||OPERATOR.equals(">")
					||OPERATOR.equals("<")||OPERATOR.equals(">=")||OPERATOR.equals("<=")||OPERATOR.equals("==")
					||OPERATOR.equals("!=")||OPERATOR.equals("&")||OPERATOR.equals("^")||OPERATOR.equals("|")
					||OPERATOR.equals("&&")||OPERATOR.equals("||")){
				if(leftBranch!=null){//���Ҫ���Ƕ���
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
				if(leftBranch!=null){//���Ҫ���ǲ�����
					leftstr = leftBranch.complieTree(vstr,COMPARAM);
				}
				if(rightBranch!=null){
					rightstr = rightBranch.complieTree(vstr,COMOBJECT);
				}
				return toDealOperator1(vstr,leftstr,rightstr);
			}else{
				throw new Exception("���ܽ����Ĳ�����:"+OPERATOR);
			}
		}
		
	}
	
	//-----------------------�﷨�����������
	//15
	/**
	 * '.'��������������;��
	 * 1.Ϊ���Բ�������
	 * 2.Ϊ������������
	 * */
	private String toDealOperator15(Vector<String> vstr,String left,
			String right,int comtype) {
		if(right.matches("[_,\\p{Alpha}][_,\\p{Alnum}]*-[0-9]+")){//������-������������������ջ�С�
			String [] parms = right.split("-");
			vstr.add("PUSH<=|Call>"+left+":"+parms[0]+":"+parms[1]+"|");//N��ʾ
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
		if(OPERATOR.equals("++")||OPERATOR.equals("--")){//��Ϊǰ�úͺ���
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
			if(rightstr.matches("[0-9]+[.]?[0-9]*")){//������
				return OPERATOR+rightstr;
			}else{//�ǲ�����
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
		if(leftstr.equals("PUSH")){//���Ҫ��ѹջ
			if(!rightstr.equals("POP")){
				//�ұ߲��ǵ�ջ��
				vstr.add("PUSH<="+rightstr);
				
			}
		}else{//��߲���ѹջ
			
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
