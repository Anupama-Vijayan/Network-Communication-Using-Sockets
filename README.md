# Network-Communication-Using-Sockets
Using Java Sockets a network communication between processes are established. A web browser was developed which will read from a URL.

We were given five urls to the web pages and were asked to print out the content of the page along with downloading the images to the current directory, if it is present. I started by learning about sockets from the example programs professor has provided and the material available in the internet. Once the program, the client connects to the web server, through an output stream, we’ll send an http request for the web page. Once the connection is setup through the input stream from the server, we’ll be getting the html source code of the web page. The html source will have a lot of unwanted tags associated with it which we have to pass out using different parsing techniques. While the text is being copied, we have to maintain a record of the number of images which could be present in the web page. If images are present, then another socket object is created along with a separate input and output stream for downloading the image. An http request is sent through the output stream for each image present in the page. Through the input stream the image is read and is saved to a file in the current directory. 

Note:

1) In the Unix command prompt, the instruction to run the program (after compiling),example, for the link http://www.december.com/html/demo/hello.html will be :
 
        java Client http://www.december.com/html/demo/hello.html

		
2) If there are images associated , it will be downloaded. And the path where the image file will be saved is displayed along with the output.The image file will have to be copied to the local system to be viewed.
 
3)The image downloaded follows the naming convention :

  Image Imagenumber
  
