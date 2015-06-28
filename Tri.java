
//Project 1
//project implement lexical analyzer

//switches array A-Z then a-z
public class Tri {
	private int[] switches;
	private char[] symbol;
	private int[] next;
	private int symbolIndex;
	private final int SIZE;
	private final int SIZESWITCHES;
	private char endchar;
	
	public Tri(){
		endchar='*';
		symbolIndex =0;
		SIZE = 2000;
		SIZESWITCHES=52;
		switches = new int[SIZESWITCHES];
		symbol = new char[SIZE];
		next = new int[SIZE];
		
		//initialize switches array with -1
		for(int i=0; i<52; i++){
			switches[i]=-1;
		}
		//initialize next array to -1
		for(int i=0; i<next.length;i++)
			next[i] = -1;
	}
	
	//method to accept string
	public void addString(String string){
		int inSwitchOrNot = checkSwitch(string);//check if first letter position been used
		if(inSwitchOrNot != -1)
			inputString(string, inSwitchOrNot);
	}
	//overload the addString method to end the string with pass char value
	public void addString(String string, char end){
		endchar = end;
		int inSwitchOrNot = checkSwitch(string);//check if first letter position been used
		if(inSwitchOrNot != -1)
			inputString(string, inSwitchOrNot);
		
		endchar ='*'; //change it back to default
	}
	
	
	//find where it need to be input-------------------------------------------------------------------------
	public void inputString(String string, int pos){
		int i =1;
		boolean exitInput = false;
		while(i<string.length() && pos<symbolIndex && exitInput!=true){
			if(string.charAt(i) == symbol[pos]){//check each letter to see if it match
				if(i+1 == string.length()){//if gone thru whole string then found match, find place for ending symbol
					if(next[pos] != -1){
						while(next[pos] != -1){
							pos=next[pos];
						}
						
						
					}
					symbol[symbolIndex] = endchar;
					next[pos] = symbolIndex;
					symbolIndex++;
					exitInput = true;
				}
				i++;
				pos++;
				//if(pos == symbolIndex)
			}
			
			else//find letter that didn't match
				if(next[pos] == -1){
					next[pos] = symbolIndex;
					fillSymbolTail(string.substring(i, string.length()));
					exitInput = true;
				}
				else{
					pos = next[pos];
				}
		}
	}
	//check if letter been used in switches array------------------------------------------------------------
	public int checkSwitch(String a){
		int pos;
		if(Character.isUpperCase(a.charAt(0))){
			pos = (int)a.charAt(0) -65;
		}
		else
			pos= (int)a.charAt(0) - 71;
		
		if(switches[pos] == -1){//return -1 if yes otherwise return the value
			switches[pos] = symbolIndex;
			fillSymbolTail(a.substring(1,a.length()));
			return -1;
		}
		else
			return switches[pos];
		
	}
	
	//if the letter has not been used fill the end of symbol array-----------------------------------------------
	public void fillSymbolTail(String string){
		for(int i=0; i<string.length(); i++){//add all char of string
			symbol[symbolIndex+i] = string.charAt(i);			
		}
		symbolIndex += string.length();
		symbol[symbolIndex] = endchar;
		symbolIndex++;
	}
	
	//print symbol array
	public  void printSymbolAndNext(){
	int k=0;
	char blank=' ';
		while(k<symbolIndex){
			int m=0;
			System.out.println();
			System.out.println();
			System.out.print("       ");
			for(int i=k;i<20+k && i<symbolIndex;i++)
				System.out.printf("%5d",i);
			System.out.println();
			System.out.print("symbol:");
			for(int i=k; i<k+20 && i<symbolIndex; i++){
				System.out.printf("%5c",symbol[i]);
			}
			System.out.println();
			System.out.print("next:  ");
			for(int i=k; i<k+20 && i<symbolIndex;i++){
				if(next[i] == -1)
					System.out.printf("%5c", blank);
				else
					System.out.printf("%5d",next[i]);
				m++;
			}
			k+=m;
		}
	}
	
	//print switch array
	public void printSwitch(){
		char[] letter = {'A', 'B', 'C', 'D', 'E','F','G','H','I','J','K',
				'L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
				'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q',
				'r','s','t','u','v','w','x','y','z'};
		
			char space =' ';
			System.out.printf("%7c",space);
			for(int j=0; j<20; j++)
				System.out.printf("%5c",letter[j]);
			
			System.out.println();
			System.out.print("switch:");
			for(int j=0;j<20; j++)
				System.out.printf("%5d",switches[j]);
			System.out.println();
			System.out.println();
			System.out.printf("%7c",space);
			for(int j=0; j<20; j++)
				System.out.printf("%5c",letter[j+20]);
			
			System.out.println();
			
			System.out.print("switch:");
			for(int j=0;j<20; j++)
				System.out.printf("%5d",switches[j+20]);
			//System.out.print(switches[i] + " ");
			System.out.println();
			System.out.println();
			System.out.printf("%7c",space);
			for(int j=40; j<52; j++)
				System.out.printf("%5c",letter[j]);
			
			System.out.println();
			System.out.print("switch:");
			for(int j=40;j<52; j++)
				System.out.printf("%5d",switches[j]);
			//System.out.print(switches[i] + " ");
			
						
		
	}
	
}
