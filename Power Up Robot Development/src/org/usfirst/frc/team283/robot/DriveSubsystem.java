package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;

public class DriveSubsystem 
{
	//Constants
	private static final double DEADZONE = 0.1;
	private static final double SLOWSPEED = 0.5;
	
	Spark leftController;
	Spark rightController;
	Encoder leftEnc;
	Encoder rightEnc;
	
	public DriveSubsystem()
	{
		leftController = new Spark(0);
		rightController = new Spark(0);
		
	}
	public void drive(double leftMagnitude, double rightMagnitude, boolean slowSpeed)
	{
		leftController.set(-1 * (Rescaler.rescale(DEADZONE, 1.0, 0.0, 1.0, leftMagnitude)) * (slowSpeed ? SLOWSPEED : 1));
		rightController.set((Rescaler.rescale(DEADZONE, 1.0, 0.0, 1.0, leftMagnitude)) * (slowSpeed ? SLOWSPEED : 1));
	}
}
