package com.sapp.tools;

import java.util.Stack;
import java.util.Vector;

public class CompliesTools {
	/**
	 * 判断是否是操作符
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
	 * 操作符有单字节操作符和多字节操作符之分
	 * 比如 +是一个单字符操作符，++是一个双字节操作符
	 * 不同的类型的操作符不一样
	 *判断唯一能判断操作的方法
	 * */
	public static int getOperatorGrade(String str){//获得这个字符串数据的类型
		int grade = -1;//最低级 ，舍弃了,号运算符
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
		}else if(str.equals("<?")){//实例运算符
			grade = 14;
		}else if(str.equals(".")){
			grade = 15;//把
		}
		return grade;
	}
	/**
	 * 判断字符串是否是操作符
	 * */
	public static boolean isOperator(String s){
		return s.equals("+=")||s.equals("-=")||s.equals("*=")||s.equals("/=")||s.equals("%=")
		||s.equals("&=")||s.equals("^=")||s.equals("|=")||s.equals("<<=")||s.equals(">>=")
		||s.equals("||")||s.equals("&&")||s.equals("==")||s.equals("!=")||s.equals(">=")
		||s.equals("<=")||s.equals(">>")||s.equals("<<")||s.equals("<?")||s.equals("++")
		||s.equals("--");//=>指向运算，这个没有运算主要
	}
	
	/**
	 * 是否是右结合运算符
	 * */
	public static boolean isRightOperator(String s){
		return s.equals("=")||s.equals("+=")||s.equals("-=")||s.equals("*=")||s.equals("/=")||s.equals("%=")
		||s.equals("&=")||s.equals("^=")||s.equals("|=")||s.equals("<<=")||s.equals(">>=");
	}
	
	/**
	 * 找出一个操作符堆栈中优先级最高的操作符
	 * */
	public static String getBestOperatorGrade(Stack<String> opers){
		String oper = null;
		int lastgrade = -1;//上一个操作符的等级
		for(int i = opers.size()-1;i>=0;i--){
			String p = opers.get(i);//获得从最后的开始的
			int nowgrade = getOperatorGrade(p);//当前的操作符的等级
			if(nowgrade>=lastgrade){//这个的优先级大于上一个
				lastgrade = nowgrade;
				oper = p;
			}
		}
		return oper;
	}
	
	//判断一个变量是否是随机变量
	public static boolean isRandomName(String name){
		return name.matches("N[0-9]+");
	}
	
	public static <T> void printVector(Vector<T> vect){
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("集合中元素的个数:"+vect.size());
		for(T t:vect){
			System.out.println(t);
		}
		System.out.println("--------------------------");
	}
}
