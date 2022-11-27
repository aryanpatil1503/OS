
import java.io.*;
import java.util.*;

public class PassOne {
	int lc = 0; // Initialize location counter
	int lit_in_current_pool = 0, littab_ptr = 0, pooltab_ptr = 0, lit_ptr = 0;
	int symIndex = 0, litIndex = 0; // Variables to access tables
	// Creating required data structures
	LinkedHashMap<String, Row> SYMTAB;
	ArrayList<Row> LITTAB;
	ArrayList<Integer> POOLTAB;
	private BufferedReader br; // Created instance to read source file

	// OPCode Table is used to get the type of mnemonics and corresponding OPCodes
	public class OPCodeTable {

		HashMap<String, Integer> AD, RG, IS, DL, CC;

		public OPCodeTable() {
			AD = new HashMap<>();
			IS = new HashMap<>();
			RG = new HashMap<>();
			CC = new HashMap<>();
			DL = new HashMap<String, Integer>();
			DL.put("DC", 01);
			DL.put("DS", 02);
			IS.put("STOP", 0);
			IS.put("ADD", 1);
			IS.put("SUB", 2);
			IS.put("MULT", 3);
			IS.put("MOVER", 4);
			IS.put("MOVEM", 5);
			IS.put("COMP", 6);
			IS.put("BC", 7);
			IS.put("DIV", 8);
			IS.put("READ", 9);
			IS.put("PRINT", 10);
			AD.put("START", 1);
			AD.put("END", 2);
			AD.put("ORIGIN", 3);
			AD.put("EQU", 4);
			AD.put("LTORG", 5);
			RG.put("AREG", 1);
			RG.put("BREG", 2);
			RG.put("CREG", 3);
			RG.put("DREG", 4);
			CC.put("LT", 1);
			CC.put("LE", 2);
			CC.put("EQ", 3);
			CC.put("GT", 4);
			CC.put("GE", 5);
			CC.put("ANY", 6);
		}

		public String getType(String s) {
			s = s.toUpperCase();
			if (AD.containsKey(s))
				return "AD";
			else if (IS.containsKey(s))
				return "IS";
			else if (DL.containsKey(s))
				return "DL";
			else if (RG.containsKey(s))
				return "RG";
			else if (CC.containsKey(s))
				return "CC";
			return "";

		}

		public int getCode(String s) {
			s = s.toUpperCase();
			if (AD.containsKey(s))
				return AD.get(s);
			else if (IS.containsKey(s))
				return IS.get(s);
			else if (DL.containsKey(s))
				return DL.get(s);
			else if (RG.containsKey(s))
				return RG.get(s);
			else if (CC.containsKey(s))
				return CC.get(s);
			return -1;
		}

	}

	// Row is used to insert a complete row of symbol, address and index in tabular
	// data structures
	public class Row {
		String symbol;
		int addess, index;

		public String getSymbol() {
			return symbol;
		}

		public Row(String symbol, int addess) {
			super();
			this.symbol = symbol;
			this.addess = addess;
			index = 0;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public Row(String symbol, int addess, int index) {
			super();
			this.symbol = symbol;
			this.addess = addess;
			this.index = index;
		}

		public int getAddess() {
			return addess;
		}

		public void setAddess(int addess) {
			this.addess = addess;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	// Constructor to initialize data members
	public PassOne() {
		// Initialize data structures
		SYMTAB = new LinkedHashMap<>();
		LITTAB = new ArrayList<>();
		POOLTAB = new ArrayList<>();
		lc = 0;
		POOLTAB.add(0);
	}

	public static void main(String[] args) {
		PassOne one = new PassOne(); // Created instance of same class to call methods
		try {
			one.ReadFile();
		} catch (Exception e) {
			System.out.println("Error: " + e); // Handle exception if occurred
		}
	}

	public void ReadFile() throws Exception {
		String line, code = "";
		br = new BufferedReader(new FileReader("testcase3.txt")); // Read
																	// source
																	// file
		BufferedWriter bw = new BufferedWriter(new FileWriter("IC_3.txt")); // Create file to store intermediate code
		OPCodeTable lookup = new OPCodeTable(); // Create instance of OPCode Table
		while ((line = br.readLine()) != null) {
			String token[] = line.split("\\s+"); // Tokenize the input file using regular expression \s+
			if (!token[0].isEmpty()) // Nonempty token at first position indicates Label
			{
				if (SYMTAB.containsKey(token[0])) // Check if symbol table already has the token
					SYMTAB.put(token[0], new Row(token[0], lc, SYMTAB.get(token[0]).getIndex())); // Insert data into
																									// symbol table
				else
					SYMTAB.put(token[0], new Row(token[0], lc, ++symIndex)); // Modify data of symbol table
			}

			if (token[1].equals("LTORG")) // If token at second position is LTORG
			{
				int ptr = POOLTAB.get(pooltab_ptr);
				for (int j = ptr; j < littab_ptr; j++) {
					LITTAB.set(j, new Row(LITTAB.get(j).getSymbol(), lc++)); // Insert data into literal table
					code = "(DL,01)\t(C," + LITTAB.get(j).symbol + ")"; // Generate intermediate code line
					bw.write(code + "\n"); // Write code to intermediate code text file
				}
				pooltab_ptr++;
				lit_ptr = lit_in_current_pool;
				POOLTAB.add(littab_ptr - lit_in_current_pool + 1);
				// System.out.println(POOLTAB.get(2));
				lit_in_current_pool = 0;
			}
			if (token[1].equals("START")) // If token at second position is START
			{
				lc = modifiedLC(token[2]); // Update LC with token at third position
				code = "(AD,01)\t(C," + lc + ")"; // Generate intermediate code line
				bw.write(code + "\n"); // Write code to intermediate code text file
			} else if (token[1].equals("ORIGIN")) // If token at second position is ORIGIN
			{
				lc = modifiedLC(token[2]); // Update LC with token at third position
				if (token[2].contains("+")) {
					String splits[] = token[2].split("\\+");
					code = "(AD,03)\t(S," + SYMTAB.get(splits[0]).getIndex() + ")+" + Integer.parseInt(splits[1]);
				} else if (token[2].contains("-")) {
					String splits[] = token[2].split("\\-");
					code = "(AD,03)\t(S," + SYMTAB.get(splits[0]).getIndex() + ")-" + Integer.parseInt(splits[1]);
				}
				bw.write(code + "\n");
			}
			if (token[1].equals("EQU")) // If token at second position is EQU
			{
				int loc = modifiedLC(token[2]); // Update LC with token at third position
				if (SYMTAB.containsKey(token[0])) // Check if symbol table already has the token
					SYMTAB.put(token[0], new Row(token[0], loc, SYMTAB.get(token[0]).getIndex())); // Insert data into
																									// symbol table
				else
					SYMTAB.put(token[0], new Row(token[0], loc, ++symIndex)); // Modify data of symbol table
			}
			if (token[1].equals("DC")) // If token at second position is DC
			{
				lc++; // Increment location counter
				int constant = Integer.parseInt(token[2].replace("=", "").replace("'", "")); // Get the constant
				code = "(DL,01)\t(C," + constant + ")"; // Generate intermediate code line
				bw.write(code + "\n"); // Write code to intermediate code text file
			} else if (token[1].equals("DS")) // If token at second position is DS
			{

				int size = Integer.parseInt(token[2].replace("'", "")); // Get the storage size

				code = "(DL,02)\t(C," + size + ")"; // Generate intermediate code line
				bw.write(code + "\n"); // Write code to intermediate code text file
				lc = lc + size; // Increment LC by size
			}
			if (lookup.getType(token[1]).equals("IS")) // If type of token is IS
			{
				code = "(IS,0" + lookup.getCode(token[1]) + ")\t"; // Get the OPCode and generate first part of
																	// intermediate code line
				int j = 2;
				String code2 = ""; // To store remaining part of intermediate code line
				while (j < token.length) // As long as tokens are more than 2
				{
					token[j] = token[j].replace(",", "");
					if (lookup.getType(token[j]).equals("RG")) // If it's register
					{
						code2 += lookup.getCode(token[j]) + "\t"; // Get its OPCode and add to code2
					} else if (lookup.getType(token[j]).equals("CC")) {
						code2 += lookup.getCode(token[j]) + "\t";
					} else {
						if (token[j].contains("=") || token[j].contains("'")) {
							token[j] = token[j].replace("=", "").replace("'", "");
							LITTAB.add(new Row(token[j], -1, ++litIndex)); // Insert data to literal table
							lit_in_current_pool++;
							littab_ptr++;
							code2 += "(L," + (litIndex) + ")"; // Get its address and add the tuple to code2
						} else if (SYMTAB.containsKey(token[j])) // If symbol table already has the token
						{
							int ind = SYMTAB.get(token[j]).getIndex(); // Get address of the symbol
							code2 += "(S,0" + ind + ")"; // Add the tuple to code2
						} else {
							SYMTAB.put(token[j], new Row(token[j], -1, ++symIndex)); // Insert data to symbol table
							int ind = SYMTAB.get(token[j]).getIndex(); // Get address of the symbol
							code2 += "(S,0" + ind + ")"; // Add the tuple to code2
						}
					}
					j++;
				}
				lc++; // Increment Location Counter
				code = code + code2; // Generate final intermediate code line
				bw.write(code + "\n"); // Write code to intermediate code text file
			}

			if (token[1].equals("END")) // If token at second position is END
			{
				code = "(AD,02)"; // Generate intermediate code line
				if (token.length > 2) {
					int ind = SYMTAB.get(token[2]).getIndex(); // Get address of the symbol
					code += " (S,0" + ind + ")"; // Add the tuple to code2
				}
				bw.write(code + "\n");
				int ptr = lit_ptr;
				for (int j = ptr; j < littab_ptr; j++) {
					LITTAB.set(j, new Row(LITTAB.get(j).getSymbol(), lc++)); // Insert data to literal table
					code = "(DL,01)\t(C," + LITTAB.get(j).symbol + ")"; // Generate intermediate code line
					bw.write(code + "\n"); // Write code to intermediate code text file
				}

				pooltab_ptr++;
				if (littab_ptr != 0) {
					POOLTAB.add(littab_ptr - lit_in_current_pool + 1); // Insert data to pool table
				}
				// Write code to intermediate code text file
			}

		}
		bw.close(); // Close the file
		PrintSYMTAB(); // Print symbol table
		PrintLITTAB(); // Print literal table
		PrintPOOLTAB(); // Print pool table
	}

	void PrintLITTAB() throws IOException {
		BufferedWriter litTabFile = new BufferedWriter(new FileWriter("LITTAB_3.txt")); // Create file to store literal
																						// table
		System.out.println("**************************");
		System.out.println("LITERAL TABLE:\n");
		// Print the literal table
		System.out.println("Index\tLiteral\tAddress");
		for (int i = 0; i < LITTAB.size(); i++) {
			Row row = LITTAB.get(i);
			System.out.println((i + 1) + "\t" + row.getSymbol() + "\t" + row.getAddess());
			litTabFile.write((i + 1) + "\t" + row.getSymbol() + "\t" + row.getAddess() + "\n");
		}
		System.out.println("\n**************************");
		litTabFile.close(); // Close the file
	}

	void PrintPOOLTAB() throws IOException {
		BufferedWriter poolTabFile = new BufferedWriter(new FileWriter("POOLTAB_3.txt")); // Create file to store pool
																							// table
		System.out.println("**************************");
		System.out.println("POOL TABLE:\n");
		// Print the pool table
		System.out.println("Literal No.");
		for (int i = 1; i < POOLTAB.size(); i++) {
			System.out.println("#" + POOLTAB.get(i));
			poolTabFile.write("#" + POOLTAB.get(i) + "\n");
		}
		System.out.println("\n**************************");
		poolTabFile.close(); // Close the file
	}

	void PrintSYMTAB() throws IOException {
		BufferedWriter symTabFile = new BufferedWriter(new FileWriter("SYMTAB_3.txt")); // Create file to store symbol
																						// table
		// Print the symbol table
		java.util.Iterator<String> iterator = SYMTAB.keySet().iterator();
		System.out.println("**************************");
		System.out.println("SYMBOL TABLE:\n");
		System.out.println("Index\tSymbol\tAddress");
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			Row value = SYMTAB.get(key);

			System.out.println(value.getIndex() + "\t" + value.getSymbol() + "\t" + value.getAddess());
			symTabFile.write(value.getIndex() + "\t" + value.getSymbol() + "\t" + value.getAddess() + "\n");
		}
		System.out.println("\n**************************");
		symTabFile.close(); // Close the file
	}

	public int modifiedLC(String str) // Return final address to modify the LC
	{
		int temp = 0;
		try {
			if (str.contains("+")) // If str has +, find address of first symbol and add integer next to plus sign
									// to get final LC
			{
				String splits[] = str.split("\\+");
				// System.out.println(splits[0] + " " + splits[1]);
				temp = SYMTAB.get(splits[0]).getAddess() + (Integer.parseInt(splits[1]));
			} else if (str.contains("-")) // If str has -, find address of first symbol and subtract integer next to
											// plus sign to get final LC
			{
				String splits[] = str.split("\\-");
				// System.out.println(splits[0] + " " + splits[1]);
				temp = SYMTAB.get(splits[0]).getAddess() - (Integer.parseInt(splits[1]));
			} else {
				temp = Integer.parseInt(str); // Convert str to int
			}
		} catch (NumberFormatException e) {
			temp = SYMTAB.get(str).getAddess();
		}
		return temp;
	}
}
