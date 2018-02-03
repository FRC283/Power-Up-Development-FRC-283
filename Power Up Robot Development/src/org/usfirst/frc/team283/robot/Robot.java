package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{
	Joystick logitech;
	Joystick xbox;
	DriveSubsystem drivetrain;
	LiftSubsystem liftSubsystem;
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	Solenoid test;
	
	@Override
	public void robotInit() 
	{
		drivetrain = new DriveSubsystem();
		liftSubsystem = new LiftSubsystem();
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
		SmartDashboard.putNumber("Voltage", pdp.getVoltage());
		drivetrain.drive(logitech.getRawAxis(Constants.LEFT_Y), logitech.getRawAxis(Constants.RIGHT_Y),(logitech.getRawAxis(Constants.RIGHT_TRIGGER) >= 0.5),logitech.getRawButton(Constants.LEFT_BUMPER));
	}
}
