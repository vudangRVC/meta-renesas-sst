# RZ SBC board #
This is the quick startup guide for RZ SBC board (hereinafter referred to as `RZpi`).
The below will describe the current status of development, how to build, set up environment for RZpi.

## Status
This is a VLP v3.0.5 release of the RZG2L development product for RZpi.

This release provides the following features:

 - Yocto build compatible with RZG2L SoC (VLP v3.0.5)
 - RZpi Linux BSP functionalities (based on RZG2L BSP v3.0.5-update1)
 - Graphic and Codec libraries supported with QT demo applications.
 - 40 IO expansion interface supported
 - On-board Wireless Modules enabled (only support for Wi-Fi)

## Building 
Step 1: Prepare environment for building package

Linux Ubuntu 20.04 is recommended for Yocto build.
Before starting the build, run the command below on the Linux Host PC to install packages to be used.
```
$ sudo apt-get update
$ sudo apt-get install gawk wget git-core diffstat unzip texinfo gcc-multilib \
build-essential chrpath socat cpio python python3 python3-pip python3-pexpect \
xz-utils debianutils iputils-ping libsdl1.2-dev xterm p7zip-full
```

Run the below commands to set the user name and email address before starting the build procedure.
```
$ git config --global user.email "you@example.com"
$ git config --global user.name "Your Name"
```

Step 2: Prepare the local build environment

After preparing the host machine for building, download necessary packages (get them from Renesas website):
- Graphic: https://www.renesas.com/us/en/document/swo/rz-mpu-graphics-library-evaluation-version-rzg2l-and-rzg2lc-rtk0ef0045z13001zj-v112enzip
- Codec: https://www.renesas.com/us/en/document/swo/rz-mpu-video-codec-library-evaluation-version-rzg2l-rtk0ef0045z15001zj-v110xxzip?r=1535641

Then create a workspace folder (example: `~/Yocto`) for the build and put the downloaded packages and support script `rzsbc_yocto.sh` into it:
```
$ mkdir ~/Yocto
$ cp *.zip ~/Yocto
$ cp rzsbc_yocto.sh ~/Yocto
```

Step 3: Build package

Build the package by executing the following commands:
```
$ cd ~/Yocto
$ ./rzsbc_yocto.sh build
```

**Please note that this build requires internet access and will take several hours.**

Step 4: Collect the output

After building Yocto, the output folder should be `~/Yocto/yocto_rzsbc_board/build/tmp/deploy/images/rzpi`

The output folder outline:
```
rzpi
├── bl2_bp-rzpi.srec
├── bootloader_flash.py                            <---- Bootloader flashing script on Linux
├── bootloader-windows-script                      <---- Bootloader flashing script package folder on Windows 
│   ├── config.ini
│   ├── flash_bootloader.bat                       <---- Bootloader flashing script on Windows
│   ├── images
│   │   ├── bl2_bp-rzpi.srec
│   │   ├── fip-rzpi.srec
│   │   └── Flash_Writer_SCIF_rzpi.mot
│   ├── Readme.txt                                 <---- Bootloader flashing script on Windows guideline
│   └── tools
│       ├── cygterm.cfg
│       ├── flash_bootloader.ttl
│       ├── TERATERM.INI
│       ├── ttermpro.exe
│       ├── ttpcmn.dll
│       ├── ttpfile.dll
│       ├── ttpmacro.exe
│       ├── ttpset.dll
│       └── ttxssh.dll
├── core-image-qt-rzpi-20231130172752.rootfs.manifest
├── core-image-qt-rzpi-20231130172752.rootfs.tar.bz2
├── core-image-qt-rzpi-20231130172752.testdata.json
├── core-image-qt-rzpi.manifest -> core-image-qt-rzpi-20231130172752.rootfs.manifest
├── core-image-qt-rzpi.tar.bz2 -> core-image-qt-rzpi-20231130172752.rootfs.tar.bz2
├── core-image-qt-rzpi.testdata.json -> core-image-qt-rzpi-20231130172752.testdata.json
├── fip-rzpi.srec
├── Flash_Writer_SCIF_rzpi.mot
├── Image -> Image--5.10.184-cip36+gitAUTOINC+a090a5a9e4-r1-rzpi-20231130172752.bin
├── Image--5.10.184-cip36+gitAUTOINC+a090a5a9e4-r1-rzpi-20231130172752.bin
├── overlays
│   ├── rzpi-can.dtbo
│   ├── rzpi-ext-i2c.dtbo
│   └── rzpi-ext-spi.dtbo
├── readme.txt
├── rzpi--5.10.184-cip36+gitAUTOINC+a090a5a9e4-r1-rzpi-20231130172752.dtb
├── rzpi.dtb -> rzpi--5.10.184-cip36+gitAUTOINC+a090a5a9e4-r1-rzpi-20231130172752.dtb
├── sd_flash.sh                                    <---- SD card flashing script
└── uEnv.txt
```

## Programming/Flashing images for RZpi

### Flash Bootloader on Linux

We prepare a suppport script `bootloader_flash.py` for flashing bootloader on Linux. The script can be achieved from Yocto build.

Please run the follow command to know how to use the script:

```
$ ./bootloader_flash.py -h
```


### Flash Bootloader on Windows

Same as Flash Bootloader on Linux, we prepare some suppport scripts for flashing bootloader on Windows.

Please get folder `bootloader-windows-script` from Yocto build output folder. Then refer to `Readme.txt` file to know how to use the scripts.

### Prepare image and rootfs in microSD card

Please prepare the rootfs image in microSD card before booting RZpi system.
We prepare a suppport script `sd_flash.sh` for this purpose. The script can be achieved from Yocto build.

Please run the follow command to know how to use the script:

```
$ ./sd_flash.sh
```

After executing SD card flashing script successfully. In U-boot console, running the below command and make sure the result is the same:

```test
=> ext4ls mmc 0:2
<DIR>       4096 .
<DIR>       4096 ..
<DIR>       4096 bin
<DIR>       4096 boot
<DIR>       4096 dev
<DIR>       4096 etc
<DIR>       4096 home
<DIR>       4096 lib
<DIR>       4096 lib64
<DIR>       4096 media
<DIR>       4096 mnt
<DIR>       4096 proc
<DIR>       4096 run
<DIR>       4096 sbin
<DIR>       4096 sys
<DIR>       4096 tmp
<DIR>       4096 usr
<DIR>       4096 var
```

### U-boot environment

In U-Boot console, execute one more command to bring RZpi system up:

```
=> boot
```

## Confirm supported features on RZpi

### QT demo applications

Some QT demo applications are suppported in this VLP release for RZpi. 
Run the following commands to enter to QT demo applications folder and run a demo app as your needs.

For example, QT smart home demo application is executed.

```
root@rzpi:~# cd /home/root/demo/scripts/
root@rzpi:~/demo/scripts# ./QtSmarthome-demo.sh
```


### 40 IO expansion interface settings

The 40 IO Expansion Interface on RZpi supports for I2C channel 0 and channel 3, SPI channel 0, SCIF channel 0, CAN channel 0 and channel 1 and GPIO pin-function (default).

By default, I2C channel 0 and SCIF channel 0 are enabled. Users can configure to use other channels.

We support an FDT overlays appoach to easily reconfigure for this Expansion Interface.

Please refer to the `readme.txt` file in `/boot` folder to know how to use the FDT overlays on RZpi.

After changing the value of overlays options, we need to run `sync` to ensure that the changes are affected. Then, execute `reboot` to apply the changes.

### On-board Wi-Fi Modules configurations

RZpi has an on-board Wireless modules on it. Currently, we only support for Wi-Fi feature in this release.

To settings for Wi-Fi on RZpi, run the following commands:

```
root@rzpi:~# connmanctl
connmanctl> enable wifi
Enabled wifi
connmanctl> agent on
Agent registered
connmanctl> scan wifi
Scan completed for wifi
connmanctl> services
    xDredme10zW          wifi_0025ca329da3_78447265646d6531307a57_managed_psk
                         wifi_0025ca329da3_hidden_managed_psk
    REL-GLOBAL           wifi_0025ca329da3_52454c2d474c4f42414c_managed_ieee8021x
    R-GUEST              wifi_0025ca329da3_522d4755455354_managed_none
    RVC-WLS              wifi_0025ca329da3_5256432d574c53_managed_ieee8021x
connmanctl> connect wifi_0025ca329da3_78447265646d6531307a57_managed_psk
Agent RequestInput wifi_0025ca329da3_78447265646d6531307a57_managed_psk
  Passphrase = [ Type=psk, Requirement=mandatory ]
Passphrase? nFjey48aT9pk
connmanctl> exit
```

To confirm the Wi-Fi is connected, ping to the outside world:

```
root@rzpi:~# ping www.google.com
PING www.google.com(hkg07s39-in-x04.1e100.net (2404:6800:4005:813::2004)) 56 data bytes
64 bytes from hkg07s39-in-x04.1e100.net (2404:6800:4005:813::2004): icmp_seq=1 ttl=57 time=43.2 ms
64 bytes from hkg07s39-in-x04.1e100.net (2404:6800:4005:813::2004): icmp_seq=2 ttl=57 time=81.1 ms
64 bytes from hkg07s39-in-x04.1e100.net (2404:6800:4005:813::2004): icmp_seq=3 ttl=57 time=124 ms
```

**Please note that before using Wi-Fi feature on RZpi, the ethernet connections need to be down.**

```
root@rzpi:~# ifconfig eth0 down
root@rzpi:~# ifconfig eth1 down
```
