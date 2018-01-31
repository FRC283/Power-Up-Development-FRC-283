package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
		leftController = new Spark(Constants.LEFT_DRIVE_CONTROLLER_PORT);
		rightController = new Spark(Constants.RIGHT_DRIVE_CONTROLER_PORT);
		leftEnc = new Encoder(Constants.LEFT_DRIVE_ENCODER_PORT_A, Constants.LEFT_DRIVE_ENCODER_PORT_B);
		rightEnc = new Encoder(Constants.RIGHT_DRIVE_ENCODER_PORT_A, Constants.RIGHT_DRIVE_ENCODER_PORT_B);
	}
	public void drive(double leftMagnitude, double rightMagnitude, boolean slowSpeed)
	{
		leftController.set(leftMagnitude);
		rightController.set((Rescaler.rescale(DEADZONE, 1.0, 0.0, 1.0, rightMagnitude)) * (slowSpeed ? SLOWSPEED : 1));
		SmartDashboard.putNumber("Left Magnitude", leftMagnitude);
		SmartDashboard.putNumber("Right Magnitude", rightMagnitude);
		SmartDashboard.putNumber("Left Encoder Value", leftEnc.getDistance());
		SmartDashboard.putNumber("Right Encoder Value", rightEnc.getDistance());
	}
}
