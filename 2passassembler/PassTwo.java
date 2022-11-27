
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class PassTwo {

	// class table row to store a row from symbol table and literal table
	public class TableRow {
		String symbol;
		int address;
		int index;

		public TableRow(String symbol, int address) {
			super();
			this.symbol = symbol;
			this.address = address;

		}

		public TableRow(String symbol, int address, int index) {
			super();
			this.symbol = symbol;
			this.address = address;
			this.index = index;

		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public int getAddress() {
			return address;
		}

		public void setAddress(int address) {
			this.address = address;
		}

	}

	ArrayList<TableRow> SYMTAB, LITTAB;
	int lc;

	// constructor to initialize symbol table and literal table array lists (cpp
	// vectors)
	public PassTwo() {
		int lc = 0;
		SYMTAB = new ArrayList<>();
		LITTAB = new ArrayList<>();
	}

	public static void main(String[] args) {
		// create pass2 object
		PassTwo pass2 = new PassTwo();

		try {
			// pass IC.txt generated from pass1 to the function and generate machine code
			pass2.generateCode("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\2passassembler\\IC_3.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readtables() {
		BufferedReader br;
		String line;
		try {
			// Read symbol table and save each row into symtab (array list)
			br = new BufferedReader(
					new FileReader("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\2passassembler\\SYMTAB_3.txt"));
			while ((line = br.readLine()) != null) {
				String parts[] = line.split("\\s+");
				SYMTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[0])));
			}
			br.close();
			// Read literal table and save each row into littab (array list)
			br = new BufferedReader(
					new FileReader("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\2passassembler\\LITTAB_3.txt"));
			while ((line = br.readLine()) != null) {
				String parts[] = line.split("\\s+");
				LITTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[0])));
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void generateCode(String filename) throws Exception {
		readtables();
		// read IC.txt
		BufferedReader br = new BufferedReader(new FileReader(filename));

		// create output file MC (Machine Code) to store output
		BufferedWriter bw = new BufferedWriter(
				new FileWriter("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\2passassembler\\Machine_Code_3.txt"));
		String line, code;

		// read lines from IC.txt
		while ((line = br.readLine()) != null) {
			String parts[] = line.split("\\s+");
			if (parts[0].contains("AD") || parts[0].contains("DL,02")) // if line contains assembly directives or DS
																		// statement
			{ // no machine code is generated for these
				if (parts[0].contains("AD")) {
					parts[0] = parts[0].replaceAll("[^0-9]", "");
					if (Integer.parseInt(parts[0]) == 1) {
						int constant = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
						lc = constant;
					} else if (Integer.parseInt(parts[0]) == 3) {
						if (parts[1].contains("+")) {
							String splits[] = parts[1].split("\\+");
							int symIndex = Integer.parseInt(splits[0].replaceAll("[^0-9]", ""));
							int symIndexlcVal = SYMTAB.get(symIndex - 1).getAddress();
							int finalval = Integer.parseInt(splits[1]) + symIndexlcVal;
							lc = finalval;
						} else if (parts[1].contains("-")) {
							String splits[] = parts[1].split("\\-");
							int symIndex = Integer.parseInt(splits[0].replaceAll("[^0-9]", ""));
							int symIndexlcVal = SYMTAB.get(symIndex - 1).getAddress();
							int finalval = Integer.parseInt(splits[1]) - symIndexlcVal;
							lc = finalval;
						}

					}
					bw.write("\n");
					continue;
				}

				else {
					bw.write(String.format("%03d", lc) + ")\n");
					lc++;
					continue;
				}
			} else if (parts.length == 2) {
				if (parts[0].contains("DL")) // DC instruction
				{
					parts[0] = parts[0].replaceAll("[^0-9]", "");
					if (Integer.parseInt(parts[0]) == 1) {
						int constant = Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); // get constant
						code = String.format("%03d", lc) + ")\t\t" + "00\t0\t" + String.format("%03d", constant) + "\n";
						bw.write(code);
						lc++;

					}
				} else if (parts[0].contains("IS")) // imperative statement with length 2
				{
					int opcode = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));

					if (parts[1].contains("S")) {
						int symIndex = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
						code = String.format("%03d", lc) + ")\t\t" + String.format("%02d", opcode) + "\t0\t"
								+ String.format("%03d", SYMTAB.get(symIndex - 1).getAddress()) + "\n";
						bw.write(code);
						lc++;
					} else if (parts[1].contains("L")) {
						int symIndex = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
						code = String.format("%03d", lc) + ")\t\t" + String.format("%02d", opcode) + "\t0\t"
								+ String.format("%03d", LITTAB.get(symIndex - 1).getAddress()) + "\n";
						bw.write(code);
						lc++;
					}

				}
			} else if (parts.length == 1 && parts[0].contains("IS")) // IS statement with length 1 usually stop
																		// statement (IS,00)
			{
				int opcode = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
				code = String.format("%03d", lc) + ")\t\t" + String.format("%02d", opcode) + "\t0\t"
						+ String.format("%03d", 0) + "\n";
				bw.write(code);
				lc++;
			} else if (parts[0].contains("IS") && parts.length == 3) // all other IS instructions
			{
				int opcode = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));

				int regcode = Integer.parseInt(parts[1]); // opcode for register or conditional code

				if (parts[2].contains("S")) {
					int symIndex = Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
					code = String.format("%03d", lc) + ")\t\t" + String.format("%02d", opcode) + "\t" + regcode + "\t"
							+ String.format("%03d", SYMTAB.get(symIndex - 1).getAddress()) + "\n";
					bw.write(code);
					lc++;
				} else if (parts[2].contains("L")) {
					int symIndex = Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
					code = String.format("%03d", lc) + ")\t\t" + String.format("%02d", opcode) + "\t" + regcode + "\t"
							+ String.format("%03d", LITTAB.get(symIndex - 1).getAddress()) + "\n";
					bw.write(code);
					lc++;
				}
			}

		}
		bw.close();
		br.close();

	}

}