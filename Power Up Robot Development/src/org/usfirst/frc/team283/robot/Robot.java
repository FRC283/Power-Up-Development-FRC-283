package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{
	/* TODO:
	 * DriveDistance function for autonomous
	 * LiftDistance function for autonomous
	 *
	 * When hooks are released, make the wench unwind with the lift
	 */
	
	Joystick logitech;
	Joystick xbox;
	DriveSubsystem drivetrain;
	LiftSubsystem liftSubsystem;
	PowerDistributionPanel pdp = new PowerDistributionPanel(); //
	
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
		drivetrain.periodic();
		liftSubsystem.periodic();
	}
	
	@Override
	public void teleopInit()
	{
		
	}
	
	@Override
	public void teleopPeriodic()
	{
		drivetrain.periodic();
		liftSubsystem.periodic();
		SmartDashboard.putNumber("Voltage", pdp.getVoltage());
		drivetrain.drive(logitech.getRawAxis(Constants.LEFT_Y), logitech.getRawAxis(Constants.RIGHT_Y),(logitech.getRawAxis(Constants.RIGHT_TRIGGER) >= 0.5));
		drivetrain.shiftGear(logitech.getRawButton(Constants.LEFT_BUMPER)); //Successfully shifts gear on initial press
		liftSubsystem.deployHooks(xbox.getRawButton(Constants.LEFT_STICK_BUTTON) && xbox.getRawButton(Constants.X)); //If the passed button is true, activates function, otherwise, does nothing
		liftSubsystem.climb(xbox.getRawAxis(Constants.LEFT_Y));
		liftSubsystem.lift(xbox.getRawAxis(Constants.RIGHT_Y));
		liftSubsystem.intake(xbox.getRawAxis(Constants.RIGHT_TRIGGER) - xbox.getRawAxis(Constants.LEFT_TRIGGER));
		liftSubsystem.grip(xbox.getRawButton(Constants.RIGHT_BUMPER), xbox.getRawButton(Constants.LEFT_BUMPER));
		//SmartDashboard.putBoolean("LEFT", xbox.getRawButton(Constants.LEFT_BUMPER));
		//SmartDashboard.putBoolean("RIGHT", xbox.getRawButton(Constants.RIGHT_BUMPER));
	}
}
