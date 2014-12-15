package elefee;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class realinput{
	public static int id=0;
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
		String input="";
		String userid="";
		String usetime="";
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
		while(true){
			input=strin.readLine();
			if(input.equals("0")){
				break;
			}
			userid=check(input);
			usetime=df.format(new Date());
			System.out.println(userid+" SEND !");
			tmpsend(id,userid,usetime);
			id++;
		}
	}
	public static void tmpsend(int elenum,String userid,String usetime) throws SQLException,ClassNotFoundException{
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="root";
		String password="19940427open";
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql="insert into tmpuse values (?,?,?,?)";
		PreparedStatement pst=link.prepareStatement(sql);
		pst.setInt(1,id);
		pst.setString(2,userid);
		pst.setString(3,usetime);
		pst.setInt(4,elenum);
		pst.executeUpdate();
		pst.close();
		link.close();
	}
	public static String check(String str)throws SQLException,ClassNotFoundException{
		String ret="";
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="root";
		String password="19940427open";
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
}
