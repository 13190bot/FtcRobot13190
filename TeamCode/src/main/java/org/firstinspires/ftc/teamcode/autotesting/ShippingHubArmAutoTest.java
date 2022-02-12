package org.firstinspires.ftc.teamcode.autotesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "ShipHubArmTest")
public class ShippingHubArmAutoTest extends LinearOpMode {

    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;
    public DcMotor armEncoder;

    @Override
    public void runOpMode() throws InterruptedException {

        Pose2d startPose = new Pose2d(0, 0, 0);

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        armRotationMotor = hardwareMap.get(DcMotor.class, "rotationMotor");
        armRotationMotor.setDirection(DcMotor.Direction.REVERSE);
        armRotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        directionServo = hardwareMap.get(Servo.class, "directionServo");
        armEncoder = hardwareMap.get(DcMotor.class, "armEncoder");


        drive.setPoseEstimate(startPose);

        Trajectory shippingHub = drive.trajectoryBuilder(startPose)
                .lineToLinearHeading(new Pose2d(40, 0, 110))
                .build();

        drive.followTrajectory(shippingHub);

        directionServo.setPosition(0.9);
        double targetPosition = 2400;
        boolean recheck = false;
        while(armEncoder.getCurrentPosition() > targetPosition+20 || armEncoder.getCurrentPosition() < targetPosition-20){
            if(recheck == true){
                armRotationMotor.setPower(0);
                recheck = false;
            }else{
                armRotationMotor.setPower(0);
                sleep(500);
                recheck = true;
            }
        }
        else{
            if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() > 400){
                armRotationMotor.setPower(-0.75);
            }else if(armEncoder.getCurrentPosition() < targetPosition && targetPosition-armEncoder.getCurrentPosition() < 400){
                armRotationMotor.setPower(-0.25);
            }else if(armEncoder.getCurrentPosition() > targetPosition && armEncoder.getCurrentPosition()-targetPosition > 400){
                armRotationMotor.setPower(0.75);
            }else{
                armRotationMotor.setPower(0.25);
            }
        }
        armRotationMotor.setPower(0.5);
        while(armRotationMotor.isBusy()){
            continue;
        }
        armRotationMotor.setPower(0);
        intakeMotor.setPower(-1);
    }
}
