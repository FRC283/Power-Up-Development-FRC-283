package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot 
{
	Joystick logitech;
	Joystick xbox;
	DriveSubsystem drivetrain;
	@Override
	public void robotInit() 
	{
		drivetrain = new DriveSubsystem();
		logitech = new Joystick(Constants.DRIVER_CONTROLLER_PORT);
		xbox = new Joystick(Constants.OPERATOR_CONTROLLER_PORT);
	}
	@Override
	public void autonomousInit()
	{
		
	}
	@Override
	public void autonomousPeriodic() 
	{
		
	}
	@Override
	public void teleopInit()
	{
		
	}
	@Override
	public void teleopPeriodic()
	{
		drivetrain.drive(logitech.getRawAxis(Constants.LEFT_Y), logitech.getRawAxis(Constants.RIGHT_Y),(logitech.getRawAxis(Constants.RIGHT_TRIGGER) >= 0.5));
	}
}
