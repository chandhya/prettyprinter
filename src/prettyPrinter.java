


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import java.io.FileReader;
import java.util.Scanner;


public class prettyPrinter {
	String sCurrentLine;
	BufferedReader br;
	static String sb ="";
	 static int countDoubleQuote=0;
	 static int countSingleQuote=0;
	 static int testCaseNo =9;
	 //use to identify whether current parenthesis is the last one.
	 static int countDoubleParenthesis = 0;
	
	 static int countNoBracketComma = 0;  
	 static int previousIndentBeforeNoBracket=0;
	 static String commaString ;
	 static boolean isNoBracketComma = false; //whether it has any spaces added because of those code without brackets.
	 static boolean isAction = false;
	 static String action="";
	 static int countSpace = 0;
	 static int index;
	 static int binaryIndex;
	 static int charCount = 0;
	 static boolean arryBracket = false;
	 static String[] arry;
	 static int countOfComments = 1 ;
	 static boolean printComments = false;
	 static boolean isInComment = false;
	 static boolean isElse = false;
	
	public static void main(String[] args) {
		prettyPrinter pr = new prettyPrinter();
		System.out.print("Enter input Test Case No:");
		Scanner sn = new Scanner(System.in);
		testCaseNo = sn.nextInt();
		pr.readFile();
		pr.generateOutput();	
		
		 arry = sb.split("((?<=\\s)|(?=\\s))|((?<=\\()|(?=\\())|((?<=\\{)|(?=\\{))|((?<=\\})|(?=\\}))|((?<=\\))|(?=\\)))|((?<=\\;)|(?=\\;))|((?<=\\t)|(?=\\t))|((?<=//)|(?=//))");
		 //use to identify whether current char is in ""
		 
		 for( index = 0; index < arry.length;index++)
		 {
		 //System.out.println(arry[index]);
			 if(arry[index].equals("\t") ) continue;
			
			 pr.checkForQuotes();
			
			 //if current char is not in quotes
			 if(countDoubleQuote%2 == 0 && countSingleQuote%2 == 0 && countDoubleParenthesis%2 == 0)
			 {
				 //assign a newline based on what is previous char
				 	pr.createNewLine();
				 	 if(arry[index].contains("//")){
						 if(countOfComments%2 == 1){
							 System.out.print(arry[index]);
							 countOfComments++;
						 }else{
							 action = "newline";
							 isAction = true;
							 countOfComments++;
						 }
		 				 continue;
					 }
				 	pr.alignComments();
				    if(arry[index].equals(" ") && action.equals("newline"))
				    continue;
				    //when read the end of the attached line, reset the space.
				    pr.resetSpace();
				    
				   	pr.actionOnBrackets();
				    pr.handleBinaryOperators();
					pr.handleCommaSeperatedValues();
			 }
			 else
			 {
			 //normal string will be print normally
				 pr.checkCharLimit();
			 }
		 }
		 
		 
		 
	}
	private void generateOutput() {
		try{
		    System.out.println("output"+testCaseNo+".txt is generated");
			System.setOut(new PrintStream(new File("output"+testCaseNo+".txt")));
		    
		}
		                
		catch(FileNotFoundException e1){
		   System.out.print("Output file not found");
		}
		
	}
	private void handleElse(){
		
			countSpace++;
			System.out.print("else");
			System.out.println();
			System.out.print(printSpaces(countSpace));
			System.out.print(arry[index].substring(4));
			countSpace--;
	}
	private void alignComments() {
	
		if(arry[index].contains("/*")||arry[index].equals("/*")){
			while(! arry[index].contains("*/")){
				if(!arry[index].equals("*") && arry[index].contains("/*")){
					System.out.print("/*");
					System.out.println();
					String c = arry[index].substring(2,arry[index].length());
					System.out.print(" "+c);
				}
				else if(arry[index].contains("--")){
					String[] c = arry[index].split("--");
 					System.out.println(c[0]);
 					System.out.print(" "+ c[1]);
				}
				else
					{
					System.out.print(arry[index]);
					}
				index++;
			}
			String[] c = arry[index].split("[*/]");
			for(String x : c){
				if(x.contains("--")){
					String[] k = arry[index].split("--");
 					System.out.println(k[0]);
 					System.out.println("*/");
 				}else if(!x.equals("")){
 					System.out.print(printSpaces(countSpace)+x.trim());
 				}else{
 					System.out.print(x.trim());
 				}
			}
 			index++;
 			action = "";
//			 charCount =0;
//			System.out.print("*/");
//			index++;
//			System.out.println();
//			 charCount =0;
 			 checkForQuotes();
		}
	}
	private void checkCharLimit() {
		String newStr = arry[index];
		if(countDoubleParenthesis%2 != 0){
			if((arry[index].trim().contains("++") || arry[index].trim().contains("--"))&&arry[index].trim().contains(" ")){
				newStr = "";
				for(char c : arry[index].trim().toCharArray()){
					if(c!=' ')
						newStr += String.valueOf(c);
				}
			}
		}
		arry[index] = newStr;
	    charCount = charCount+arry[index].length();

		if(charCount<80)
			   System.out.print(arry[index]);
		 else
		 	   {  
 		       	 System.out.print( "\"+");
				  System.out.println();
				 System.out.print("   ");
				  System.out.print("\"");
				  charCount =1;
				  System.out.print(arry[index]);
			   }
	}

	private void handleCommaSeperatedValues() {
		if(arry[index].contains(","))
			commaString = arry[index];
		if(commaString !=null)
		for(int q=0;q<commaString.length();q++){
			if(commaString.charAt(q)==',')
				{System.out.print(commaString.charAt(q));
				if(q+1<commaString.length() )
					if(!(commaString.charAt(q+1)==' '))
					System.out.print(" ");
				}
			else 
				System.out.print(commaString.charAt(q));
		}
		commaString =null;
		
	}

	private void handleBinaryOperators() {
		
		if(arry[index].contains("+") ||arry[index].contains("%"))
		{
			String binaryString = arry[index];
			binaryIndex = binaryString.indexOf("+");
				for(int k=0;k<binaryString.length();k++){
					
					if(k+1<binaryString.length() && (binaryString.charAt(k) == ('+')||binaryString.charAt(k) == '%') && !((binaryString.charAt(k-1) == '+')||binaryString.charAt(k+1) == '='))		
					{
						System.out.print(" " + binaryString.charAt(k)+" ");
					}
					
					else{ 
					System.out.print(binaryString.charAt(k));
					}
				}
		}
				
	}
	//Method that performs actions if the current character is {
	private void actionOnBrackets() {
//		if(!arry[index].trim().contains("\t")){
		 if(arry[index].equals("{")){
				if(!arry[index-1].equals("")){
					  if(arry[index-1].charAt(arry[index-1].length()-1) != ']')
					  {
						 System.out.println();
						 charCount =0;
						 System.out.print(printSpaces(countSpace) + arry[index]);
						 countSpace++;
						 
						 if(!arry[index-1].equals("{") ||arry[index+1].equals("}")){
							 isAction = true;
							 action = "newline";
						 }
					  }else{
							 arryBracket = true;
							 System.out.print(arry[index]);
					  }
				}else{
 					 charCount =0;
 					 
					 System.out.print(printSpaces(countSpace) + arry[index]);
					 countSpace++;
//					 isAction = true;
//					 action = "newline";
				}
		   }else if(arry[index].equals("}")){//action when current char is }
			 if(arryBracket == false){
				 countSpace--;
				 System.out.print(printSpaces(countSpace));
				 System.out.print(arry[index].trim());
				 isAction = true;
				 action = "newline";
				
			 }else{
				 System.out.print(arry[index].trim());
				 arryBracket = false;
			 }
//			 if(index+1 <arry.length &&arry[index+1].equals(";")){
//	 			  System.out.print(";");
//				 isAction = true;
//				 action = "newline";
//			 }
		  }else if(arry[index].trim().equals(";")){ //action when current char is ;
		     System.out.print(";");
		     isAction = true;
			 action = "newline";
		   }else if(arry[index].equals(")"))
		   {//if there is no { for loop or if
			   int j = 1;
			   //find the first character after current one, if it is char, should give a newline
			   while(arry[index+j].equals(" ")){
				   j++;
			   }
			   if(String.valueOf(arry[index+j].charAt(0)).matches("[a-zA-Z]") && !arry[index+j].equals("{")){
				   checkCharLimit();
					 countSpace++;
					 isAction = true;
					 action ="newline";
					 isNoBracketComma =  true;
					 countNoBracketComma++;
					 previousIndentBeforeNoBracket++;
			   }else{
				   if(!arry[index].contains("+") && !arry[index].contains(",") && !arry[index].equals(";") && !arry[index].contains("%"))
					   checkCharLimit();
				   action="";
			   }
		   }
		   else if(arry[index].trim().contains("else")){
			   handleElse();
 		   }	
		   else{
			   if(!arry[index].contains("+") && !arry[index].contains(",")&& !arry[index].equals(";") && !arry[index].contains("%"))
				   checkCharLimit();
		  	   action="";
		   }
//		}
//		else{
//			System.out.print("}");
//		}
	}
	//Method that resets space
	private void resetSpace() {
		 if(isNoBracketComma && !action.equals("newline") )
		    {
		    	if(countNoBracketComma == 0 && arry[index].equals(";")){
		    		isNoBracketComma = false;
		    		countSpace = countSpace - previousIndentBeforeNoBracket;
		    		previousIndentBeforeNoBracket = 0;
		    	}
		    }
	}
	//Method that assigns a newline based on previous character 
	private void createNewLine() {
		 if(isAction && action.equals("newline")&& !arry[index].trim().equals(""))
		    {
		  		System.out.println();
		  		 charCount =0;
 		  		 
		  		if(!arry[index].trim().equals("}") && !arry[index].trim().contains("/*")){
			  		System.out.print(printSpaces(countSpace));
		  		}
		  		isAction = false;
		  		//the line attaching to loop or if statement is printing this time, so next time space should be the same as
		  		//previous statement.
		  		if(isNoBracketComma){
		  			countNoBracketComma--;
		  		}	
		  		//get rid of all the space after each word
		    }
	}
	//Method to check for quotes
	private void checkForQuotes() {
		 if(arry[index].contains("\"")){
//		     System.out.print(arry[index]);
			 caculateQuote(arry[index],'\"',"countDoubleQuote");
//			 countDoubleQuote++;
		 }else if(arry[index].contains("\'")){		    
//			 System.out.print(arry[index]);
//			 countSingleQuote++;
			 caculateQuote(arry[index],'\'',"countSingleQuote");
		 }else if(arry[index].equals("(")){
			 countDoubleParenthesis++;
		 }else if(arry[index].equals(")")){
			 countDoubleParenthesis--;
		 }
		
	}
	//calcualte how many quotes
	public void caculateQuote(String str,char quote,String count){
		for(int i = 0 ; i < str.length();i++){
			if(str.charAt(i) == quote){
				if(count.equals("countDoubleQuote"))
					countDoubleQuote++;
				else if(count.equals("countSingleQuote"))
					countSingleQuote++;
			}
		}
	}
	//Method to read input file
	private void readFile() {
		try {
			br = new BufferedReader(new FileReader("t"+testCaseNo+".txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.trim().contains("//"))
					sb += sCurrentLine.trim()+"//";
				else if(sCurrentLine.trim().contains("/*")){
					sb += sCurrentLine.trim();
					isInComment = true;
				}else if(isInComment &&!sCurrentLine.trim().contains("*/")){
					sb += sCurrentLine.trim() + "--";
				}else if(sCurrentLine.trim().contains("*/")){
					isInComment = false;
					sb += sCurrentLine.trim();
				}else if((sCurrentLine.trim().contains("++")) && (sCurrentLine.trim().contains(" "))){
					String newstr = "";
					String last ="";
					
					int index = 0;
					if(sCurrentLine.trim().contains("++"))
						index = sCurrentLine.indexOf("++");
					 last = sCurrentLine.substring(index);
 					for( index = index-1;index>=0;index--){
						char c = sCurrentLine.charAt(index);
						if( c != ' '&& c != ';'){
							newstr += String.valueOf(c); 
						}else if(c == ';') break;
					}
					sb += sCurrentLine.substring(0,index+1) + newstr + last;
				}else if(sCurrentLine.contains("<") || sCurrentLine.contains("=")  || sCurrentLine.contains(">")||sCurrentLine.contains("%") ){
					int index = 0;
					String symbol = "";
					String newstr = "";
					
					if(sCurrentLine.contains("<") ){
						index = sCurrentLine.indexOf('<');
						symbol = "<";
					}else if(sCurrentLine.contains(">")){
						index = sCurrentLine.indexOf('>');
						symbol = ">";
					}else if(sCurrentLine.contains("=")  ){
						index = sCurrentLine.indexOf('=');
						symbol = "=";
					}
					else if(sCurrentLine.contains("%")){
						index = sCurrentLine.indexOf('%');
						symbol = "%";
						
					}
					String first = sCurrentLine.substring(0,index);
					String last = sCurrentLine.substring(index+1);
					for(int i = first.length()-1;i >= 0;i--){
						if(first.charAt(i)==' '){
							first = first.substring(0,i);
						}else{
							break;
						}
					}
					for(int i = 0;i < last.length();i++){
						if(last.charAt(i)==' '){
							last = last.substring(i+1);
						}else{
							break;
						}
					}
					newstr = first + " " + symbol + " " + last;
					sb += newstr.trim();

				}
				else{
					sb += sCurrentLine.trim();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	//Method to print spaces
	public static String printSpaces(int n){
		String space = "";
		for(int i =0;i<n; i++){
			space +="   ";
		}
		return space;
	}

}
