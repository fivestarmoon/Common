package fsm.common.swing;

import java.awt.GridLayout;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fsm.common.Log;

@SuppressWarnings("serial")
public class DatePickerPanel extends JPanel
{  
   public DatePickerPanel(Calendar initialDate)
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
      jcd_ = new JComboBox<String>(days);
      jcm_ = new JComboBox<String>(dfs.getMonths());
      jcy_ = new JComboBox<String>(years);

      jcd_.setEditable(true); 
      jcd_.setSelectedIndex(initialDate.get(Calendar.DAY_OF_MONTH) - 1);
      jcm_.setEditable(true);
      jcm_.setSelectedIndex(initialDate.get(Calendar.MONTH));
      jcy_.setEditable(true);
      jcy_.setSelectedIndex(1);
      
      this.setLayout(new GridLayout(2,3));
      this.add(new JLabel("Year"));
      this.add(new JLabel("Month"));
      this.add(new JLabel("Day"));
      this.add(jcy_);
      this.add(jcm_);
      this.add(jcd_);
   }
   
   public Calendar getDate()
   {
      Calendar pickedDate = Calendar.getInstance();
      try
      {
         pickedDate.set(Calendar.YEAR, Integer.valueOf((String) jcy_.getSelectedItem()));
         pickedDate.set(Calendar.MONTH, jcm_.getSelectedIndex());
         pickedDate.set(Calendar.DAY_OF_MONTH, jcd_.getSelectedIndex()+1);
      }
      catch(Exception e)
      {
         Log.severe("Failed to convert date", e);
      }
      return pickedDate;
   }
   
   // --- PRIVATE

   JComboBox<String> jcd_;
   JComboBox<String> jcm_;
   JComboBox<String> jcy_;

}
