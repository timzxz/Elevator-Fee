package elefee;
import java.sql.*;
public class analysis{
	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		String[] user={"Mr.rainy","timzxz","yjc","wcwj"};
		int[] count=new int[4];
		int i=0,s=0;
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
		};
		s=count[0]+count[1]+count[2]+count[3];
		System.out.println("------Total use is "+s+"------");
		System.out.printf("Mr.rainy use "+count[0]+" levels. Rate is %.1f",(count[0]/(s*0.01)));
		System.out.println("%!");
		System.out.printf("timzxz use "+count[1]+" levels. Rate is %.1f",(count[1]/(s*0.01)));
		System.out.println("%!");
		System.out.printf("yjc use "+count[2]+" levels. Rate is %.1f",(count[2]/(s*0.01)));
		System.out.println("%!");
		System.out.printf("wcwj use "+count[3]+" levels. Rate is %.1f",(count[3]/(s*0.01)));
		System.out.println("%!");
	}

}
