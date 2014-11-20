package elefee;
import javax.swing.JOptionPane;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
public class makeinput {
	public static int swift;
	public static int usernum;
	public static String[] user=new String[1000];
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		usernum=getuserinfo();
		int ele=Integer.parseInt(JOptionPane.showInputDialog(null,"INPUT THE CODE FOR THE ELE",
				"INPUT ELE NUM!",JOptionPane.QUESTION_MESSAGE));
		swift=readswift(ele);//获取流水号
		int line=Integer.parseInt(JOptionPane.showInputDialog(null,"ELE make LINE",
				"NOW LINE",JOptionPane.QUESTION_MESSAGE));
		int i,s,e;
		Random r=new Random(100);
		String tmpline="";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
		File f=new File("input.txt");
		FileWriter fw=new FileWriter(f);
		for(i=1;i<=line;i++){
			tmpline+=String.valueOf(ele);
			tmpline+=" ";
			tmpline+=String.valueOf(swift);
			swift++;
			tmpline+=" ";
			tmpline+=user[(Math.abs(r.nextInt())%usernum)];
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
		writeswift(swiftupdate(ele,swift));
		
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
	public static int getuserinfo() throws SQLException,ClassNotFoundException{
		int ret=0;
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="root";
		String password="19940427open";
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql;
		sql="select * from eleuser";
		PreparedStatement pst=link.prepareStatement(sql);
		ResultSet rs=pst.executeQuery();
		while(rs.next()){
			user[ret]=rs.getString(2);
			ret++;
		}
		return ret;
	}
}
