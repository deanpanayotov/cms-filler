package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import code.Generator;

public class Main {
		
		//////////FONT////////////////////
		public static int fontSize=13;
		public static Font textFont= new Font("Arial",Font.PLAIN,fontSize);
		//////////////////////////////////
	
	////////////////////////////////////////////
			
	//////////////COLORS////////////////////////
	public static Color cFrameBgr= new Color(15,15,15,255);
	public static Color cTextAreaBgr= new Color(90,90,90,255);
	public static Color cFont= new Color(255,255,255,255);
	////////////////////////////////////////////
		
	/////////////VARIABLES//////////////////////
	public static String wStr=""; 				  // workString
	public static Window frame; 			 	  //window
	public static Generator generator; //calculate instance  
	////////////////////////////////////////////
	
	public static void main(String[] args){
		setLnF();
		frame= new Window();	
		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(WindowEvent winEvt) {
		    	frame.onExit(); 
		    }
		});
		generator=new Generator();
	}
	
	private static void setLnF(){
		try {
            // Set System L&F
        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	    }
	    catch (ClassNotFoundException e) {
	    }
	    catch (InstantiationException e) {
	    }
	    catch (IllegalAccessException e) {
	    }
	}
}
