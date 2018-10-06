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
	private static final double P_CONSTANT = 1/100;
	private static final double I_CONSTANT = 0;
	
	/** If the abs value of the difference of turning values is greater than this, we switch to turning gear*/
	private static final double TURNING_THRESHOLD = 0.75;
	
	/** PI Control continues until the error is below this */
	private static final double MAX_ALLOWABLE_ERROR = 1;
	
	
	//Signage Chart
	/*
	 * 					     | True 	   | False	   |
	 * -------------------------------------------------------
	 * Gear Shift Solenoid   |  High Gear  |  Low Gear |
	 * -------------------------------------------------------
	 * 					     |  +1  	   |  -1  	   |
	 * -------------------------------------------------------
	 * Left Drive Joystick   |   Backwards | Forwards  |
	 * Right Drive Joystick  |   Backwards | Forwards  |
	 * Left Drive Controller |   Forwards  | Backwards |
	 * Right Drive Controller|   Backwards | Forwards  |
	 * Left Drive Encoder    |   Forward   |  Backward |
	 * Right Drive Encoder   |   Backward  |  Forward  |
	 * 
	 */
	
	//Variables
	/** Previous value of the button that controllers this gearshift solenoid */
	boolean lastGearShiftState = false;
	/** Number of inches */
	double leftDriveTarget = 0;
	/** Number of inches */
	double rightDriveTarget = 0;
	/** True while PI control is active */
	boolean leftCurrentlyControlling;
	/** True while PI control is active */
	boolean rightCurrentlyControlling;
	/** accumulation of error on left side */
	double aggrLeftError = 0;
	/** accumulation of error on right side */
	double aggrRightError = 0;

	//Power Caps
	private double leftPowerCap = 1;
	private double rightPowerCap = 1;
	
	//Actuators\Sensors
	Spark leftController;
	Spark rightController;
	Encoder leftEnc;
	Encoder rightEnc;
	Solenoid gearShiftSol;
	
	public DriveSubsystem()
	{
		leftController = new Spark(Constants.LEFT_DRIVE_CONTROLLER_PORT);
		rightController = new Spark(Constants.RIGHT_DRIVE_CONTROLER_PORT);
		leftEnc = new Encoder(Constants.LEFT_DRIVE_ENCODER_PORT_A, Constants.LEFT_DRIVE_ENCODER_PORT_B);
		rightEnc = new Encoder(Constants.RIGHT_DRIVE_ENCODER_PORT_A, Constants.RIGHT_DRIVE_ENCODER_PORT_B);
		gearShiftSol = new Solenoid(Constants.DRIVE_GEARSHIFT_PORT);
	}
	
	/**
	 * Controls the drive speed of both sides of the robot
	 * @param leftMagnitude - Left drive magnitude
	 * @param rightMagnitude - Right drive magnitude
	 * @param slowSpeed - If true, cut speed by factor of 0.5ish (see code for value)
	 */
	@Schema(Utilities283.LOGITECH_LEFT_Y)
	@Schema(Utilities283.LOGITECH_RIGHT_Y)
	@Schema(value = Utilities283.LOGITECH_RIGHT_BUMPER, desc = SLOWSPEED + " speed")
	public void drive(double leftMagnitude, double rightMagnitude, boolean slowSpeed)
	{
		//Below: if we are turning (difference between drive side magnitudes is above a certain value) then switch to turning gear
		if (Math.abs(leftMagnitude - rightMagnitude) > TURNING_THRESHOLD)
		{
			gearShiftSol.set(false); //Gearing that allows turning
		}
		leftController.set(-1 * (leftMagnitude) * (slowSpeed ? SLOWSPEED : 1));
		rightController.set((rightMagnitude) * (slowSpeed ? SLOWSPEED : 1));
		//SmartDashboard.putNumber("Left Magnitude", leftMagnitude);
		//SmartDashboard.putNumber("Right Magnitude", rightMagnitude);
	}
	public void EncDrive(double distance)
	{
		int radius = 6;
		double circ = Math.PI * radius * radius;
		double resolution = 360;
		double dots = distance /(circ/resolution);
		leftDriveDistanceInit(dots,0.5);
		rightDriveDistanceInit(dots,0.5);
	}
	public void turn(double degrees, String direction)
	{
		int radius = 6;
		double circ = Math.PI * radius * radius;
		double resolution = 360;
		double width = 36;
		double dots = ((degrees/360)*(2 * Math.PI * width))/(circ/resolution);
		if(direction == "Right")
		{
			rightDriveDistanceInit(dots,0.5);
		}
		else if(direction == "Left")
		{
			leftDriveDistanceInit(dots,0.5);
		}
		else
		{
			
		}
	}
	
	/**
	 * Shifts the drive gear
	 * @param gearShiftValue - state of the button to execute shifting gears
	 *
	 */
	@Schema(Utilities283.LOGITECH_RIGHT_STICK_BUTTON)
	public void shiftGear(boolean gearShiftValue)
	{
		if (lastGearShiftState == false && gearShiftValue == true)
		{
			gearShiftSol.set(!gearShiftSol.get());
		}
		lastGearShiftState = gearShiftValue;
	}
	
	
	/**
	 * Drives the right drivetrain the specified number of inches
	 * You must periodically call 'driveDistancePeriodic()'
	 * @param inches - # of inches
	 * @param powerCap - Maximum power to set
	 */
	@Deprecated
	public void leftDriveDistanceInit(double inches, double powerCap)
	{
		leftDriveTarget = inches;
		leftCurrentlyControlling = true;
		this.leftPowerCap = powerCap;
	}
	
	/**
	 * Drives the left drivetrain the specified number of inches
	 * You must periodically call 'driveDistancePeriodic()'
	 * @param inches
	 */
	public void leftDriveDistanceInit(double inches)
	{
		leftDriveDistanceInit(inches, 1);
	}
	
	/**
	 * Drives the left drivetrain the specified number of inches
	 * You must periodically call 'driveDistancePeriodic()'
	 * @param inches - # of inches
	 */
	@Deprecated
	public void rightDriveDistanceInit(double inches, double powerCap)
	{
		rightDriveTarget = inches;
		rightCurrentlyControlling = true;
		this.rightPowerCap = powerCap;
	}
	
	/**
	 * Drives the left drivetrain the specified number of inches
	 * You must periodically call 'driveDistancePeriodic()' 
	 * @param inches
	 */
	public void rightDriveDistanceInit(double inches)
	{
		rightDriveDistanceInit(inches, 1);
	}
	/**
	 * Continuously updates both of the drive motors for target distance 
	 */
	@Deprecated
	public boolean driveDistancePeriodic()
	{
		double leftError = leftEnc.get() - leftDriveTarget;
		double rightError = rightEnc.get() - rightDriveTarget;
		
		//--LEFT SIDE--
		if (leftCurrentlyControlling == true)
		{
			if (Math.abs(leftError) < MAX_ALLOWABLE_ERROR) //If within allowable error
			{
				leftCurrentlyControlling = false; //Stop controlling
				leftController.set(0);
			}
			else
			{
				leftController.set(-P_CONSTANT * leftError + -I_CONSTANT * aggrLeftError);
				aggrLeftError += leftError;
			}
		}
		//Nothing happens if this is false
		
		//--RIGHT SIDE--
		if (rightCurrentlyControlling == true)
		{
			if (Math.abs(rightError) < MAX_ALLOWABLE_ERROR) //If within allowable error
			{
				rightCurrentlyControlling = false; //Stop controlling
				rightController.set(0);
			}
			else
			{
				rightController.set(-P_CONSTANT * rightError + -I_CONSTANT * aggrRightError);
				aggrRightError += rightError;
			}
		}
		if(Math.abs(rightError) < MAX_ALLOWABLE_ERROR && Math.abs(leftError) < MAX_ALLOWABLE_ERROR)
		{
			return true; //Returns true on function for auto for loop
		}
		else 
		{
			return false;//Returns false on function for auto for loop 
		}
		//Nothing happens if this is false
	}
	
	public boolean periodic()
	{
		driveDistancePeriodic();
		SmartDashboard.putBoolean("High Speed", gearShiftSol.get());
		if(rightCurrentlyControlling == false && leftCurrentlyControlling == false)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
