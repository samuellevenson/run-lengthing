import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Turns black and white images into a text file comtaining the run lengthing equivalent
 * Will not work if image file extension is not 3 letters long
 */
public class bitmapToRunlengthing {
  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    String filepath = in.next();
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      System.out.println("Unable to read image");
    }
    int[] imageArray = new int[image.getWidth()*image.getHeight()]; int idx = 0; //convert image into 1d array of pixel rgb values
    for(int row = 0; row < image.getHeight(); row++) {
      for(int col = image.getWidth() - 1; col >= 0; col--) {
        imageArray[idx] = image.getRGB(col,row);
        idx++;
      }
    }
    File output = new File(filepath.substring(0,filepath.lastIndexOf("/")),"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
    try {
      output.createNewFile();
    } catch (IOException e) {
      System.out.println("Unable to create new file");
    }
    //System.out.println(filepath.substring(0,filepath.lastIndexOf("/")+1)+"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
    try {
      FileWriter fwriter = new FileWriter(filepath.substring(0,filepath.lastIndexOf("/")+1)+"text"+filepath.substring(filepath.lastIndexOf("/")+1,filepath.length()-4));
      BufferedWriter bwriter = new BufferedWriter(fwriter);
      
      bwriter.write("Width " + image.getWidth() + "\n"); //saves the image width in the text file
      int consec = 1;
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
      System.out.println("Unable to output to file");
    }
    System.exit(0); //progam does not finish without this
  }
}