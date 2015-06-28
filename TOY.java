
//Project 1
//project implement lexical analyzer

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

//keyword end with @ and id end with *
public class TOY {
	private static String[] reservedWords = {"boolean", "break", "class", "double", "else", 
			"extends", "false", "for", "if", "implements", "int", "interface", "newarray",
			"println", "readln", "return", "string", "true", "void", "while"};
	private String[] operators = {"+","-","*","/","%","<","<=",">",">=","==", "!=",
			"&&","||", "!","=",";",",",".","(",")","[","]","{", "}"};
	private Tri myTri;	
	private File file;
	private FileInputStream fis;
	private BufferedReader reader;
	private StringBuilder unknown;
	private char current; //current char
	private String str; //
	private boolean multComm = false; // boolean for multi-line comment
	private boolean strConst = false; //for string constant
	private boolean isDub = false;
	private char  next;		//next
	
//constructor---------------------------------------------------------------------------
private TOY() throws IOException{
		myTri = new Tri();
		//add reserver word to container (Tri)
		for(int i=0; i<reservedWords.length; i++)
			myTri.addString(reservedWords[i], '@');
			unknown=new StringBuilder();
			file= new File("input.txt");
			
			fis = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(fis));
			start(fis);
		
	}
	//----------------------------------------------------------------------------------
//main program to start TOY
private void start(FileInputStream fis) throws IOException{
loop:
	while((str = reader.readLine()) != null){
	for(int i = 0; i< str.length(); i++){		
		current = str.charAt(i);
		isDub = false;
		if(!strConst && (current == '"' || current == '“')) //string constant
		{
			System.out.print("stringconstant ");
			strConst = true;
			continue;
		}
		if(strConst)
		{
			if(current == '"' || current == '”')
			{
				strConst = false;
				continue;
			}
			else
				continue;
		}
		if(multComm)
		{
			if(current == '*' && str.length() != i+1 && str.charAt(i+1) == '/'){ //end multiline comment
				multComm = false;
				i +=1;
				if( i < str.length())
					continue;
				else 
					continue loop;
			}
			else
				continue;
		}
	   if(Character.toUpperCase(current) == 'E' && str.charAt(i+1) == '+'){
		   isDub = true;
		   i++;
		   unknown.append(current);
		   unknown.append(str.charAt(i));
		   continue;
	   }
	   if(current == '.'  && Character.isDigit(str.charAt(i+1)))
	   {
		   isDub = true;
		   i++;
		   unknown.append(current);
		   unknown.append(str.charAt(i));
		   continue;
	   }
	   if(Arrays.asList(operators).contains(Character.toString(current)) && !isDub){//if char a operator
			if(current == '/'){ //comment
				if(str.length() != i+1 && str.charAt(i+1) == '/') // line comment
					continue loop;
				else if(str.length() != i+1 && str.charAt(i+1) == '*'){ //multiline comment
					multComm = true;
					continue;
				}
			}
						
			if(unknown.length() > 0 && unknown.charAt(0) != ' '){ //print what it currently has{		
				if(Character.isDigit(unknown.charAt(0)))
					checkNum();
				else{
					if(unknown.toString().equals("true") || unknown.toString().equals("false"))
						System.out.print("booleanconstant ");
					else System.out.print("id ");
					myTri.addString(unknown.toString(), '@');
				}
				unknown.delete(0, unknown.length());
			}
			checkOperator(Character.toString(current), fis);
		}
		else if(current == ' ' || current == '\t')
			checkUnknown();
		else
			unknown.append(current);
		}			
		if(!multComm) System.out.println();
	}
    System.out.println();
	myTri.printSwitch();
	System.out.println();
	myTri.printSymbolAndNext();
}
//----------------------------------------------------------------------------------
//check string for reserved word
private void checkUnknown(){
	unknown.trimToSize();
	if(unknown.length()>0){
		if(Character.isDigit(unknown.charAt(0)))
			checkNum();
		else{
			if(Arrays.asList(reservedWords).contains(unknown.toString()))
				System.out.print(unknown + " ");		
			else {
				System.out.print("id ");
				myTri.addString(unknown.toString(), '@');
			}
		}
	}
	unknown.delete(0, unknown.length());
}
//----------------------------------------------------------------------------------
//check to see if the current char is a operator
private void checkOperator(String stringcheck, FileInputStream fis) throws IOException{
	
		//next = (char)fis.read(); check <= or >= ==
		if(Arrays.asList(operators).contains(next)){
			stringcheck +=next;
			next = (char)fis.read();
		}
		printOperator(stringcheck);			
}
//----------------------------------------------------------------------------------
private void checkNum(){ //checks to see if intconstant or double constant and 
						//leaves off the rest
	int i = 0; //index for substring
	boolean isHex = false;
	boolean isDouble = false;
	String subString;
	if(unknown.charAt(0) == '0' && unknown.length() > 1 
	                            && Character.toUpperCase(unknown.charAt(i+1)) != 'X'){
		isHex = true;
		i = 2;
	}
	if(isHex){
		for(; i < unknown.length(); i++)
		{
			if(Character.toUpperCase(unknown.charAt(i)) == 'A' ||
				Character.toUpperCase(unknown.charAt(i)) == 'B' ||
				Character.toUpperCase(unknown.charAt(i)) == 'C' ||
				Character.toUpperCase(unknown.charAt(i)) == 'D' ||
				Character.toUpperCase(unknown.charAt(i)) == 'E' ||
				Character.toUpperCase(unknown.charAt(i)) == 'F' ||
				Character.isDigit(unknown.charAt(i)))
				continue;
			else
			{
				subString = unknown.substring(i);
				unknown.delete(0, unknown.length());
				unknown.append(subString);
			}
		}
		System.out.print("intconstant ");
	}
	else{ //regular integer constant or a double constant
		for(; i < unknown.length();i++)
		{
			if(Character.isDigit(unknown.charAt(i)))
				continue;
			else if(unknown.charAt(i) == '.'){
				isDouble = true;
				continue;
			}
			if(Character.toUpperCase(unknown.charAt(i)) == 'E')
				continue;
			if(unknown.charAt(i) == '+')
				continue;
		}
		subString = unknown.substring(i);
		unknown.delete(0, unknown.length());
		unknown.append(subString);
		if(isDouble) System.out.print("doubleconstant ");
		else System.out.print("intconstant ");
			
	}
}
//----------------------------------------------------------------------------------
//check which operator it is then print id
private void printOperator(String stringcheck){
	switch (stringcheck){
	case "+":
		System.out.print("plus ");
		break;
	case "-":
		System.out.print("minus ");
		break;
	case "*":
		System.out.print("multiplication ");
		break;
	case "/":
		System.out.print("division ");
		break;
	case "%":
		System.out.print("mod ");
		break;
	case "<":
		System.out.print("less ");
		break;
	case "<=":
		System.out.print("lessequal ");
		break;
	case ">":
		System.out.print("greater ");
		break;
	case ">=":
		System.out.print("greaterequal ");
		break;
	case "==":
		System.out.print("equal ");
		break;
	case "!=":
		System.out.print("notequal ");
		break;
	case "&&":
		System.out.print("and ");
		break;
	case "||":
		System.out.print("or ");
		break;
	case "!":
		System.out.print("not ");
		break;
	case "=":
		System.out.print("assignop ");
		break;
	case ";":
		System.out.print("semicolon ");
		break;
	case ",":
		System.out.print("comma ");
		break;
	case ".":
		System.out.print("period ");
		break;
	case "(":
		System.out.print("leftparen ");
		break;
	case ")":
		System.out.print("rightparen ");
		break;
	case "[":
		System.out.print("leftbracket ");
		break;
	case "]":
		System.out.print("rightbracket ");
		break;
	case "{":
		System.out.print("leftbrace ");
		break;
	case "}":
		System.out.print("rightbrace ");
		break;
	default:
		break;
	}
}
//----------------------------------------------------------------------------------
public static void main(String[] args) throws IOException{ TOY myToy = new TOY(); }
}
