package org.firstinspires.ftc.teamcode.autotesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShipHubArmTest")
public class ShippingHubArmAutoTest extends LinearOpMode {

    DcMotor duckMotor;

    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;
    public DcMotor armEncoder;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, 0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        armRotationMotor = hardwareMap.get(DcMotor.class, "rotationMotor");
        armRotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        directionServo = hardwareMap.get(Servo.class, "directionServo");
        armEncoder = hardwareMap.get(DcMotor.class, "armEncoder");


        drive.setPoseEstimate(startPose);

        Trajectory shippingHub = drive.trajectoryBuilder(startPose)
                .strafeTo(new Vector2d(40, 0))
                .build();

        Trajectory shippingHub_2 = drive.trajectoryBuilder(startPose)
                .strafeRight(50)
                .build();

        Trajectory forwardShippingHub = drive.trajectoryBuilder(shippingHub_2.end())
                .forward(27)
                .build();



        Trajectory duck = drive.trajectoryBuilder(shippingHub.end())
                .lineToLinearHeading(new Pose2d(0, 27, Math.toRadians(-111)))
                .build();

        Trajectory duck_2 = drive.trajectoryBuilder(forwardShippingHub.end())
                .strafeRight(40)
                .build();

        Trajectory duck_2back = drive.trajectoryBuilder(duck_2.end())
                .back(50)
                .build();

        Trajectory warehouse = drive.trajectoryBuilder(duck.end())
                .strafeTo(new Vector2d(-20, -52))
                .build();

        Trajectory left = drive.trajectoryBuilder(warehouse.end())
                .strafeLeft(40)
                .build();

        Trajectory park = drive.trajectoryBuilder(left.end())
                .forward(75)
                .build();

        Trajectory right = drive.trajectoryBuilder(park.end())
                .strafeRight(30)
                .build();


        drive.followTrajectory(shippingHub_2);
        drive.followTrajectory(forwardShippingHub);


        boolean done = false;
        directionServo.setPosition(0.9);
        double targetPosition = 2750;
        boolean recheck = false;
        intakeMotor.setPower(0.2);
        while(!done){
            telemetry.addData("armPos", armEncoder.getCurrentPosition());
            if(armEncoder.getCurrentPosition() < targetPosition+20 && armEncoder.getCurrentPosition() > targetPosition-20){
                if(recheck == true){
                    armRotationMotor.setPower(0);
                    done = true;
                }else{
                    armRotationMotor.setPower(0);
                    sleep(500);
                    recheck = true;
                }
            }
            else {
                if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() > 600){
                    armRotationMotor.setPower(-0.3);
                }else if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() < 600){
                    armRotationMotor.setPower(-0.25);
                }else if(armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition()-targetPosition > 600){
                    armRotationMotor.setPower(0.3);
                }else{
                    armRotationMotor.setPower(0.25);
                }
            }
            telemetry.update();
        }

/*
        directionServo.setPosition(0.9);
        double targetPosition = 2380;
        intakeMotor.setPower(0.2);
        while(armEncoder.getCurrentPosition() > targetPosition+40 || armEncoder.getCurrentPosition() < targetPosition-40){
            if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() > 600){
                armRotationMotor.setPower(-0.3);
            }else if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() < 600){
                armRotationMotor.setPower(-0.2);
            }else if(armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition()-targetPosition > 600){
                armRotationMotor.setPower(0.3);
            }else{
                armRotationMotor.setPower(0.2);
            }
        }
        armRotationMotor.setPower(0);
        sleep(500);
        while(armEncoder.getCurrentPosition() > targetPosition+40 || armEncoder.getCurrentPosition() < targetPosition-40){
            if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() > 600){
                armRotationMotor.setPower(-0.1);
            }else if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() < 600){
                armRotationMotor.setPower(-0.1);
            }else if(armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition()-targetPosition > 600){
                armRotationMotor.setPower(0.1);
            }else{
                armRotationMotor.setPower(0.1);
            }
        }
        armRotationMotor.setPower(0);
        intakeMotor.setPower(0);
 */

        //drive.followTrajectory(shippingHub);
        //Alternative below

        drive.turn(Math.toRadians(-215));

        intakeMotor.setPower(-1);
        sleep(2500);
        intakeMotor.setPower(0);

        directionServo.setPosition(1);
        while(armEncoder.getCurrentPosition() > 0){
            armRotationMotor.setPower(0.4);
        }
        armRotationMotor.setPower(0);

        //drive.followTrajectory(duck);
        drive.turn(Math.toRadians(111));
        drive.followTrajectory(duck_2);
        drive.followTrajectory(duck_2back);
        duckMotor.setPower(.7);
        sleep(2000);
        duckMotor.setPower(0);
        drive.followTrajectory(warehouse);
        drive.followTrajectory(left);
        drive.followTrajectory(park);
        drive.followTrajectory(right);
    }
}
