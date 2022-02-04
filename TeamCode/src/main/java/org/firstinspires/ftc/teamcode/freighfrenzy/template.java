package org.firstinspires.ftc.teamcode.freighfrenzy;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class template extends LinearOpMode {

    static final double COUNTS_PER_MOTOR_REV = 537.7;
    static final double GEAR_CHANGE = 4.0;

    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;
    public boolean manual = false;

    public void initialize() {
        telemetry.addData("Status", "Initializing Arm Motors");
        telemetry.update();
        armRotationMotor = hardwareMap.get(DcMotor.class, "rotationMotor");
        armRotationMotor.setDirection(DcMotor.Direction.REVERSE);

        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        directionServo = hardwareMap.get(Servo.class, "directionServo");
    }

    public boolean isRotationBusy() {
        if (armRotationMotor.isBusy()) {
            return true;
        } else {
            if (armRotationMotor.getMode() != DcMotor.RunMode.RUN_USING_ENCODER) {
                armRotationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
            if (armRotationMotor.getPower() != 0 && !manual) {
                armRotationMotor.setPower(0);
            }
            return false;
        }
    }

    public void toPickupPosition() {
        armRotationMotor.setTargetPosition(0);
        armRotationMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRotationMotor.setPower(0.5);
        directionServo.setPosition(0.66);
    }

    public void toBottomLevel() {
        double targetAngle = 155;
        moveThings(targetAngle);
        directionServo.setPosition(0.3);
    }

    public void toMiddleLevel() {
        double targetAngle = 130;
        moveThings(targetAngle);
        directionServo.setPosition(0.33);
    }

    public void toTopLevel() {
        double targetAngle = 95;
        moveThings(targetAngle);
        directionServo.setPosition(0.51);
    }

    /*
    public boolean isRotationTooFar(){
        if(armRotationMotor.getCurrentPosition() > 180*COUNTS_PER_MOTOR_REV*GEAR_CHANGE/360 && armRotationMotor.getPower()>0){
            return true;
        }else return false;
    }*/
    private void moveThings(double targetAngle) {
        int targetPosition = (int) (targetAngle * COUNTS_PER_MOTOR_REV * GEAR_CHANGE) / 360;
        armRotationMotor.setTargetPosition(targetPosition);
        armRotationMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRotationMotor.setPower(0.5);
    }
}