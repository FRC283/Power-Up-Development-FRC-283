package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

public class Arm 
{
	 /* Left Arm Solenoid   |  Extended    |  Retracted  |
	 * Right Arm Solenoid   |  Extended    |  Retracted  |
	 *  					|      +1      |     -1      |
	 * Left Arm Controller |   ?  |   ?  |
	 * Right Arm Controller|   ?  |   ?  |
	 */  
	//See LiftSubsystem for relevant signage chart
	
	//Components
	Spark rollerController;
	Solenoid gripSol;
	
	//Constants
	private final double ROLLER_DEADZONE = 0.1;
	
	public Arm(int sparkPort, int solenoidPort)
	{
		rollerController = new Spark(sparkPort);
		gripSol = new Solenoid(solenoidPort);
	}
	
	/**
	 * Rolls the arm wheel in or out
	 * @param rollerMagnitude
	 */
	public void intake(double rollerMagnitude)
	{
		rollerController.set(Utilities283.deadzone(rollerMagnitude, ROLLER_DEADZONE));
	}
	
	/**
	 * Closes and opens the arms
	 * @param solenoidState
	 */
	public void grip(boolean solenoidState)
	{
		gripSol.set(solenoidState); //Invert the solenoid value
	}
}
