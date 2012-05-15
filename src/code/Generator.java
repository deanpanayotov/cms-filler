// needs connector J driver added to the classpath / build path


package code;
import graphics.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

public class Generator {
	private Connection con;
	private Statement stmt;
	private String str="", query="";
	public String driverName= "com.mysql.jdbc.Driver";
	public String url = "jdbc:mysql://localhost:3306/mysql";
	public String user= "root";
	public String password= "";
	private String textSource="";
	private String textSourceFP="content\\text_source.txt";
	private String postText="";
	private String[] fPath = new String[20];
	private String[] templates_delete= new String[20];
	private String[] templates_front = new String[20];
	private String[] templates = new String[20];
	private String[][] VAR = new String[4][100];
	public String B2E_DATABASE_NAME ="b2e_test";
	public String WP_DATABASE_NAME ="wp_test";	
	public int connected=0;
	private boolean largeScale=false;
	
	public int postCount=300;
	public int catCount=20;
	public int tagCount=50;
	public int maxTag=7;
	public int comCount=300;
	public int editMaxCount=15;
	
	private int iUnique=0;
	public int sendAt=50;
	
	public Generator(){
		
		try{
			textSource=readFileAsString(textSourceFP);
			Main.frame.textAdd("File Read successful, length:"+textSource.length());
			loadTemplates();
			Main.frame.textAdd("Templates Loaded successfuly");
		}catch( Exception e ) {
			handleException(e);
		}
	}
	
	public void loadTemplates(){
		
		fPath[0]="content\\wp\\cat_pt0.txt";
		fPath[1]="content\\wp\\cat_pt1.txt";
		fPath[2]="content\\wp\\comment_pt0.txt";
		fPath[3]="content\\wp\\post_pt0.txt";
		fPath[4]="content\\wp\\post_pt1.txt";
		fPath[5]="content\\wp\\tag_pt0.txt";
		fPath[6]="content\\wp\\tag_pt1.txt";
		fPath[7]="content\\b2e\\cat_pt0.txt";
		fPath[8]="content\\b2e\\comment_pt0.txt";
		fPath[9]="content\\b2e\\post_pt0.txt";
		fPath[10]="content\\b2e\\post_pt1.txt";
		fPath[11]="content\\b2e\\post_pt2.txt";
		fPath[12]="content\\b2e\\post_pt3.txt";
		fPath[13]="content\\b2e\\post_pt4.txt";
		fPath[14]="content\\b2e\\tag_pt0.txt";
		
		try{
			for(int i=0;i<15;i++){
				if(fPath[i]!=""){
					templates[i]=readFileAsString(fPath[i]);
					templates[i]=updateDBName(templates[i]);
					templates_front[i]=getSQLFront(templates[i]);
					templates[i]=getSQLBack(templates[i]);
				}
			}
			for(int i=0;i<17;i++){
				templates_delete[i]=readFileAsString("content\\delete\\delete_pt"+i+".txt");
				templates_delete[i]=updateDBName(templates_delete[i]);
			}
		}catch( Exception e ) {
			handleException(e);
		}
	}
	
	public void run(){
			runFull();
		     //runGenerateWords();
	}
	
	public void runFull(){
		if(connected==1){
			runDeleteTablesBoth();
			runInsertCategories();
			runInsertTags();
			runInsertPosts();
			runInsertComments();
			Main.frame.textAdd("Done.");
		}else{
			Main.frame.textAdd("Please connect to the server first.");
		}
	}
	public void runInsertCategories(){
		Main.frame.textAdd("Starting insertCat");
		
		try {
			stmt = con.createStatement();
			Random rand = new Random();
			int i;
			String query0=templates_front[0];
			String query1=templates_front[1];
			String query14=templates_front[7];
			
			for(i=1;i<catCount;i++){

				VAR[1][0]=Integer.toString(i);  //cat_ID
				VAR[1][1]=generateWord(textSource, 5, 20);	//cat_name
				VAR[1][2]=titleToName(VAR[1][1])+unique();	//cat_slug
				
				VAR[1][3]=VAR[1][0];		// tax id
				VAR[1][4]=VAR[1][0]; // term id
				if(rand.nextInt(2)==0){
					VAR[1][5]=Integer.toString(rand.nextInt(i));
				}else{
					VAR[1][5]="0";
				}
				str=templates[0];
				str=updateVariables(str,1,0,2);
				query0+=str+", "; 
				str=templates[1];
				str=updateVariables(str,1,3,5);
				query1+=str+", "; 
				//b2e
				str=templates[7];
				str=updateVariables(str,1,0,2);
				str=updateVariables(str,1,5,5);
				query14+=str+", "; 
				if(i%sendAt==0){
					query0=query0.substring(0,query0.length()-2);
					str=query0+";";
					stmt.executeUpdate(str);
					query1=query1.substring(0,query1.length()-2);
					str=query1+";";
					stmt.executeUpdate(str);
					query14=query14.substring(0,query14.length()-2);
					str=query14+";";
					stmt.executeUpdate(str);
					
					query0=templates_front[0];
					query1=templates_front[1];
					query14=templates_front[7];
					
					Main.frame.textAdd("Sent at: "+i);
				}
		
			}
			
			if((i-1)%sendAt!=0){
				query0=query0.substring(0,query0.length()-2);
				str=query0+";";
				stmt.executeUpdate(str);
				query1=query1.substring(0,query1.length()-2);
				str=query1+";";
				stmt.executeUpdate(str);
				query14=query14.substring(0,query14.length()-2);
				str=query14+";";
				stmt.executeUpdate(str);
			}

			
		}catch( Exception e ) {
			handleException(e);
		}//end catch
	}
	
	public String unique(){
		String result="";
		iUnique++;
		/*if(iUnique<10) result+="0";
		if(iUnique<100) result+="0";
		if(iUnique<1000) result+="0";*/
		result+=Integer.toString(iUnique);
		return result;
	}
	
	public void runInsertTags(){
		Main.frame.textAdd("Starting insertTags");
		int i;
		String query5=templates_front[5];
		String query6=templates_front[6];
		String query14=templates_front[14];
		
		try {
			stmt = con.createStatement();
			Random rand = new Random();
			for( i=catCount;i<tagCount+catCount;i++){

				VAR[2][0]=Integer.toString(i);  //cat_ID
				VAR[2][1]=generateWord(textSource, 5, 20)+unique();	//cat_name
				VAR[2][2]=titleToName(VAR[2][1])+unique();	//cat_slug
				
				VAR[2][3]=VAR[2][0];		// tax id
				VAR[2][4]=VAR[2][0]; // term id
				
				str=templates[5];
				str=updateVariables(str,2,0,2);
				query5+=str+", "; 
				str=templates[6];
				str=updateVariables(str,2,3,4);
				query6+=str+", "; 
				//b2e
				str=templates[14];
				str=updateVariables(str,2,0,1);
				query14+=str+", "; 
				
				if(i%sendAt==0){
					query5=query5.substring(0,query5.length()-2);
					str=query5+";";
					stmt.executeUpdate(str);
					query6=query6.substring(0,query6.length()-2);
					str=query6+";";
					stmt.executeUpdate(str);
					query14=query14.substring(0,query14.length()-2);
					str=query14+";";
					stmt.executeUpdate(str);
					
					query5=templates_front[5];
					query6=templates_front[6];
					query14=templates_front[14];
					
					Main.frame.textAdd("Sent at: "+i);
					
					
				}
				
			}
			
			if((i-1)%sendAt!=0){
				query5=query5.substring(0,query5.length()-2);
				str=query5+";";
				stmt.executeUpdate(str);
				query6=query6.substring(0,query6.length()-2);
				str=query6+";";
				stmt.executeUpdate(str);
				query14=query14.substring(0,query14.length()-2);
				str=query14+";";
				stmt.executeUpdate(str);
			}
			
		}catch( Exception e ) {
			handleException(e);
		}//end catch
	}
	
	public void runInsertPosts(){
		Main.frame.textAdd("Starting insertPosts");
		int i;
		String query3=templates_front[3];
		String query4=templates_front[4];
		String query9=templates_front[9];
		String query10=templates_front[10];
		String query11=templates_front[11];
		String query12=templates_front[12];
		String query13=templates_front[13];
		
		try {
			stmt = con.createStatement();
			Random rand = new Random();
			String gText="";
			for(i=1;i<postCount;i++){

				gText=generateText(textSource, 150, 1500, 15, 30);
				VAR[0][0]=Integer.toString(i);  //POST_ID
				VAR[0][1]=generateDate();	//post_date
				VAR[0][2]=VAR[0][1];	//post_date_gmt
				VAR[0][3]=textEnrichEditing(textEnrichParagraph(gText),20,60); //post_content 
				VAR[0][4]=generateTitle(textSource, 5); //post_title
				VAR[0][5]="abc"+unique();
						//generateWord(textSource,5,20)+unique();//titleToName(VAR[0][4])+unique();  //post_name
				VAR[0][6]=VAR[0][1];  //post_modified
				VAR[0][7]=VAR[0][1];	//post_modified_gmt
				VAR[0][8]=VAR[0][0]; 	//guid
				VAR[0][9]=Integer.toString(rand.nextInt(15)); //comment_count
				
				VAR[0][10]=VAR[0][0];  // object_id
				VAR[0][11]=Integer.toString(rand.nextInt(catCount)); // main cat
				//b2e
				VAR[0][12]=gText; // excerpt
				VAR[0][13]=Integer.toString(i*2); //slug 1
				VAR[0][14]=Integer.toString(i*2+1); //slug 2
				VAR[0][15]=Integer.toString(rand.nextInt(30)); // views 
				VAR[0][16]=Integer.toString(rand.nextInt(200)); //wordcount
				VAR[0][17]="a"+unique(); // unique slug for twitter
				
				str=templates[3];
				str=updateVariables(str,0,0,9);
				query3+=str+", "; 
				
				str=templates[4];
				str=updateVariables(str,0,10,11);
				query4+=str+", "; 
				//b2e
				str=templates[9];  //insert post
				str=updateVariables(str,0,0,6);
				str=updateVariables(str,0,11,16);
				query9+=str+", "; 
				
				str=templates[10]; // prerender
				str=updateVariables(str,0,0,1);
				str=updateVariables(str,0,3,3);
				query10+=str+", "; 
				
				str=templates[11]; //slugs
				str=updateVariables(str,0,0,0);
				str=updateVariables(str,0,5,5);
				str=updateVariables(str,0,13,14);
				str=updateVariables(str,0,17,17);
				query11+=str+", "; 
				
				str=templates[12]; // main cat
				str=updateVariables(str,0,0,0);
				str=updateVariables(str,0,11,11);
				query12+=str+", "; 
			
				
				int tags=rand.nextInt(maxTag)+1;
				String tagsAttached="";
				for(int j=0;j<tags;j++){
					
					VAR[0][11]=Integer.toString(rand.nextInt(tagCount)+catCount);
					while(tagsAttached.contains(VAR[0][11]+" ")){
						VAR[0][11]=Integer.toString(rand.nextInt(tagCount)+catCount);
					}
					tagsAttached+=VAR[0][11]+" ";
					str=templates[4];
					str=updateVariables(str,0,10,11);
					query4+=str+", "; 
					//b2e
					str=templates[13]; //tags..
					str=updateVariables(str,0,10,11);
					query13+=str+", "; 
				}
				
				if(i%sendAt==0){
					query3=query3.substring(0,query3.length()-2);
					str=query3+";";
					stmt.executeUpdate(str);
					query4=query4.substring(0,query4.length()-2);
					str=query4+";";
					stmt.executeUpdate(str);
					query9=query9.substring(0,query9.length()-2);
					str=query9+";";
					stmt.executeUpdate(str);
					query10=query10.substring(0,query10.length()-2);
					str=query10+";";
					stmt.executeUpdate(str);
					query11=query11.substring(0,query11.length()-2);
					str=query11+";";
					stmt.executeUpdate(str);
					query12=query12.substring(0,query12.length()-2);
					str=query12+";";
					stmt.executeUpdate(str);
					query13=query13.substring(0,query13.length()-2);
					str=query13+";";
					stmt.executeUpdate(str);
					
					query3=templates_front[3];
					query4=templates_front[4];
					query9=templates_front[9];
					query10=templates_front[10];
					query11=templates_front[11];
					query12=templates_front[12];
					query13=templates_front[13];
					
					
					Main.frame.textAdd("Sent at: "+i);
					
					
				}
				
				
			}
			if((i-1)%sendAt!=0){
				
				Main.frame.textAdd("I:"+i);
				
				query3=query3.substring(0,query3.length()-2);
				str=query3+";";
				stmt.executeUpdate(str);
				query4=query4.substring(0,query4.length()-2);
				str=query4+";";
				stmt.executeUpdate(str);
				query9=query9.substring(0,query9.length()-2);
				str=query9+";";
				stmt.executeUpdate(str);
				query10=query10.substring(0,query10.length()-2);
				str=query10+";";
				stmt.executeUpdate(str);
				query11=query11.substring(0,query11.length()-2);
				str=query11+";";
				stmt.executeUpdate(str);
				query12=query12.substring(0,query12.length()-2);
				str=query12+";";
				stmt.executeUpdate(str);
				query13=query13.substring(0,query13.length()-2);
				str=query13+";";
				stmt.executeUpdate(str);
			}
			
		}catch( Exception e ) {
			handleException(e);
		}//end catch
	}
	
	public void runInsertComments(){
		Main.frame.textAdd("Starting insertComments");
		int i;
		String query2=templates_front[2];
		String query8=templates_front[8];
		
		try {
			stmt = con.createStatement();
			Random rand = new Random();
			for(i=1;i<comCount;i++){

				
				VAR[3][0]=Integer.toString(i);  //comment id
				VAR[3][1]=Integer.toString(rand.nextInt(postCount)+1);
				VAR[3][2]=generateWord(textSource, 5, 20);	// user name
				VAR[3][3]=generateWord(textSource, 5, 20)+"@"+
						generateWord(textSource, 5, 20)+"."+
						generateWord(textSource, 5, 20); // email
						
				VAR[3][4]=generateWord(textSource, 5, 20)+".com"; //site...
				VAR[3][5]= generateIP(); //ip address
				VAR[3][6]=generateDate();	//date..
				VAR[3][7]=VAR[3][6];	//post_modified_gmt
				VAR[3][8]=textEnrichParagraph(generateText(textSource, 100, 400, 15, 30)); //comment_content 
				
				
				str=templates[2];
				str=updateVariables(str,3,0,8);
				query2+=str+", ";
				//b2e
				str=templates[8];
				str=updateVariables(str,3,0,6);
				str=updateVariables(str,3,8,8);
				query8+=str+", ";	
				
				if(i%sendAt==0){
					query2=query2.substring(0,query2.length()-2);
					str=query2+";";
					stmt.executeUpdate(str);
					query8=query8.substring(0,query8.length()-2);
					str=query8+";";
					stmt.executeUpdate(str);
					
					query2=templates_front[2];
					query8=templates_front[8];
					
					Main.frame.textAdd("Sent at: "+i);
					
				}
				
			}
			if((i-1)%sendAt!=0){
				query2=query2.substring(0,query2.length()-2);
				str=query2+";";
				stmt.executeUpdate(str);
				query8=query8.substring(0,query8.length()-2);
				str=query8+";";
				stmt.executeUpdate(str);
			}
			
		}catch( Exception e ) {
			handleException(e);
		}//end catch
	}
	
	public String updateVariables(String str,int x, int startY, int endY){
		String result=str;
		for(int i=startY;i<=endY;i++){
			result=result.replace("VAR["+Integer.toString(x)+"]["+Integer.toString(i)+"]", VAR[x][i]);
		}
		return result;
	}
	
	public String updateDBName(String str){
		String result=str;
		result=result.replace("B2E_DATABASE_NAME",B2E_DATABASE_NAME);
		result=result.replace("WP_DATABASE_NAME",WP_DATABASE_NAME);
		return result;
	}
	
	public void runGenerateText(){
		for(int i=0;i<1000;i++){
			postText=generateText(textSource, 100, 400, 15, 30);
			postText=textEnrichParagraph(postText);
			Main.frame.textAdd(postText);
			Main.frame.textAdd("Length:"+postText.length());
			Main.frame.textAdd("Generate:"+i);
		}
	}//end main
	
	public void runSendQuery(){
		try {
			stmt = con.createStatement();
			for(int i=0;i<fPath.length;i++){
				if(fPath[i]!=""){
					//Main.frame.textAdd("Reading file :"+fPath[i]);
					str=readFileAsString(fPath[i]);
					stmt.executeUpdate(str);
				}else{
					break;
				}
			}
		}catch( Exception e ) {
			handleException(e);
		}//end catch
	}
	 
	public void runGenerateWords(){
		Main.frame.textAdd("Generating words: ");
		for(int i=0;i<20;i++){
			Main.frame.textAdd(generateWord(textSource, 5, 20));
		}
	}
	
	public void runGenerateTitle(){
		Main.frame.textAdd("Generating Titles: ");
		for(int i=0;i<10000;i++){
			Main.frame.textAdd(generateTitle(textSource, 5));
		}
	}

	public String titleToName(String title){
		String result=title.toLowerCase();
		result=validateWord(result);
		result=result.replaceAll(" ", "_");
		return result;
	}
	
	public void runDeleteTablesBoth(){
		Main.frame.textAdd("Deleting old data");
		try {
			stmt = con.createStatement();
			for(int i=0;i<17;i++){
					stmt.executeUpdate(templates_delete[i]);
			}
		}catch( Exception e ) {
			handleException(e);
		}//end catch
		
	}
	
	public void runStringTesting(){
		String testString="THIS IS THE STRING TO BE TESTED COME ON DO IT";
		testString=textEnrichEditing(testString, 5, 10);
		Main.frame.textAdd(testString);
	}
	
	private String getSQLFront(String str){
		return str.substring(0,str.indexOf("VALUES[a][b][c]")+6);
	}
	
	private String getSQLBack(String str){
		return str.substring(str.indexOf("VALUES[a][b][c]")+15);
	}
	
	public void runGenerateDate(){
		for(int i=0;i<10;i++){
			Main.frame.textAdd(generateDate());
			Main.frame.textAdd(generateIP());
			}
	}
	
	public String generateDate(){
		Random rand=new Random();
		int temp=0;
		int feb=0;
		String result="2012-";
		temp=rand.nextInt(4)+1;
		if(temp<10)
			result+="0";
		if(temp==2)
			feb=3;
		result+=Integer.toString(temp)+"-";
		temp=rand.nextInt(30-feb)+1;
		if(temp<10)
			result+="0";
		result+=Integer.toString(temp)+" ";
		temp=rand.nextInt(23)+1;
		if(temp<10)
			result+="0";
		result+=Integer.toString(temp)+":";
		temp=rand.nextInt(59)+1;
		if(temp<10)
			result+="0";
		result+=Integer.toString(temp)+":";
		temp=rand.nextInt(59)+1;
		if(temp<10)
			result+="0";
		result+=Integer.toString(temp);
		if(result.contains("2012-03-25")){
			result=result.replace("2012-03-25", "2012-03-24");
		}
		return result;
	}
	
	public String generateIP(){
		Random rand=new Random();
		String result=Integer.toString(rand.nextInt(255)+1)+"."+Integer.toString(rand.nextInt(255)+1)+"."+Integer.toString(rand.nextInt(255)+1)+"."+Integer.toString(rand.nextInt(255)+1);
		return result;
	}
	
	public void wipeFilePaths(){
		for(int i=0;i<fPath.length;i++){
			fPath[i]="";
		}
	}
	
	public String textEnrichParagraph(String str){
		String temporary="", result="",text="<p>"+str+"</p>";
		
		while(text.indexOf("\n")!=-1){
			temporary=text.substring(0, text.indexOf("\n"));
			text=text.substring(text.indexOf("\n")+1);
			result+=temporary+"</p><p>";
		}
		result+=text;
		return result;
	}
	
	public String textEnrichEditing(String str,int minSnip, int maxSnip){
		Random rand= new Random();
		String edit_front[]={ 
				"<strong>",
				"<em>",
				"<del>",
				"<p style=\"text-align: right;\">",
				"<p style=\"text-align: center;\">",
				"<span style=\"color: #ff0000;\">",
				"<span style=\"color: #0000ff;\">",
				"<span style=\"color: #00ff00;\">",
				"<span style=\"color: #ffff00;\">",
				"<a href=\"http:/"+"/google.com\" target=\"_blank\">"
		};
		String edit_back[]={
				"</strong>",
				"</em>",
				"</del>",
				"</p>",
				"</p>",
				"</span>",
				"</span>",
				"</span>",
				"</span>",
				"</a>"
		};
		int edit=0;
		int start=0;
		int end=0;
		int snip;
		String temp="";
		String replacement="";
		int max=rand.nextInt(editMaxCount);
		for(int i=0;i<1;i++){
			while(temp.indexOf(" ")<0 || temp.indexOf(" ")==temp.lastIndexOf(" ")){
				edit=rand.nextInt(edit_back.length);
				end=rand.nextInt(str.length());
				snip=rand.nextInt(maxSnip-minSnip)+minSnip;
				start=end-snip+1;
				if(start<0){
					end+=start*-1;
					start=0;
				}
				
				/*start=rand.nextInt(str.length()-maxSnip);
				snip=rand.nextInt(maxSnip-minSnip)+minSnip;
				end=start+snip;*/
				temp=str.substring(start,end);
			}
			end=start+temp.lastIndexOf(" ");
			start+=temp.indexOf(" ");
			replacement=edit_front[edit]+str.substring(start,end)+edit_back[edit];
			str = str.replace(str.substring(start,end), replacement);
		}
		return str;
		
	}
	
	/*	String result="<p>"+str+"</p>";
		int index=0;
		while(index>-1){
			index+=subIndexOf(result,index,"\n");
			if(index>-1){
			result =new StringBuffer(result).insert(index+1, "</p><p>").toString();
			//System.out.println(Integer.toString(index));
			//Main.frame.textAdd(Integer.toString(index));
			index+=8;
			}
		}
		return result;
	}
	
	public int subIndexOf(String str,int index,String sub){
		String temporary=str.substring(index);
		//Main.frame.textAdd(Integer.toString(index));
		return temporary.indexOf(sub);
	}*/
	
	public void closeConnection(){
		if(connected==1){
			try{
				con.close();
			}catch( Exception e ) {
				handleException(e);
			}
		}
	}
	 private static String readFileAsString(String filePath)
			    throws java.io.IOException{
			        StringBuffer fileData = new StringBuffer(1000);
			        BufferedReader reader = new BufferedReader(
			                new FileReader(filePath));
			        char[] buf = new char[1024];
			        int numRead=0;
			        while((numRead=reader.read(buf)) != -1){
			            String readData = String.valueOf(buf, 0, numRead);
			            fileData.append(readData);
			            buf = new char[1024];
			        }
			        reader.close();
			      //  Main.frame.textAdd("[readFileAsString][1]\n");
			        //System.out.print("FILE READ:\n"+fileData.toString());
			        return fileData.toString();
			    }
	 private static String generateText(String textSource, int minLength, int maxLength, int minSnippet, int maxSnippet){
		 String text="", temporary="";
		 int index=0,setLength=0,subLength=0;
		 Random rand=new Random();
		 int maxIndex=textSource.length()-maxSnippet-10;
		 setLength=rand.nextInt(maxLength-minLength)+minLength;
		 while(text.length()<setLength){
			 index=rand.nextInt(maxIndex);
			 subLength=rand.nextInt(maxSnippet-minSnippet)+minSnippet;
			 temporary=textSource.substring(index, index+subLength);
			 text+=stringTrim(temporary);//+"[cut]";
		 }
		 return text;
	 }
	 
	 private static String generateWord(String textSource, int minLength, int sampleLength){
		 String word="", temporary="";
		 int index=0;
		 Random rand=new Random();
		 int maxIndex=textSource.length()-sampleLength-10;
		 while(word.length()<minLength){
			 index=rand.nextInt(maxIndex);
			 temporary=textSource.substring(index, index+sampleLength);
			 
			 if(temporary.indexOf(' ')!=-1){
				 temporary=temporary.substring(temporary.indexOf(' ')+1);
			 }
			 if(temporary.indexOf(' ')!=-1)
				 word=temporary.substring(0,temporary.indexOf(' '));
			 word=validateWord(word);
		 }
		 return word;
	 }
	 private static String validateWord(String word){
		 String result="";
		 for(int i=0;i<word.length();i++){
			 if(Character.isLetter(word.charAt(i))){
				 result+=word.charAt(i);
			 }
		 }
		 return result;
	 }
	 
	 private static String generateTitle(String textSource,int maxWords){
		 String title="";
		 Random rand=new Random();
		 int words=rand.nextInt(maxWords)+1;
		 for(int i=0;i<words;i++){
			 title+=generateWord(textSource, 5, 20)+" ";
		 }
		 return title;
	 }
	 
	 private static String stringTrim(String str){
		 return str.substring(str.indexOf(" ")+1, str.lastIndexOf(" ")+1);
	 }
	 private void handleException(Exception e){
		 e.printStackTrace();
		 Main.frame.textAdd("Exception:"+e.toString());
	 }
	 public void connect(){
		 if(connected==0){
			 try{
			 Class.forName(driverName);
				con =
				DriverManager.getConnection(url, user,password);
				
				Main.frame.textAdd("URL: " + url);
				Main.frame.textAdd("Connection: " + con);
				connected=1;
			 }catch( Exception e ) {
					handleException(e);
			 }
		 }else{
			 Main.frame.textAdd("Already connected.");
		 }
	 }
}