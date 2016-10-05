import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.imageio.ImageIO;
import java.io.*;

public class ShowImage extends JPanel {
  /**
  * extra class that somehow is used to show images
  * @version 10/4: first version
  */
  public void paint(Graphics g) {
    File filepath = new File("/Users/sammy/Documents/compsi/compsci12th/testImages/dog.bmp");
    BufferedImage img = null;
    try {
      img = ImageIO.read(filepath);
    } catch (IOException e) {
      System.out.println("Unable to read image: " + e.getMessage());
      System.exit(1);
    }
    g.drawImage(img, 20,20,null);
  }
  public static void main(String[] args) {
    JFrame imgframe = new JFrame();
    imgframe.setSize(500, 500);
    imgframe.getContentPane().add(new ShowImage());
    imgframe.setVisible(true);
  }
}
