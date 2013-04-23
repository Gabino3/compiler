package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Driver {
	
	
	public static void main(String[] args) throws Exception {
		String location = "";
		final String INPUT_FILE = "input\\"+location+"script.txt";
		final String OUTPUT_FILE = "input\\"+location+"token_list.txt";
		final String SPEC_FILE = "input\\"+location+"token_spec.txt";
		final String GRAMMER_NAME = "EL GIGANTE";
		
		String s;
		NFA giant = null;
		ArrayList<ParseTree<String>> trees = new ArrayList<ParseTree<String>>();
		ArrayList<String> NFANames = new ArrayList<String>();
		ArrayList<NFA> nfas = new ArrayList<NFA>();
		int endPrimitives = 0;
		FileReader fr = new FileReader(SPEC_FILE);
		BufferedReader br = new BufferedReader(fr);
		
		//call recursive descent parser on each line
		int i=0;
		while ((s = br.readLine()) != null) {

			if(!s.equals("")){
				trees.add(RDP.rdparser(s.trim(), NFANames));
				NFANames.add(trees.get(trees.size()-1).getRoot().getData());
				i++;
			}else {
				if(endPrimitives == 0)
					endPrimitives = i;
			}
			
		}

		fr.close();
		//Interpret the trees made from the recursive descent parser
		for(ParseTree<String> t : trees){
			nfas.add(RDP.treeParser(t, nfas));
		}
		
		
		for (int j = endPrimitives; j < nfas.size(); j++) {
			//System.out.println(nfa);
			giant = NFA.union(GRAMMER_NAME, giant, nfas.get(j));
		}
			

		//make the dfa
		DFA gigante = NFA.toDFA(giant);
		
		//System.out.println(gigante);
		
		FileWriter fw = new FileWriter(OUTPUT_FILE);
		fr = new FileReader(INPUT_FILE);
		br = new BufferedReader(fr);
		
		
		
		while ((s = br.readLine()) != null) {

			if(!s.equals("")){
				fw.write(TexasRanger.Walker(s, gigante));
			}
			
		}
		fr.close();
		fw.close();

		
	}
	
	
	
	
	

}
