package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

public class Arm 
{
	Spark roller;
	Solenoid gripSol;
	private boolean storedState = false;
	public Arm(int sparkPort, int solenoidPort)
	{
		roller = new Spark(sparkPort);
		gripSol = new Solenoid(solenoidPort);
	}
	public void intake(int rollerMagnitude, boolean solenoidState)
	{
		if(solenoidState == true && storedState == false)
		{
			gripSol.set(!gripSol.get());
		}
		storedState = solenoidState;
		roller.set(rollerMagnitude);
	}
}
