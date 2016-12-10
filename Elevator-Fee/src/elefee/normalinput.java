package elefee;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
public class normalinput {
	public static int swift;
	public static String s_buf[]=new String[20];
	public static int i_buf[][]=new int[20][3];//i_buf[][0]为标志位，i_buf[][1]为入层，i_buf[][2]为出层
	public static Random r=new Random(100);
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		int bufnum;
		BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
		String input="";
		String userid="";
		String usetime="";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
		swift=readswift(9);//获取流水号
		while(true){
			input=strin.readLine();
			if(input.equals("0")){
				break;
			}
			userid=check(input);
			bufnum=updatebuf(userid);
			if(bufnum!=-1){
				usetime=df.format(new Date());
				System.out.println(userid+" SEND !");
				send(9,swift,userid,usetime,i_buf[bufnum][1],i_buf[bufnum][2]);
				swift++;
			}
		}
		writeswift(swiftupdate(9,swift));//默认在9号电梯进行。
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
	public static String check(String str)throws SQLException,ClassNotFoundException{
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
		for(head=0;head<20;head++){
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
	public static int updatebuf(String str) throws IOException{
		int i;
		for(i=0;i<20;i++){
			if(i_buf[i][0]==1&&s_buf[i].equals(str)){
				System.out.println(str+" OUT !");
				i_buf[i][2]=getlevel();
				i_buf[i][0]=0;
				return i;
			}
		}
		for(i=0;i<20;i++){
			if(i_buf[i][0]==0){
				System.out.println(str+" IN !");
				s_buf[i]=str;
				i_buf[i][1]=getlevel();
				i_buf[i][0]=1;
				return -1;
			}
		}
		return -1;
	}
	public static int getlevel() throws IOException{
		//return Math.abs(r.nextInt()%80);
		System.out.print("Level:");
		BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
		String input=strin.readLine();
		return Integer.parseInt(input);
	}
}
