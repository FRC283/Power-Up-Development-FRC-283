package org.usfirst.frc.team283.robot;

import org.usfirst.frc.team283.robot.Utilities283.Schema;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LiftSubsystem 
{	
	//Constants
	private static final double DEADZONE = 0.1;
	private static final double P_CONSTANT = 1/100;
	private static final double I_CONSTANT = 0;
	/** PI Control continues until the error is below this */	
	private static final double MAX_ALLOWABLE_ERROR = 1;
	//Signage Chart
	/*
	 * 					   |     True     |     False   |
	 * ----------------------------------------------------
	 * Left Hook Solenoid  |  Retracted   |  Extended   |
	 * Right Hook Solenoid |  Retracted   |  Extended   |
	 * --------------------------------------
	 * 					   |  +1  |  -1  |
	 * --------------------------------------
	 * Lift Controller     |   ?  |   ?  |
	 * Winch Controller    |   ?  |   ?  |
	 * Winch Encoder       |   ?  |   ?  |
	 * Lift Encoder        |   ?  |   ?  |
	 * --------------------------------------
	 * 
	 */
	//Variables
	/** Number of inches */
	double liftDriveTarget = 0;
	/** True while PI control is active */
	boolean liftCurrentlyControlling;
	/** accumulation of error on lift */
	double aggrLiftError = 0;
	/** determines if winch is ready to reel in*/
	boolean winchUnlocked = false;
	//Components
	Arm leftArm;
	Arm rightArm;
	VictorSP winch;
	Encoder winchEnc;
	Encoder liftEnc;
	Spark liftController;
	
	public LiftSubsystem()
	{
		liftController = new Spark(Constants.LIFT_CONTROLLER_PORT);
		liftEnc = new Encoder(Constants.LIFT_ENCODER_PORT_A,Constants.LIFT_ENCODER_PORT_B);
		leftArm = new Arm(Constants.LEFT_ARM_CONTROLLER_PORT,Constants.LEFT_ARM_SOLENOID);
		rightArm = new Arm(Constants.RIGHT_ARM_CONTROLLER_PORT,Constants.RIGHT_ARM_SOLENOID);
		winch = new VictorSP(Constants.WINCH_CONTROLLER_PORT);
		winchEnc = new Encoder(Constants.WINCH_ENCODER_PORT_A,Constants.WINCH_ENCODER_PORT_B);
		
	}
	
	public void periodic()
	{
		SmartDashboard.putNumber("Winch Encoder", winchEnc.get());
		SmartDashboard.putNumber("Lift Encoder", liftEnc.get());
		SmartDashboard.putBoolean("Arm Grip State", leftArm.gripSol.get());
	}
	
	/**
	 * Deploys hooks so that the lift may take them up
	 * @param deployHook - deploy hooks
	 */
	@Schema(value = Utilities283.XBOX_X, desc = "ready hooks (+  " + Utilities283.LOGITECH_LEFT_STICK_BUTTON + ")")
	@Schema(value = Utilities283.XBOX_LEFT_STICK_BUTTON, desc = "ready hooks (+ " + Utilities283.XBOX_X + ")")
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
	 * @param liftMagnitude - magnitude of pulley
	 */
	@Schema(Utilities283.XBOX_RIGHT_Y)
	public void lift(double liftMagnitude)
	{
		liftController.set(Utilities283.rescale(DEADZONE, 1.0, 0.0, 1.0, liftMagnitude));
	}
	
	/** 
	 * 
	 * @param rollerMagnitude - magnitude of rollers
	 */
	@Schema(value = Utilities283.XBOX_RIGHT_TRIGGER, desc = "Intake boxes")
	@Schema(value = Utilities283.XBOX_LEFT_TRIGGER, desc = "Expel boxes")
	public void intake(double rollerMagnitude)
	{
		leftArm.intake(rollerMagnitude);
		rightArm.intake(rollerMagnitude * -1);
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
			leftArm.grip(true);
			rightArm.grip(true);			
		}
		
		else if(release == true)
		{
			leftArm.grip(false);
			rightArm.grip(false);			
		}

	}
	public void liftLiftDistance(double inches)
	{
		liftDriveTarget = inches;
		liftCurrentlyControlling = true;
	}
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
