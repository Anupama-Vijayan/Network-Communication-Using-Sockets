
import java.io.*;
import java.util.Scanner;
import java.net.*;

public class Client
{
String[] Pics = new String[10];
Socket socket = null;
Socket socket1 = null; // for image
PrintWriter out = null;
PrintWriter out1 = null;
File file = null;
BufferedInputStream inputStream = null;
FileOutputStream outputStream = null;
BufferedReader in = null;//read from html
static String path;
int i = 0;
int imageCount=0;

public static void main(String[] args)
{
    path = "/";
    int port;
   if (args.length != 1)
   {
      System.out.println("Usage:  client hostname port");
	 System.exit(1);
   }

   Client client = new Client();
  try
  {
	  
   String host = args[0]; // the url
	  
	//the url parsing
   
   String[] temp1 = host.split("//");
   host= temp1[1];
   // if the port number is specified in the url
   if(host.contains(":"))
   {
	   String[] temp2 = host.split(":");
	   port = Integer.parseInt(temp2[1].substring(0,temp2[1].length()-1));
	   host = temp2[0];
   }
   
   //the default port number is 80
   else
   {
	   port=80;   
	   path = host.substring(host.indexOf( '/' ),host.length());
	   host = host.substring(0,host.indexOf( '/' ));
   }
 
   client.listenSocket(host, port);
   client.communicate(host,path,port);
}
  catch (IndexOutOfBoundsException e)
  {
     System.out.println("Not found");
     System.exit(1);
  }
}

public void listenSocket(String host, int port)
{
   //Create socket connection
   try
   {
	 socket = new Socket(host, port);
	 // for http request
	 out = new PrintWriter(socket.getOutputStream(), true);
	 // to get the html text file
	 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
   }
   catch (UnknownHostException e) 
   {
	 System.out.println("Unknown host");
	 System.exit(1);
   } 
   catch (IOException e) 
   {
	 System.out.println("No I/O");
	 System.exit(1);
   }
}

public void communicate(String host, String path, int port)
{
     boolean flag = false; 
     //the http request is sent
     out.print("GET "+ path + " HTTP/1.0\r\n");
     out.print("Host: "+ host+":"+port+ "\r\n\r\n");
     out.println("");
     out.flush();
   
   // its connected to the web server  
   System.out.println("Connecting...");
   String input_line;
   
   //Receive text from server
   try
   {      
      while((input_line=in.readLine())!=null )
      {
    	// if the page is not found
    	if(input_line.equals("HTTP/1.1 404 Not Found"))
    	{
    		System.out.println("404 Not Found");
    		System.exit(1);
    	}
    	  
    	else
    	{
    	//to remove all the headers and just start with parts below the html tag  
	     if (input_line.contains("<html>"))
	    		 flag = true;
	     if(flag == true)	
	     {
	    	 //to remove the style part 	    	 
	    	 if(input_line.contains("<style"))
	    	 {
	    		 while(!input_line.contains("</style>") && (input_line=in.readLine())!=null )
	    		 {
	    			 
	    		 }
	    		 continue;
	    	 }
	    	 
	    	 //to remove the title of the page
	    	 if(input_line.contains("<title"))
	    	 {
	    		 while(!input_line.contains("</title>") && (input_line=in.readLine())!=null )
	    		 {
	    			 
	    		 }
	    		 continue;
	    	 }	 
	     //to parse all the tags 
	     input_line = parse(input_line);      
	     
	    System.out.println(input_line);
	     }
	    }
	   }
      //closing the input and the output streams for the html
	   out.close();
	   in.close();
	      
	      //If pictures are present in the web page, this part is executed
	      if(Pics[0]!=null)
	      {
	      System.out.println("The images we have are : ");
	  	  for(int j= 0; j <i; j++)
	      {
	  		System.out.println("Image"+(j+1)+" : "+Pics[j]);
	      }
		  	System.out.println();
		  	System.out.println();
		  	System.out.println();
	      }
	      
	      for(int j= 0; j <i; j++)
	      {
	    	  //calling the function to download the image
	    	  image(Pics[j],host,port);
	    	  System.out.println();
	    	  System.out.println();
	      }
   } 
   catch (IOException e)
   {
      System.out.println("Read failed");
      System.exit(1);
   }
}

//function for parsing the html
public String parse(String line)
{	
	//if the web page has an image, the url for the image is parsed out and is stored in an array
	 if(line.contains("<img src"))
	 {
		 	 int index3 = line.indexOf("<img src");
			  line = line.substring(index3);
			 int index1= line.indexOf("\"");
			 int index2 = line.indexOf("\"",index1 + 1);
			//everything between " "
			 String trial = line.substring(index1+1,index2);
			 System.out.println("Image :  "+ trial);
				 Pics[i]= trial;
				 i++;		
	 }
	 
	 //removing all the tags
	 line= line.replaceAll("\\<.*?>","");
	 int first = line.indexOf("<");
	 int last = line.indexOf(">");
	 
	 //to handle the case where the tags are split into multiple lines
	 if(first != -1 && last == -1)
	 {
		 String temp1 = line.substring(first, line.length());
		 line = line . replaceAll(temp1,"");
	 }
	 if(first == -1 && last != -1)
	 {
		 String temp2 = line.substring(0,last+1);
		 line = line . replaceAll(temp2,"");
	 }
	 return line;
}

// function to handle saving the images in the server
public void image(String Image,String host, int port) 
{	
	// if the image url contains http
	if(Image.contains("http"))
	{
		Image = Image.substring(Image.indexOf("//")+2);
		int index5 = Image.indexOf("/");
		host = Image.substring(0,index5);
		Image= Image.substring(index5);
	}
	else
	{
		int index6 = path.lastIndexOf("/");
		String append = path.substring(0,index6+1);
		Image= append + Image;
	}
	try
	{
	// connection for giving the html request			
	socket1 = new Socket(host, port);
	out1 = new PrintWriter(socket1.getOutputStream(), true);
	inputStream = new BufferedInputStream(socket1.getInputStream());
    String type = "";
    int index = Image.lastIndexOf(".");    
    type = Image.substring(index);
    //http request for Image  
    out1.print("GET " + Image + " HTTP/1.0\r\n");
    out1.print("Host: " + host+ "\r\n\r\n");
    out1.print("");
    out1.flush();
    
    String userHomeFolder = System.getProperty("user.home", "Desktop");
    String Name_Of_file = ("Image"+imageCount + type);
    imageCount++;    
    File fileName = new File(userHomeFolder, "Desktop\\"+ Name_Of_file);
    outputStream = new FileOutputStream(fileName);
    byte[] buf = new byte[2048];
    int n = 0;
    boolean loop =true;              
   System.out.println("Name of the file "+fileName);

   //\r\n\r\n
    while(loop)
    {
    	if(inputStream.read()== '\r')
            if(inputStream.read()== '\n')
                  if(inputStream.read()== '\r')
                       if(inputStream.read()== '\n')
                           loop =false;
    }
    
    while (-1 != (n = inputStream.read(buf))) 
    {
        System.out.println("Buffer Read of length: " + n);
        outputStream.write(buf, 0, n);
    }      
     	out1.close();
     	inputStream.close();
    	outputStream.close();    	
    }
	catch(Exception e)
	{
		System.out.println("exception :"+ e.getMessage());
	}	
  } 
}