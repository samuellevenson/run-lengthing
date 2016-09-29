import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Turns black and white images into a text file comtaining the run lengthing equivalent.
 * Will not work if image file extension is not 3 letters long.
 * 
 * @author Samuel Levenson
 * 
 * @version 9/27: Changed class name, added ability to enter filepath as commandline argument, 
 * got rid of loop part of making rgb array (there was a method build into image class), and
 * added image height and first rgb color to textfile output.
 */
public class BitmapToRunLength {
  public static void main(String[] args) {
    String filepath = null;
    if(args.length == 0) {
      System.out.println("enter the filepath of the image you want in run length form");
      Scanner in = new Scanner(System.in);
      filepath = in.next();
    }
    else {
      filepath = args[0];
    }
    
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      System.out.println("Unable to read image: " + e.getMessage());
      System.exit(1);
    }
    int[] rgbArray = new int[image.getWidth()*image.getHeight()];
    image.getRGB(0,0,image.getWidth(),image.getHeight(),rgbArray,0,image.getWidth()); //puts rgb values of image into 1D array
    
    //creates a new text file in the same directory as the original image with the name 'text' + the original name
    File output = new File(filepath.substring(0,filepath.lastIndexOf("/")),"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
    try {
      output.createNewFile();
    } catch (IOException e) {
      System.out.println("Unable to create new file: " + e.getMessage());
      System.exit(1);
    }
    try {
      FileWriter fwriter = new FileWriter(filepath.substring(0,filepath.lastIndexOf("/")+1)+"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
      BufferedWriter bwriter = new BufferedWriter(fwriter);
      
      bwriter.write("Width " + image.getWidth() + "\n"); 
      bwriter.write("Height " + image.getHeight() + "\n"); 
      String firstColor = (image.getRGB(0,0) == -1)? "White":"Black";
      bwriter.write("Firstcolor " + firstColor + "\n"); //saves the image width, height, and first rgb color in the text file
      int consec = 1; // POSSIBLY START AT 0??
      for(int i = 1; i < rgbArray.length; i++) {
        if(rgbArray[i-1] == rgbArray[i]){
          consec++;
        }
        else {
          bwriter.write(consec + ",");
          consec = 1;
        }
      }
      bwriter.write(consec+"!"); //the last value of consec does not write to file unless there is some character after it...(?)
      bwriter.close();
    } catch(IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
      System.exit(1);
    }
    System.out.println("Done.");
    System.exit(0); //progam does not finish without this (something about awt)
  }
}
