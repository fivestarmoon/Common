package csm.common.swing;

import java.awt.GridLayout;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import fsm.common.Log;

public class DatePickerDialog
{  
   public static Calendar GetDate(JFrame window,
                                  String title, 
                                  Calendar initialDate)
   {
      String[] days = new String[31];
      for ( int ii=0; ii<days.length; ii++)
      {
         days[ii] = Integer.toString(ii+1);
      }
      DateFormatSymbols dfs = new DateFormatSymbols();
      String[] years = new String[5];
      for ( int ii=0; ii<years.length; ii++)
      {
         years[ii] = Integer.toString(initialDate.get(Calendar.YEAR)-1+ii);
      }
      JComboBox<String> jcd = new JComboBox<String>(days);
      JComboBox<String> jcm = new JComboBox<String>(dfs.getMonths());
      JComboBox<String> jcy = new JComboBox<String>(years);

      jcd.setEditable(true); 
      jcd.setSelectedIndex(initialDate.get(Calendar.DAY_OF_MONTH) - 1);
      jcm.setEditable(true);
      jcm.setSelectedIndex(initialDate.get(Calendar.MONTH));
      jcy.setEditable(true);
      jcy.setSelectedIndex(1);
      
      JPanel myPanel = new JPanel();
      myPanel.setLayout(new GridLayout(2,3));
      myPanel.add(new JLabel("Year"));
      myPanel.add(new JLabel("Month"));
      myPanel.add(new JLabel("Day"));
      myPanel.add(jcy);
      myPanel.add(jcm);
      myPanel.add(jcd);

      int result = JOptionPane.showConfirmDialog(
         window, 
         myPanel,
         title, 
         JOptionPane.OK_CANCEL_OPTION);
      Calendar pickedDate = (Calendar) initialDate.clone();
      if (result == JOptionPane.OK_OPTION) 
      {
         try
         {
            pickedDate.set(Calendar.YEAR, Integer.valueOf((String) jcy.getSelectedItem()));
            pickedDate.set(Calendar.MONTH, jcm.getSelectedIndex());
            pickedDate.set(Calendar.DAY_OF_MONTH, jcd.getSelectedIndex()+1);
         }
         catch(Exception e)
         {
            Log.severe("Failed to convert date", e);
            pickedDate = initialDate;
         }
      }
      else
      {
         return null;
      }
      return pickedDate;
   }

}
