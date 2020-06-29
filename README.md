# home-automation

Home Automation is building automation for a home, called a smart home or smart
house.
Here we are going to make automation in RETRO MODE. A home automation system will
control lighting, climate, entertainment systems, and appliances. In our project, we are
going to control lighting and anything which can be controlled by the switch.
It may also include home security such as access control and alarm systems. So we are
going to build up mechanisms for both control lighting and security.
Here we are having two different circuits to perform two tasks (Home Automation and
Security). In Home Automation, we are having Bluetooth module attached to our
Arduino which is responsible for triggering the mechanical switch through mobile. This
Bluetooth module takes the different type of signal and then give those signals to a
microcontroller for computing. After getting digital signals, the microcontroller(Arduino)
process the information and then it gives commands to motors to make the electronic
switch in state ON/OFF.
In Home Security we connect a switch inside door well. When anyone presses the
doorbell, the button is also get pressed and accordingly it triggers the microcontroller to
send the notification to the owner of that house by using Bluetooth module. Here a
physical interface model is also connected to this device which is having a camera
attached on it. So when someone presses this device's button, it makes this camera
attached on physical interface model to ON state and sent pics to user’s mobile using
Bluetooth module. It will automatically stop after 10 seconds. The user can also make a
trigger by giving a control signal by the mobile application which will be received by the
device through the Bluetooth module. In Home Security, we are having a proximity
sensor which is detecting whether a window is open or not and sending information to
the mobile application by Bluetooth module
