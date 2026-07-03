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

# GFX package — userspace only. pvrsrvkm (kernel-module-gles) is out of
# scope: it's provided by the kernel (linux-yocto), not this layer.
RDEPENDS:packagegroup-graphics-renesas-gles = " \
    gles-user-module \
"

DEPENDS:packagegroup-graphics-renesas-wayland = "libegl libgles2"

RDEPENDS:packagegroup-graphics-renesas-wayland = " \
    libgbm \
    libgbm-dev \
    wayland-kms \
    wayland-wsegl \
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
