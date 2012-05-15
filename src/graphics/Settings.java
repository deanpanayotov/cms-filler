package graphics;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JOptionPane;

public class Settings extends JFrame {
	private int x=0;
	private String[][] names={
			 {
				 "User name", "Password", "Server URL", "Driver name", "Database name WP", "Database name B2E"
			 },
			 {
				"Posts", "Categories", "Tags", "Max tags per post", "Comments", "Max formattings per post" 
			 },
			 {
				 "Post min length", "Post max length", "Text snippet min length", "Text snippet max length",
				 "Formatting min length", "Formatting max length"
			 }
	 
	 };
	public Settings(int x){
		this.x=x;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(Main.cFrameBgr);
		getContentPane().setForeground(Main.cFont);
		initComponents();
		center(this);
	}

	protected void center(JFrame frame){
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension scrnsize = toolkit.getScreenSize();
		frame.setBounds((int)(scrnsize.getWidth()/2-frame.getBounds().width/2),
				(int)(scrnsize.getHeight()/2-frame.getBounds().height/2),
				frame.getWidth(), frame.getHeight());
		
	}
	
	protected void initComponents(){
	
	 JButton button[]= new JButton[10];
	 JLabel label[]=new JLabel[10];
	 
	 GroupLayout layout = new GroupLayout(getContentPane());
     getContentPane().setLayout(layout);
     layout.setAutoCreateGaps(true);
     layout.setAutoCreateContainerGaps(true);  
     
     SequentialGroup h0 = layout.createSequentialGroup();
     ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
     ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
     
     SequentialGroup v0 = layout.createSequentialGroup();
     HandlerClass handler = new HandlerClass();
	 for(int i=0;i<names[x].length;i++){
		 button[i] = new JButton();
		 button[i].setText(Integer.toString(i+1));
		 button[i].addActionListener(handler);
		 label[i]= new JLabel();
		 label[i].setText(names[x][i]);
		 label[i].setForeground(Main.cFont);
		 v0.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				 .addComponent(button[i])
				 .addComponent(label[i]));
		 h1.addComponent(button[i]);
		 h2.addComponent(label[i]);
		 
	 }
	 
	 h0.addGroup(h1);
	 h0.addGap(100);
	 h0.addGroup(h2);
	 layout.setHorizontalGroup(h0);
	 layout.setVerticalGroup(v0);
	 pack();
	 
	}
	
	private class HandlerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
		switch(x){
		case 0:{
			switch(Integer.parseInt(event.getActionCommand())){
			case 1:{
				
				Main.generator.user= changestr(Main.generator.user, 1);
				break;
			}
			case 2:{
							
				Main.generator.password= changestr(Main.generator.password, 2);
				break;
			}
			case 3:{
				
				Main.generator.url= changestr(Main.generator.url, 3);
				break;
			}
			case 4:{
				
				Main.generator.driverName= changestr(Main.generator.driverName, 4);
				break;
			}
			case 5:{
				
				Main.generator.WP_DATABASE_NAME= changestr(Main.generator.WP_DATABASE_NAME, 5);
				break;
			}
			case 6:{
				
				Main.generator.B2E_DATABASE_NAME= changestr(Main.generator.B2E_DATABASE_NAME, 6);
				break;
			}
			}
			break;
		}
		case 1:{
			
			switch(Integer.parseInt(event.getActionCommand())){
			case 1:{
				
				Main.generator.postCount= changevar(Main.generator.postCount, 1);
				break;
			}
			case 2:{
							
				Main.generator.catCount= changevar(Main.generator.catCount, 2);
				break;
			}
			case 3:{
				
				Main.generator.tagCount= changevar(Main.generator.tagCount, 3);
				break;
			}
			case 4:{
				
				Main.generator.maxTag= changevar(Main.generator.maxTag, 4);
				break;
			}
			case 5:{
				
				Main.generator.comCount= changevar(Main.generator.comCount, 5);
				break;
			}
			case 6:{
				
				Main.generator.sendAt= changevar(Main.generator.sendAt, 6);
				break;
			}
			}
			break;
			
		}
		case 2:{}
		}
		
		
			
			
		}
		
	}
	
	private int changevar(int v, int i){
		int tempI=-1;
		try{
			tempI=Integer.parseInt(JOptionPane.showInputDialog(names[x][i-1],v));
		}catch( Exception e){
			//ignored
		}
		if (tempI>0)
			return tempI;
		return v;
	}
	private String changestr(String v, int i){
		String tempI=(JOptionPane.showInputDialog(names[x][i-1],v));
		if (tempI!=null)
			return tempI;
		return v;
	}
}
