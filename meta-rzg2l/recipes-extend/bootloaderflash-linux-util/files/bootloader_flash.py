#!/usr/bin/python3

# Imports
import serial
import argparse
import time
import os
import zipfile
import subprocess
from subprocess import Popen, PIPE, CalledProcessError
from sys import platform
import glob

class FlashUtil:
	def __init__(self):
		self.__scriptDir = os.getcwd()
		self.__setupArgumentParser()

		self.__setupSerialPort()
		self.__writeBootloader()

	# Setup CLI parser
	def __setupArgumentParser(self):
		# Create parser
		self.__parser = argparse.ArgumentParser(description='Utility to flash bootloader on RZ SBC Board.\n', epilog='Example:\n\t./bootloader_flash.py')

		# Add arguments
		# Serial port arguments
		self.__parser.add_argument('--serial_port', default='/dev/ttyUSB0', dest='serialPort', action='store', help='Serial port used to talk to board (defaults to: /dev/ttyUSB0).')
		self.__parser.add_argument('--serial_port_baud', default=115200, dest='baudRate', action='store', type=int, help='Baud rate for serial port (defaults to: 115200).')

		# Images
		self.__parser.add_argument('--image_writer', default=f'{self.__scriptDir}/Flash_Writer_SCIF_rzpi.mot', dest='flashWriterImage', action='store', type=str, help="Path to Flash Writer image (defaults to: <SCRIPT_DIR>/Flash_Writer_SCIF_rzpi.mot).")
		self.__parser.add_argument('--image_bl2', default=f'{self.__scriptDir}/bl2_bp-rzpi.srec', dest='bl2Image', action='store', type=str, help='Path to bl2 image (defaults to: <SCRIPT_DIR>/bl2_bp-rzpi.srec).')
		self.__parser.add_argument('--image_fip', default=f'{self.__scriptDir}/fip-rzpi.srec', dest='fipImage', action='store', type=str, help='Path to FIP image (defaults to: <SCRIPT_DIR>/fip-rzpi.srec).')

		self.__args = self.__parser.parse_args()

	# Setup Serial Port
	def __setupSerialPort(self):
		try:
			self.__serialPort = serial.Serial(port=self.__args.serialPort, baudrate = self.__args.baudRate)
		except:
			die(msg='Unable to open serial port.')

	# Setup Serial Port SUP
	def __setupSerialPort_SUP(self):
		try:
			self.__serialPort = serial.Serial(port=self.__args.serialPort, baudrate = 921600)
		except:
			die(msg='Unable to open serial port 921600 bps.')

	# Function to write bootloader
	def __writeBootloader(self):
		start_time = time.time()

		# Wait for device to be ready to receive image.
		print('Please power on board. Make sure you changed switches to SCIF download mode.')
		self.__serialPort.read_until('please send !'.encode())

		# Write flash writer application
		print('Writing Flash Writer application...')
		self.__writeFileToSerial(self.__args.flashWriterImage)
		print('Write Flash Writer application - OK')

		print('Changing speed to 921600 bps..')
		self.__serialPort.write('\rSUP\r'.encode())
		time.sleep(2)

		self.__setupSerialPort_SUP()
		print('Change speed to 921600 bps - OK')

		# TODO: Wait for '>' instead of just time based.
		time.sleep(1)
		self.__serialPort.write('\rXLS2\r'.encode())

		time.sleep(1)
		self.__serialPort.write('11E00\r'.encode())

		time.sleep(1)
		self.__serialPort.write('\r00000\r'.encode())

		time.sleep(2)
		print('Writing bl2 image...')
		self.__writeFileToSerial(self.__args.bl2Image)
		self.__serialPort.read_until('Clear OK'.encode())
		self.__serialPort.write('\ry\r'.encode())
		self.__serialPort.read_until('>'.encode())
		print('Write bl2 image - OK')

		time.sleep(1)
		self.__serialPort.write('XLS2\r'.encode())

		time.sleep(1)
		self.__serialPort.write('00000\r'.encode())

		time.sleep(1)
		self.__serialPort.write('1D200\r'.encode())

		time.sleep(2)
		print('Writing FIP image...')
		self.__writeFileToSerial(self.__args.fipImage)

		self.__serialPort.read_until('Clear OK'.encode())
		self.__serialPort.write('\ry\r'.encode())
		self.__serialPort.read_until('>'.encode())
		print('Write FIP image - OK')

		print("Closed serial port.")
		self.__serialPort.close()

		end_time = time.time()
		elapsed_time = end_time - start_time
		print(f"Elapsed time: {elapsed_time:.6f} seconds")

	def __writeSerialCmd(self, cmd):
		self.__serialPort.write(f'{cmd}\r'.encode())

	# Function to write file over serial
	def __writeFileToSerial(self, file):
		with open(file, 'rb') as f:
			self.__serialPort.write(f.read())
			f.close()

	# Function to wait and print contents of serial buffer
	def __serialRead(self, cond='\n', print=False):
		buf = self.__serialPort.read_until(cond.encode())

		if print:
			print(f'{buf.decode()}')

# Util function to die with error
def die(msg='', code=1):
	print(f'Error: {msg}')
	exit(code)

def main():
	flashUtil = FlashUtil()

if __name__ == '__main__':
	main()
