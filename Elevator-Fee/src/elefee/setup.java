package elefee;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;
public class setup {
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		//需要设置电梯数量和设置用户信息
		int inputtype;
		inputtype=Integer.parseInt(JOptionPane.showInputDialog(null,"1:Set Ele  0:Set User",
				"INPUT TYPE!",JOptionPane.QUESTION_MESSAGE));
		if(inputtype==1){setele();}
		if(inputtype==0){setuser();}
		if(inputtype==-1){return;}
	}
	public static void setele() throws IOException{
		int elenum,i;
		String tmpline="";
		elenum=Integer.parseInt(JOptionPane.showInputDialog(null,"INPUT ELENUM",
				"INPUT THE ELE NUMBER OF THE SYSTEM",JOptionPane.QUESTION_MESSAGE));
		File f=new File("swiftnum.txt");
		FileWriter fw=new FileWriter(f);
		fw.write(String.valueOf(elenum));
		fw.write('\n');
		for(i=1;i<=elenum;i++){
			tmpline+=String.valueOf(i);
			tmpline+=" 0";
			if(i!=elenum){
				tmpline+="\n";
			}
			fw.write(tmpline);
			tmpline="";
		}
		fw.close();
	}
	public static void setuser() throws SQLException,ClassNotFoundException{
		int type;
		String name,idnum;
		type=Integer.parseInt(JOptionPane.showInputDialog(null,"1:ADD  0:CLEAN  -1:DELETE",
				"SELECT FUNC",JOptionPane.QUESTION_MESSAGE));
		String url="jdbc:mysql://localhost:3306/eledb";
		String account="";
		String password="";
		Connection link;
		Class.forName("com.mysql.jdbc.Driver");
		link=DriverManager.getConnection(url,account,password);
		String sql;
		if(type==1){
			name=JOptionPane.showInputDialog(null,"NEW USERID",
					"ADDing USER(1)",JOptionPane.QUESTION_MESSAGE);
			idnum=JOptionPane.showInputDialog(null,"NEW USER IDNUM",
					"ADDing USER(2)",JOptionPane.QUESTION_MESSAGE);
			sql="insert into eleuser values (?,?)";
			PreparedStatement pst=link.prepareStatement(sql);
			pst.setInt(1,Integer.parseInt(idnum));
			pst.setString(2,name);
			pst.executeUpdate();
			pst.close();
			link.close();
		}
		if(type==-1){
			name=JOptionPane.showInputDialog(null,"DEL USERID",
					"DELETEing USER",JOptionPane.QUESTION_MESSAGE);
			sql="delete from eleuser where userid=?";
			PreparedStatement pst=link.prepareStatement(sql);
			pst.setString(1,name);
			pst.executeUpdate();
			pst.close();
			link.close();
		}
		if(type==0){
			sql="delete from eleuser";
			PreparedStatement pst=link.prepareStatement(sql);
			pst.executeUpdate();
			pst.close();
			link.close();
		}
		if(type!=1&&type!=0&&type!=-1){
			link.close();
		}
	}
}
