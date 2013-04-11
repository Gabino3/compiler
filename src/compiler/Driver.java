package compiler;

import java.io.BufferedReader;
import java.io.FileReader;

public class Driver {
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		FileReader fr = new FileReader("input\\SampleSpec");
		BufferedReader br = new BufferedReader(fr);
		String s;
		PrimitiveParser primitive = new PrimitiveParser();
		while ((s = br.readLine()) != null && !s.equals("")) {

			primitive.NFAs.add(primitive.charClass(s.trim()));
			
		}
		
		//parse.NFAs.add(NFA.concat("Identifier", parse.NFAs.get(4), NFA.star("$Identifier", NFA.union("$Identifier", parse.NFAs.get(0), parse.NFAs.get(4))) ) );
		//parse.NFAs.add(NFA.concat("$identifier", parse.NFAs.get(0), parse.NFAs.get(3)));
		//parse.NFAs.add(NFA.star("$numberOrNothing", parse.NFAs.get(0)));
		//parse.NFAs.add(NFA.plus("$number", parse.NFAs.get(0)));
		
		//System.out.println(primitive.NFAs);
		
		
		while ((s = br.readLine()) != null) {

			System.out.println(s);
		}

		fr.close();

		/*
		 * 
		 * parse.charClass(); parse.regex();
		 */
	}

}
