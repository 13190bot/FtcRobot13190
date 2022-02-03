package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name="FinalBackBlueStartToDuck")
public class FinalBackBlueStartToDuck extends LinearOpMode {
    DcMotor duckMotor;
    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        duckMotor = hardwareMap.get(DcMotor.class, "duckMotor");

        drive.setPoseEstimate(startPose);
        Trajectory strafeRight = drive.trajectoryBuilder(startPose)
                .strafeRight(37)
                .build();
        Trajectory forwardTraj = drive.trajectoryBuilder(strafeRight.end())
                .forward(36)
                .build();
        Trajectory toPark = drive.trajectoryBuilder(forwardTraj.end())
                .strafeRight(18)
                .build();

        drive.followTrajectory(strafeRight);
        duckMotor.setPower(-0.5);
        sleep(4000);
        duckMotor.setPower(0);
        drive.followTrajectory(forwardTraj);
        drive.followTrajectory(toPark);
    }
}
