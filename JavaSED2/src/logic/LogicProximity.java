package logic;

import iotsuite.pubsubmiddleware.PubSubMiddleware;
import android.content.Context;
import iotsuite.semanticmodel.*;

import framework.*;
public class  LogicProximity extends Proximity  {  
	
	private double userTempPreference = 0;
	private String userProfile;
	private boolean responseArrived = false;
	private boolean continueFlag = true;

   public LogicProximity(PubSubMiddleware pubSubM, Device deviceInfo, Object ui,Context myContext) {
    super(pubSubM, deviceInfo);
    
   }   

          
            @Override
            public  void onNewbadgeDisappeared( BadgeStruct arg){     
            //TODO : write code here. 
            	// Person is leaving the room.
        		// So, provide negative value to indicate to off the heater.
        		TempStruct tempStruct = new TempStruct(-100, "C");
        		settempPref(tempStruct);
            }
            
          

          
            @Override
            public  void onNewbadgeDetected( BadgeStruct arg){     
            //TODO : write code here. 
            	userProfile = arg.getbadgeID();
        		// Request profileDB for his preference
        		getprofile(userProfile);
            }
            
           
    
        
          public void onNewprofileReceived( TempStruct arg)
          {
            //TODO : write code here     
        	  userTempPreference = arg.gettempValue();
      		TempStruct tempStruct = new TempStruct(userTempPreference, "C");
      		// Set temperature to Temperature Controller
      		settempPref(tempStruct);
          }     
      
}