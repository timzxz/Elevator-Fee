package elefee;
import javax.swing.JOptionPane;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
public class makeinput {
	public static int swift;
	public static void main(String[] args) throws IOException{
		swift=readswift();//获取流水号
		int line=Integer.parseInt(JOptionPane.showInputDialog(null,"ELE 1 make LINE",
				"NOW LINE",JOptionPane.QUESTION_MESSAGE));
		int i,s,e;
		String[] user={"Mr.rainy","timzxz","yjc","wcwj"};
		Random r=new Random(100);
		String tmpline="";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
		File f=new File("input.txt");
		FileWriter fw=new FileWriter(f);
		for(i=1;i<=line;i++){
			tmpline+="1 ";
			tmpline+=String.valueOf(swift);
			swift++;
			tmpline+=" ";
			tmpline+=user[(Math.abs(r.nextInt())%4)];
			tmpline+=" ";
			tmpline+=df.format(new Date());
			tmpline+=" ";
			s=(Math.abs(r.nextInt())%90);
			tmpline+=String.valueOf(s);
			tmpline+=" ";
			e=(Math.abs(r.nextInt())%90);
			tmpline+=String.valueOf(e);
			tmpline+=" ";
			tmpline+=String.valueOf(Math.abs(s-e));
			if(i!=line)tmpline+='\n';
			fw.write(tmpline);
			tmpline="";
		}
		fw.close();
		swiftupdate(swift);
	}
	public static int readswift() throws IOException{
		int ret=0;
		BufferedReader br=new BufferedReader(new FileReader("swiftnum.txt"));
		String data=br.readLine();
		ret=Integer.parseInt(data);
		br.close();
		return ret;
	}
	public static void swiftupdate(int update) throws IOException{
		File f=new File("swiftnum.txt");
		FileWriter fw=new FileWriter(f);
		fw.write(String.valueOf(swift));
		fw.close();
	}
}
