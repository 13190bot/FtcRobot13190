package org.firstinspires.ftc.teamcode.drive.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "FinalBackRedStartToDuck")
public class FinalBackRedStartToDuck extends LinearOpMode {
    DcMotor duckMotor;
    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        duckMotor = hardwareMap.get(DcMotor.class, "duckMotor");

        drive.setPoseEstimate(startPose);

        Trajectory traj1 = drive.trajectoryBuilder(startPose)
                .strafeLeft(20)
                .build();

        Trajectory traj2 = drive.trajectoryBuilder(traj1.end())
                .forward(20)
                .build();

        Trajectory traj3 = drive.trajectoryBuilder(traj2.end())
                .back(26)
                .build();

        Trajectory traj4 = drive.trajectoryBuilder(traj3.end())
                .forward(27)
                .build();

        Trajectory traj5 = drive.trajectoryBuilder(traj4.end())
                .strafeLeft(35)
                .build();

        Trajectory traj6 = drive.trajectoryBuilder(traj5.end())
                .forward(19)
                .build();



        waitForStart();

        if (isStopRequested()) return;

        drive.followTrajectory(traj1);
        drive.followTrajectory(traj2);
        drive.turn(Math.toRadians(-90));
        drive.followTrajectory(traj3);
        duckMotor.setPower(0.5);
        sleep(4000);
        duckMotor.setPower(0);
        drive.followTrajectory(traj4);
        drive.turn(Math.toRadians(83));




        drive.followTrajectory(traj5);
        drive.followTrajectory(traj6);


    }
}
