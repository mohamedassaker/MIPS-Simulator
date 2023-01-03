
public class Fetch {
	
	final static int x = 0;
	public static int [] registerFile = new int [32];
	public static String Instructions[] = new String [1024];
	public static int Data[] = new int [1024];
	public static String Memory[] = new String [2048];
	public static int pc = 0;
	public static int opcode = 0; // bits 31:28
	public static int r1 = 0; // bits 27:23
	public static int r2 = 0; // bit 22:18
	public static int r3 = 0; // bits 17:13
	public static int shamt = 0; // bits 12:0
	public static int imm = 0; // bits 17:0
	public static int address = 0; // bits 27:0
	public static int clockcycles = 1;
	public static char type;
	public static int indexR1;
	public static int indexR2;
	public static int indexR3;
	public static boolean flag;
	public static int j=0;
	public static int last;
	
	public static void fetch() {
		String instruction;
		
		while (pc < Instructions.length) {
			if(Instructions[pc] != null && !Instructions[pc].equals("0")) {
				j++;
				if(j!=1) {
					flag = false;
					clockcycles=clockcycles+2;
				}
				else {
					clockcycles++;
					flag = true;
				}
				instruction = Instructions[pc];
				System.out.println("Instruction is " + instruction);
				decode(instruction);
				System.out.println("");
			}
			registerFile[0] = x;
			pc++;
		}
		
		for(int i = 0;i<1024;i++) {
			if(Instructions[i] == null) {
				Instructions[i] = "0";
			}	
			Memory[i] = Instructions[i];
			Memory[i+1024] = Data[i]+"";
		}
	}
	
	public static void decode(String instruction) {
		String[] s = instruction.split(" ");
		for(int i = 0; i<s.length;i++) {
			System.out.println(s[i]);
		}
		if(flag) {
			clockcycles++;
		}
		opcode(s[0]);
		System.out.println("opcode = "+opcode);
		execute(type,s);
	}
	
	public static void execute(char type, String[] s) {
		if(flag) {
			clockcycles=clockcycles+2;
		}
		
		String op; 
		String r_1;
		String r_2;
		String r_3;
		String sh;
		String im;
		String add;
		
		switch(type) {
		case 'R':
			indexR1 = Integer.parseInt(s[1].substring(1));
			r1 = registerFile[indexR1];
			indexR2 = Integer.parseInt(s[2].substring(1));
			r2 = registerFile[indexR2];
			if(s[3].contains("R")) {
				indexR3 = Integer.parseInt(s[3].substring(1));
				r3 = registerFile[indexR3];
			}else {
				shamt = Integer.parseInt(s[3]);
			}
			executeR(opcode,r1,r2,r3);
			
			op = toBinary(opcode,4);
			r_1 = toBinary(indexR1,5);
			r_2 = toBinary(indexR2,5);
			r_3 = toBinary(indexR3,5);
			sh = toBinary(shamt,13);
			System.out.println("Instruction in binary = "+op+" "+r_1+" "+r_2+" "+r_3+" "+sh);
			
			System.out.println("r1 = " + r1);
			System.out.println("r2 = " + r2);
			System.out.println("r3 = " + r3);break;
			
		case 'I':
			indexR1 = Integer.parseInt(s[1].substring(1));
			r1 = registerFile[indexR1];
			indexR2 = Integer.parseInt(s[2].substring(1));
			r2 = registerFile[indexR2];
			imm = Integer.parseInt(s[s.length-1]);
			
			executeI(opcode,r1,r2,imm);
			
			op = toBinary(opcode,4);
			r_1 = toBinary(indexR1,5);
			r_2 = toBinary(indexR2,5);
			im = toBinary(imm,18);
			System.out.println("Instruction in binary = "+op+" "+r_1+" "+r_2+" "+im);
			
			System.out.println("r1 = " + r1);
			System.out.println("r2 = " + r2);
			System.out.println("imm = " + imm);break;
			
		case 'J': 
			address = Integer.parseInt(s[s.length-1]);
			int temp1 = pc & 0b11110000000000000000000000000000;
			temp1 = temp1 >> 28;
			pc = temp1 | address;
			pc--;
			
			op = toBinary(opcode,4);
			add = toBinary(address,28);
			System.out.println("Instruction in binary = "+op+" "+add);
			
			System.out.println("address = " + address);break;
		default:break; 
		}
	}
	
	public static void executeI(int opcode, int r1, int r2, int imm) {
		if(flag) {
			clockcycles=clockcycles+2;
		}
		switch(opcode) {
		// Multiply Immediate
		case 2: r1 = r2 * imm; 
				registerFile[indexR1] = r1;break;
		// Add Immediate
		case 3: r1 = r2 + imm; 
				registerFile[indexR1] = r1;break;
		// Branch if not equal 
		case 4: 
			if(r1 != r2) {
				pc = pc+1+imm; 
				pc--;
			}
			break;
		// And Immediate 
		case 5: r1 = r2 & imm; 
				registerFile[indexR1] = r1;break;
		// Or Immediate
		case 6: r1 = r2 | imm; 
				registerFile[indexR1] = r1;break;
		// Load Word
		case 10:
			r1 = Data[r2+imm];
			registerFile[indexR1] = r1;
			break;
		// Store Word
		case 11: 
			Data[r2+imm] = r1; 
			System.out.println("Data Memory after storing");
			for (int i = 0; i < Data.length; i++) {
				System.out.println(Data[i]);
			}
			break;
		default: break;
		}
		System.out.println("Register new value of R"+indexR1+" is " +r1);
	}
	
	public static void executeR(int opcode, int r1, int r2, int r3) {
		switch(opcode) {
		// ADD 
		case 0: r1 = r2 + r3;
				registerFile[indexR1] = r1; break;
		// Subtract
		case 1: r1 = r2 - r3; 
				registerFile[indexR1] = r1;break;
		// SLL
		case 8: 
			r1 = r2 << shamt;
			registerFile[indexR1] = r1;break;
		// SRL
		case 9: 
			r1 = r2 >>> shamt;
			registerFile[indexR1] = r1;break;
		default: break;
		}
		System.out.println("Register new value of R"+indexR1+" is " +r1);
	}
	
	public static void opcode(String s) {
		switch(s) {
		case "ADD":opcode = 0;
				type = 'R';break;
		case "SUB":opcode = 1;
				type = 'R';break;
		case "MULI":opcode = 2;
				type = 'I';break;
		case "ADDI":opcode = 3;
				type = 'I';break;
		case "BNE":opcode = 4;
				type = 'I';break;
		case "ANDI":opcode = 5;
				type = 'I';break;
		case "ORI":opcode = 6;
				type = 'I';break;
		case "J":opcode = 7;
				type = 'J';break;
		case "SLL":opcode = 8;
				type = 'R';break;
		case "SRL":opcode = 9;
				type = 'R';break;
		case "LW":opcode = 10;
				type = 'I';break;
		case "SW":opcode = 11;
				type = 'I';break;
		default: break;
		}
	}
	
	public static String toBinary(int x, int len)
    {
        if (len > 0)
        {
            return String.format("%" + len + "s",
                            Integer.toBinaryString(x)).replaceAll(" ", "0");
        }
 
        return null;
    }
	
	
}
