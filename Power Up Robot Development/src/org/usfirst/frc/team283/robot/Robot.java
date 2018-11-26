package org.usfirst.frc.team283.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.*;

public class Robot extends IterativeRobot 
{
	//TODO: Signage Chart
	int autoStep = 0;			//Phase of the automode (used to divide modes into steps)		
	enum AutoMode 				//Your choice of automode
	{
		kSimpleForwards, 		//Uses timer logic to move past the base line
		kForwards, 				//Uses distance logic to move past baseline
		kLeftRightLeft, 		//Drives forward, turns left, forward, right, forward, turn 90 degrees right, drop cube in switch
		kRightLeftRight, 		//Drives forward, turns right, forward, left, forward, turn 90 degrees left, drop cube in switch
		kAllLeft, 				//Drives forward, turns left, forward, right, forward, turn 90 degrees right, drop cube in switch
		kAllRight, 				//Drives forward, turns right, forward, left, forward, turn 90 degrees left, drop cube in switch
		kAlwaysRight,			//Schnaar's Plan + Cube Drop: Right Side
		kAlwaysLeft,			//Schnaar's Plan + Cube Drop: Left Side
		kAutoQuest,				//Start in center. Turn left or right to get the cube in the switch
		kStop,					//Does nothing
		kDS1ForLoop
	};
	int[] DS1Array = new int[] {1};
	Joystick logitech;										   //
	Joystick xbox;											   //
	Timer autoTimer;										   //Used for various timing tasks
	DriveSubsystem drivetrain;								   //
	LiftSubsystem liftSubsystem;							   //
	MecanumDrive mecDrivetrain;
	PowerDistributionPanel pdp = new PowerDistributionPanel(); //
	private AutoMode aM = AutoMode.kAutoQuest;				   //The actual chosen value of our autonomous
	String gameData;										   //Contains the data about the switch/scale colors given by FMS
	boolean mecanumActive;
	@Override
	public void robotInit() 
	{
		mecanumActive = false;
		//Set this to true if you just want to do the move forwards autonomous
		drivetrain = new DriveSubsystem();
		liftSubsystem = new LiftSubsystem();
		mecDrivetrain = new MecanumDrive(drivetrain.leftFront, drivetrain.leftBack, drivetrain.rightFront, drivetrain.rightBack);
		autoTimer = new Timer();
		logitech = new Joystick(Constants.DRIVER_CONTROLLER_PORT);
		xbox = new Joystick(Constants.OPERATOR_CONTROLLER_PORT);
		gameData = DriverStation.getInstance().getGameSpecificMessage();
	}
	
	@Override
	public void autonomousInit()
	{
		
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void autonomousPeriodic() 
	{
		/*
		if(gameData.length() > 0)
		{
			//Since the FMS returns YOUR field placement (as in alliance color setup) the value returned will be for your sides of the switch and scale from your perspective
			if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'R')			//If FMS returns 'LRL' - We ignore last character
			{
				aM = AutoMode.kLeftRightLeft; 	//Set auto to 'LRL'
			}
			else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'L')		//If FMS returns 'RLR' - We ignore last character
			{
				aM = AutoMode.kRightLeftRight;	//Set auto to 'RLR'
			}
			else if(gameData.charAt(0) == 'L' && gameData.charAt(1) == 'L') 	//If FMS returns 'LLL' - We ignore last character
			{
				aM = AutoMode.kAllLeft;			//Set auto to 'LLL'
			}
			else if(gameData.charAt(0) == 'R' && gameData.charAt(1) == 'R')		//If FMS returns 'RRR' - We ignore last character
			{
				aM = AutoMode.kAllRight;		//Set auto to 'RRR'
			}
		}
		*/
		drivetrain.periodic();
		liftSubsystem.periodic();
		switch (aM) //Executes an autonomous based on the values of aM
		{
			case kSimpleForwards: //Simple timer-based forward movement
				switch (autoStep)
				{
					case 0:
						autoTimer.start(); //Start timer
						autoStep++; //Next phase
					break;
					case 1:
						drivetrain.drive(-0.50, -0.50, false); //Drive forwards at quarter power
						if (autoTimer.get() > 3) //Wait until 3 seconds have passed
						{
							drivetrain.drive(0, 0, false); //Cut drive power
							autoStep++; //Advance to next phase
						}
					break;
					case 2:
						//Do nothing - this auto is done
						autoTimer.stop();
						autoTimer.reset();
					break;
				}
				
			case kDS1ForLoop:
				String[][] DS1L = {
									{"Forward","Left","Forward","Right","Forward"},		//Drive Station 1 Left Auto Values
									{"36",     "90",  "30",     "90",   "30"}
					  			  };
				String[][] DS1R = {
									{"Forward","Left","Forward","Right","Forward"},		//Drive Station 1 Right Auto Values
									{"30",     "90",  "30",     "90",   "30"}
								  };
				String[][] DS2L = {
									{"Forward","Left","Forward","Right","Forward"},		//Drive Station 2 Left Auto Values
									{"30",     "90",  "30",     "90",   "30"}
					  			  };
				String[][] DS2R = {
									{"Forward","Left","Forward","Right","Forward"},		//Drive Station 2 Right Auto Values
									{"30",     "90",  "30",     "90",   "30"}
					  			  };
				String[][] DS3L = {
									{"Forward","Left","Forward","Right","Forward"},		//Drive Station 3 Left Auto Values
									{"30",     "90",  "30",     "90",   "30"}
								  };
				String[][] DS3R = {
									{"Forward","Left","Forward","Right","Forward"},		//Drive Station 3 Right Auto Values
									{"30",     "90",  "30",     "90",   "30"}
								  };
				
				String[][] DS1Array = (gameData.charAt(0) == 'L') ? DS1L.clone() : 
					((gameData.charAt(0) == 'R') ? DS1R.clone() : null); // Reads gameData and clones the movement array accordingly
	
				for(int i = 0;i < DS1Array[0].length;)
				{
					if(drivetrain.driveDistancePeriodic() == true) 		//Wait for condition
					{
						++i;												//Increment only when condition is met
						if(DS1Array[0][i] == "Forward")						//If MoveName = Forward, drive forward for DS1Array[1][i] inches
						{
							drivetrain.EncDrive(Double.valueOf(DS1Array[1][i]));
						}
						else if(DS1Array[0][i] == "Left")					//If MoveName = Left, turn Left for DS1Array[1][i] degrees
						{
							drivetrain.turn(Double.valueOf(DS1Array[1][i]),"left");
						}
						else if(DS1Array[0][i] == "Right")					//If MoveName = Right, turn Right for DS1Array[1][i] degrees
						{
							drivetrain.turn(Double.valueOf(DS1Array[1][i]),"right");						
						}
					}
				}
			case kAlwaysLeft:
				switch(autoStep)
				{
					case 0: //
						autoTimer.stop(); //Safety measures to ensure timer is stopped
						autoTimer.reset();
						//System.out.println("Detected the first character as: " + gameData.charAt(0));
						if (gameData.charAt(0) == 'L' || gameData.charAt(0) == 'R') //Making sure it's not null
						{
							if (gameData.charAt(0) == 'L') //If we're on the side with our team's plate
							{
								System.out.println("Detected left - its what we want");
								autoStep = 0;
								aM = AutoMode.kSimpleForwards; //Switch to a simple forward motion
								//That will collide us into the switch fence and drop the cube in
							}
							else //Otherwise
							{
								drivetrain.drive(-0.8, -0.5, false); //Drive forwards
								autoTimer.start();
								autoStep++;
							}
						}
					break;
					case 1:
						if (autoTimer.get() > 1) //After 1 second of driving forwards...
						{
							System.out.println("Successfully drove forwards");
							autoStep++; //Next step
							drivetrain.drive(0, 0, false); //Stop
							autoTimer.stop();
							autoTimer.reset();
							autoTimer.start();
						}
					break;
					case 2:
						if (autoTimer.get() > 1) //After .5 seconds of being stopped
						{
							System.out.println("Successfully stopped");
							autoStep++; //Next step
							drivetrain.drive(-0.50, -0.25, false); //Begin going forwards again
							autoTimer.stop();
							autoTimer.reset();
							autoTimer.start();
						}
					break;
					case 3:
						if (autoTimer.get() > 4) //After 2 seconds of driving forwards...
						{
							System.out.println("Successfully drove forwards for second time");
							autoStep++; //Next step
							drivetrain.drive(0, 0, false); //Stop
							autoTimer.stop();
							autoTimer.reset();
						}
					break;
					case 4:
						//Do nothing
					break;
				}
			break;
			case kAlwaysRight:
				switch(autoStep)
				{
					case 0: //
						autoTimer.stop(); //Safety measures to ensure timer is stopped
						autoTimer.reset();
						if (gameData.charAt(0) == 'L' || gameData.charAt(0) == 'R') //Making sure it's not null
						{
							if (gameData.charAt(0) == 'R') //If we're on the side with our team's plate
							{
								System.out.println("Detected right - it's what we want");
								autoStep = 0;
								aM = AutoMode.kSimpleForwards; //Switch to a simple forward motion
								//That will collide us into the switch fence and drop the cube in
							}
							else //Otherwise
							{
								drivetrain.drive(-0.5, -0.5, false); //Drive forwards
								autoTimer.start();
								autoStep++;
							}
						}
					break;
					case 1:
						if (autoTimer.get() > 1) //After 1 second of driving forwards...
						{
							System.out.println("Successfully drove forwards");
							autoStep++; //Next step
							drivetrain.drive(0, 0, false); //Stop
							autoTimer.stop();
							autoTimer.reset();
							autoTimer.start();
						}
					break;
					case 2:
						if (autoTimer.get() > 1) //After 1 seconds of being stopped
						{
							System.out.println("Successfully stopped");
							autoStep++; //Next step
							drivetrain.drive(-0.25, -0.25, false); //Begin going forwards again
							autoTimer.stop();
							autoTimer.reset();
							autoTimer.start();
						}
					break;
					case 3:
						if (autoTimer.get() > 4) //After 4 seconds of driving forwards...
						{
							System.out.println("Successfully drove forwards for second time");
							autoStep++; //Next step
							drivetrain.drive(0, 0, false); //Stop
							autoTimer.stop();
							autoTimer.reset();
						}
					break;
					case 4:
						//Do nothing
					break;
				}
			break;
			case kAutoQuest:
				switch(autoStep)
				{
					case 0:
						if (gameData.charAt(0) == 'L' || gameData.charAt(0) == 'R') //Making sure it's not null
						{
							System.out.println("Switch goal is on the " + gameData.charAt(0) + " side.");
							autoStep++;
							if(gameData.charAt(0) == 'L')
							{
								drivetrain.drive(-0.55, -0.70, false);
							}
							else if(gameData.charAt(0) == 'R')
							{
								drivetrain.drive(-0.75, -0.55, false);
							}
							autoTimer.stop();
							autoTimer.reset();
							autoTimer.start();
						}
					break;
					/*case 1: //Keep going forward for 3 seconds the jolt backward
						autoStep++						if(autoTimer.get() > 3)
						{
							drivetrain.drive(0.8, 0.8, false);
						}
						autoTimer.stop();
						autoTimer.reset();
						autoTimer.start();
					break;*/
					case 1: //Jolting backward then stopping to finish Auto
						//System.out.println("Time: " + autoTimer.get());
						if(autoTimer.get() > 1.4)
						{
							drivetrain.drive(0, 0, false);
							autoTimer.stop();
							autoTimer.reset();
						}
					break;
				}
			break;
			case kStop:
				//Do nothing
			break;
			default:
				//Do nothing
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
		//Disabled standard drivetrain.drive in favor of drivetrain.arcadeDrive
		//drivetrain.drive(logitech.getRawAxis(Constants.LEFT_Y), logitech.getRawAxis(Constants.RIGHT_Y), logitech.getRawButton(Constants.RIGHT_BUMPER));
		
		if(!mecanumActive)
			drivetrain.arcadeDrive(logitech.getRawAxis(Constants.LEFT_Y), logitech.getRawAxis(Constants.RIGHT_X), logitech.getRawButton(Constants.RIGHT_BUMPER));
		else
			//Mecanum drivetrain periodic
			mecDrivetrain.driveCartesian(logitech.getRawAxis(Constants.LEFT_Y),logitech.getRawAxis(Constants.LEFT_X), logitech.getRawAxis(Constants.RIGHT_X), 0);
		
		drivetrain.shiftGear(logitech.getRawButton(Constants.RIGHT_STICK_BUTTON)); //Shifts the gearing to the
		liftSubsystem.intake(logitech.getRawAxis(Constants.LEFT_TRIGGER) - logitech.getRawAxis(Constants.RIGHT_TRIGGER));
		liftSubsystem.grip(logitech.getRawButton(Constants.LEFT_BUMPER));
		liftSubsystem.unlockWinch(xbox.getRawButton(Constants.LEFT_STICK_BUTTON) && xbox.getRawButton(Constants.RIGHT_STICK_BUTTON)); //If the passed button is true, activates function, otherwise, does nothing
		liftSubsystem.climb(xbox.getRawAxis(Constants.LEFT_Y));
		liftSubsystem.lift(xbox.getRawAxis(Constants.RIGHT_Y));
		SmartDashboard.putNumber("Logitech Left Y: ", logitech.getRawAxis(Constants.LEFT_Y));
		SmartDashboard.putNumber("Logitech Right Y: ", logitech.getRawAxis(Constants.RIGHT_Y));
		SmartDashboard.putNumber("Xbox Right Y: ", xbox.getRawAxis(Constants.RIGHT_Y));
		SmartDashboard.putNumber("Xbox Right Trigger: ", xbox.getRawAxis(Constants.RIGHT_TRIGGER));
		SmartDashboard.putNumber("Xbox Left Trigger: ", xbox.getRawAxis(Constants.LEFT_TRIGGER));
	}
}
