package org.firstinspires.ftc.teamcode.finalauto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShipHubDuckPark")
public class ShipHubDuckPark extends LinearOpMode {



    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor duckMotor;

        Pose2d startPose = new Pose2d(0, 0, 0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        duckMotor = hardwareMap.get(DcMotor.class, "duckMotor");




        drive.setPoseEstimate(startPose);

        Trajectory shippingHub = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(40, -15, Math.toRadians(108)))
                .build();

        Trajectory duck = drive.trajectoryBuilder(shippingHub.end())
                .lineToLinearHeading(new Pose2d(1, 27, Math.toRadians(-108)))
                .build();

        Trajectory warehouse = drive.trajectoryBuilder(duck.end())
                .forward(75)
                .build();

        Trajectory warehouse_2 = drive.trajectoryBuilder(warehouse.end())
                .strafeLeft(35)
                .build();

        Trajectory warehouse_3 = drive.trajectoryBuilder(warehouse_2.end())
                .forward(80)
                .build();



        drive.followTrajectory(shippingHub);
        //Do arm stuff
        drive.followTrajectory(duck);
        duckMotor.setPower(0.5);
        sleep(3000);
        duckMotor.setPower(0);
        drive.followTrajectory(warehouse);
        drive.followTrajectory(warehouse_2);
        drive.followTrajectory(warehouse_3);
    }
}
