import java.io.*;

public class Main {
	public static void main(String[] args) {
		
		File file = new File("C:\\Users\\m8122\\Desktop\\test.txt");
		String[] testing = new String[1024];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Instructions to be executed:");
		
		String st;
		int i = 0;
		try {
			while ((st = br.readLine()) != null) {
				System.out.println(st);
				testing[i] = st;
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("*************");
		System.out.println("Instructions Details:");
		
		Fetch.Instructions = testing;
//		for(int i = 0;i<testing.length;i++) {
		Fetch.fetch();
//			Fetch.instType();
//		}
		
		System.out.println("*************");
		System.out.println("Memory contents:");
		
		for (int j = 0; j < Fetch.Memory.length; j++) {
			if(j==0) {
				System.out.println("Instructions Memory:");
			}
			if(j==1024) {
				System.out.println("Data Memory:");
			}
			System.out.println(Fetch.Memory[j]);
		}
		
		for (int j = 0; j < Fetch.registerFile.length; j++) {
			System.out.println("R"+j+" = "+ Fetch.registerFile[j]);
		}
		
		System.out.println("Clock Cycles = " + Fetch.clockcycles);
//		System.out.println("Number of instructions = "+Fetch.j);
	}
}
