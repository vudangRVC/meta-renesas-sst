# Install loadable modules that are off-loaded from the system boot
IMAGE_INSTALL_append = " ${@oe.utils.conditional("OPTIMIZE_KERN", "1", " \
       kernel-module-ov5640 \
       kernel-module-rzsbc-ft5406 \
       kernel-module-panel-toshiba-tc358762 \
       kernel-module-rzsbc-mcu \
       kernel-module-hid-multitouch \
       kernel-module-panel-ilitek-ili9881c \
       kernel-module-uinput \
       kernel-module-evdev \
       kernel-module-uvcvideo \
       kernel-module-usbtouchscreen \
       kernel-module-mousedev \
       kernel-module-u-ether \
       kernel-module-u-serial \
       kernel-module-rcar-canfd \
       ", "", d)}"
