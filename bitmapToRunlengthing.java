import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Turns black and white images into a text file comtaining the run lengthing equivalent
 * Will not work if image file extension is not 3 letters long.
 * 
 * @author YOUR NAME HERE!
 * @version date, version info, etc
 */
public class BitmapToRunlengthing {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    // Prompt user and/or take from command line argument
    String filepath = in.next();
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      System.out.println("Unable to read image: " + e.getMessage());
      System.exit(1);
    }
    int[] imageArray = new int[image.getWidth()*image.getHeight()]; int idx = 0; //convert image into 1d array of pixel rgb values
    // REVERSE ROW/COL and check if top or bottom row is 0
    for(int row = 0; row < image.getHeight(); row++) {
      for(int col = image.getWidth() - 1; col >= 0; col--) {
        imageArray[idx] = image.getRGB(col,row);
        idx++;
      }
    }
    // ADD COMMENT TO EXPLAIN
    File output = new File(filepath.substring(0,filepath.lastIndexOf("/")),"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
    try {
      output.createNewFile();
    } catch (IOException e) {
      System.out.println("Unable to create new file: " + e.getMessage());
    }
    //System.out.println(filepath.substring(0,filepath.lastIndexOf("/")+1)+"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
    try {
      FileWriter fwriter = new FileWriter(filepath.substring(0,filepath.lastIndexOf("/")+1)+"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
      BufferedWriter bwriter = new BufferedWriter(fwriter);
      
      bwriter.write("Width " + image.getWidth() + "\n"); //saves the image width in the text file
      // ADD HEIGHT AS WELL
      int consec = 1; // POSSIBLY START AT 0??
      for(int i = 1; i < imageArray.length; i++) {
        if(imageArray[i-1] == imageArray[i]){
          consec++;
        }
        else {
          bwriter.write(consec + ",");
          consec = 1;
        }
      }
      bwriter.write(consec);
      bwriter.close();
    } catch(IOException e) {
      System.out.println("Unable to output to file: " + e.getMessage());
    }
    System.out.println("Done.");
    System.exit(0); //progam does not finish without this
  }
}
