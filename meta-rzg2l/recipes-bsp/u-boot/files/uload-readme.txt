# Program IPL from U-Boot console

In case users want to program IPL directly from U-Boot, we prepare some scripts to automate the flashing process.

The scripts support on both Linux and Windows OS.

## Outline of the folder
```
uload-bootloader
├── bl2_bp-rzpi.bin
├── fip-rzpi.bin
├── uload_bootloader_flash.py                    <---- Bootloader flashing script on Linux
├── uload-bootloader-windows-script              <---- Bootloader flashing script package on Linux
│   ├── config.ini
│   ├── Readme.txt                               <---- Bootloader flashing guideline on Windows
│   ├── tools
│   │   ├── cygterm.cfg
│   │   ├── TERATERM.INI
│   │   ├── ttermpro.exe
│   │   ├── ttpcmn.dll
│   │   ├── ttpfile.dll
│   │   ├── ttpmacro.exe
│   │   ├── ttpset.dll
│   │   ├── ttxssh.dll
│   │   └── uload-flash_bootloader.ttl
│   └── uload-flash_bootloader.bat               <---- Bootloader flashing script on Windows
└── uload-readme.txt                             <---- This document
```

## On Linux

Please run the follow command to know how to use the script:

```
./uload_bootloader_flash.py -h
```

## On Windows

Please refer to `Readme.txt` file inside `uload-bootloader-windows-script` to know how to use the scripts.