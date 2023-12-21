# How to program IPL from U-Boot console

In case users want to program IPL directly from U-Boot, following the below steps:
- Step 1: probe the QSPI NOR flash on RZG2L SBC board.
```
=> sf probe
```

- Step 2: erase the current IPL 

**Warning: this step will erase all data on QSPI NOR flash. If step 3 and step 4 are not proceed next, you will not be able to boot RZG2L SBC board.**
```
=> sf erase 0 100000
```

- Step 3: load bl2 bootloader into DRAM and then write to QSPI NOR flash.

(This example supposes the bl2 bootloader `bl2_bp-rzpi.bin` is located in partition 1 of SD card at root folder `/`.)

```
=> fatload mmc 0:1 0x48000000 bl2_bp-rzpi.bin
=> sf write 0x48000000 0 $filesize
```

- Step 4: load fip file into DRAM and then write to QSPI NOR flash.

(This example supposes the fip file `fip-rzpi.bin` is located in partition 1 of SD card at root folder `/`.)

```
=> fatload mmc 0:1 0x48000000 fip-rzpi.bin
=> sf write 0x48000000 1d200 $filesize
```

Please note that, we use `bin` files (`bl2_bp-rzpi.bin`, `fip-rzpi.bin`) for programming IPL from U-Boot. 
It is a different format from the `srec` format when using with Flash Writer (`bl2_bp-rzpi.srec`, `fip-rzpi.srec`).

Pre-built IPL binaries for each release will be located at: `/boot/uload-bootloader` on Root filesystem.
