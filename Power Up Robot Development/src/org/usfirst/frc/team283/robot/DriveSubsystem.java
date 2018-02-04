package org.usfirst.frc.team283.robot;

import org.usfirst.frc.team283.robot.Utilities283.Schema;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveSubsystem
{
	//Constants
	private static final double DEADZONE = 0.1;
	private static final double SLOWSPEED = 0.5;
	
	//Signage Chart
	/*
	 * 					     | True | False|
	 * --------------------------------------
	 * Left Shift Solenoid   |  ?   |  ?   |
	 * Right Shift Solenoid  |  ?   |  ?   |
	 * --------------------------------------
	 * 					     |  +1  |  -1  |
	 * --------------------------------------
	 * Left Drive Controller |   ?  |   ?  |
	 * Right Drive Controller|   ?  |   ?  |
	 * Left Drive Encoder    |   ?  |   ?  |
	 * Right Drive Encoder   |   ?  |   ?  |
	 * --------------------------------------
	 * 
	 */
	
	//Variables
	/** Previous value of the button that controllers this gearshift solenoid */
	boolean lastGearShiftState = false;
	
	//Actuators\Sensors
	Spark leftController;
	Spark rightController;
	Encoder leftEnc;
	Encoder rightEnc;
	Solenoid leftGearShift;
	Solenoid rightGearShift;
	
	public DriveSubsystem()
	{
		leftController = new Spark(Constants.LEFT_DRIVE_CONTROLLER_PORT);
		rightController = new Spark(Constants.RIGHT_DRIVE_CONTROLER_PORT);
		leftEnc = new Encoder(Constants.LEFT_DRIVE_ENCODER_PORT_A, Constants.LEFT_DRIVE_ENCODER_PORT_B);
		rightEnc = new Encoder(Constants.RIGHT_DRIVE_ENCODER_PORT_A, Constants.RIGHT_DRIVE_ENCODER_PORT_B);
		leftGearShift = new Solenoid(Constants.LEFT_DRIVE_GEARSHIFT);
		rightGearShift = new Solenoid(Constants.RIGHT_DRIVE_GEARSHIFT);
	}
	
	/**
	 * Controls the drive speed of both sides of the robot
	 * @param leftMagnitude - Left drive magnitude
	 * @param rightMagnitude - Right drive magnitude
	 * @param slowSpeed - If true, cut speed by factor of 0.5
	 */
	@Schema(Utilities283.LOGITECH_LEFT_Y)
	@Schema(Utilities283.LOGITECH_RIGHT_Y)
	@Schema(value = Utilities283.LOGITECH_RIGHT_TRIGGER, desc = SLOWSPEED + " speed")
	public void drive(double leftMagnitude, double rightMagnitude, boolean slowSpeed)
	{
		leftController.set(-1 * (Utilities283.rescale(DEADZONE, 1.0, 0.0, 1.0, leftMagnitude)) * (slowSpeed ? SLOWSPEED : 1));
		rightController.set((Utilities283.rescale(DEADZONE, 1.0, 0.0, 1.0, rightMagnitude)) * (slowSpeed ? SLOWSPEED : 1));
		//SmartDashboard.putNumber("Left Magnitude", leftMagnitude);
		//SmartDashboard.putNumber("Right Magnitude", rightMagnitude);
	}
	
	/**
	 * Shifts the drive gear
	 * @param gearShiftValue - state of the button to execute shifting gears
	 */
	@Schema(Utilities283.LOGITECH_LEFT_BUMPER)
	public void shiftGear(boolean gearShiftValue)
	{
		if (lastGearShiftState == false && gearShiftValue == true)
		{
			leftGearShift.set(!leftGearShift.get());
			rightGearShift.set(!rightGearShift.get());
		}
		lastGearShiftState = gearShiftValue;
	}
	
	public void periodic()
	{
		SmartDashboard.putNumber("Left Encoder Value", leftEnc.getDistance());
		SmartDashboard.putNumber("Right Encoder Value", rightEnc.getDistance());
		SmartDashboard.getBoolean("High Speed", leftGearShift.get());
	}
}
