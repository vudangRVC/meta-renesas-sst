require include/core-image-renesas-base.inc
require include/core-image-bsp.inc
require include/core-image-renesas-mmp.inc
require include/core-image-renesas-sbc.inc

# PowerVR (Renesas GSX) userspace GPU stack for rz-cmn (Sparrow Hawk / V4H):
# the proprietary libEGL/libGLESv2 + wayland-kms/wsegl + libgbm libraries.
# packagegroup-renesas-graphics is COMPATIBLE_MACHINE = "rz-cmn", so this is
# a no-op on machines that don't set it.
IMAGE_INSTALL:append = " packagegroup-renesas-graphics"
