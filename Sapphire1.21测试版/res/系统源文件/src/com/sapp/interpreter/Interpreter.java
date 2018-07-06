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
 * ����һ��������,��������Ĵ���ParamsInterface
 * */
public class Interpreter {
	
	//------����
	private SappEngine eng = null;
	//------�ַ�������ֵMap
	private Map<String,String> fcmap = null;
	//��ö�Ӧ�ַ�������ֵ���������
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
	
	//------���ڵ���,�����ȫ�־�Ϊnull
	/**
	 * ���ִ�е���һ����ķ���
	 * ���������
	 * */
	private SappClass classo = null;
	
	/**
	 * ���ִ�е���һ�������Ļ�
	 * */
	private SappMethod sappmet = null;
	//-----------------������
	/**
	 * ȫ�ֱ�����
	 * */
	private Map<String,SappClass> qjBLQ = null;//ȫ��
	/**
	 * ʵ��������
	 * */
	private Map<String,SappClass> slBLQ = null;//ʵ��
	/**
	 * ���������
	 * */
	private Map<String,SappClass> sjBLQ = null;//���
	/**
	 * �����ռ�
	 * */
	private Map<String,SappClass> ldxKJ = null;//�����ռ�
	//-----------------
	/**
	 * ��Ӧ����������
	 * */
	private Map<String,SappClass> dyldxBL = null;//��Ӧ����������
	
	//----------------------------------
	/**
	 * ��ʱ��ջ
	 * */
	public Stack<SappClass> stackspace = new Stack<SappClass>();
	//̫�������÷�����ֵ�洢
	public SappClass stackPop(){
	//	System.out.println(this+"��ջ->"+stackspace.peek().className);
		return stackspace.pop();
	}
	public void stackPush(SappClass sc){
	//	System.out.println(this+"ѹջ-<"+sc.className);
		stackspace.push(sc);
	}
	/**
	 * ��ʱ�����ջ����Ҫ�ֶ���ջ��
	 * */
	public Stack<SappClass> repstack = new Stack<SappClass>();
	//----------------------------------
	
	/**
	 * ����
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
		//���������
		if(classo!=null){//��������
			dyldxBL = ldxKJ.get(classo.className).slParams;
		}
	}

	/**
	 * ����ֵ����
	 * */
	public static final int RUNOK = -1;
	public static final int RETURN = 0;
	public static final int BREAK = 1;
	public static final int CONTINUE = 2;
	public static final int THROW = 3;
	public static final int RUNSTOP = 4;
	
	/**
	 * ִ��
	 * @return int 
	 * RUNOK ����ִ��;
	 * RETURN ����return;
	 * BREAK ����break;
	 * CONTINUE ����continue;
	 * THROW ����throw;
	 * RUNSTOP ����������������˳��źš�
	 * @throws  
	 * */
	public int exe(Vector<String> vcmds) {
		//��������������λ��
		int explposi = 0;
		//�ȴ�����ִ�е���ʱ���
		Vector<String> exestrs = null;
		//����������
		while(explposi<vcmds.size()){
			//ÿһ��������״̬
			int runstate = RUNOK;
			
			if(vcmds.elementAt(explposi).equals("[IF]")){//�ҵ���һ��[IF��]
				//���һ����
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//ִ����һ����
				runstate = toExeIF(exestrs);//����ֹͣ
			}else if(vcmds.elementAt(explposi).equals("[TRY]")){
				//���һ����
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//ִ����һ����
				runstate = toExeTRY(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[DO]")){
				//���һ����
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//ִ����һ����
				runstate = toExeDO(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[FOR]")){
				//���һ����
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//ִ����һ����
				runstate = toExeFOR(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[WHILE]")){
				//���һ����
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//ִ����һ����
				runstate = toExeWHILE(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[SWITCH]")){
				//���һ����
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//ִ����һ����
				runstate = toExeSWITCH(exestrs);
			}else if(vcmds.elementAt(explposi).equals("[SYNCHRONIZED]")){
				//���һ����
				exestrs = new Vector<String>();
				explposi = getPartCode(exestrs,vcmds,explposi);
				//ִ����һ����
				runstate = toExeSYNCHRONIZED(exestrs);
			}else {
				if(vcmds.elementAt(explposi).equals("[POP]")){
				//	stackspace.pop();//������ʱ����
					stackPop();
					explposi++;
				}else if(vcmds.elementAt(explposi).equals("[POPSTACK]")){
					repstack.pop();//������ʱ����
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
			if(runstate!=RUNOK){//ֻҪ���к��״̬��Ϊ�������оͷ���
				return runstate;
			}
		}

		return RUNOK;
	}
	
	/**
	 * ִ��while���
	 * @throws Exception 
	 * */
	private int toExeWHILE(Vector<String> exestrs){
		
		//�ҵ�[PD]��ǩ
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		//�ҵ�[LP]��ǩ
		int lpindex = 0;
		for(;lpindex<exestrs.size();lpindex++){
			if(exestrs.elementAt(lpindex).equals("[LP]")){
				break;
			}
		}
		//�ж�ǰִ�е����
		Vector<String> pdqv = new Vector<String>();
		for(int i=1;i<pdindex;i++){
			pdqv.add(exestrs.elementAt(i));
		}
		//�ж����
		String pdyj = exestrs.elementAt(pdindex);
		
		//ѭ�����
		Vector<String> xhtyj = new Vector<String>();
		for(int i=lpindex+1;i<exestrs.size()-1;i++){
			xhtyj.add(exestrs.elementAt(i));
		}
		//ִ��
		do{
			//ִ���ж�ǰ�����
			int state1 = exe(pdqv);
			if(state1!=RUNOK){//������ִ��
				return state1;
			}
			
			//�ж�
			Boolean boo = toExePD(pdyj);
			if(boo == null){//�жϳ����쳣
				return RUNSTOP;
			}else if(boo){//true
				int state = exe(xhtyj);//ִ��ѭ�����״̬
				if(state == BREAK){//����break
					break;
				}else if(state == CONTINUE){//����continue
					continue;
				}else if(state != RUNOK){//����������״̬
					return state;
				}
			}else{//false
				break;
			}
		}while(true);
		
		return RUNOK;//����ִ��
	}
	/**
	 * ִ��do���
	 * @throws Exception 
	 * */
	private int toExeDO(Vector<String> exestrs) {

		//�ҵ�[PD]��ǩ
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		//�ҵ�[LP]��ǩ
		int lpindex = 0;
		for(;lpindex<exestrs.size();lpindex++){
			if(exestrs.elementAt(lpindex).equals("[LP]")){
				break;
			}
		}
		//�ж�ǰִ�е����
		Vector<String> pdqv = new Vector<String>();
		for(int i=1;i<pdindex;i++){
			pdqv.add(exestrs.elementAt(i));
		}
		//�ж����
		String pdyj = exestrs.elementAt(pdindex);
		
		//ѭ�����
		Vector<String> xhtyj = new Vector<String>();
		for(int i=lpindex+1;i<exestrs.size()-1;i++){
			xhtyj.add(exestrs.elementAt(i));
		}
		//ִ��
		do{
			//��ʼִ��ѭ����
			int state = exe(xhtyj);//ִ��ѭ�����״̬
			if(state == BREAK){//����break
				break;
			}else if(state == CONTINUE){//����continue
				continue;
			}else if(state != RUNOK){
				return state;
			}
			
			//ִ���ж�ǰ�����
			state = exe(pdqv);
			if(state != RUNOK){
				return state;
			}
			
			//�ж�
			Boolean boo = toExePD(pdyj);
			if(boo == null){//�жϳ����쳣
				return RUNSTOP;
			}else if(boo){//true
				continue;
			}else{//false
				break;
			}
		}while(true);
		
		return RUNOK;//����ִ��
		
	}
	/**
	 * ִ��FORѭ��
	 * @throws Exception 
	 * */
	private int toExeFOR(Vector<String> exestrs){

		//�ҵ�[PD]��ǩ
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		//�ҵ�[LP]��ǩ
		int lpindex = 0;
		for(;lpindex<exestrs.size();lpindex++){
			if(exestrs.elementAt(lpindex).equals("[LP]")){
				break;
			}
		}
		//�ж�ǰִ�е����
		Vector<String> pdqv = new Vector<String>();
		for(int i=1;i<pdindex;i++){
			pdqv.add(exestrs.elementAt(i));
		}
		//�ж����
		String pdyj = exestrs.elementAt(pdindex);
		//�����������
		Vector<String> dscsyj = new Vector<String>();
		for(int i=pdindex+1;i<lpindex;i++){
			dscsyj.add(exestrs.elementAt(i));
		}
		//ѭ�����
		Vector<String> xhtyj = new Vector<String>();
		for(int i=lpindex+1;i<exestrs.size()-1;i++){
			xhtyj.add(exestrs.elementAt(i));
		}
		//ִ��
		do{
			//ִ���ж�ǰ�����
			int state = exe(pdqv);
			if(state != RUNOK){
				return state;
			}
			//�ж�
			Boolean boo = toExePD(pdyj);
			if(boo == null){//�жϳ����쳣
				return RUNSTOP;
			}else if(!boo){//false
				break;
			}
			
			//��ʼִ��ѭ����
			state = exe(xhtyj);//ִ��ѭ�����״̬
			if(state == BREAK){//����break
				break;
			}else if(state != RUNOK&&state != CONTINUE){//,����continue���������ִ��.
				return state;
			}
			
			//ѭ������
			state = exe(dscsyj);
			if(state != RUNOK){
				return state;
			}
			
		}while(true);
		
		return RUNOK;
	}
	
	/**
	 * ִ��TRY���
	 * */
	private int toExeTRY(Vector<String> exestrs){
		//�ҵ����׵�[CATCH]��ǩ��
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
		
		//ִ����
		Vector<String> exepart = new Vector<String>();
		for(int i=1;i<caindex;i++){
			exepart.add(exestrs.elementAt(i));
		}
		//����catch��֧
		Vector<Vector<String>> catchstr = new Vector<Vector<String>>();
		//һ��catch��֧
		Vector<String> cathcs = new Vector<String>();
		//Ѱ��catch��֧
		
		int state12 = 0;//��־
		while(true){
			caindex ++;//Խ����һ��catch���
			String str = exestrs.elementAt(caindex);
			if(str.equals("[IF]")||str.equals("[DO]")||str.equals("[WHILE]")
					||str.equals("[TRY]")||str.equals("[FOR]")||str.equals("[SWITCH]")
					||str.equals("[SYNCHRONIZED]")){
				state12++;
			}else if(str.equals("[END]")){
				state12 --;
				if(state12 == -1){//��������end��
					catchstr.add(cathcs);
					break;
				}
			}else if(str.equals("[CATCH]")){
				if(state12 == 0){//������һ���catch�ˡ�
					catchstr.add(cathcs);
					cathcs = new Vector<String>();
					continue;
				}
			}
			cathcs.add(str);
		}
		
		//ִ��
		int state1 = exe(exepart);
		if(state1 == THROW){//�׳����쳣
			//�Ƿ��ܲ�׽
			boolean cancatch = false;
			for(Vector<String> vstr:catchstr){
				int state2 = toExeFZ(vstr);
				if(state2 == IFTRUE){
					cancatch = true;//��׽�ɹ�
					break;
				}else if(state2 == IFFALSE){//�����false��ִ����һ������
					continue;
				}else if(state2 != RUNOK){
					return state2;
				}
			}
			if(!cancatch){//û�в�׽�ɹ��ͼ����׳���
				return THROW;
			}
		}else if(state1 != RUNSTOP){
			return state1;
		}
		
		return RUNOK;
	}
	/**
	 * ִ��IF���
	 * */
	private int toExeIF(Vector<String> exestrs) {
		//���еķ���
		Vector<Vector<String>> fenzhis = new Vector<Vector<String>>();
		//�������if���ķ���
		Vector<String> onefz = new Vector<String>();//һ����֧
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
		//��ʼִ��ÿһ������
		for(Vector<String> vstrs:fenzhis){
			int bzf = toExeFZ(vstrs);//����
			if(bzf == IFTRUE){
				break;
			}else if(bzf == IFFALSE){//�����false��ִ����һ������
				continue;
			}else if(bzf != RUNOK){
				return bzf;
			}
		}
		
		return RUNOK;
	}
	/**
	 * ִ��һ����֧��������if����catch
	 * */
	private int toExeFZ(Vector<String> exestrs){
		//�ҵ�[PD]��ǩ
		int pdindex = 0;
		for(;pdindex<exestrs.size();pdindex++){
			if(exestrs.elementAt(pdindex).startsWith("[PD]")){
				break;
			}
		}
		if(pdindex == exestrs.size()){//û�ҵ�[PD]
			//ִ���������
			int state = exe(exestrs);
			if(state!=RUNOK){
				return state;
			}
			return IFTRUE;
		}else{
			//�ҵ���
			//�ж�ǰִ�е����
			Vector<String> pdqv = new Vector<String>();
			for(int i=0;i<pdindex;i++){
				pdqv.add(exestrs.elementAt(i));
			}
			//�ж����
			String pdyj = exestrs.elementAt(pdindex);
			
			//ִ����
			Vector<String> exepart = new Vector<String>();
			for(int i=pdindex+1;i<exestrs.size();i++){
				exepart.add(exestrs.elementAt(i));
			}
			
			//ִ��
			//�ж�ǰ
			int state = exe(pdqv);
			if(state!=RUNOK){
				return state;
			}
			//�ж�
			Boolean boo = toExePD(pdyj);
			if(boo == null){
				return RUNSTOP;
			}else if(boo){//ִ��
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
	//if���Ϊһ��false
	public static final int IFFALSE = 10;
	public static final int IFTRUE = 11;
	/**
	 * ִ��һ��switch��
	 * */
	private int toExeSWITCH(Vector<String> exestrs) {
		//�ҵ�switch�����еĶ��󣬴�[SWITCH]����һ��[CASE]
		//�ҵ�[PD]��ǩ
		int fkindex = 0;
		for(;fkindex<exestrs.size();fkindex++){
			if(exestrs.elementAt(fkindex).startsWith("[CASE]")){
				break;
			}
		}
		//�ҵ���Ҫ����
		Vector<String> zydx = new Vector<String>();
		for(int i=1;i<fkindex;i++){
			zydx.add(exestrs.elementAt(i));
		}
		
		//�ҵ�CASE����DEFAULT
		boolean havedefault = false;//�Ƿ���Ĭ��ֵ
		Vector<String> defals = new Vector<String>();
		//����CASE��֧
		Vector<Vector<String>> casestr = new Vector<Vector<String>>();
		//һ��CASE��֧
		Vector<String> cases = new Vector<String>();
		//Ѱ��CASE��֧
		int state12 = 0;//��־
		while(true){
			fkindex ++;//Խ����һ��case���
			String str = exestrs.elementAt(fkindex);
			if(str.equals("[IF]")||str.equals("[DO]")||str.equals("[WHILE]")
					||str.equals("[TRY]")||str.equals("[FOR]")||str.equals("[SWITCH]")
					||str.equals("[SYNCHRONIZED]")){
				state12++;
			}else if(str.equals("[END]")){
				state12 --;
				if(state12 == -1){//��������end��
					casestr.add(cases);
					break;
				}
			}else if(str.equals("[CASE]")||str.equals("[DEFAULT]")){
				if(state12 == 0){//������һ���catch�ˡ�
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
		//�鿴�Ƿ���default
		state12 = 0;
		if(havedefault){//��
			while(true){
				fkindex ++;//Խ����һ��case��default���
				String str = exestrs.elementAt(fkindex);
				if(str.equals("[IF]")||str.equals("[DO]")||str.equals("[WHILE]")
						||str.equals("[TRY]")||str.equals("[FOR]")||str.equals("[SWITCH]")
						||str.equals("[SYNCHRONIZED]")){
					state12++;
				}else if(str.equals("[END]")){
					state12 --;
					if(state12 == -1){//��������end��
						break;
					}
				}
				defals.add(str);
			}
		}
		//��case�����жϲ��ֺ�ִ�в��ַֿ���Ϊһ����
		HashMap<Integer,Integer> swindex = new HashMap<Integer,Integer>();//�жϴ��������ִ�в���λ��
		Vector<Vector<String>> fenzipd = new Vector<Vector<String>>();//��֧�ж�ǰ����
		Vector<String> pdcode = new Vector<String>();//�жϴ��롣
		Vector<String> allcode = new Vector<String>();//�ܹ���ִ�д��롣
		for(Vector<String> vstr:casestr){
			//�ҵ�[PD]��ǩ
			int pdindex = 0;
			for(;pdindex<vstr.size();pdindex++){
				if(vstr.elementAt(pdindex).startsWith("[PD]")){
					break;
				}
			}
			swindex.put(pdcode.size(), allcode.size());//��λ
			
			Vector<String> pdfz = new Vector<String>();//�ж�ǰ
			for(int i=0;i<pdindex;i++){
				pdfz.add(vstr.elementAt(i));
			}
			fenzipd.add(pdfz);//��ӽ��ж�ǰ��伯
			
			pdcode.add(vstr.elementAt(pdindex));//�ж�
			
			for(int i=pdindex+1;i<vstr.size();i++){ //ִ�����
				allcode.add(vstr.elementAt(i));
			}
		}
		//default���ӵ��ܼ�����
		if(havedefault){
			for(String code:defals){
				allcode.add(code);
			}
		}
		
		//ִ��switch���
		int state = exe(zydx);//�����Ҫ�ıȽ϶���
		if(state!=RUNOK){
			return state;
		}
		
		//ִ��case�����.
		boolean cancase = false;
		//�жϷ�֧��
		for(int i = 0;i<pdcode.size();i++){
			//�ж�ǰ���
			state = exe(fenzipd.elementAt(i));
			if(state!=RUNOK){
				return state;
			}
			//�ж�
			Boolean boo = toExePD(pdcode.elementAt(i));
			if(boo == null){
				return RUNSTOP;
			}else if(boo){//ִ�о��Ǵ����￪ʼִ��
				cancase = true;
				
				//��ȡ������ִ�еĶ�
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
		if(!cancase&&havedefault){//û�гɹ�ִ��case��������default��䡣
			state = exe(defals);//defalut;
			if(state!=RUNOK&&state!=BREAK){
				return state;
			}
		}
		return RUNOK;
	}
	

	private int toExeSYNCHRONIZED(Vector<String> exestrs) {
		//���ͬ������
		String sycpco = exestrs.elementAt(1);
		String sycpa = sycpco.substring(sycpco.indexOf(']')+1, sycpco.length());
		
		Vector<String> sycvcode = new Vector<String>();
		for(int i = 2;i<exestrs.size()-1;i++){
			sycvcode.add(exestrs.elementAt(i));
		}
		
		//ִ��
		//���ͬ������
		SappClass sycobj = null;
		try {
			sycobj = getObject(sycpa);
		}catch(RunStateException e){
			return e.state;//�쳣�Ĵ���
		} catch (Exception e) {
			eng.printError("�쳣:"+e.getMessage());
			return RUNSTOP;
		}
		//ִ��ͬ����
		synchronized(sycobj){
			//System.out.println("��ס����:"+sycobj);
			int state = exe(sycvcode);
			if(state!=RUNOK){
				return state;
			}
		}
		//System.out.println("��������:"+sycobj);
		return RUNOK;
	}
	/**
	 * ���һ���εĴ���
	 * */
	private int getPartCode(Vector<String> returns,Vector<String> allcodes,int startindex){
		//״̬��־
		int state = 0;
		for(;startindex<allcodes.size();startindex++){
			
			String nowcode = allcodes.elementAt(startindex);//��õ�ǰ�Ĵ���
			
			returns.add(nowcode);//�����һ����ǩ
			//�ж���ӵ���һ����ǩ
			if(nowcode.equals("[IF]")||nowcode.equals("[DO]")||nowcode.equals("[WHILE]")
					||nowcode.equals("[TRY]")||nowcode.equals("[FOR]")||nowcode.equals("[SWITCH]")
					||nowcode.equals("[SYNCHRONIZED]")){
				state ++;
			}else if(nowcode.equals("[END]")){
				state --;
				if(state == 0){//�����
					//������һ����ǩ
					startindex++;
					break;
				}
			}
		}
		return startindex;
	}
	
	//ִ���ж����.[PD]
	private Boolean toExePD(String cmd){
		//��ȡ�жϵ�����ַ�������
		String pdcmd = cmd.substring("[PD]".length(), cmd.length());
		
		SappClass lsobj = null;
		try {
			lsobj = getObject(pdcmd);
		} catch (Exception e) {
			System.out.println("û���ҵ�����:"+pdcmd);
			e.printStackTrace();
		}
		
		if(!lsobj.className.equals(fcmap.get("true"))){//����ǲ��ǿ����жϵ���
			eng.printError("���ܲ���������ж�:"+lsobj.className);
			return null;
		}
		
		return (Boolean)lsobj.object;
	}
	/**
	 * ִ����ͨ��䣬�����̿������
	 * @throws Exception 
	 * */
	private int toExeState(String cmd){
		/**
		 * ���ϵͳ����״̬
		 * */
		if(eng.runstate == SappEngine.STOP){//��鵽��ֹͣ״̬
			return RUNSTOP;
		}
		
		//��PT������������������
		//System.out.println("\nexe->"+cmd);
		//һ�����̵����,ֻ������[DY][FZ]
		//���ǰ��Ĳ�����
		int czf = cmd.indexOf("<=");
		String qian = cmd.substring(0, czf);
		String hou = cmd.substring(czf+2, cmd.length());
	
		//���ͺ���Ķ���
		SappClass houobj = null;
		try{
			houobj = getObject(hou);
		}catch(RunStateException e){
			return e.state;//�쳣�Ĵ���
		}catch(Exception e){
			e.printStackTrace();
			eng.printError("�쳣:"+e.getMessage());
			return RUNSTOP;
		}
		
		//System.out.println(qian+":"+hou+"="+houobj.className);
		
		//����ǰ��Ĳ���ȷ������
		//����ǰ��Ķ���
		if(qian.equals("PUSH")){
			stackPush(houobj);
			//stackspace.push(houobj);//ѹջ
			//System.out.println("ѹջ��"+houobj.className);
		}else if(qian.equals("PDOBJ")){//��ʱ����
			//System.out.println("ѹ����ʱ��ջ:"+houobj);
			repstack.push(houobj);
		}else if(qian.equals("this")){
			classo.transClass(houobj);
		}else if(qian.indexOf('.')!=-1){
			
			String []objs = qian.split("[.]");
			
			//ǰ��Ķ���
			SappClass lsobj = null;
			try{
				lsobj = getObject(objs[0]);
			}catch(RunStateException e){
				return e.state;//�쳣�Ĵ���
			}catch(Exception e){
				e.printStackTrace();
				eng.printError("�쳣:"+e.getMessage());
				return RUNSTOP;
			}
			
			//����Ĳ�����
			if(objs[1].equals("this")){
				lsobj.transClass(houobj);
			}else{
				if(objs[1].matches("[$]\\p{Alnum}+")){//��ȫ�ֶ���
					qjBLQ.put(objs[1], houobj);
				}else if(objs[1].matches("@@\\p{Alnum}+")){//��������ж���
					//���������ҵ������
					eng.thisClass.get(lsobj.className).slParams.put(objs[1], houobj);
				}else if(objs[1].matches("@\\p{Alnum}+")){//��ʵ�������ж���
					lsobj.slParams.put(objs[1], houobj);
				}
			}
			
		}else if(qian.indexOf("->")!=-1){//�±���� []
			
			//�ֽ������
			String[] pars = qian.split("->");
			
			//ǰ��ʱ����
			SappClass qianlsobj = null;
			SappClass houlsobj = null;
			try{
				qianlsobj = getObject(pars[0]);
				houlsobj = getObject(pars[1]);
			}catch(RunStateException e){
				return e.state;//�쳣�Ĵ���
			}catch(Exception e){
				e.printStackTrace();
				eng.printError("�쳣:"+e.getMessage());
				return RUNSTOP;
			}
			
			//���ǰ������put������
			SappMethod metob = qianlsobj.getMethodByName(qianlsobj.className,"set");
			if(metob==null){
				eng.printError("����:"+qianlsobj+"û���ҵ�����:\n��"
						+qianlsobj.className+",����:[]");
				return RUNSTOP;
			}
			
			//ִ���������
			try {
				exeMethod(qianlsobj,metob,houlsobj,houobj);
			} catch (RunStateException e) {
				return e.state;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			if(qian.matches("[$]\\p{Alnum}+")){//ԭʼ����
				qjBLQ.put(qian, houobj);
			}else if(qian.matches("@@\\p{Alnum}+")){
				dyldxBL.put(qian, houobj);
			}else if(qian.matches("@\\p{Alnum}+")){
				slBLQ.put(qian, houobj);
			}else if(qian.matches("\\p{Alpha}\\p{Alnum}*")){//�������
				sjBLQ.put(qian, houobj);
			}else{
				eng.printError("�Ƿ��ı�����:"+qian);
				return RUNSTOP;
			}
		}
		
		return RUNOK;
	}
	/**
	 * ִ��һ������
	 * �෽���ǹ����
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
		
		//����������Ҫ��ǰ�������
		String allname = met.methodName;
		//��ü���
		String metjhname = allname.substring(allname.indexOf('.')+1);
		
		if(!met.isNative){
			met = met.clone();
		}
		//����ִ�ж���
		met.setExeObject(obj);
		
		if(metjhname.equals("new")){
			
			SappClass retobj = obj.clone();
			
			SappMethod mets = retobj.getMethodByName(retobj.className,"new").clone();
			
			mets.setExeObject(retobj);
			
			mets.exeMethod(pacl);
			
			return retobj;
			
		}else{
			return met.exeMethod(pacl);//ִ�л�÷���ֵ
		}
	}
	/**
	 * ��ú����������
	 * @throws Exception 
	 * @throws Exception ���ݴ��������
	 * */
	private SappClass getObject(String houstr) throws Exception {
		
		SappClass houobj = null;
		//1.����������ַ����ǲ��� �ַ�������ֵ,Hash���������
		
		String lin = null;//��ʱ�ַ������ڼ�����Ƿ����ַ�������ֵʱ�������������ס��
		if(houstr.equals("POP")){
			houobj = stackPop();	
		}else if(houstr.equals("PDOBJ")){//��ʱ����
			houobj = repstack.peek();
			//System.out.println("�����ʱ��ջ����:"+houobj);
		}else if(houstr.equals("this")){
			houobj = classo;
		}else if((lin = getZFZMZclassname(houstr))!=null){
			//����������ֵ������ַ����Ļ���Ҫ����ת���ַ�
			if(houstr.matches("(\"|')(.|\n|\r)*(\"|')")){//�ַ��������ַ�
				houstr = InterpreterTools.toExplainString(houstr);
			}
			//�����ַ�������ֵ�뱾���ഴ��һ������.
			//System.out.println("����ֵ:"+houstr);
			houobj = toCreateObject(lin,houstr);
			
			if(houstr.matches("\\[[0-9]*\\]")){//����
				
				//��������еĶ������
				int objnum = Integer.parseInt(houstr.substring(1, houstr.length()-1));
				SappClass [] objs = new SappClass[objnum];
				for(int i = objnum-1;i>=0;i--){
					objs[i] = stackPop();//������������˳��
				}
				
				//�����������put��������native������
				SappMethod metp = houobj.getMethodByName(houobj.className,"put");
				if(metp==null){
					eng.printError("����:"+houobj+"û���ҵ�����:\n��"
							+houobj.className+",����:[]");
					eng.printError("put����Ҳ��һ���Ƚ�����ķ����������г�ʼ��ʱ����������" +
							"put(����)�����ݳ�ʼ������������ӡ�");
					throw new RunStateException(RUNSTOP);
				}
				
				//ִ�����Ԫ�ط���
				for(SappClass param:objs){//�����Ԫ�ض���������������
					exeMethod(houobj,metp,param);
				}
			}else if(houstr.matches("\\{[0-9]*\\}")){//Hash��
				
				//��������еĶ������
				int objnum = Integer.parseInt(houstr.substring(1, houstr.length()-1))*2;//����
				SappClass [] objs = new SappClass[objnum];
				for(int i = objnum-1;i>=0;i--){
					objs[i] = stackPop();//������������˳��
				}
				
				//���put������
				SappMethod metp = houobj.getMethodByName(houobj.className,"put");
				if(metp==null){
					eng.printError("����:"+houobj+"û���ҵ�����:\n��"
							+houobj.className+",����:[]");
					eng.printError("put����Ҳ��һ���Ƚ�����ķ�����Hash���г�ʼ��ʱ��ӱ����" +
							"put(����)�����ݳ�ʼ������������ӡ�");
					throw new RunStateException(RUNSTOP);
				}
				
				//ִ�з�����
				for(int i = 0;i<objnum;i+=2){
					exeMethod(houobj,metp,objs[i],objs[i+1]);
				}
			}
		}else if(houstr.matches("[|]{1}.+[|]{1}")){//�Ƿ�������Ĳ���
			//ȥ�������������||
			String opers = houstr.substring(1, houstr.length()-1);
			//��Ϊ����:һ�Ƿ��ʷ����������ж�ʵ��
			if(opers.startsWith("Call")){
				//��ȡ��ʼλ��
				int dys = opers.indexOf('>');
				//��ȡ������
				int nind = opers.indexOf(':');
				String objname = opers.substring(dys+1, nind).trim();
				//��ȡ������
				int find = opers.indexOf(':',nind+1);
				String metname = opers.substring(nind+1, find).trim();
				//��ò���
				String pars = opers.substring(find+1, opers.length()).trim();
				
				//���Ƚ���������
				SappClass [] pacl = null;
				if(pars.length()>0){
					if(pars.matches("[0-9]+")){//������һ������
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
				
				//���ҵ�����
				SappClass lsobj = null;//��ʱ����,�����Ķ���
				//��÷���
				SappMethod met = null;
				if(objname.equals("super")){
					lsobj = classo;//ִ�еĶ���Ϊ��ǰ�Ķ���
					//����������ķ���
					met = lsobj.getMethodByName(lsobj.className, metname);
					
					//�����������ĸ��Ƿ���
					met = met.getOverrided();
				}else{//sappmet
					lsobj = getObject(objname);	
					if(lsobj==classo){//ִ�е���this����
						met = lsobj.getMethodByName(sappmet.classname, metname);
					}else{
						met = lsobj.getMethodByName(lsobj.className, metname);
					}
				}				
				//��÷���
				if(met==null){
					eng.printError("����:"+lsobj+"û���ҵ�����:\n��"
							+lsobj.className+",����:"+metname);
					throw new RunStateException(RUNSTOP);
				}
				
				//���ݷ������������޸Ĳ���
				
				Vector<String> pastr = met.paramsVstr;
				for(int i = 0;i<pastr.size()&&i<pacl.length;i++){
					if(pastr.elementAt(i).startsWith("*")){//�������ò�����Ҫ��¡
						pacl[i] = pacl[i].clone();
					}
				}
				houobj = exeMethod(lsobj,met,pacl);
				
			}else if(opers.startsWith("Instance")){
				//��ö�������
				int dys = opers.indexOf('>');
				int fgh = opers.indexOf(':');
				//����
				String dxsy = opers.substring(dys+1, fgh).trim();
				//����
				String lname = opers.substring(fgh+1, opers.length()).trim();

				SappClass lsobj = getObject(dxsy);//��������
				
				//�����ٵ��ַ�������ֵ
				String zmz = eng.isInstance(lsobj.className,lname)?"true":"false";
				//�����ַ�������ֵ��ÿ�¡����
				houobj = toCreateObject(getZFZMZclassname(zmz),zmz);
			}else if(opers.startsWith("Get")){//|Get> : |
				//��ö�������
				int dys = opers.indexOf('>');
				int fgh = opers.indexOf(':');
				//����
				String dxsy = opers.substring(dys+1, fgh).trim();
				//����
				String lname = opers.substring(fgh+1, opers.length()).trim();
				
				SappClass lsobj = getObject(dxsy);//��ö���
				
				if(lname.matches("@@\\p{Alnum}+")){
					houobj = eng.thisClass.get(lsobj.className).slParams.get(houstr);
				}else if(lname.matches("@\\p{Alnum}+")){
					houobj = lsobj.slParams.get(houstr);
				}
				
			}
			
		}else{//�����ַ�������ֵ,��һ������
			if(houstr.matches("[$]\\p{Alnum}+")){
				houobj = qjBLQ.get(houstr);
			}else if(houstr.matches("@@\\p{Alnum}+")){
				houobj = dyldxBL.get(houstr);
			}else if(houstr.matches("@\\p{Alnum}+")){
				houobj = slBLQ.get(houstr);
			}else if(houstr.matches("\\p{Alpha}\\p{Alnum}*")){//�������
				if(ldxKJ.containsKey(houstr)){
					houobj = ldxKJ.get(houstr);
				}else{
					houobj = sjBLQ.get(houstr);
				}
			}else{
				eng.printError("�Ƿ��ı�����:"+houstr);
				throw new RunStateException(RUNSTOP);
			}
			/*if(houstr.equals("name")){
				System.out.println(houobj.className);
			}*/
			
		}
		//���������������ǿն���Ļ�
		if(houobj == null){
			houobj = eng.thisClass.get(fcmap.get("null")).clone();
		}
		
		return houobj;
	}
	
	//�����ַ�������ֵ�ͱ����ഴ������
	/**
	 * @param clan ���ض�Ӧ�ַ�������ֵ�����������
	 * @param hou ����ֵ
	 * @throws Exception 
	 * */
	private SappClass toCreateObject(String clan,String hou) throws Exception{
		
		//java������
		String jacl = eng.nativeJava;
		//System.out.println("����ת����:"+jacl);
		//����һ��java�����ࡣ
		SappClass paramobj = eng.thisClass.get(jacl).clone();
		paramobj.object = hou;
		
		//ִ�ж���
		SappClass obj = eng.thisClass.get(clan);
		
		//��÷���
		SappMethod met = obj.getMethodByName(obj.className, "create");
		
		return exeMethod(obj, met, paramobj);
	}
}
