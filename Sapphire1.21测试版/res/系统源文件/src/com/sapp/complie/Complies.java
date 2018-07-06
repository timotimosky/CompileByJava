package com.sapp.complie;
import java.util.Vector;

/**
 * ����һ��������ࡣ
 * ��Ҫ����䷭��Ϊ����ʽ���롣
 * */
public class Complies implements Cloneable{
	
	/**
	 * ������ࡣ
	 * */
	public static final int FENLEI = 0;
	/**
	 * ��䴦��
	 * */
	public static final int YJCD = 1;
	/**
	 * if�鴦��
	 * */
	public static final int IFCD = 2;
	/**
	 * do�鴦��
	 * */
	public static final int DOCD = 3;
	/**
	 * for�鴦��
	 * */
	public static final int FORCD = 4;
	/**
	 * while�鴦��
	 * */
	public static final int WHHILECD = 5;
	/**
	 * try�鴦��
	 * */
	public static final int TRYCD = 6;
	/**
	 * switch�鴦��
	 * */
	public static final int SWITCHCD = 8;
	/**
	 * break,continue��return,throw����
	 * */
	public static final int BRKCONRE = 7;
	/**
	 * synchronized�鴦��
	 * */
	public static final int SYNCHRONCD = 9;
	
	/***
	 * @param vcmds ��������������.
	 * @param code Ҫ����Ĵ���.
	 * @param dealtype ���������.
	 * */
	public void complies(Vector<String> vcmds,String code,int dealtype) throws Exception{
		code = code.trim();//ÿ��ȥ�����ߵĿ�
		//���ദ�����
		switch(dealtype){
		case FENLEI://�������
			toDealFENLEI(vcmds,code);
		break;
		case YJCD://�����룬һ�����������ȥ������Ҫ�Ŀո�
			
			toDealYJCD(vcmds,code);
		break;
		case IFCD://if��
			toDealIFCD(vcmds,code);
		break;
		case DOCD://do��
			toDealDOCD(vcmds,code);
		break;
		case FORCD://for��
			toDealFORCD(vcmds,code);
		break;
		case WHHILECD://while��
			toDealWHHILECD(vcmds,code);
		break;
		case TRYCD://try��
			toDealTRYCD(vcmds,code);
		break;
		case SWITCHCD://switch��
			toDealSWITCHCD(vcmds,code);
		break;
		case BRKCONRE://break,continue��return��throw  
			toDealBRKCONRE(vcmds,code);
		break;
		case SYNCHRONCD://synchronized��
			toDealSYNCHRONCD(vcmds,code);
		break;
		default:
			throw new Exception("δ֪�Ĵ������͡�");
		}
	}
	
	/***************ϸ�ڷ���
	 * @throws Exception 
	 * ******************/
	//----�������
	private void toDealFENLEI(Vector<String> vcmds, String code) throws Exception {
		Vector<String> cmdstrs = new Codedivide(code.trim()).divideCmdBody();//������û������������ַ�
		for(String cmd:cmdstrs){//ѭ������
			
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
			}else if(cmd.startsWith("synchronized")){//ͬ��
				complies(vcmds,cmd,SYNCHRONCD);
			}else if(cmd.startsWith("try")){//
				complies(vcmds,cmd,TRYCD);
				//�ؼ���
			}else if(cmd.startsWith("break")||cmd.startsWith("continue")
					||cmd.startsWith("return")||cmd.startsWith("throw")){
				complies(vcmds,cmd,BRKCONRE);
			}else{
				complies(vcmds,cmd,YJCD);
			}
		}
	}

	/**
	 * ������䣬ʹ���﷨����
	 * @throws Exception 
	 * */
	private void toDealYJCD(Vector<String> vcmds, String code) throws Exception {
		AnalysedTree nantre = new AnalysedTree(null);//�޸��﷨��
		nantre.analysedCode(code.substring(0, code.length()-1));//�������
		nantre.getRoot().complieTree(vcmds, AnalysedTree.COMOBJECT);//�������
	}
	
	//һЩ�ؼ���break,continue,��return.
	private void toDealBRKCONRE(Vector<String> vcmds, String code) throws Exception {
		//System.out.println(code);
		code = code.substring(0, code.length()-1).trim();
		
		if(code.equals("break")){
			vcmds.add("[BREAK]");
		}else if(code.equals("continue")){
			vcmds.add("[CONTINUE]");
		}else if(code.startsWith("return")){
			if(code.trim().length()!=6){//��ֻ��return
				
				String ayj = "PUSH="+code.substring(6).trim()+";";
				complies(vcmds,ayj,YJCD);
			}
			vcmds.add("[RETURN]");
		}else if(code.startsWith("throw")){//�׳��쳣
			String excname = "$exception";
			String excobj = code.substring(5).trim();
			if(code.matches("throw[ ]*[(](.|\n|\t)*")){//�������쳣����
				excname = code.substring(code.indexOf('(')+1, code.indexOf(')')).trim();
				excobj = code.substring(code.indexOf(')')+1).trim();
			}
			String nowcode = excname+"="+excobj+";";//������ʽ����һ��������������
			complies(vcmds,nowcode,YJCD);
			vcmds.add("[THROW]");
		}else{
			throw new Exception("���ܽ����ķ���:"+code);
		}
	}
	//----------����try catch��
	private void toDealTRYCD(Vector<String> vcmds, String code) throws Exception {
		
		//���д������ν���
		//����λ��
		int index = 0;
		
		while(index<code.length()){
			//�ƶ����ǿո�
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
				//��ȡcatch�е����
				int state = 0;
				int staz = index;
				do{
					char ch = code.charAt(index);
					if(ch == '('){
						state ++;
					}else if(ch == ')'){
						state --;
						if(state == 0){//�ҵ��˴������
							break;
						}
					}
					index++;
				}while(index<code.length());
				
				String pjstr = code.substring(staz+1, index++);//�������һ�����
				
				complies(vcmds, "PUSH="+pjstr+";", YJCD);
				
				vcmds.add("[PD]POP");//[PD]����.
				
			}else if(code.startsWith("{", index)){
				//�����Ĵ����
				int bhs = index;
				
				//�ҵ���һ�������}
				int state = 0;
				do{
					char ch = code.charAt(index);
					if(ch == '{'){
						state ++;
					}else if(ch == '}'){
						state --;
						if(state == 0){//�ҵ��˴������
							break;
						}
					}
					index++;
				}while(index<code.length());
				
				String bhstr = code.substring(bhs+1, index++);
				
				complies(vcmds, bhstr, FENLEI);//�����˰����Ĵ��롣
			}
		}
		vcmds.add("[END]");
	}
	private void toDealWHHILECD(Vector<String> vcmds, String code) throws Exception {
	
		int index = 0;
		
		int xkhs = index = code.indexOf('(', index);
		//Ѱ������)
		int state = 0;
		do{
			char ch = code.charAt(index);
			if(ch == '('){
				state ++;
			}else if(ch == ')'){
				state --;
				if(state == 0){//�ҵ��˴������
					break;
				}
			}
			index++;
		}while(index<code.length());
		
		String bhstr = code.substring(xkhs+1,index++);
		
		/*******����Ϊwhile��ִ����������ѭ����********/
		//whileѭ����ʼ��־
		vcmds.add("[WHILE]");
		
		complies(vcmds, "PUSH="+bhstr+";", YJCD);//ִ��Ҫ�жϵ����
		
		vcmds.add("[PD]POP");
		
		vcmds.add("[LP]");
		
		//����
		int dkhs = index = code.indexOf('{', index);
		//Ѱ������}
		do{
			char ch = code.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//�ҵ��˴������
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
		//����forѭ����һ������
		//��ȡforѭ���е�()�еĲ���
		int index = 0;
		
		int xkhs = index = code.indexOf('(', index);
		//Ѱ������)
		int state = 0;
		do{
			char ch = code.charAt(index);
			if(ch == '('){
				state ++;
			}else if(ch == ')'){
				state --;
				if(state == 0){//�ҵ��˴������
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
			//ִ�е�һ������
			complies(vcmds, cmdstr, YJCD);
		}
		
		//forѭ����ʼ
		vcmds.add("[FOR]");
		
		//�ڶ�������
		//����һ���жϵ��������
		if(parts[1].trim().length()!=0){//�еڶ���
			complies(vcmds, "PUSH="+parts[1].trim()+";", YJCD);
		}else{
			complies(vcmds, "PUSH=true;", YJCD);
			
		}
		
		//PD���
		vcmds.add("[PD]POP");

		//����������
		if(parts[2].trim().length()!=0){
			String zj = parts[2].trim()+";";
			//ִ�������仯
			complies(vcmds, zj, YJCD);
		}
		
		vcmds.add("[LP]");
		
		//ѭ����
		int dkhs = index = code.indexOf('{', index);
		//Ѱ������}
		do{
			char ch = code.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//�ҵ��˴������
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
		
		//�жϱ��
		int state = 0;
		
		int xkhj = code.lastIndexOf(')');//���������)��λ��
		
		int laststart = xkhj;//��¼����λ��
		
		//��ǰѰ������(
		do{
			char ch = code.charAt(xkhj);
			if(ch == ')'){
				state ++;
			}else if(ch == '('){
				state --;
				if(state == 0){//�ҵ��˴������
					break;
				}
			}
			xkhj --;
		}while(xkhj>0);
		
		String xhstr = code.substring(xkhj+1, laststart);
		
		//doѭ����ʼ��־
		vcmds.add("[DO]");
		
		complies(vcmds, "PUSH="+xhstr+";", YJCD);
		vcmds.add("[PD]POP");
		
		//ѭ����
		vcmds.add("[LP]");
		
		int dkhj = code.lastIndexOf('}');
		
		laststart = dkhj;
		//��ǰѰ������{
		do{
			char ch = code.charAt(dkhj);
			if(ch == '}'){
				state ++;
			}else if(ch == '{'){
				state --;
				if(state == 0){//�ҵ��˴������
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
		//���������ַ�������
		int index = 0;
		
		while(index<code.length()){
			//�ƶ����ǿո�
			while(index<code.length()&&code.charAt(index)==' '){
				index++;
			}
			if(code.startsWith("if", index)){
				if(!isadd){
					vcmds.add("[IF]");//ֻ����һ��
					isadd = true;
				}
				index += 2;
			}else if(code.startsWith("else", index)){
				vcmds.add("[ELSE]");
				index += 4;
			}else if(code.startsWith("(", index)){
				//����һ���ж�
				int xkhs = index;
				//Ѱ������)
				int state = 0;
				do{
					char ch = code.charAt(index);
					if(ch == '('){
						state ++;
					}else if(ch == ')'){
						state --;
						if(state == 0){//�ҵ��˴������
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
				//Ѱ������}
				int state = 0;
				do{
					char ch = code.charAt(index);
					if(ch == '{'){
						state ++;
					}else if(ch == '}'){
						state --;
						if(state == 0){//�ҵ��˴������
							break;
						}
					}
					index++;
				}while(index<code.length());
				
				String dhstr = code.substring(dkhs+1, index++);
				
				complies(vcmds, dhstr, FENLEI);
				
			}else{
				throw new Exception("δ�ܽ�����if��䣺"+code.substring(index));
			}
		}
		vcmds.add("[END]");
	}
	private void toDealSWITCHCD(Vector<String> vcmds, String code) throws Exception {//switch��
		
		vcmds.add("[SWITCH]");
	
		//�ҵ���֧��ʼ���
		int startin = code.indexOf('(');
		int state = 0;
		int index = startin;
		do{
			char ch = code.charAt(index);
			if(ch == '('){
				state ++;
			}else if(ch == ')'){
				state --;
				if(state == 0){//�ҵ��˴������
					break;
				}
			}
			index++;
		}while(index<code.length());
		String pddx = code.substring(startin+1, index++).trim();
		
		complies(vcmds, "PDOBJ="+pddx+";", YJCD);
		
		//�ҵ��������е�{}����
		index = code.indexOf('{',index);
		index++;
		while(index<code.length()){
			//�ƶ����ǿո�
			while(index<code.length()&&code.charAt(index)==' '){
				index++;
			}
			
			if(code.startsWith("case", index)){//һ��case��֧
				
				vcmds.add("[CASE]");
				
				int cast = index;
				
				index = code.indexOf(':',index);
				
				String bjdu = code.substring(cast+4, index++).trim();//����������
				
				complies(vcmds, "PUSH="+bjdu+";", YJCD);
				
				complies(vcmds, "PUSH=PDOBJ==POP;", YJCD);
				
				vcmds.add("[PD]POP");
				
				//����Ĵ��� �ҵ���һ��case,default����}
				int start = index;//��ʼ�����
				int statet = 0;//  ; ��־ 
				
				while(index<code.length()){
					char ch = code.charAt(index);					
					if(ch == '{'){//������ֻ��{}�п�����case
						statet ++;
					}else if(ch == '}'){//}						
						if(statet == 0){
							index++;
							break;
						}
						statet --;
					}else if(statet ==0&&ch == 'c'){//case�Ŀ�ͷ
						if(code.startsWith("case", index)){
							break;
						}
					}else if(statet ==0&&ch == 'd'){//default�Ŀ�ͷ
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
				//�ҵ���������
				int start = ++index;//��ʼ�����
				int statet = 0;//  ; ��־ 
				while(index<code.length()){
					char ch = code.charAt(index);
					if(ch == '{'){//������ֻ��{}�п�����case
						statet ++;
					}else if(ch == '}'){//}
						if(statet == 0){
							index ++;
							break;
						}
						statet --;
					}else if(statet ==0&&ch == 'c'){//����������
						if(code.startsWith("case", index)){
							throw new Exception("defaultӦ�������ķ��ӣ���������������case��");
						}
					}else if(statet ==0&&ch == 'd'){//����������
						if(code.startsWith("default", index)){
							throw new Exception("switch��ֻ����һ��default��֧��");
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
	private void toDealSYNCHRONCD(Vector<String> vcmds, String code) throws Exception {//ͬ���飬
		//���ͬ���ı�������
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
 * һ�����봦�����
 * */
class Codedivide{
	
	//����
	Codedivide(String code){
		cmdbody = code;
	}
	//----------------------����ԭʼ�����
	/**
	 * ��������λ��
	 * */
	int index = 0;//��ǰ��������λ��
	/**
	 * Ӧ�ý������ַ���
	 * */
	private String cmdbody = null;
	/**
	 * �����е����Ϳ�ֽ⿪��
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
			//�ƶ�����һ�����ǿո��λ��
			removeSpace();
		}
		return cmdstrs;
	}

	/**
	 * �Ƴ���ǰ�Ŀո�
	 * */
	private void removeSpace(){
		while(index<cmdbody.length()&&cmdbody.charAt(index)==' '){
			index ++;
		}
	}
	/**
	 * ����switch��
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
				if(state == 0){//�ҵ��˴������
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		
		return cmdbody.substring(startindex, ++index);
	}
	/**
	 * ������һ��if���
	 * */
	private String cutToIF(){
		int startindex = index;
		int state = 0;
		
		boolean boo = true;//ѭ���жϱ���
		do{
			//�ҵ�����һ��}
			do{
				char ch = cmdbody.charAt(index);
				if(ch == '{'){
					state ++;
				}else if(ch == '}'){
					state --;
					if(state == 0){//�ҵ��˴������
						//�жϺ�����û��else
						//ȥ���ո�
						
						index++;
						
						removeSpace();
						
						if(!cmdbody.startsWith("else", index)){//����
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
	 * ������һ��for����while
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
				if(state == 0){//�ҵ��˴������
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		
		return cmdbody.substring(startindex, ++index);
	}
	/**
	 * ������һ��do
	 * */
	private String cutToDO(){
		//�ҵ�������ġ�{����λ��
		int startindex = index;
		
		int state = 0;
		do{
			char ch = cmdbody.charAt(index);
			if(ch == '{'){
				state ++;
			}else if(ch == '}'){
				state --;
				if(state == 0){//�ҵ��˴������
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		//���ڵ�index�������һ�������ŵ�λ��
		
		return cmdbody.substring(startindex, index = cmdbody.indexOf(';', index)+1);
	}
	/**
	 * ����index����һ�����
	 * */
	private String cutToFENHAO(){
		StringBuilder lins = new StringBuilder();
		char ch;
		int state = 0;//��־λ
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
    * ������һ��try
    * */
	private String cutToTRY(){
		
		int startindex = index;
		do{//��һ���ҵ�try�����ɣ��ڶ��ξ��ж�catch.
			int state = 0;
			do{
				char ch = cmdbody.charAt(index);
				if(ch == '{'){
					state ++;
				}else if(ch == '}'){
					state --;
					if(state == 0){//�ҵ��˴������
						index++;
						break;
					}
				}
				index++;
			}while(index<cmdbody.length());
			
			removeSpace();//�ƶ�����һ�����ǿո�ĵط�
			
			if(cmdbody.startsWith("catch", index)){
				continue;
			}else{
				break;
			}
		}while(true);
		return cmdbody.substring(startindex, index);
	}
	
	/**
	 * һ��ͬ����
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
				if(state == 0){//�ҵ��˴������
					break;
				}
			}
			index++;
		}while(index<cmdbody.length());
		
		return cmdbody.substring(startindex, ++index);
	}
}