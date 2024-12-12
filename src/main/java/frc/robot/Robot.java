// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final TalonFX m_frontLeftDriveMotor = new TalonFX(23);
  private final TalonFX m_frontRightDriveMotor = new TalonFX(21);
  private final TalonFX m_backLeftDriveMotor = new TalonFX(17);
  private final TalonFX m_backRightDriveMotor = new TalonFX(19);

  private final VoltageOut m_output = new VoltageOut(0).withEnableFOC(false);
  private double m_voltage = 0;

  private final PS4Controller m_joystick = new PS4Controller(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("You are now running the slip current test. The robot should already be touching a wall.");
    System.out.println("If the wheels are not facing the wall, either run a SwerveRequest.PointWheelsAt from a different program, or turn off the robot and manually align the wheels.");
    System.out.println("Connect to the Phoenix Diagnostics server with AdvantageScope, and graph all drive motor velocities and stator currents.");
    System.out.println("Then hold the cross button on the PS4 Controller until at least one velocity becomes non-zero and at least one stator current drops.");
    System.out.println("This is your slip current limit.");
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {
    // Slip current test
    if (m_joystick.getCrossButton()) {
      m_voltage += 0.01;
      m_frontLeftDriveMotor.setControl(m_output.withOutput(m_voltage));
      m_frontRightDriveMotor.setControl(m_output.withOutput(m_voltage));
      m_backLeftDriveMotor.setControl(m_output.withOutput(m_voltage));
      m_backRightDriveMotor.setControl(m_output.withOutput(m_voltage));
    }

    if (m_joystick.getCrossButtonReleased()) {
      m_frontLeftDriveMotor.stopMotor();
      m_frontRightDriveMotor.stopMotor();
      m_backLeftDriveMotor.stopMotor();
      m_backRightDriveMotor.stopMotor();
      m_voltage = 0;
    }
  }

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
