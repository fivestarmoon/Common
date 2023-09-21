package fsm.common;

public class FsmTimer
{
   public FsmTimer()
   {
      timeStart_ = System.currentTimeMillis();
   }
   public void reset()
   {
      timeStart_ = System.currentTimeMillis();
   }
   
   public double getElapseTimeInSeconds()
   {
      return (System.currentTimeMillis() - timeStart_)/1000.0;
   }
   
   private long timeStart_;

}
