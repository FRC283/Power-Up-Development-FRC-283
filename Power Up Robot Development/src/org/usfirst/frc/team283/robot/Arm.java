package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

public class Arm 
{
	Spark roller;
	Solenoid gripSol;
	public Arm()
	{
		roller = new Spark(0);
		gripSol = new Solenoid(0);
	}
}
