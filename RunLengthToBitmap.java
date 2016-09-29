import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Color;

/**
 * Turns text file with run length encoding of a black and white image into 
 * an apropriate bitmap image.
 * 
 * @author Samuel Levenson
 * 
 * @version 9/28: First version
 */
public class RunLengthToBitmap {
  public static void main(String[] args) {
    File inputFile = null;
    if(args.length == 0) {
      System.out.println("enter the filepath of the run-length encoding you want as an image");
      Scanner input = new Scanner(System.in);
      inputFile = new File(input.next());
    }
    else {
      inputFile = new File(args[0]);
    }
    
    BufferedImage image = null;
    
    try {
      Scanner input = new Scanner(inputFile);
      
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
          //System.out.println(col + "," + row + "," + currentColor.getRGB());
          image.setRGB(col,row,currentColor.getRGB());
          col++;
          if(col >= width) {
            row++;
            col = 0;
          }
        }
        currentColor = switchColor(currentColor);
      }
      
    } catch (IOException e) {
      System.out.println("Unable to read file: " + e.getMessage());
      System.exit(1);
    }
    
    try {
      File outputImage = new File(inputFile.getParentFile(),inputFile.getName().replace("text","") + ".bmp");
      outputImage.createNewFile();
      //System.out.println(image);
      ImageIO.write(image, "bmp", outputImage);
    } catch (IOException e) {
      System.out.println("Unable to create image: " + e.getMessage());
      System.exit(1);
    }
    
    System.out.println("Done.");
    System.exit(0);
  }
  
  private static Color switchColor(Color orig) {
    if(orig.equals(Color.BLACK)) {
      return new Color(255,255,255);
    }
    return new Color(0,0,0);
  }
}