package elefee;
import java.sql.*;
public class use {
	private int elenum=1;
	private int flow;
	private String userid;
	private String usetime;//"usetime" is the moment the user walk out of the elevator
	private int startlevel;
	private int endlevel;
	private int uselevel;
	use(int elenum,int flow,String userid,String usetime,int startlevel,int endlevel,int uselevel){
		this.flow=flow;
		this.userid=userid;
		this.usetime=usetime;
		this.startlevel=startlevel;
		this.endlevel=endlevel;
		this.uselevel=uselevel;
	}
	int getelenum(){return this.elenum;}
	int getflow(){return this.flow;}
	String getuserid(){return this.userid;}
	String getusetime(){return this.usetime;}
	int getstart(){return this.startlevel;}
	int getend(){return this.endlevel;}
	int getuselevel(){return this.uselevel;}
	public void send() throws SQLException,ClassNotFoundException{
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="root";
		String password="19940427open";
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql="insert into eleuse values (?,?,?,?,?,?,?)";
		PreparedStatement pst=link.prepareStatement(sql);
		pst.setInt(1,this.elenum);
		pst.setInt(2,this.flow);
		pst.setString(3,this.userid);
		pst.setString(4,this.usetime);
		pst.setInt(5,this.startlevel);
		pst.setInt(6,this.endlevel);
		pst.setInt(7,this.uselevel);
		pst.executeUpdate();
		System.out.println("send to database! swift:"+this.flow+"\n");
	}
}
