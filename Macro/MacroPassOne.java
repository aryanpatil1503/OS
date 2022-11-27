import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class MacroPassOne {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\Macro\\testcase1.txt"));

		FileWriter mnt = new FileWriter("mnt3.txt"); // macro name table // index name pp kp mdtp kpdtp
		FileWriter mdt = new FileWriter("mdt3.txt"); // macro definition table // opcode operands
		FileWriter kpdt = new FileWriter("kpdt3.txt"); // keyword parameter table // name default value
		FileWriter pnt = new FileWriter("pntab3.txt"); // parameter name table // name: parameters

		LinkedHashMap<String, Integer> pntab = new LinkedHashMap<>(); // map to temporarily store parameter name and
																		// number
		String line;
		String Macroname = null;
		int mdtp = 1, kpdtp = 0, paramNo = 1, pp = 0, kp = 0, flag = 0;
		while ((line = br.readLine()) != null) {

			String parts[] = line.split("\\s+");
			if (parts[0].equalsIgnoreCase("MACRO")) // if string is equal to 'macro'
			{
				// the next line contains macro name and parameters
				flag = 1; // to mark the start of macro
				line = br.readLine();
				parts = line.split("\\s+");
				Macroname = parts[0];
				if (parts.length <= 1) // if zero parameters
				{
					mnt.write(parts[0] + "\t" + pp + "\t" + kp + "\t" + mdtp + "\t" + (kp == 0 ? "--" : (kpdtp + 1))
							+ "\n");
					continue;
				}
				for (int i = 1; i < parts.length; i++) // processing of parameters
				{
					parts[i] = parts[i].replaceAll("[&,]", ""); // save parameter names
					// System.out.println(parts[i]);
					if (parts[i].contains("=")) // if keyword parameters
					{
						++kp;
						String keywordParam[] = parts[i].split("=");
						pntab.put(keywordParam[0], paramNo++);
						if (keywordParam.length == 2) // if keyword parameter contains default value
						{
							kpdt.write(keywordParam[0] + "\t" + keywordParam[1] + "\n");
						} else {
							kpdt.write(keywordParam[0] + "\t--\n");
						}
					} else // if positional parameter
					{
						pntab.put(parts[i], paramNo++);
						pp++;
					}
				}
				mnt.write(
						parts[0] + "\t" + pp + "\t" + kp + "\t" + mdtp + "\t" + (kp == 0 ? "--" : (kpdtp + 1)) + "\n");
				kpdtp = kpdtp + kp;

			} else if (parts[0].equalsIgnoreCase("MEND")) // if string value equals 'mend'
			{
				mdt.write(line + "\n");
				flag = kp = pp = 0;
				mdtp++;
				paramNo = 1;
				pnt.write(Macroname + ":\t");
				Iterator<String> itr = pntab.keySet().iterator(); // iterator to iterate map
				while (itr.hasNext()) // write parameters for the macro in the parameter name table
				{
					pnt.write(itr.next() + "\t");
				}
				pnt.write("\n");
				pntab.clear();
			} else if (flag == 1) // enter statements in mdt accordingly
			{
				for (int i = 0; i < parts.length; i++) {
					if (parts[i].contains("&")) // uses value of parameter
					{
						parts[i] = parts[i].replaceAll("[&,]", "");
						mdt.write("(P," + pntab.get(parts[i]) + ")\t"); // get index of parameter from pntab
					} else {
						mdt.write(parts[i] + "\t");
					}
				}
				mdt.write("\n");
				mdtp++;
			}

		}
		br.close();
		mdt.close();
		mnt.close();

		pnt.close();
		kpdt.close();
		System.out.println("Macro Pass1 Processing done.");
	}

}