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
		self.__parser = argparse.ArgumentParser(description='Utility to flash bootloader on RZ SBC Board (from U-Boot console).\n', epilog='Example:\n\t./uload_bootloader_flash.py')

		# Add arguments
		# Serial port arguments
		self.__parser.add_argument('--serial_port', default='/dev/ttyUSB0', dest='serialPort', action='store', help='Serial port used to talk to board (defaults to: /dev/ttyUSB0).')
		self.__parser.add_argument('--serial_port_baud', default=115200, dest='baudRate', action='store', type=int, help='Baud rate for serial port (defaults to: 115200).')

		self.__args = self.__parser.parse_args()

	# Setup Serial Port
	def __setupSerialPort(self):
		try:
			self.__serialPort = serial.Serial(port=self.__args.serialPort, baudrate = self.__args.baudRate)
		except:
			die(msg='Unable to open serial port.')

	# Function to write bootloader
	def __writeBootloader(self):
		start_time = time.time()

		# Wait for device to be ready to receive image.
		print('Please power on board. Make sure you changed switches to normal boot mode.')
		self.__serialPort.read_until('Hit any key to stop autoboot:'.encode())
		self.__serialPort.write('\r\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		self.__serialPort.write('\r sf probe \r'.encode())
		self.__serialPort.read_until('MiB'.encode())
		print('sf probe - OK')

		self.__serialPort.write('\rtrue\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		self.__serialPort.write('\rtrue\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		print('sf erase...')
		self.__serialPort.write('\r sf erase 0 100000 \r'.encode())
		self.__serialPort.read_until('OK'.encode())
		print('sf erase - OK')

		self.__serialPort.write('\rtrue\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		self.__serialPort.write('\rtrue\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		print('loading bl2...')
		self.__serialPort.write('\r ext4load mmc 0:2 0x48000000 boot/uload-bootloader/bl2_bp-rzpi.bin \r'.encode())
		self.__serialPort.read_until('MiB'.encode())
		print('load bl2 - OK')

		self.__serialPort.write('true\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		self.__serialPort.write('\rtrue\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		print('writing bl2...')
		self.__serialPort.write('\r sf write 0x48000000 0 $filesize \r'.encode())
		self.__serialPort.read_until('OK'.encode())
		print('write bl2 - OK')

		self.__serialPort.write('\rtrue\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		self.__serialPort.write('\rtrue\r'.encode())
		self.__serialPort.read_until('=>'.encode())

		print('loading fip...')
		self.__serialPort.write('\r ext4load mmc 0:2 0x48000000 boot/uload-bootloader/fip-rzpi.bin \r'.encode())
		self.__serialPort.read_until('MiB'.encode())
		print('load fip - OK')

		print('writing fip...')
		self.__serialPort.write('\r sf write 0x48000000 1d200 $filesize \r'.encode())
		buf = self.__serialPort.read_until('OK'.encode())
		print('write fip - OK')

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
	def __serialRead(self, cond='\n'):
		buf = self.__serialPort.read_until(cond.encode())
		print(f'{buf.decode()}')

# Util function to die with error
def die(msg='', code=1):
	print(f'Error: {msg}')
	exit(code)

def main():
	flashUtil = FlashUtil()

if __name__ == '__main__':
	main()
