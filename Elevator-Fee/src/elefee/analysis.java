package elefee;
import java.sql.*;
import java.util.Arrays;
public class analysis{
	public static String[] user=new String[50];
	public static int usernum;
	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		int[] count=new int[50];
		int[] tmp=new int[50];
		int[] rank=new int[50];
		int i=0,s=0,j=0;
		for(i=0;i<50;i++)
			count[i]=0;
		usernum=getuserinfo();
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="root";
		String password="19940427open";
		ResultSet result;
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql="select * from eleuse;";
		PreparedStatement pst=link.prepareStatement(sql);
		result=pst.executeQuery();
		while(result.next()){
			for(i=0;i<usernum;i++){
				if(user[i].equals(result.getString(3))){
					count[i]+=result.getInt(7);
				}
			}
		}
		link.close();
		for(i=0;i<usernum;i++){
			s+=count[i];
			tmp[i]=-count[i];
		}
		Arrays.sort(tmp);
		for(i=0;i<usernum;i++)
			for(j=0;j<usernum;j++)
				if(count[i]==(-tmp[j]))
					rank[i]=j+1;
		System.out.println("---------Total use is "+s+"---------\n");
		for(i=0;i<usernum;i++){
			System.out.printf("%8s use "+count[i]+" levels. Rate is %.2f",user[i],(count[i]/(s*0.01)));
			System.out.println("%!\n         use rank is "+rank[i]+" of "+usernum+" !\n");
		}
	}
	public static int getuserinfo() throws SQLException,ClassNotFoundException{
		int ret=0;
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="";
		String password="";
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
