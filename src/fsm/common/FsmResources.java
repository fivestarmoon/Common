package fsm.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class FsmResources
{ 
   static public Image getImageResource(String imageName) 
   {

      BufferedImage img = null;
      try
      {
         img = ImageIO.read(FsmResources.class.getResource("resources/" + imageName));
      }
      catch (IOException e)
      {
          e.printStackTrace();
          return null;
      }
      return img;
   }
   
   
   static public ImageIcon getIconResource(String imageName) 
   {
      ImageIcon icon = null;
      URL imgURL = FsmResources.class.getResource("resources/" + imageName);
      if ( imgURL != null )
      {
         try
         {
            icon = new ImageIcon(imgURL, imageName);
         } 
         catch (Exception e) 
         {
            e.printStackTrace();
         }
      }
      return icon;
   }
}
