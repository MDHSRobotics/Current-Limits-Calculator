// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayList;

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

  private StatusSignal<Double> m_drivePosition = m_frontRightDriveMotor.getPosition();
  private StatusSignal<Double> m_steerPosition = m_frontRightSteerMotor.getPosition();
  private ArrayList<Double> m_coupleRatioReadings = new ArrayList<Double>(100);

  private final PS4Controller m_joystick = new PS4Controller(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    System.out.println("Drive Position: " + m_drivePosition.getValue());
    System.out.println("Steer Position: " + m_steerPosition.getValue());
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

    if (m_joystick.getCircleButton()) {
      BaseStatusSignal.refreshAll(m_drivePosition, m_steerPosition);
      double coupleRatio = m_drivePosition.getValue() / m_steerPosition.getValue();
      m_coupleRatioReadings.add(coupleRatio);
      System.out.println(coupleRatio);
    }

    if (m_joystick.getCircleButtonReleased()) {
      m_frontRightSteerMotor.stopMotor();
      
      int i = 0;
      double mean = 0;
      for (Double coupleRatio : m_coupleRatioReadings) {
        mean += coupleRatio;
        i++;
      }
      mean /= i;
      System.out.println("Mean couple ratio is " + mean);
    }
  }

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
