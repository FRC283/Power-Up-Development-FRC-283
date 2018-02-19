package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.DriverStation;
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
	int autoStep = 0;
	enum AutoMode 
	{
		kForwards, kDone, kForwardGear
	};
	Joystick logitech;
	Joystick xbox;
	DriveSubsystem drivetrain;
	LiftSubsystem liftSubsystem;
	PowerDistributionPanel pdp = new PowerDistributionPanel(); //
	private AutoMode aM = AutoMode.kForwards;
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
		switch (aM) //Executes an autonomous based on the values of aM
		{
			case kForwards:
				switch (autoStep) //Determines the phase of the autonomous
				{
					case 0:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 1:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 2:
						//Do nothing, end of this autonomous.
					break;
				}
			break;
			case kForwardGear:
				switch (autoStep) //Determines the phase of the autonomous
				{
					case 0:
						drivetrain.leftDriveDistanceInit(85); //Drive forwards 
						drivetrain.rightDriveDistanceInit(85);
						autoStep++;
					break;
					case 1:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 2:
						//Opens the manibles for the pouch, begins waiting. If it returns true, gear is ejected.
						
							autoStep++; //Gear was ejected, advance to next step
						
					break;
					case 3:
						
						
							autoStep++; //System retracted, advance to next step
						
					break;
					case 4:
						drivetrain.leftDriveDistanceInit(-25); //Drive back - this is so the human player can pull the gear up.
						drivetrain.rightDriveDistanceInit(-25);
						autoStep++;
					break;
					case 5:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 { 
							 autoStep++;//When we reach target, advance step
						 }
					case 6:
						//Finished.
					break;
				}
			break;
			case kDone:
			break;
		}
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
		drivetrain.drive(logitech.getRawAxis(Constants.LEFT_Y), logitech.getRawAxis(Constants.RIGHT_Y),(logitech.getRawAxis(Constants.RIGHT_BUMPER) >= 0.5));
		drivetrain.shiftGear(logitech.getRawButton(Constants.LEFT_BUMPER)); //Successfully shifts gear on initial press
		liftSubsystem.unlockWinch(xbox.getRawButton(Constants.LEFT_STICK_BUTTON) && xbox.getRawButton(Constants.RIGHT_STICK_BUTTON)); //If the passed button is true, activates function, otherwise, does nothing
		liftSubsystem.climb(xbox.getRawAxis(Constants.LEFT_Y));
		liftSubsystem.lift(xbox.getRawAxis(Constants.RIGHT_Y));
		liftSubsystem.intake(xbox.getRawAxis(Constants.RIGHT_TRIGGER) - xbox.getRawAxis(Constants.LEFT_TRIGGER));
		liftSubsystem.grip(xbox.getRawButton(Constants.RIGHT_BUMPER), xbox.getRawButton(Constants.LEFT_BUMPER));
		//SmartDashboard.putBoolean("LEFT", xbox.getRawButton(Constants.LEFT_BUMPER));
		//SmartDashboard.putBoolean("RIGHT", xbox.getRawButton(Constants.RIGHT_BUMPER));
	}
	
	@Override
	public void disabledInit()
	{
		//The arms need to be maintained in the position they are currently in at time of disabled
		liftSubsystem.regrip();
	}
}
