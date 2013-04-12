package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import compiler.ParseTree.Node;
import compiler.RDP.Terminal;

public class Driver {
	
	
	public static void main(String[] args) throws Exception {
		
		String s;
		NFA giant = null;
		ArrayList<ParseTree<String>> trees = new ArrayList<ParseTree<String>>();
		ArrayList<String> NFANames = new ArrayList<String>();
		ArrayList<NFA> nfas = new ArrayList<NFA>();
		
		FileReader fr = new FileReader("input\\SampleSpec");
		BufferedReader br = new BufferedReader(fr);
		
		//call recursive descent parser on each line
		while ((s = br.readLine()) != null) {

			if(!s.equals("")){
				trees.add(RDP.rdparser(s.trim(), NFANames));
				NFANames.add(trees.get(trees.size()-1).getRoot().getData());
			}
			
		}

		fr.close();
		//Interpret the trees made from the recursive descent parser
		for(ParseTree<String> t : trees){
			nfas.add(RDP.treeParser(t, nfas));
		}
		
		
		for(NFA nfa : nfas){
			//System.out.println(nfa);
			giant = NFA.union("El Gigante", giant, nfa);
		}
		
		DFA gigante = NFA.toDFA(giant);
		
		System.out.println(gigante);
		FileWriter fw = new FileWriter("input\\Output");
		fr = new FileReader("input\\SampleInput");
		br = new BufferedReader(fr);
		
		while ((s = br.readLine()) != null) {

			if(!s.equals("")){
				fw.write(TexasRanger.Walker(s, gigante));
			}
			
		}

		/*
		 * 
		 * parse.charClass(); parse.regex();
		 */
	}
	
	
	
	
	

}
