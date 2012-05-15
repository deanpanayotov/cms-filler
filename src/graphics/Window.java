package graphics;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Window extends JFrame {
	
	private Settings settingsWindow;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	private String sButton0="Host Info", sButton1="Item Quantity", sButton2="Post Options";
	public Window(){
		setResizable(true);
		setTitle("CMS Filler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Main.cFrameBgr);
		getContentPane().setForeground(Main.cFont);
		initComponents();
		center(this);
		
		
	}
	
	public void onExit(){
		JOptionPane.showMessageDialog(null, "Window Closing");
    	Main.generator.closeConnection();
        System.exit(0);
	}
	protected void initComponents(){
		
		JButton bSetHost= new JButton(sButton0);
		JButton bSetQuantity= new JButton(sButton1);
		JButton bSetPost= new JButton(sButton2);
		JButton bConnect= new JButton("Connect");
		JButton bRun= new JButton("Run");
		JButton bQuit= new JButton("Quit");
		
		JLabel lSettings = new JLabel("Settings: ");
		JLabel lName = new JLabel("Dean Panayotov 2012");
		
		lSettings.setForeground(Main.cFont);
		lName.setForeground(Main.cFont);
		
		textArea= new JTextArea();
		
		textArea.setColumns(60);
        textArea.setLineWrap(true);
        textArea.setRows(15);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(Main.cTextAreaBgr);
        textArea.setForeground(Main.cFont);
        
        scrollPane = new JScrollPane(textArea);
        /*
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
        	public void adjustmentValueChanged(AdjustmentEvent e) {  
        	e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
        	}}); */
        HandlerClass handler = new HandlerClass();
        
        bSetHost.addActionListener(handler);
        bSetQuantity.addActionListener(handler);
        bSetPost.addActionListener(handler);
        bConnect.addActionListener(handler);
        bRun.addActionListener(handler);
        bQuit.addActionListener(handler);
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);  
        
        /////////HORIZONTAL//////////
        
        ParallelGroup h1 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup h2 = layout.createSequentialGroup();
        SequentialGroup h3 = layout.createSequentialGroup();
        
        h3.addComponent(bConnect);
        h3.addComponent(bRun);
        h3.addComponent(bQuit);
        
        h2.addComponent(lSettings);
        h2.addComponent(bSetHost);
        h2.addComponent(bSetQuantity);
        h2.addComponent(bSetPost);
        
   
        h1.addGroup(GroupLayout.Alignment.TRAILING, h2);
        h1.addComponent(scrollPane);
        h1.addGroup(GroupLayout.Alignment.TRAILING, h3);
        h1.addComponent(lName);
        
        layout.setHorizontalGroup(h1);
        
        /////////VERTICAL//////////
        
        ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        SequentialGroup v1 = layout.createSequentialGroup();
        ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        ParallelGroup v3 = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        ParallelGroup v4 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        
        v3.addComponent(bConnect);
        v4.addComponent(bQuit);
        v4.addComponent(bRun);
        
        v3.addComponent(lName);
        v3.addGroup(v4);
        
        v2.addComponent(lSettings);
        v2.addComponent(bSetHost);
        v2.addComponent(bSetQuantity);
        v2.addComponent(bSetPost);

        v1.addGroup(v2);
        v1.addComponent(scrollPane);
        v1.addGroup(v3);
        
        layout.setVerticalGroup(v1);
        
        pack();
        
        textAdd("Welcome.");
        
	}
	
	protected void center(JFrame frame){
		
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension scrnsize = toolkit.getScreenSize();
			frame.setBounds((int)(scrnsize.getWidth()/2-frame.getBounds().width/2),
					(int)(scrnsize.getHeight()/2-frame.getBounds().height/2),
					frame.getWidth(), frame.getHeight());
			
	}
	
	public void textAdd(String str){
		str=getStamp()+str;
		if(str.lastIndexOf("\n")!=str.length()-1)
			str+="\n";
		textArea.append(str);
		scrollToBottom();
		repaint();
		this.revalidate();
	}
	private String getStamp(){
		String sHours="",sMinutes="",sSeconds="";
		long time =System.currentTimeMillis();
		int raw=(int) (time/1000);
		int seconds= raw%60;
		raw/=60;
		int minutes= raw%60;
		raw/=60;
		int hours= raw%24 + 3;
		if(hours>23) hours-=24;
		if(hours<10) sHours+="0";
		sHours+=Integer.toString(hours);
		if(minutes<10) sMinutes+="0";
		sMinutes+=Integer.toString(minutes);
		if(seconds<10) sSeconds+="0";
		sSeconds+=Integer.toString(seconds);
		return "["+sHours+":"+sMinutes+":"+sSeconds+"] ";
	}
	private void scrollToBottom(){  // Obsolete
		textArea.selectAll();
		int x=textArea.getSelectionEnd();
		textArea.select(x, x);
	}
	
	private void openSettings(String category, int x){
		if(settingsWindow!=null)
			settingsWindow.dispose();
		settingsWindow = new Settings(x);	
		settingsWindow.setVisible(true);
		settingsWindow.setTitle(category);
	}
	
	private class AdjustmentClass implements AdjustmentListener{

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			
			
		//	StrPan.repaint();
		//	Main.calc.setScroll();
			
		}
		
	}
	
	private class HandlerClass implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			switch(event.getActionCommand()){
			
			case "Quit":{
				onExit();
				break;
			}
			case "Connect":{
				Main.generator.connect();
				break;
			}
			case "Run":{
				Main.generator.run();
				break;
			}
			case "Host Info":{
				openSettings(sButton0,0);
				break;
			}
			case "Item Quantity":{
				openSettings(sButton1,1);
				break;
			}
			case "Post Options":{
				openSettings(sButton2,2);
				break;
			}
			}
			
			
		}
		
	}
	
}
