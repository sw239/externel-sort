//Shizhen Wang 1240171
import java.io.*;
import java.util.*; 
import java.nio.file.Files;
public class PolyMerge{
    static boolean check = false;
    static FileWriter fw;
    static BufferedWriter bw;
    static File output = null;
    static BufferedReader br;
    //To store the Fibonacci calculation result
    public static int[] fibo;
    //used to track and calculate the good distribution
    public static int[] count;
    //heap used to compare and find the min
    static List<String> heap = new ArrayList<String>();
    static File [] processFiles = null;
    //link to the runs folder
    static File proccessFolder = new File("ProcessRuns");   
    static int x;
    static int c;
    //used to count up times a file is opened
    static int total = 0;
    public static void main(String[] args){
        
        fibo = new int[Integer.parseInt(args[0])];
        count = new int[Integer.parseInt(args[0])];
        c = Integer.parseInt(args[0]);
	x = Integer.parseInt(args[1]);
        //open the Result file from MakeRuns
        File file = new File("MakeRunsOutput.txt");
        if (!file.exists()) {
            System.out.println("Please run MakeRuns first.");
            System.exit(1);
        }
        //store the read line
        String line = null;
        try{
            br = new BufferedReader(new FileReader(file));
            Fibonacci(x);
	    // if the folder not exist
            if (!proccessFolder.exists()){
                proccessFolder.mkdir();
            }
                for (int i = 0;i<fibo.length;i++) {
                int cnt = 0;
                while(cnt<fibo[i]){
                    line = br.readLine();
                    if(line==null){
                        cnt++;                      
                        writeFile("ProcessRuns/"+i,"   ");
			            if (cnt<fibo[i]) {
				            writeFile("ProcessRuns/"+i,"");
                        }                        
                    }
                    else if(line.equals("")){
                        cnt++;
                        if(cnt < fibo[i]){
                        File f = new File("ProcessRuns/"+i);
			            if (f.exists()) {
				            writeFile("ProcessRuns/"+i,"");
                        } 
                        }
                    }else{
                         writeFile("ProcessRuns/"+i,line);      
                    }
                } 
            }
            br.close();
            check = true;
            Merge();
            bw.close();
            fw.close();
            File dest = new File("FinallySorted");
            Files.copy(output.toPath(), dest.toPath());
            output.delete();
            proccessFolder.delete();
            System.err.println(total + " times any file is opened for output");
            
        }catch(IOException e){
            System.out.println("open or access file error!");
            System.exit(1);
        }

        
    }
    
    // merge all the file together in alphabet order
    public static void Merge()throws IOException{
        List<File> emptyFile = new ArrayList<File>();
        File file = new File("ProcessRuns/"+c);
		if (!file.exists()) {
			file.createNewFile();
        }
        //keeping running, until only one file left
        while((processFiles = proccessFolder.listFiles()).length>1){
            int index = 0;
	    //the biggest ascii to compare
            String line = "~";
            if((index=Findmin())>=0&&(line=getFirstline(processFiles[index]))!=null&&!line.equals("")){
                heap.set(index,line);       
            }
            //if the file ends
            if(line == null){
                emptyFile.add(processFiles[index]);
            }
            if(index == -1){
                //if no file runs out
                if(emptyFile.isEmpty()){
             
                    File f = new File("ProcessRuns/"+c);
                    if (f.length()>0){
                        writeFile("ProcessRuns/"+c,"");
                    }
                    for(int i = 0; i < processFiles.length; i++){
                        if(processFiles[i].getName().equals(c+"")){
                            heap.add(i,null);
                        }else{                           
                            heap.add(i,getFirstline(processFiles[i]));
                        }
                    }
                }else{
                    
                    for(int i = 0; i < emptyFile.size(); i++){
                        emptyFile.get(i).delete();
                    }
                    
                    emptyFile.clear();
                    c++;
                    if((processFiles = proccessFolder.listFiles()).length>1){
                        File f = new File("ProcessRuns/"+c);
		                if (!f.exists()) {
			                f.createNewFile();
                        }
                    }
                }
            }
        }
        
    }
    //this method is used to find the min data 
    public static int Findmin(){
        int minIndex = -1;
        String MinString = "~";
        //go through the heap list
        for(int i = 0; i < heap.size(); i++){
            //if the item is null
            if(heap.get(i) == null){
		    // skip next if
                    continue;
            }
            //campare and record the min
            if(heap.get(i).compareTo(MinString) <= 0){
                minIndex = i;
                MinString = heap.get(i);
            }
        }
        //if find the min
        if(minIndex != -1){
            if(!MinString.replace(" ", "").equals("")){
                writeFile("ProcessRuns/"+c,MinString);
            }
            heap.set(minIndex,null);
        }
        return minIndex;
    }

    public static void writeFile(String fn, String s) {
        try {
            //if the output file not change 
            if(output!=null&&("ProcessRuns/"+output.getName()).equals(fn)){
                //output 
                bw.write(s + "\n");
                bw.flush(); 
            }else{
                //if changed close the old file 
			    output = new File(fn);
			    if (!output.exists()) {
				    output.createNewFile();
                }
                if(bw!=null){
			        bw.close();
                    fw.close();
                }
                //open the new file and output
		fw = new FileWriter(output, true);
                bw = new BufferedWriter(fw);
                bw.write(s + "\n");
                bw.flush();
                if(check)
                    total+=1;
            }
        } catch (IOException ioe) {
        ioe.printStackTrace();
        }
    }

    //read the frist line of the file
	public static String getFirstline(File fileName) throws IOException { 
		String fl = null;
		br = new BufferedReader(new FileReader(fileName));
		if ((fl = br.readLine()) != null) {   
			br.close();
			removeFirstLine(fileName); 
			return fl;
		}
		br.close();
		return fl;   
	}
    
    // remove the first line of a file
	public static void removeFirstLine(File fileName) throws IOException {
		RandomAccessFile rfile = new RandomAccessFile(fileName, "rw");
		long po = rfile.getFilePointer();
		rfile.readLine();
		long ro = rfile.getFilePointer();
		byte[] buff = new byte[2048];
		int n;
		while (-1 != (x = rfile.read(buff))) {
			rfile.seek(po);
			rfile.write(buff, 0, x);
			ro += x;
			po += x;
			rfile.seek(ro);
		}
		rfile.setLength(po);
		rfile.close();
	} 
	//set runs in fibonacci order into the files
    public static void Fibonacci(int fileNO){
        fibo[fibo.length-1]=1;
        int total = 0;
        int index = 0;
        while(total < fileNO){
            total = 0;
            for(int i=0; i<fibo.length; i++){
                total += fibo[i];
            }
            if(total < fileNO){
                fibo[index] = total;
            }
            index++;
            if(index>=fibo.length){
                index = 0;
            } 
        }
    }
}
