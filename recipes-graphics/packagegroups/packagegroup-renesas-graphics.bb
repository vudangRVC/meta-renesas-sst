SUMMARY = "Renesas package group for Wayland/Weston and OpenGL ES"
LICENSE = "CLOSED & MIT"

COMPATIBLE_MACHINE = "rz-cmn"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PR = "r0"

PACKAGES = " \
    packagegroup-renesas-graphics \
    packagegroup-graphics-renesas-gles \
    packagegroup-graphics-renesas-wayland \
    packagegroup-graphics-oss-wayland \
    packagegroup-graphics-oss-opencl \
"

RDEPENDS:packagegroup-renesas-graphics = " \
    packagegroup-graphics-renesas-gles \
    packagegroup-graphics-renesas-wayland \
    packagegroup-graphics-oss-wayland \
    packagegroup-graphics-oss-opencl \
"

# The userspace package pulls in gles-user-module, which in turn depends on the
# out-of-tree kernel-module-gles package containing pvrsrvkm.ko.
RDEPENDS:packagegroup-graphics-renesas-gles = " \
    gles-user-module \
    pvr-libgbm \
    pvr-wayland-kms \
    pvr-wayland-wsegl \
"

DEPENDS:packagegroup-graphics-renesas-wayland = "virtual/egl virtual/libgles2 virtual/libgbm"

RDEPENDS:packagegroup-graphics-renesas-wayland = " \
    libgbm \
"

RDEPENDS:packagegroup-graphics-oss-wayland = " \
    wayland \
    weston \
    weston-examples \
    alsa-utils \
    alsa-tools \
    libdrm-tests \
"

RDEPENDS:packagegroup-graphics-oss-opencl = " \
    clinfo \
"
