import junit.framework.TestCase;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * Test class for RunLengthBitmapConverter
 * 
 * @author Samuel Levenson
 * 
 * @version 10/11
 */
public class RunLengthBitmapConverterTest extends TestCase{
  public static void main(String[] args) {
    BufferedImage image = new BufferedImage(2,2,BufferedImage.TYPE_BYTE_BINARY);
    int[] rgbArray = {0,1,0,1};
    image.setRGB(0,0,2,2,rgbArray,0,2);
    System.out.println(rgbArray[0]);
    System.out.println(image.getRGB(1,0));
  }

  public void testReadRunLength() {
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File("dog.bmp"));
    } catch (IOException e) {
      System.exit(1);
    }
    assertEquals(RunLengthBitmapConverter.readRunLength(new File("dog.runlength.txt")),image);
  }

  public void testWriteBitmap() {
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File("dog.bmp"));
    } catch (IOException e) {
      System.exit(1);
    }
    File output = new File("dog.bmp");
    assertEquals(RunLengthBitmapConverter.writeBitmap(output,image),output);
  }

  public void testWriteRunLength() {
    BufferedImage image = null;
    try {
      image = ImageIO.read(new File("dog.bmp"));
    } catch (IOException e) {
      System.exit(1);
    }
    assertEquals(RunLengthBitmapConverter.writeRunLength(new File("dog.bmp"),image),
                 new File("dog.runlength.txt"));
  }
}
