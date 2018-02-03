package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class LiftSubsystem 
{
	Arm leftArm;
	Arm rightArm;
	VictorSP winch;
	Encoder winchEnc;
	Encoder liftEnc;
	Encoder leftArmEnc;
	Encoder rightArmEnc;
	Solenoid leftHook;
	Solenoid rightHook;
	
	public LiftSubsystem()
	{
		
		leftArm = new Arm(Constants.LEFT_ARM_CONTROLLER_PORT,Constants.LEFT_ARM_SOLENOID);
		rightArm = new Arm(Constants.RIGHT_ARM_CONTROLLER_PORT,Constants.RIGHT_ARM_SOLENOID);
		winch = new VictorSP(Constants.WINCH_CONTROLLER_PORT);
		winchEnc = new Encoder(Constants.WINCH_ENCODER_PORT_A,Constants.WINCH_ENCODER_PORT_B);
		leftArmEnc = new Encoder(Constants.LEFT_ARM_ENCODER_PORT_A,Constants.LEFT_ARM_ENCODER_PORT_B);
		rightArmEnc = new Encoder(Constants.RIGHT_ARM_ENCODER_PORT_A,Constants.RIGHT_ARM_ENCODER_PORT_B);
		leftHook = new Solenoid(Constants.LEFT_HOOK_SOLENOID);
		rightHook = new Solenoid(Constants.RIGHT_HOOK_SOLENOID);
		
	}
	public void periodic()
	{
		
	}
	public void deployHooks()
	{
		
	}
	public void climb()
	{
		
	}

	public void lift()
	{
		
	}
}
