package elefee;
import java.sql.*;
import java.util.Arrays;
public class analysis{
	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		String[] user={"Mr.rainy","timzxz","yjc","wcwj"};
		int[] count=new int[4];
		int[] tmp=new int[4];
		int[] rank=new int[4];
		int i=0,s=0,j=0;
		for(i=0;i<4;i++)
			count[i]=0;
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
			for(i=0;i<4;i++){
				if(user[i].equals(result.getString(3))){
					count[i]+=result.getInt(7);
				}
			}
		}
		link.close();
		s=count[0]+count[1]+count[2]+count[3];
		for(i=0;i<4;i++)
			tmp[i]=count[i];
		Arrays.sort(tmp);
		for(i=0;i<4;i++)
			for(j=0;j<4;j++)
				if(count[i]==tmp[j])
					rank[i]=4-j;
		System.out.println("---------Total use is "+s+"---------\n");
		for(i=0;i<4;i++){
			System.out.printf("%8s use "+count[i]+" levels. Rate is %.2f",user[i],(count[i]/(s*0.01)));
			System.out.println("%!\n---------use rank is "+rank[i]+" of 4 !\n");
		}
		
	}
}