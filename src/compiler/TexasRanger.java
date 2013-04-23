package compiler;


public class TexasRanger {
	
	@SuppressWarnings("serial")
	private static class walkError extends Exception {
		walkError(String message) {
			super(message);
		}
	}
	
	public static String Walker(String s, DFA dfa) throws walkError{
		StringBuffer output = new StringBuffer();
		dfa.setCurrentState(dfa.getStartState());
		//char[] inputs = s.toCharArray();
		
		//String[] inputs = s.split(" ");
		String input = s.trim();
		
		
		//for(String input : inputs){
			if(!input.equals("")) {
				
				
				
				for(int i=0;i<input.length();i++){
					if(!dfa.getAlphabet().contains(input.charAt(i))){
						throw new walkError("Invalid Input");
					}
					if(dfa.canMove(input.charAt(i))){
						dfa.move(input.charAt(i));
						output.append(input.charAt(i));
					}else {
						if (dfa.isAccept()){
							output.insert(output.lastIndexOf("\n")+1, dfa.whichRegex() + " ");
							output.append('\n');
							dfa.setCurrentState(dfa.getStartState());
							i--;
						}else if(Character.isWhitespace(input.charAt(i))){
							
						}else {
							
							throw new walkError("Invalid Input");
						}
						
					}
				}
				if (dfa.isAccept()){
					output.insert(output.lastIndexOf("\n")+1, dfa.whichRegex() + " ");
					output.append('\n');
					dfa.setCurrentState(dfa.getStartState());
				}else{
					
					throw new walkError("Invalid Input");
				}
			}
		//}
		

		

		
		return output.toString();
	}

}


/*
for(String input : inputs){
	
	
	
	if(!(input == "")){
		char[] chars = input.toCharArray();
		int ichars = 0;
		while(ichars<chars.length){
			dfa.setCurrentState(dfa.getStartState());
		stack = new ArrayList<ArrayList<Integer>>();
	
	
		for(char action : chars){
		
			stack.add(dfa.move(action));
			
				
		}
checkaccept:for(int i=stack.size()-1;i>=ichars;i--){
			if (dfa.getAcceptStates().contains(stack.get(i))){
				String a = new String(chars);
				output.append(a.substring(ichars, i+1));
				output.append("\n");
				ichars = i+1;
				break checkaccept;
			}
		}
	
		}
	
	
	
	}
	
}
*/
/*	
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
*/
