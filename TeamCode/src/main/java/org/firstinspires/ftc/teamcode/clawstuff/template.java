package org.firstinspires.ftc.teamcode.clawstuff;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class template extends LinearOpMode {

    static final double COUNTS_PER_MOTOR_REV = 1992.6;
    static final double GEAR_CHANGE = 6.0;

    public DcMotor armRotationMotor;
    public DcMotor intakeMotor;
    public Servo directionServo;
    public boolean manual = false;
    public double rotationPower = 0;

    public void initialize() {
        telemetry.addData("Status", "Initializing Arm Motors");
        telemetry.update();
        armRotationMotor = hardwareMap.get(DcMotor.class, "rotationMotor");

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
                rotationPower = 0;
            }
            return false;
        }
    }

    public void toPickupPosition() {
        moveThings(0);
        directionServo.setPosition(0.66);
    }

    public void toBottomLevel() {
        double targetAngle = 170;
        moveThings(targetAngle);
        directionServo.setPosition(0.3);
    }

    public void toMiddleLevel() {
        double targetAngle = 140;
        moveThings(targetAngle);
        directionServo.setPosition(0.33);
    }

    public void toTopLevel() {
        double targetAngle = 110;
        moveThings(targetAngle);
        directionServo.setPosition(0.51);
    }

    public void controlMotorPower(){
        double position = armRotationMotor.getCurrentPosition()*360/(COUNTS_PER_MOTOR_REV*GEAR_CHANGE);
        telemetry.addData("rotationPos", position);
        if(position <= 40 && position >= 0 && rotationPower>0){
            armRotationMotor.setPower(rotationPower*0.6);
        }else if(position <=70 && position>40 && rotationPower>0){
            armRotationMotor.setPower(rotationPower*0.5);
        }else if(position <=110 && position>70 && rotationPower>0){
            armRotationMotor.setPower(rotationPower*0.4);
        }else if(position <=140 && position>110 && rotationPower>0){
            armRotationMotor.setPower(rotationPower*0.35);
        }else if(position <= 180 && position>140 && rotationPower>0) {
            armRotationMotor.setPower(rotationPower * 0.3);
        }else if(position <= 40 && position >= 0 && rotationPower<0){
            armRotationMotor.setPower(rotationPower*0.3);
        }else if(position <=70 && position>40 && rotationPower<0){
            armRotationMotor.setPower(rotationPower*0.35);
        }else if(position <=110 && position>70 && rotationPower<0){
            armRotationMotor.setPower(rotationPower*0.4);
        }else if(position <=140 && position>110 && rotationPower<0){
            armRotationMotor.setPower(rotationPower*0.5);
        }else if(position <= 180 && position>140 && rotationPower<0){
            armRotationMotor.setPower(rotationPower*0.6);
        }else{
            armRotationMotor.setPower(rotationPower*0.5);
        }
    }
    private void moveThings(double targetAngle) {
        int targetPosition = (int) (targetAngle * COUNTS_PER_MOTOR_REV * GEAR_CHANGE) / 360;
        armRotationMotor.setTargetPosition(targetPosition);
        armRotationMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if(armRotationMotor.getCurrentPosition() > armRotationMotor.getTargetPosition()){
            rotationPower = -1;
        }else{
            rotationPower = 1;
        }
    }
}