import java.awt.image.BufferedImage;
/*import java.io.IOException;
 import java.io.File;
 import java.io.BufferedWriter;
 import java.io.FileWriter;*/
import java.io.*;
import javax.imageio.ImageIO;

public class bitmapRunLengthing {
  public static void main(String[] args) {
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File("/Users/sammy/Documents/dog.bmp"));
    } catch (IOException e) {
      System.out.println("Unable to read image");
    }
    int[] imageArray = new int[image.getWidth()*image.getHeight()]; int idx = 0;
    for(int row = 0; row < image.getHeight(); row++) {
      for(int col = image.getWidth() - 1; col >= 0; col--) {
        imageArray[idx] = image.getRGB(col,row);
        idx++;
      }
    }
    
    File output = new File("/Users/sammy/Documents","textdog");
    try {
      output.createNewFile();
    } catch (IOException e) {
      System.out.println("Unable to create new file");
    }
    
    try {
      FileWriter fwriter = new FileWriter("/Users/sammy/Documents/textdog");
      BufferedWriter bwriter = new BufferedWriter(fwriter);
      
      bwriter.write("Width " + image.getWidth() + "\n");
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
      bwriter.close();
    } catch(IOException e) {
      System.out.println("Unable to output to file");
    }
    System.exit(0);
  }
}