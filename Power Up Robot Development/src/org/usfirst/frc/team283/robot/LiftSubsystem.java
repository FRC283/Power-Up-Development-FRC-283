package org.usfirst.frc.team283.robot;

import org.usfirst.frc.team283.robot.Utilities283.Schema;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftSubsystem 
{	
	//Constants
	private static final double DEADZONE = 0.1;
	private static final double ROLLER_DEADZONE = 0.1;
	private static final double P_CONSTANT = 1/100;
	private static final double I_CONSTANT = 0;
	/** PI Control continues until the error is below this */	
	private static final double MAX_ALLOWABLE_ERROR = 1;
	//Signage Chart
	/*
	 * 					   |     True     |     False   |
	 * ----------------------------------------------------
	 * Arms Solenoid  	   |  Retracted   |  Extended   |
	 * Lift Limit Switch   |  Unpressed	  |  Pressed    |
	 * ---------------------------------------------------------
	 * 					   |      +1      |      -1     |
	 * ------------------------------------------------------
	 * Xbox Lift Joystick  |	Down	  |		Up   	|
	 * Lift Controller     |    Up/Down   |   Down/Up   |
	 * Winch Controller    |   Reel In    |   Reel Out  |
	 * Winch Encoder       |   ?          |   ?         |
	 * Lift Encoder        |    Up/Down   |   Down/Up   |
	 * 
	 */
	//Variables
	/** Number of inches */
	double liftDriveTarget = 0;
	
	/** previous state of limit switch */
	boolean prevLimitState = true;
	
	/** previous signage of magnitude*/
	double previousMag = 0;
	
	/** True while PI control is active */
	boolean liftCurrentlyControlling = false;
	
	/** accumulation of error on lift */
	double aggrLiftError = 0;
	
	/** determines if winch is ready to reel in*/
	boolean winchUnlocked = false;
	
	//Components
	Spark leftRollerController;
	Spark rightRollerController;
	VictorSP winch;
	Encoder winchEnc;
	Encoder liftEnc;
	Spark liftController;
	Solenoid armsSol;
	DigitalInput upperLimitSwitch;
	
	public LiftSubsystem()
	{
		liftController = new Spark(Constants.LIFT_CONTROLLER_PORT);
		liftEnc = new Encoder(Constants.LIFT_ENCODER_PORT_A,Constants.LIFT_ENCODER_PORT_B);
		leftRollerController = new Spark(Constants.LEFT_ARM_CONTROLLER_PORT);
		rightRollerController = new Spark(Constants.RIGHT_ARM_CONTROLLER_PORT);
		winch = new VictorSP(Constants.WINCH_CONTROLLER_PORT);
		winchEnc = new Encoder(Constants.WINCH_ENCODER_PORT_A,Constants.WINCH_ENCODER_PORT_B);
		armsSol = new Solenoid(Constants.ARM_SOLENOID_PORT);
		upperLimitSwitch = new DigitalInput(Constants.LIFT_LIMIT_PORT);
	}
	
	/**A function that must be called continuously
	 * 
	 */
	public boolean periodic()
	{
		SmartDashboard.putNumber("Winch Encoder", winchEnc.get());
		SmartDashboard.putNumber("Lift Encoder", liftEnc.get());
		SmartDashboard.putBoolean("Arm Grip State", armsSol.get());
		SmartDashboard.putBoolean("State of Lift Limit", upperLimitSwitch.get());
		SmartDashboard.putBoolean("Previous Limit State", prevLimitState);
		SmartDashboard.putBoolean("Intakes are Rolling In", (liftController.get() > 0));
		SmartDashboard.putNumber("Previous Lift Magnitude", previousMag);
		
		
		//Below: logic used during autonomous
		if(liftCurrentlyControlling == false)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Deploys hooks so that the lift may take them up
	 * @param deployHook - deploy hooks
	 */
	@Schema(value = Utilities283.XBOX_RIGHT_STICK_BUTTON, desc = "ready hooks (+  " + Utilities283.XBOX_LEFT_STICK_BUTTON + ")")
	@Schema(value = Utilities283.XBOX_LEFT_STICK_BUTTON, desc = "ready hooks (+ " + Utilities283.XBOX_RIGHT_STICK_BUTTON + ")")
	public void unlockWinch(boolean unlockStick)
	{
		if (unlockStick == true)
		{
			winchUnlocked = true;
		}
	}
	
	/**
	 * Controls the climbing of the robot. Requires hooks to be released
	 * @param winchMagnitude - magnitude of winch
	 */
	@Schema(Utilities283.XBOX_LEFT_Y)
	public void climb(double winchMagnitude)
	{
		if(winchUnlocked == true) //If both hooks are picked up by the lift
		{
			winch.set(Utilities283.rescale(DEADZONE, 1.0, 0, 1.0, winchMagnitude)); //Allow control of 
		}
		else
		{
			winch.set(0); //Otherwise, lock the winch motor in place
		}
	}

	/**
	 * Controls up-down motion of lift
	 * @param liftMagnitude - controller input
	 */
	@Schema(Utilities283.XBOX_RIGHT_Y)
	public void lift(double liftMagnitude)
	{
		liftMagnitude *= 0.75; //Cut lift speed in half
		System.out.println("Lift Magnitude = " + liftMagnitude);
		//REMINDER: "FALSE" ON THE LIMIT SWITCH IS HIT
		if (upperLimitSwitch.get() == false && this.prevLimitState == false) //every cycle after the first that the limit is hit
		{
			if (liftMagnitude > 0 && this.previousMag < 0) //no negative mag
			{
				liftController.set(Utilities283.rescale(DEADZONE, 1.0, 0.0, 1.0, liftMagnitude));
			}
			else if (liftMagnitude < 0 && this.previousMag > 0) // no positive mag
			{
				liftController.set(Utilities283.rescale(DEADZONE, 1.0, 0.0, 1.0, liftMagnitude));
			}
			else
			{
				liftController.set(0);
			}
		}
		else if (upperLimitSwitch.get() == false && this.prevLimitState == true) // first cycle limit is hit
		{
			liftController.set(0); //stops lift
			previousMag = liftMagnitude; // sets previous magnitude to prevent breaking the lift
		}
		else if (upperLimitSwitch.get() == true && this.prevLimitState == false) //limit is released
		{
			previousMag = 0; //reset magnitude memory
		}
		else
		{
			liftController.set(Utilities283.rescale(DEADZONE, 1.0, 0.0, 1.0, liftMagnitude));
		}
		prevLimitState = upperLimitSwitch.get();
		//liftController.set(Utilities283.rescale(DEADZONE, 1.0, 0.0, 1.0, liftMagnitude));
	}
	
	/** 
	 * 
	 * @param rollerMagnitude - magnitude of rollers
	 */
	@Schema(value = Utilities283.XBOX_RIGHT_TRIGGER, desc = "Intake boxes")
	@Schema(value = Utilities283.XBOX_LEFT_TRIGGER, desc = "Expel boxes")
	public void intake(double rollerMagnitude)
	{
		leftRollerController.set(Utilities283.deadzone(rollerMagnitude, ROLLER_DEADZONE));
		rightRollerController.set(Utilities283.deadzone(rollerMagnitude, ROLLER_DEADZONE) * -1);
	}
	
	/** 
	 * 
	 * @param grab - state of button to grab cube
	 * @param release - state of button to release cube
	 */
	@Schema(value = Utilities283.XBOX_RIGHT_BUMPER, desc = "Grab box")
	@Schema(value = Utilities283.XBOX_LEFT_BUMPER, desc = "Release box")
	public void grip(boolean grab, boolean release)
	{
		if(grab == true)
		{
			armsSol.set(true);			
		}
		
		else if(release == true)
		{
			armsSol.set(false);		
		}
	}
	
	@Deprecated
	public void liftDistanceInit(double inches)
	{
		liftDriveTarget = inches;
		liftCurrentlyControlling = true;
	}
	
	@Deprecated
	public void liftDistancePeriodic()
	{
		double liftError = liftEnc.get() - liftDriveTarget ;
		if (liftCurrentlyControlling == true)
		{
			if (Math.abs(liftError) < MAX_ALLOWABLE_ERROR) //If within allowable error
			{
				liftCurrentlyControlling = false; //Stop controlling
				liftController.set(0);
			}
			else
			{
				liftController.set(-P_CONSTANT * liftError + -I_CONSTANT * aggrLiftError);
				aggrLiftError += liftError;
			}
		}
	}
}
