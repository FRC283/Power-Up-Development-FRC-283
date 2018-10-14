package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot 
{
	enum AutoMode 				//Your choice of automode
	{
		AUTO_LINE, //Drives forward to the autoline
		LEFT_TO_GOAL, //Starting on the left, drive to the proper switch side and score
		RIGHT_TO_GOAL, //Starting on the right, drive to the proper switch side and score
		STOP //Does nothing
	};
	Joystick logitech;										   //
	Joystick xbox;											   //
	PhantomDriver pDriver;
	Timer autoTimer;										   //Used for various timing tasks
	DriveSubsystem drivetrain;								   //
	LiftSubsystem liftSubsystem;							   //
	PowerDistributionPanel pdp = new PowerDistributionPanel(); //
	String gameData;										   //Contains the data about the switch/scale colors given by FMS
	boolean previousYState;
	/** The number that is added to in order to change which route is recorded to */
	int recordingIndex; 
	
	//***THIS IS WHERE YOU CHANGE THE AUTONOMOUS VALUE FOR NOW****
	private AutoMode aM = AutoMode.AUTO_LINE;				   //The actual chosen value of our autonomous
	
	@Override
	public void robotInit() 
	{
		previousYState = false;
		recordingIndex = 0;
		//Set this to true if you just want to do the move forwards autonomous
		drivetrain = new DriveSubsystem();
		liftSubsystem = new LiftSubsystem();
		autoTimer = new Timer();
		logitech = new Joystick(Constants.DRIVER_CONTROLLER_PORT);
		xbox = new Joystick(Constants.OPERATOR_CONTROLLER_PORT);
		pDriver = new PhantomDriver(logitech, xbox);
		pDriver.createRoute("auto line", "guillotine", "autonomous for AUTO_LINE scenario");
		pDriver.createRoute("left to left", "guillotine", "autonomous for LEFT_TO_GOAL scenario, where goal is left");
		pDriver.createRoute("left to right", "guillotine", "autonomous for LEFT_TO_GOAL scenario, where goal is right");
		pDriver.createRoute("right to left", "guillotine", "autonomous for RIGHT_TO_GOAL scenario, where goal is left");
		pDriver.createRoute("right to right", "guillotine", "autonomous for RIGHT_TO_GOAL scenario, where goal is right");
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}
	
	@Override
	public void autonomousInit()
	{
		//If this is set to true, run no autonomous
		boolean stop = false; 
		
		//R if our team has the switch on the right, L if we have it on the left
		char switchSide = '?';
		
		//Grab game length
		if (gameData.length() > 0)
		{
			switchSide = gameData.toLowerCase().charAt(0);
		}
		
		switch (aM)
		{
			case AUTO_LINE:
				pDriver.setActiveRoute("guillotine_auto_line");
			break;
			case LEFT_TO_GOAL:
				if (switchSide == 'l')
				{
					pDriver.setActiveRoute("guillotine_left_to_left");
				}
				else if (switchSide == 'r')
				{
					pDriver.setActiveRoute("guillotine_left_to_right");
				}
				else
				{
					//Default to autoline
					pDriver.setActiveRoute("guillotine_auto_line");
				}
			break;
			case RIGHT_TO_GOAL:
				if (switchSide == 'l')
				{
					pDriver.setActiveRoute("guillotine_right_to_left");
				}
				else if (switchSide == 'r')
				{
					pDriver.setActiveRoute("guillotine_right_to_right");	
				}
				else
				{
					//Default to autoline
					pDriver.setActiveRoute("guillotine_auto_line");
				}
			break;
			case STOP:
				stop = true;
			break;
			default:
				stop = true;
			break;
		}
		
		//Start the route
		if (!stop)
			pDriver.playbackInit();
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void autonomousPeriodic() 
	{
		drivetrain.drive(pDriver.getRawAxis(0, Constants.LEFT_Y), pDriver.getRawAxis(0, Constants.RIGHT_Y), pDriver.getRawButton(0, Constants.RIGHT_BUMPER));
		drivetrain.shiftGear(pDriver.getRawButton(0, Constants.RIGHT_STICK_BUTTON)); //Shifts the gearing to the
		liftSubsystem.intake(pDriver.getRawAxis(0, Constants.LEFT_TRIGGER) - pDriver.getRawAxis(0, Constants.RIGHT_TRIGGER));
		liftSubsystem.grip(pDriver.getRawButton(0, Constants.LEFT_BUMPER));
		liftSubsystem.unlockWinch(pDriver.getRawButton(1, Constants.LEFT_STICK_BUTTON) && pDriver.getRawButton(1, Constants.RIGHT_STICK_BUTTON)); //If the passed button is true, activates function, otherwise, does nothing
		liftSubsystem.climb(pDriver.getRawAxis(1, Constants.LEFT_Y));
		liftSubsystem.lift(pDriver.getRawAxis(1, Constants.RIGHT_Y));
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
		drivetrain.drive(logitech.getRawAxis(Constants.LEFT_Y), logitech.getRawAxis(Constants.RIGHT_Y), logitech.getRawButton(Constants.RIGHT_BUMPER));
		drivetrain.shiftGear(logitech.getRawButton(Constants.RIGHT_STICK_BUTTON)); //Shifts the gearing to the
		liftSubsystem.intake(logitech.getRawAxis(Constants.LEFT_TRIGGER) - logitech.getRawAxis(Constants.RIGHT_TRIGGER));
		liftSubsystem.grip(logitech.getRawButton(Constants.LEFT_BUMPER));
		liftSubsystem.unlockWinch(xbox.getRawButton(Constants.LEFT_STICK_BUTTON) && xbox.getRawButton(Constants.RIGHT_STICK_BUTTON)); //If the passed button is true, activates function, otherwise, does nothing
		liftSubsystem.climb(xbox.getRawAxis(Constants.LEFT_Y));
		liftSubsystem.lift(xbox.getRawAxis(Constants.RIGHT_Y));
		
		//Cycle through selected route
		if (xbox.getRawButton(Constants.Y) && !previousYState)
		{
			recordingIndex++;
			
			//Mod operator creates cycling. If you make more routes, change the number off of 5
			int routeNumber = recordingIndex % 5;
			switch (routeNumber)
			{
				case 0:
					System.out.println("Active route now set to: guillotine_auto_line");
					pDriver.setActiveRoute("guillotine_auto_line");
				break;
				case 1:
					System.out.println("Active route now set to: guillotine_left_to_left");
					pDriver.setActiveRoute("guillotine_left_to_left");
				break;
				case 2:
					System.out.println("Active route now set to: guillotine_left_to_right");
					pDriver.setActiveRoute("guillotine_left_to_right");
				break;
				case 3:
					System.out.println("Active route now set to: guillotine_right_to_left");
					pDriver.setActiveRoute("guillotine_right_to_left");
				break;
				case 4:
					System.out.println("Active route now set to: right_to_right");
					pDriver.setActiveRoute("guillotine_right_to_right");
				break;
			}
		}
		
		//Print overviews
		if (xbox.getRawButton(Constants.A))
		{
			pDriver.printAllOverviews();
		}
		
		//Start recording
		if (xbox.getRawButton(Constants.START))
		{
			System.out.println("Recoring has begun.");
			pDriver.recordInit();
		}
		
		//Stop recording
		if (xbox.getRawButton(Constants.BACK))
		{
			System.out.println("Recording has stopped.");
			pDriver.recordStop();
		}
		
		previousYState = xbox.getRawButton(Constants.Y);
	}
}
