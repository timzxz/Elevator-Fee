package elefee;
import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.*;

import javax.swing.JOptionPane;
public class client {
	public static int txtswift,uiswift,ele;//流水号！！电梯编号
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		int inputtype,flag=0;//if 0  :input by txt..if 1  :input by interface..if -1 :quit..
		for(;;){
			inputtype=Integer.parseInt(JOptionPane.showInputDialog(null,"ELE INPUT TYPE",
					"1:FILE  0:NORMAL!",JOptionPane.QUESTION_MESSAGE));
			if(inputtype==1){txtinput();}
			if(inputtype==0){
				if(flag==0){
					ele=Integer.parseInt(JOptionPane.showInputDialog(null,"INPUT ELE NUM!",
					"INPUT THE CODE FOR THE ELE",JOptionPane.QUESTION_MESSAGE));
					uiswift=readswift(ele);//获取流水号
					flag=1;
				}
				uiinput();
			}
			if(inputtype==-1){break;}
		}
		if(inputtype==0){writeswift(swiftupdate(ele,uiswift));}
		if(inputtype==1){writeswift(swiftupdate(ele,txtswift));}
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
		      send(Integer.parseInt(tmpline[0]),Integer.parseInt(tmpline[1]),
		    		  tmpline[2],tmpline[3],start,end,Math.abs(start-end));
		      data=br.readLine(); //接着读下一行
		}
		br.close();
		txtswift=Integer.parseInt(tmpline[1])+1;
		ele=Integer.parseInt(tmpline[0]);
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
		int uselevel=Math.abs(startlevel-endlevel);
		send(ele,uiswift,userid,usetime,startlevel,endlevel,uselevel);
		uiswift++;
	}
	public static int readswift(int elenum) throws IOException{
		int head,flag=-1,get;
		String tmp="";
		FileReader fr=new FileReader("swiftnum.txt");
		while((head=fr.read())!=-1){
			if(head=='\n'&&flag!=1){
				flag=0;
			}
			if(flag==0){
				if(head>='0'&&head<='9'){
					tmp+=(char)head;
				}
				if(head==' '){
					get=Integer.parseInt(tmp);
					tmp="";
					if(get==elenum){
						flag=1;
						continue;
					}else{
						flag=-1;
					}
				}
			}
			if(flag==1){
				if(head>='0'&&head<='9'){
					tmp+=(char)head;
				}
				if(head=='\n'){
					fr.close();
					return Integer.parseInt(tmp);
				}
			}
		}
		fr.close();
		return -1;
	}
	public static String swiftupdate(int elenum,int update) throws IOException{
		int head,get,flag=-1;
		String info="",cmp="";
		FileReader fr=new FileReader("swiftnum.txt");
		while((head=fr.read())!=-1){
			if(flag!=1){
				info+=(char)head;
			}
			if(head=='\n'&&flag!=1){
				flag=0;
			}
			if(flag==0){
				if(head>='0'&&head<='9'){
					cmp+=(char)head;
				}
				if(head==' '){
					get=Integer.parseInt(cmp);
					cmp="";
					if(get==elenum){
						flag=1;
					}else{
						flag=-1;
					}
				}
			}
			if(flag==1){
				if(head=='\n'){
					info+=String.valueOf(update);
					info+='\n';
					flag=-1;
				}
			}
		}
		fr.close();
		System.out.println(info);
		return info;
	}
	public static void writeswift(String info) throws IOException{
		File f=new File("swiftnum.txt");
		FileWriter fw=new FileWriter(f);
		fw.write(info);
		fw.close();
	}
	public static void send(int elenum,int flow,String userid,String usetime,int startlevel,
						int endlevel,int uselevel) throws SQLException,ClassNotFoundException{
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="root";
		String password="19940427open";
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql="insert into eleuse values (?,?,?,?,?,?,?,?)";
		PreparedStatement pst=link.prepareStatement(sql);
		pst.setInt(1,elenum);
		pst.setInt(2,flow);
		pst.setString(3,userid);
		pst.setString(4,usetime);
		pst.setInt(5,startlevel);
		pst.setInt(6,endlevel);
		pst.setInt(7,uselevel);
		pst.setInt(8,elenum*100000+flow);
		pst.executeUpdate();
		pst.close();
		link.close();
	}
}
