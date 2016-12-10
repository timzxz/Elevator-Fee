package elefee;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
public class pair {
	public static int swift;
	public static String[] rctime=new String[90000];
	public static double[] rcheight=new double[90000];
	public static int rclength=0;
	public static String[] dbtime=new String[20000];
	public static String[] dbuser=new String[20000];
	public static int dblength=0;
	public static String s_buf[]=new String[100];
	public static int i_buf[][]=new int[100][3];//i_buf[][0]为标志位，i_buf[][1]为入层，i_buf[][2]为出层
	public static Random r=new Random(100);
	public static double[][] leveldata=new double[100][2];//leveldata[][0]为下界，leveldata[][1]为下界
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		setlevel();
		swift=readswift(9);//获取流水号
		dblength=getdbinfo();
		rclength=getrcinfo();
		match();
		writeswift(swiftupdate(9,swift));//默认在9号电梯进行。
	}
	public static void setlevel(){
			leveldata[1][0]=-6.4;leveldata[1][1]=-2.4;
			leveldata[2][0]=-1.4;leveldata[2][1]=1.4;
			leveldata[3][0]=3.6;leveldata[1][1]=6.3;
			leveldata[4][0]=8.1;leveldata[4][1]=10.8;
			leveldata[5][0]=12.2;leveldata[5][1]=14.8;
			leveldata[6][0]=16.2;leveldata[6][1]=18.6;
			leveldata[7][0]=20.6;leveldata[17][1]=22.8;
			leveldata[8][0]=24.7;leveldata[8][1]=26.7;
	}
	public static int getdbinfo() throws SQLException,ClassNotFoundException{//获取临时使用信息
		int i=0;
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="";
		String password="";
		ResultSet result;
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql="select * from tmpuse;";
		PreparedStatement pst=link.prepareStatement(sql);
		result=pst.executeQuery();
		while(result.next()){
			dbuser[i]=result.getString(2);
			//System.out.println(dbuser[i]);
			dbtime[i]=result.getString(3);
			//System.out.println(dbtime[i]);
			i++;
		}
		link.close();
		//System.out.println(i);
		return i;
	}
	public static int getrcinfo() throws IOException{//获取记录的高度信息
		int head,i=0,flag=0;
		String tmp="";
		FileReader fr=new FileReader("output.txt");
		while((head=fr.read())!=-1){
			if(head!=' '&&flag==0){
				tmp+=(char)head;
			}
			if(head==' '){
				rctime[i]=tmp;
				tmp="";
				flag=1;
				//System.out.println(rctime[i]);
			}
			if(flag==1&&((head>='0'&&head<='9')||head=='.'||head=='-')){
				tmp+=(char)head;
			}
			if(head=='\n'){
				if(!tmp.equals("")){
					rcheight[i]=Double.valueOf(tmp);
				}
				tmp="";
				flag=0;
				//System.out.println(rcheight[i]);
				i++;
			}
		}
		fr.close();
		//System.out.println(i);
		return i;
	}
	public static int getlevel(double height){
		for(int i=1;i<=9;i++){
			if(height>=leveldata[i][0]&&height<=leveldata[i][1]){
				return i;
			}
		}
		return 1;
	}
	public static void match()throws IOException,SQLException,ClassNotFoundException{
		int i,j,bufnum;
		for(i=0;i<dblength;i++){
			for(j=0;j<rclength;j++){
				if(dbtime[i].equals(rctime[j])){
					//System.out.println(dbtime[i]);
					bufnum=updatebuf(dbuser[i],getlevel(rcheight[j]));
					if(bufnum!=-1){
						System.out.println("-->"+dbuser[i]+" SEND");
						send(9,swift,dbuser[i],rctime[j],i_buf[bufnum][1],i_buf[bufnum][2]);
						swift++;
					}
				}
			}
		}
	}
	public static void send(int elenum,int flow,String userid,String usetime,int startlevel,
			int endlevel) throws SQLException,ClassNotFoundException{
		int uselevel=Math.abs(startlevel-endlevel);
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="";
		String password="";
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
	public static String check(String str) throws SQLException,ClassNotFoundException{
		String ret="";
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="";
		String password="";
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql="select * from eleuser where cardnum=?";
		PreparedStatement pst=link.prepareStatement(sql);
		pst.setString(1,str);
		ResultSet rst=pst.executeQuery();
		while(rst.next()){
			ret=rst.getString(2);
		}
		return ret;
	}
	public static int readswift(int elenum) throws IOException{
		int get,head,flag=-1;
		for(head=0;head<100;head++){
			s_buf[head]="";
			i_buf[head][0]=0;
			i_buf[head][1]=0;
			i_buf[head][2]=0;
		}
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
		return info;
	}
	public static void writeswift(String info) throws IOException{
		File f=new File("swiftnum.txt");
		FileWriter fw=new FileWriter(f);
		fw.write(info);
		fw.close();
	}
	public static int updatebuf(String str,int level) throws IOException{
		int i;
		for(i=0;i<100;i++){
			if(i_buf[i][0]==1&&s_buf[i].equals(str)){
				System.out.println(str+" OUT !");
				i_buf[i][2]=level;
				i_buf[i][0]=0;
				return i;
			}
		}
		for(i=0;i<100;i++){
			if(i_buf[i][0]==0){
				System.out.println(str+" IN !");
				s_buf[i]=str;
				i_buf[i][1]=level;
				i_buf[i][0]=1;
				return -1;
			}
		}
		return -1;
	}
}
