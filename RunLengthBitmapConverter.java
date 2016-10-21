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
 * @version 10/19: 
 * added method javadocs
 * 
 */
public class RunLengthBitmapConverter extends JPanel{
  /**
   * if not given a file as a command line argument, opens a jfilechooser window to let the user select a file
   * figures out if the file is a .bmp image, a .txt text file, or neither
   */
  public static void main(String[] args) {
    File filepath = null;
    if(args.length == 0) {      
      JFileChooser fileChooser = new JFileChooser();
      int choice = fileChooser.showOpenDialog(null);
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
    filepath = filepath.getAbsoluteFile();
    String filename = filepath.getName();
    if(filename.substring(filename.length()-3).equals("bmp")) {
      convertToRunLength(filepath);
      System.out.println("Done");
    }
    
    else if(filename.substring(filename.length()-3).equals("txt")) {
      convertToBitmap(filepath);
      System.out.println("Done");
    }
    
    else {
      System.out.println("You must choose either a .bmp image or a .txt textfile");
    }
  }
  
  /**
   * given a text file containing the run length encooding, sends file to other methods to be converted to a bufferedimage, written to a file, and displayed
   */
  public static File convertToBitmap(File inputfile) {
    BufferedImage image = readRunLength(inputfile);
    File outputfile = new File(inputfile.getParent(), inputfile.getName().replace(".txt",".bmp"));
    writeBitmap(outputfile,image);
    showImage(image);
    return outputfile;
  }
  /**
   * given a file containing an bmp image, reads the file and sends it to other methods to be displayed, turned into a run length encoding, and written to a text file
   */
  public static File convertToRunLength(File inputfile) {
    BufferedImage image = null;
    
    try {
      image = ImageIO.read(inputfile);
      System.out.println(image);
    } catch (IOException e) {
      System.out.println("Unable to read image: " + e.getMessage());
      System.exit(1);
    }
    showImage(image);
    
    File outputfile = new File(inputfile.getParent(), inputfile.getName().replace(".bmp",".runlength.txt"));
    try {
      outputfile.createNewFile();
    } catch (IOException e) {
      System.out.println("Unable to create new file: " + e.getMessage());
      System.exit(1);
    }
    
    writeRunLength(inputfile, image);
      return outputfile;
  }
  
  /**
   * returns black if input color is white, returns white if input color is black
   */
  private static Color switchColor(Color orig) {
    if(orig.equals(Color.BLACK)) {
      return new Color(255,255,255);
    }
    return new Color(0,0,0);
  }
  
  /**
   * displays the given image in a jframe
   */
  private static void showImage(BufferedImage image) {
    JFrame imgframe = new JFrame();
    imgframe.getContentPane().add(new JLabel(new ImageIcon(image)));
    imgframe.pack();
    imgframe.setVisible(true);
  }
  
  /**
   * takes a file containing the run length encoding of a black and white image and returns the apropriate bufferedimage
   */
  public static BufferedImage readRunLength(File runlength) {
    try {
      Scanner input = new Scanner(runlength);
      
      input.skip("Width");
      int width = input.nextInt();
      input.skip("\nHeight");
      int height = input.nextInt();
      input.skip("\nFirstcolor");
      Color currentColor = new Color(input.nextInt());
      input.skip("\n");
      input.useDelimiter(",");
      BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_BINARY);
      
      int col = 0;
      int row = 0;
      while(input.hasNextInt() == true) {
        int run = input.nextInt();
        for(int i = 0; i < run; i++) {
          image.setRGB(col,row,currentColor.getRGB());
          col++;
          if(col >= width) {
            row++;
            col = 0;
          }
        }
        currentColor = switchColor(currentColor);
      }
      return image;
    } catch (IOException e) {
      System.out.println("Unable to read file: " + e.getMessage());
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
  /**
   * given a bufferedimage and an empty file, writes the bufferedimage to the file and returns the file it wrote to (the same one it was given)
   */
  public static File writeBitmap(File output, BufferedImage image) {
    try {
      output.createNewFile();
      ImageIO.write(image, "bmp", output);
      return output;
    } catch (IOException e) {
      System.out.println("Unable to create image: " + e.getMessage());
      System.exit(1);
    }
    return null;
  }
  /**
   * given a bufferediamge and the file it was read from and writes the run length encoding of the image to a new file, returns the file it wrote to
   */
  public static File writeRunLength(File input, BufferedImage image) {
    int[] rgbArray = rgbArray(image);
    
    File output = new File(input.getParent() + "/" + input.getName().replace(".bmp",".runlength.txt"));
    try {
      FileWriter fwriter = new FileWriter(output);
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
      return output;
    } catch(IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
      System.exit(1);
    }
    return null;
  }
  /**
   * given a bufferedimage, returns an array storing the rgb values of the pixels, in row major order, starting from the bottom right
   */
  public static int[] rgbArray(BufferedImage image) {
    int[] rgbArray = new int[image.getWidth()*image.getHeight()];
    image.getRGB(0,0,image.getWidth(),image.getHeight(),rgbArray,0,image.getWidth());
    return rgbArray;
  }
}
