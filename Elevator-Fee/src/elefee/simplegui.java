package elefee;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
public class simplegui {
	public static void main(String[] args) throws IOException,SQLException,ClassNotFoundException{
		EventQueue.invokeLater(new Runnable(){
		public void run(){
			TextComponentFrame frame=new TextComponentFrame();
			System.out.println("get info");
			frame.setTitle("guitext");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			}
		});
	}
}
@SuppressWarnings("serial")
class TextComponentFrame extends JFrame{
	public static int swift;//流水号！！
	public String[] info=new String[3];
	public static final int TEXTAREA_ROWS=10;
	public static final int TEXTAREA_COLUMNS=30;
	public TextComponentFrame(){
		setLocation(400,250);
		final JTextField textField1=new JTextField();
		final JTextField textField2=new JTextField();
		final JTextField textField3=new JTextField();
		JPanel northPanel=new JPanel();
		northPanel.setLayout(new GridLayout(3,2));
		northPanel.add(new JLabel("User name: ",SwingConstants.RIGHT));
		northPanel.add(textField1);
		northPanel.add(new JLabel("startlevel: ",SwingConstants.RIGHT));
		northPanel.add(textField2);
		northPanel.add(new JLabel("endlevel: ",SwingConstants.RIGHT));
		northPanel.add(textField3);
		add(northPanel,BorderLayout.NORTH);
		final JTextArea textArea=new JTextArea(TEXTAREA_ROWS,TEXTAREA_COLUMNS);
		JScrollPane scrollPane=new JScrollPane(textArea);
		add(scrollPane,BorderLayout.CENTER);
		JPanel southPanel=new JPanel();
		JButton insertButton=new JButton("Finish");
		southPanel.add(insertButton);
		insertButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				textArea.append("user name: "+textField1.getText()+"\n"
						+"start level: "+textField2.getText()+"\n"+
						"end level: "+textField3.getText()+"\n");
				try{
					swift=readswift();
				}catch(IOException e1){
					e1.printStackTrace();
				}//获取流水号
				info[0]=textField1.getText();
				info[1]=textField2.getText();
				info[2]=textField3.getText();
				System.out.println("get info "+info[0]+" "+info[1]+" "+info[2]);
				int startlevel=0;
				int endlevel=0;
				String userid="";
				String usetime="";
				SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
				usetime=df.format(new Date());
				if(info[0]!=null&&info[1]!=null&&info[2]!=null){
					userid=info[0];
					startlevel=Integer.parseInt(info[1]);
					endlevel=Integer.parseInt(info[2]);
					int elenum=1;
					int uselevel=Math.abs(startlevel-endlevel);
					try{
						send(elenum,swift,userid,usetime,startlevel,endlevel,uselevel);
					}catch(ClassNotFoundException e1){
						e1.printStackTrace();
					}catch(SQLException e1){
						e1.printStackTrace();
					}
					swift++;
					try{
						swiftupdate(swift);
					}catch(IOException e){
						e.printStackTrace();
					}//更新流水号
				}
			}
		});
		add(southPanel,BorderLayout.SOUTH);
		pack();
	}
	public static int readswift() throws IOException{
		int ret=0;
		BufferedReader br=new BufferedReader(new FileReader("swiftnum.txt"));
		String data=br.readLine();
		ret=Integer.parseInt(data);
		br.close();
		return ret;
	}
	public static void swiftupdate(int update) throws IOException{
		File f=new File("swiftnum.txt");
		FileWriter fw=new FileWriter(f);
		fw.write(String.valueOf(swift));
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
		String sql="insert into eleuse values (?,?,?,?,?,?,?)";
		PreparedStatement pst=link.prepareStatement(sql);
		pst.setInt(1,elenum);
		pst.setInt(2,flow);
		pst.setString(3,userid);
		pst.setString(4,usetime);
		pst.setInt(5,startlevel);
		pst.setInt(6,endlevel);
		pst.setInt(7,uselevel);
		pst.executeUpdate();
		pst.close();
		link.close();
	}
}