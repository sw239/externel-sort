//Shizhen Wang 1240171
import java.io.*;
public class MakeRuns {
//declare variables 
    public static String[] heap;
    public static String cur="0";
    public static String MINIMAX;
    public static int count=0;
    public static void main(String [] args){
	//enter values when starting the program	
	heap = new String[Integer.parseInt(args[0])];
	File f= new File(args[1]);		
	String s=null;
	try{
		//create bufferreader to readfile
		BufferedReader br = new BufferedReader(new FileReader(f));	
		//add till full the heap array	
		for(int i=0;i<heap.length;i++){
			heap[i]=br.readLine();
			if(heap[i].equals("")&&heap[i].equals(" ")){i--;}
		}
		// read file and detect empty line and space
		while((s=br.readLine())!=null){
			if(!s.equals("")&&!s.equals(" ")){
			heap[Findmin()]=s;
			}
				
		}		
		//after all data been read, change display the remaining ones in heap array
		for(int i=0;i<heap.length;i++){
			heap[Findmin()]="";	
		}
		br.close();
		System.out.println("Runs is "+count);
	}
	catch(Exception e){
		System.out.println(e.toString());
       		System.out.println(e.getMessage());
		e.printStackTrace() ;	
	}	
		
    }
//the function to find the minimum letters that is equal or larger than last one
public static int Findmin(){
	MINIMAX=null;int index=0;
	//go througth the heap array and compare each element that is not empty
	for(int i=0;i<heap.length;i++){	
		// check if it is bigger than last MINIMAX	
		if(heap[i].compareTo(cur)>=0)
		{
			if(MINIMAX==null){MINIMAX=heap[i];}

				if(MINIMAX.compareTo(heap[i])>=0)
				{
					MINIMAX=heap[i];			
					index=i;
				}				
		}
	}
	cur=MINIMAX;
	//if there is none bigger than the last MINIMAX
	if(MINIMAX==null){
		//Give a variable an compareble value
		for(int i=0;i<heap.length;i++){
			if(heap[i]!=""){
			MINIMAX=heap[i]; 
			}
		}
			Wfile("");count++;
		//go through the list to find a new minimum that starts a new run
		for(int i=0;i<heap.length;i++){
			if(heap[i]!=""){
			if(MINIMAX.compareTo(heap[i])>=0)
				{
					MINIMAX=heap[i];
					index=i;			
				}
			}
		}
		
		cur=MINIMAX;
	}
	
	Wfile(MINIMAX);
	return index;
}
// A method to write into a output file 
public static void Wfile(String x){
	BufferedWriter bw = null;
	FileWriter fw = null;
	try {
		fw = new FileWriter("MakeRunsOutput.txt",true);
		bw = new BufferedWriter(fw);
		//write and go to next line
		bw.write(x+"\n");
		bw.close();
		fw.close();
	}catch (IOException e) {
		e.printStackTrace();
	} 
}
}

