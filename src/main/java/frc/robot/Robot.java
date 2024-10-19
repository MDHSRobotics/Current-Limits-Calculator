// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
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
  private final TalonFX m_frontRightDriveMotor = new TalonFX(21);
  private final TalonFX m_frontRightSteerMotor = new TalonFX(20);
  private final VoltageOut m_output = new VoltageOut(0).withEnableFOC(false);
  private double m_voltage = 0;

  private StatusSignal<Double> m_steerPosition = m_frontRightSteerMotor.getPosition();
  private double m_steerStartingPosition;

  private final PS4Controller m_joystick = new PS4Controller(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    BaseStatusSignal.refreshAll(m_steerPosition);
    m_steerStartingPosition = m_steerPosition.getValue();
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
      System.out.println(m_voltage);
      m_frontRightDriveMotor.setControl(m_output.withOutput(m_voltage));
    }

    if (m_joystick.getL3ButtonReleased()) {
      m_frontRightDriveMotor.stopMotor();
      m_voltage = 0;
    }

    // Couple ratio test
    if (m_joystick.getCircleButtonPressed()) {
      m_frontRightSteerMotor.setControl(m_output.withOutput(2));
    }
    m_steerPosition.refresh();
    if (m_joystick.getCircleButtonReleased() || m_steerPosition.getValue() > m_steerStartingPosition * 2) {
      m_frontRightSteerMotor.stopMotor();
    }
  }

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
