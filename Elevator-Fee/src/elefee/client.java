package elefee;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
import java.text.*;
import javax.swing.JOptionPane;
public class client {
	public static int swift;//流水号！！
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		swift=readswift();//获取流水号
		int inputtype;//if 0  :input by txt..if 1  :input by interface..if -1 :quit..
		for(;;){
			inputtype=Integer.parseInt(JOptionPane.showInputDialog(null,"INPUT TYPE!",
					"ELE INPUT TYPE",JOptionPane.QUESTION_MESSAGE));
			if(inputtype==1){txtinput();}
			if(inputtype==0){uiinput();}
			if(inputtype==-1){break;}
		}
		swiftupdate(swift);//更新流水号
		return;
	}
	public static void txtinput() throws IOException,SQLException,ClassNotFoundException{
		String[] tmpline=new String[7];
		int i,counter,tmpstart,tmpend,start,end;
		BufferedReader br=new BufferedReader(new FileReader("input.txt"));
		String data=br.readLine();//一次读入一行，直到0读入null为文件结束  
		while(data!=null){
		      tmpstart=0;
		      tmpend=0;
		      counter=0;
		      for(i=0;i<data.length();i++){
		    	  if(data.charAt(i)!=' '){
		    		  tmpend++;
		    	  }else{
		    		  tmpline[counter]=data.substring(tmpstart, tmpend);
		    		  //System.out.println(tmpline[counter]);
		    		  tmpend++;
		    		  tmpstart=tmpend;
		    		  counter++;
		    	  }
		      }
		      for(i=data.length()-1;data.charAt(i)!=' ';i--){
		    	  tmpstart=i;
		      }
		      tmpline[counter]=data.substring(tmpstart, tmpend);
		      //System.out.println(tmpline[counter]);
		      start=Integer.parseInt(tmpline[4]);
		      end=Integer.parseInt(tmpline[5]);
		      use bytxt=new use(Integer.parseInt(tmpline[0]),Integer.parseInt(tmpline[1]),
		    		  tmpline[2],tmpline[3],start,end,Math.abs(start-end));
		      bytxt.send();
		      data=br.readLine(); //接着读下一行
		}
		br.close();
		swift=Integer.parseInt(tmpline[1])+1;
	}
	public static void uiinput()throws SQLException,ClassNotFoundException{
		String tmp;
		int startlevel=0;
		int endlevel=0;
		String userid="";
		String usetime="";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
		usetime=df.format(new Date());
		userid=JOptionPane.showInputDialog(null, "please input the userid", "ELE INPUT:1 OF 3",JOptionPane.QUESTION_MESSAGE);
		tmp=JOptionPane.showInputDialog(null, "please input the start level", "ELE INPUT:2 OF 3",JOptionPane.QUESTION_MESSAGE);
		startlevel=Integer.parseInt(tmp);
		tmp=JOptionPane.showInputDialog(null, "please input the end level", "ELE INPUT 3 OF 3",JOptionPane.QUESTION_MESSAGE);
		endlevel=Integer.parseInt(tmp);
		int elenum=1;
		int uselevel=Math.abs(startlevel-endlevel);
		use byui=new use(elenum,swift,userid,usetime,startlevel,endlevel,uselevel);
		byui.send();
		swift++;
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
