import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;

import java.util.Scanner;
import java.io.*;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Turns a black and white .bmp image into a run length encoding of it or 
 * turns a .txt file containing a run length encoding into a black and white .bmp image
 * 
 * @author Samuel Levenson
 * 
 * @version 10/5: Merged two previous classes into one class that converts both ways. 
 * Added option to choose file with jfilechooser; jfilechooser window only pops up the first time the program
 * is run after being opened.
 * Changed filename of .bmp images created to original.runlength
 * Displays images
 * 
 */
public class RunLengthBitmapConverter extends JPanel{
  public static void main(String[] args) {
    File filepath = null;
    if(args.length == 0) {      
      JFileChooser fileChooser = new JFileChooser();
      int choice = fileChooser.showOpenDialog(null); //not entirely sure what should be in place of null
      if(choice == JFileChooser.APPROVE_OPTION) {
        filepath = fileChooser.getSelectedFile();
      }
      else {
        System.exit(1);
      }
    }
    else {
      filepath = new File(args[0]);
    }
    String filename = filepath.getName();
    if(filename.substring(filename.length()-3).equals("bmp")) {
      convertToRunLength(filepath);
      System.out.println("Done");
      //System.exit(0);
    }
    
    else if(filename.substring(filename.length()-3).equals("txt")) {
      convertToBitmap(filepath);
      System.out.println("Done");
      //System.exit(0);
    }
    
    else {
      System.out.println("You must choose either a .bmp image or a .txt textfile");
    }
  }
  
  public static File convertToBitmap(File inputfile) {
    BufferedImage image = null;
    
    try {
      Scanner input = new Scanner(inputfile);
      
      input.skip("Width");
      int width = input.nextInt();
      input.skip("\nHeight");
      int height = input.nextInt();
      input.skip("\nFirstcolor");
      Color currentColor = new Color(input.nextInt());
      input.skip("\n");
      input.useDelimiter(",");
      image = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_BINARY);
      
      int col = 0;
      int row = 0;
      while(input.hasNextInt() == true) {
        int runlength = input.nextInt();
        for(int i = 0; i < runlength; i++) {
          image.setRGB(col,row,currentColor.getRGB());
          col++;
          if(col >= width) {
            row++;
            col = 0;
          }
        }
        currentColor = switchColor(currentColor);
      }
      System.out.println(image);
      
    } catch (IOException e) {
      System.out.println("Unable to read file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    File outputfile = new File(inputfile.getParentFile(),inputfile.getName().replace(".txt",".runlength") + ".bmp");
    try {
      
      outputfile.createNewFile();
      ImageIO.write(image, "bmp", outputfile);
    } catch (IOException e) {
      System.out.println("Unable to create image: " + e.getMessage());
      System.exit(1);
    }
    //show image
    JFrame imgframe = new JFrame();
    // imgframe.setSize(500, 500);
    //imgframe.getContentPane().add(new ShowImage());
    imgframe.getContentPane().add(new JLabel(new ImageIcon(image)));
    imgframe.pack();
    imgframe.setVisible(true);
    // Graphics g = image.getGraphics();
    // g.drawImage(image,0,0,null);
    
    return outputfile;
  }
  
  public static File convertToRunLength(File inputfile) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(inputfile);
      System.out.println(image);
    } catch (IOException e) {
      System.out.println("Unable to read image: " + e.getMessage());
      System.exit(1);
    }
    //show image
    // TODO: Extract into method since repeated twice
    JFrame imgframe = new JFrame();
    // imgframe.setSize(500, 500);
    imgframe.getContentPane().add(new JLabel(new ImageIcon(image)));
    imgframe.pack();
    imgframe.setVisible(true);
    
    int[] rgbArray = new int[image.getWidth()*image.getHeight()];
    image.getRGB(0,0,image.getWidth(),image.getHeight(),rgbArray,0,image.getWidth()); //puts rgb values of image into 1D array
    
    File outputfile = new File(inputfile.getParent() + "/" + inputfile.getName().replace(".bmp",".txt"));
    try {
      outputfile.createNewFile();
    } catch (IOException e) {
      System.out.println("Unable to create new file: " + e.getMessage());
      System.exit(1);
    }
    
    try {
      FileWriter fwriter = new FileWriter(inputfile.getParent() + "/" + inputfile.getName().replace(".bmp",".txt"));
      BufferedWriter bwriter = new BufferedWriter(fwriter);
      
      bwriter.write("Width " + image.getWidth() + "\n"); 
      bwriter.write("Height " + image.getHeight() + "\n"); 
      bwriter.write("Firstcolor " + image.getRGB(0,0) + "\n"); //saves the image width, height, and first rgb color in the text file
      int consec = 1;
      for(int i = 1; i < rgbArray.length; i++) {
        if(rgbArray[i-1] == rgbArray[i]){
          consec++;
        }
        else {
          bwriter.write(consec + ",");
          consec = 1;
        }
      }
      bwriter.write(consec + ","); //the last value of consec does not write to file unless there is some character after it...(?)
      bwriter.close();
    } catch(IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
      System.exit(1);
    }
    return outputfile;
  }
  private static Color switchColor(Color orig) {
    if(orig.equals(Color.BLACK)) {
      return new Color(255,255,255);
    }
    return new Color(0,0,0);
  }
}