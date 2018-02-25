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
	boolean autoForward;
	enum AutoMode 
	{
		kForwards, kLeftRightLeft, kRightLeftRight, kAllLeft, kAllRight, kDone
	};
	Joystick logitech;
	Joystick xbox;
	DriveSubsystem drivetrain;
	LiftSubsystem liftSubsystem;
	PowerDistributionPanel pdp = new PowerDistributionPanel(); //
	private AutoMode aM = AutoMode.kForwards;
	String gameData;
	@Override
	public void robotInit() 
	{
		//Set this to true if you just want to do the move forwards autonomous
		autoForward = true;
		drivetrain = new DriveSubsystem();
		liftSubsystem = new LiftSubsystem();
		logitech = new Joystick(Constants.DRIVER_CONTROLLER_PORT);
		xbox = new Joystick(Constants.OPERATOR_CONTROLLER_PORT);
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}
	
	@Override
	public void autonomousInit()
	{
		
	}
	
	@Override
	public void autonomousPeriodic() 
	{
		while(aM == AutoMode.kForwards && autoForward == false)
		{
			if(gameData.length() > 0)
			{
				if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R')
				{
					aM = AutoMode.kLeftRightLeft;
				}
				else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L')
				{
					aM = AutoMode.kRightLeftRight;
				}
				else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L')
				{
					aM = AutoMode.kAllLeft;
				}
				else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R')
				{
					aM = AutoMode.kAllRight;
				}
				else
				{
					aM = AutoMode.kForwards;
				}
			}
		}
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
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 3:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 4:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 5:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
				}
			break;
			case kLeftRightLeft:
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
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 3:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 4:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 5:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
				}
			break;
			case kRightLeftRight:
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
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 3:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 4:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 5:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
				}
			break;
			case kAllLeft:
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
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 3:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 4:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 5:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 6:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 7:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 8:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 9:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 10:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 11:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 12:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 13:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 14:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 15:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 16:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 17:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
					case 18:
						drivetrain.leftDriveDistanceInit(150); //Drive forwards
						drivetrain.rightDriveDistanceInit(150);
						autoStep++;
					break;
					case 19:
						 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
						 {
							 autoStep++; //When we reach target, advance step
						 }
					break;
				}
			break;
			case kAllRight:
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
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 3:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 4:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 5:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 6:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 7:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 8:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 9:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 10:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 11:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 12:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 13:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 14:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 15:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 16:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 17:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
				break;
				case 18:
					drivetrain.leftDriveDistanceInit(150); //Drive forwards
					drivetrain.rightDriveDistanceInit(150);
					autoStep++;
				break;
				case 19:
					 if (drivetrain.periodic() == false) //Wait for the forward motion to finish (When it is false, it is done)
					 {
						 autoStep++; //When we reach target, advance step
					 }
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
		drivetrain.shiftGear(logitech.getRawButton(Constants.LEFT_BUMPER)); //Shifts the gearing to the
		liftSubsystem.unlockWinch(xbox.getRawButton(Constants.LEFT_STICK_BUTTON) && xbox.getRawButton(Constants.RIGHT_STICK_BUTTON)); //If the passed button is true, activates function, otherwise, does nothing
		liftSubsystem.climb(xbox.getRawAxis(Constants.LEFT_Y));
		liftSubsystem.lift(xbox.getRawAxis(Constants.RIGHT_Y));
		liftSubsystem.intake(xbox.getRawAxis(Constants.RIGHT_TRIGGER) - xbox.getRawAxis(Constants.LEFT_TRIGGER));
		liftSubsystem.grip(xbox.getRawButton(Constants.RIGHT_BUMPER), xbox.getRawButton(Constants.LEFT_BUMPER));
		//SmartDashboard.putBoolean("LEFT", xbox.getRawButton(Constants.LEFT_BUMPER));
		//SmartDashboard.putBoolean("RIGHT", xbox.getRawButton(Constants.RIGHT_BUMPER));
	}
}
