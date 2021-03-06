/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.ColorType;
import frc.robot.subsystems.RotateSubsystem;

public class RotationalControl extends CommandBase {

	private RotateSubsystem rotateSubsystem;

	private ColorType startingColor;
	private ColorType lastColor;
	private final double maxRotations = 4;
	private double numRotations = 0;

	private boolean done = false;

	public RotationalControl(RotateSubsystem rotateSubsystem) {
		this.rotateSubsystem = rotateSubsystem;
		addRequirements(rotateSubsystem);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		done = false;
		numRotations = 0;

		startingColor = rotateSubsystem.guessColor(rotateSubsystem.getColor());
		lastColor = ColorType.Unknown;

		if (startingColor == ColorType.Unknown)
			done = true;
		else
			rotateSubsystem.setSpeed(1);
		
		SmartDashboard.putString("Starting color", startingColor.name());
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		/*** Counts the number of rotations based on how many times the robot has seen the start color. ***/
		ColorType currentColor = rotateSubsystem.guessColor(rotateSubsystem.getColor());
		if (numRotations < maxRotations) {
			if (startingColor != lastColor && currentColor == startingColor)
				numRotations += 0.5; // Only increment by 0.5 because a color will be seen twice per rotation
			if (currentColor	 != ColorType.Unknown)
				lastColor = currentColor;
		} else
			done = true;
		
		SmartDashboard.putString("Status", "Rotational control");
		SmartDashboard.putString("Current color", currentColor.name());
		SmartDashboard.putString("Last color", lastColor.name());
		SmartDashboard.putString("# of Rotations", String.valueOf(numRotations));
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		rotateSubsystem.setSpeed(0);
		SmartDashboard.putString("Status", "");
		SmartDashboard.putString("Current color", "Not measuring");
		SmartDashboard.putString("# of Rotations", "Finished");
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return done;
	}
}
