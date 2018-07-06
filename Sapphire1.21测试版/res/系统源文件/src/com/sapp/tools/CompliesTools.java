package com.sapp.tools;

import java.util.Stack;
import java.util.Vector;

public class CompliesTools {
	/**
	 * �ж��Ƿ��ǲ�����
	 * */
	public static boolean isOperator(char ch){
		return ch == '.'//
				||ch == '!'||ch == '~'||ch == '+'
					||ch == '-'||ch == '*'||ch == '/'
						||ch == '%'||ch == '<'||ch == '>'
							||ch == '='||ch == '&'||ch == '|'
								||ch == '^'||ch == '?'
									||ch == ',';
	}
	/**
	 * �������е��ֽڲ������Ͷ��ֽڲ�����֮��
	 * ���� +��һ�����ַ���������++��һ��˫�ֽڲ�����
	 * ��ͬ�����͵Ĳ�������һ��
	 *�ж�Ψһ���жϲ����ķ���
	 * */
	public static int getOperatorGrade(String str){//�������ַ������ݵ�����
		int grade = -1;//��ͼ� ��������,�������
		if(str.equals(",")){
			grade = 0;
		}else if(str.equals("=")||str.equals("+=")||str.equals("-=")||str.equals("*=")||str.equals("/=")||str.equals("%=")
				||str.equals("&=")||str.equals("^=")||str.equals("|=")||str.equals(">>=")||str.equals("<<=")){
			grade = 1;
		}else if(str.equals("||")){
			grade = 3;
		}else if(str.equals("&&")){
			grade = 4;
		}else if(str.equals("|")){
			grade = 5;
		}else if(str.equals("^")){
			grade = 6;
		}else if(str.equals("&")){
			grade = 7;
		}else if(str.equals("==")||str.equals("!=")){
			grade = 8;
		}else if(str.equals("<")||str.equals(">")||str.equals("<=")||str.equals(">=")){
			grade = 9;
		}else if(str.equals("<<")||str.equals(">>")){
			grade = 10;
		}else if(str.equals("+")||str.equals("-")){
			grade = 11;
		}else if(str.equals("*")||str.equals("/")||str.equals("%")){
			grade = 12;
		}else if(str.equals("!")||str.equals("~")||str.equals("++")||str.equals("--")){
			grade = 13;
		}else if(str.equals("<?")){//ʵ�������
			grade = 14;
		}else if(str.equals(".")){
			grade = 15;//��
		}
		return grade;
	}
	/**
	 * �ж��ַ����Ƿ��ǲ�����
	 * */
	public static boolean isOperator(String s){
		return s.equals("+=")||s.equals("-=")||s.equals("*=")||s.equals("/=")||s.equals("%=")
		||s.equals("&=")||s.equals("^=")||s.equals("|=")||s.equals("<<=")||s.equals(">>=")
		||s.equals("||")||s.equals("&&")||s.equals("==")||s.equals("!=")||s.equals(">=")
		||s.equals("<=")||s.equals(">>")||s.equals("<<")||s.equals("<?")||s.equals("++")
		||s.equals("--");//=>ָ�����㣬���û��������Ҫ
	}
	
	/**
	 * �Ƿ����ҽ�������
	 * */
	public static boolean isRightOperator(String s){
		return s.equals("=")||s.equals("+=")||s.equals("-=")||s.equals("*=")||s.equals("/=")||s.equals("%=")
		||s.equals("&=")||s.equals("^=")||s.equals("|=")||s.equals("<<=")||s.equals(">>=");
	}
	
	/**
	 * �ҳ�һ����������ջ�����ȼ���ߵĲ�����
	 * */
	public static String getBestOperatorGrade(Stack<String> opers){
		String oper = null;
		int lastgrade = -1;//��һ���������ĵȼ�
		for(int i = opers.size()-1;i>=0;i--){
			String p = opers.get(i);//��ô����Ŀ�ʼ��
			int nowgrade = getOperatorGrade(p);//��ǰ�Ĳ������ĵȼ�
			if(nowgrade>=lastgrade){//��������ȼ�������һ��
				lastgrade = nowgrade;
				oper = p;
			}
		}
		return oper;
	}
	
	//�ж�һ�������Ƿ����������
	public static boolean isRandomName(String name){
		return name.matches("N[0-9]+");
	}
	
	public static <T> void printVector(Vector<T> vect){
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("������Ԫ�صĸ���:"+vect.size());
		for(T t:vect){
			System.out.println(t);
		}
		System.out.println("--------------------------");
	}
}
