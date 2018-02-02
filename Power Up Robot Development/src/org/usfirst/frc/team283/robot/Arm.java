package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;

public class Arm 
{
	Spark roller;
	Solenoid gripSol;
	public Arm(int sparkPort, int solenoidPort)
	{
		roller = new Spark(sparkPort);
		gripSol = new Solenoid(solenoidPort);
	}
}
