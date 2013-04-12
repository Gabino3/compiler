package compiler;

public class TexasRanger {
	
	public static String Walker(String s, DFA d){
		String output = "";
		d.setCurrentState(d.getStartState());
		char[] inputs = s.toCharArray();
		for(char input : inputs){
			
			if(input != ' '){
				d.move(input);
				output+=input;
			}else {
				if(d.isAccept()){
					d.setCurrentState(d.getStartState());
					output+="\n";
				}else{
					System.out.println("Bad Input");
					return null;
				}
			}
			
			
		}
		
		return output;
	}

}
