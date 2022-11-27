import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Vector;

class MNTRow {
	String name;
	int pp, kp, mdtp, kpdtp;

	public MNTRow(String name, int pp, int kp, int mdtp, int kpdtp) {
		super();
		this.name = name;
		this.pp = pp;
		this.kp = kp;
		this.mdtp = mdtp;
		this.kpdtp = kpdtp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPp() {
		return pp;
	}

	public void setPp(int pp) {
		this.pp = pp;
	}

	public int getKp() {
		return kp;
	}

	public void setKp(int kp) {
		this.kp = kp;
	}

	public int getMdtp() {
		return mdtp;
	}

	public void setMdtp(int mdtp) {
		this.mdtp = mdtp;
	}

	public int getKpdtp() {
		return kpdtp;
	}

	public void setKpdtp(int kpdtp) {
		this.kpdtp = kpdtp;
	}

}

public class MacroPassTwo {

	public static void main(String[] args) throws Exception {
		BufferedReader inp = new BufferedReader(
				new FileReader("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\Macro\\inputm2.txt"));
		BufferedReader mdtb = new BufferedReader(
				new FileReader("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\Macro\\mdt3.txt"));
		BufferedReader kpdtb = new BufferedReader(
				new FileReader("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\Macro\\kpdt3.txt"));
		BufferedReader mntb = new BufferedReader(
				new FileReader("C:\\Users\\aryan\\Desktop\\TE SEM5\\LP-1\\Macro\\mnt3.txt"));

		FileWriter fr = new FileWriter("pass2_1.txt");
		FileWriter ap = new FileWriter("aptab_1.txt");

		HashMap<String, MNTRow> mnt = new HashMap<>();
		HashMap<Integer, String> aptab = new HashMap<>(); // actual parameter table- a map with index as key and
															// parameter value as value
		HashMap<String, Integer> aptabInverse = new HashMap<>(); // key is parameter value and value is index

		Vector<String> mdt = new Vector<String>();
		Vector<String> kpdt = new Vector<String>();

		int pp, kp, mdtp, kpdtp, paramNo;
		String line;
		while ((line = mdtb.readLine()) != null) // read mdt and store lines in vector
		{
			mdt.addElement(line);
		}
		while ((line = kpdtb.readLine()) != null) // read kpdt and store lines in vector
		{
			kpdt.addElement(line);
		}
		while ((line = mntb.readLine()) != null) // store data of mnt in map where key is macro name and value is
													// instance of mntentry
		{
			String parts[] = line.split("\\s+");
			if (parts[4].contains("--")) {
				mnt.put(parts[0], new MNTRow(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]), -1));
				// mntrow(macroname, pp, kp, mdtp, kpdtp)
			} else {
				mnt.put(parts[0], new MNTRow(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));

			}
		}

		while ((line = inp.readLine()) != null) // read intermediate file
		{
			String[] parts = line.split("\\s+");

			if (mnt.containsKey(parts[0])) // if it is a macro call
			{
				// ap.write(line +"\n");
				pp = mnt.get(parts[0]).getPp(); // get number of positional parameters for the macro
				kp = mnt.get(parts[0]).getKp(); // number of kp
				kpdtp = mnt.get(parts[0]).getKpdtp(); // value of kpdtp
				mdtp = mnt.get(parts[0]).getMdtp(); // value of mdtp
				paramNo = 1;
				for (int i = 0; i < pp; i++) // for pp
				{
					parts[paramNo] = parts[paramNo].replace(",", "");
					aptab.put(paramNo, parts[paramNo]); // store parameter index and value in aptab
					aptabInverse.put(parts[paramNo], paramNo);
					// ap.write(paramNo+"\t"+ parts[paramNo]+"\n");
					paramNo++;
				}
				int j = kpdtp - 1;
				if (j >= 0) {
					for (int i = 0; i < kp; i++) // for kp
					{
						String temp[] = kpdt.get(j).split("\t"); // parameter name, default value
						aptab.put(paramNo, temp[1]);
						aptabInverse.put(temp[0], paramNo);
						j++;
						paramNo++;
					}

					for (int i = pp + 1; i < parts.length; i++) // write values of kpdt either default or value provided
					{
						parts[i] = parts[i].replace(",", "");
						String splits[] = parts[i].split("=");
						String name = splits[0].replaceAll("&", "");
						aptab.put(aptabInverse.get(name), splits[1]);
						// ap.write(aptabInverse.get(name)+ '\t'+ splits[1]+'\n');
					}
				}
				int i = mdtp - 1;
				while (!mdt.get(i).equalsIgnoreCase("MEND")) {
					String splits[] = mdt.get(i).split("\\s+"); // splits mdt row
					fr.write("+");
					for (int k = 0; k < splits.length; k++) // for each part in mdt
					{
						if (splits[k].contains("(P,")) // if refers to a parameter
						{
							splits[k] = splits[k].replaceAll("[^0-9]", "");// not containing number
							String value = aptab.get(Integer.parseInt(splits[k])); // get value from aptab using index
							fr.write(value + "\t");
						} else {
							fr.write(splits[k] + "\t");
						}
					}
					fr.write("\n");
					i++;
				}

				ap.write("Macro: " + parts[0] + "\nIndex\tValue\n"); // store aptab for the macro call in the file
				for (Integer x : aptab.keySet())
					ap.write(x + "\t:\t" + aptab.get(x) + "\n");
				ap.write("\n");

				aptab.clear();
				aptabInverse.clear();
			} else {
				fr.write(line + "\n"); // if mend
			}

		}

		fr.close();
		ap.close();
		mntb.close();
		mdtb.close();
		kpdtb.close();
		inp.close();
		System.out.println("Done");
	}
}
