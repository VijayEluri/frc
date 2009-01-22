#include "DriveTrain.h"

DriveTrain::DriveTrain()
{
	motor_left = new Victor(1);
	invert_left = false;
	motor_right = new Jaguar(2);
	invert_right = true;
	
	timer = new Timer();
	timer->Start();
	encoder_left = new Encoder(4,2,4,3);
	encoder_right = encoder_left;
	gyro = new Gyro(2);
	accel = new Accelerometer(1);
}

//	Lets you specify a float directly instead of being stuck with Y Axiss
void DriveTrain::SetMotors(float left, float right)
{
	if (invert_left){
		left *= -1;
	}
	if (invert_right){
		right *= -1;
	}
	motor_left->Set(left);
	motor_right->Set(right);
}

//	TankDrive (uses Y axis)
void DriveTrain::TankDrive(Joystick *left, Joystick *right)
{
	float l = left->GetY();
	float r = right->GetY();
	SetMotors(l, r);
}

bool DriveTrain::GoshasCode(){
	bool atspeed = false;
	                /* We have to set some initial values */
			if(slip.time[0] == 0){
				slip.time[0] = timer->Get();
				slip.displ_l[0] = encoder_left->GetDistance();
				slip.displ_r[0] = encoder_right->GetDistance();
			}
			else if(slip.time[1] == 0){
				slip.time[1]= timer->Get();
				slip.displ_l[1] = encoder_left->GetDistance();
				slip.displ_r[1] = encoder_right->GetDistance();
			}
			/* after here, we're doing it for real */
			else {
				slip.time[2]= timer->Get();
				slip.displ_l[2] = encoder_left->GetDistance();
				slip.displ_r[2] = encoder_right->GetDistance();

			/*slip.accel_l =
				((slip.displ_l[2] - slip.displ_l[1])
				* (slip.time[1] - slip.time[0])
				- (slip.displ_l[1] - slip.displ_l[0])
				* (slip.time[2] - slip.time[1]))
				/ ((slip.time[2] - slip.time[1])
				* (slip.time[2] - slip.time[1])
				* (slip.time[1] - slip.time[0]))*/
			  //let's restate that
				//ok suppose we have times i < m < f and s = diplacement, v = velocity, a = acceleration
				slip.accel_l/*a_f*/ = ((((slip.displ_l[2]/*s_f*/ - slip.displ_l[1]/*s_m*/) / (slip.time[2]/*f*/ - slip.time[1]/*m*/))/*v_f*/
							- ((slip.displ_l[1]/*s_m*/ - slip.displ_l[0]/*s_i*/) / (slip.time[1]/*m*/ - slip.time[0]/*i*/))/*v_m*/)
						       / (slip.time[1]/*f*/ - slip.time[0]/*m*/));

			/*slip.accel_r = 
				((slip.displ_r[2] - slip.displ_r[1])
				* (slip.time[1] - slip.time[0])
				- (slip.displ_r[1] - slip.displ_r[0])
				* (slip.time[2] - slip.time[1]))
				/ ((slip.time[2] - slip.time[1])
				* (slip.time[2] - slip.time[1])
				* (slip.time[1] - slip.time[0]));*/
			//let's restate that
				//ok suppose we have times i < m < f and s = diplacement, v = velocity, a = acceleration
                                slip.accel_r/*a_f*/ = ((((slip.displ_r[2]/*s_f*/ - slip.displ_r[1]/*s_m*/) / (slip.time[2]/*f*/ - slip.time[1]/*m*/))/*v_f*/
							- ((slip.displ_r[1]/*s_m*/ - slip.displ_r[0]/*s_i*/) / (slip.time[1]/*m*/ - slip.time[0]/*i*/))/*v_m*/)
                                                       / (slip.time[1]/*f*/ - slip.time[0]/*m*/));
				/*let's push our values down by one section of time*/
				slip.displ_r[0] = slip.displ_r[1];
				slip.displ_r[1] = slip.displ_r[2];
				slip.displ_l[0] = slip.displ_l[1];
				slip.displ_l[1] = slip.displ_l[2];
				/*likewise, let's push down our times so that they correspond with the values*/
				slip.time[0] = slip.time[1];
				slip.time[1] = slip.time[2];
			
			/*
			if((slip.accel_r + slip.accel_l
				- 2 * accel->GetAcceleration()
				* (double)980 >= OK_DIFFERENCE) ||
				(slip.accel_r + slip.accel_l
							- 2 * accel->GetAcceleration()
							* (double)980 <= -OK_DIFFERENCE)){
					printf("OH TEH NOES WE ARE SLIPPING\n");
			}
			else {
				printf("\t\t\tNO SLIPPING\n");
			}*/
			printf("%f\n", slip.accel_r + slip.accel_l
				- 2 * accel->GetAcceleration()
				* 980.0);
			}
	return atspeed;
	
}

