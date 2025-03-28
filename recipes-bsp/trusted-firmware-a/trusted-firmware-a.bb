DESCRIPTION = "Trusted Firmware-A for Renesas RZ"

inherit deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "BSD-3-Clause & MIT & Apache-2.0"
LIC_FILES_CHKSUM = " \
	file://docs/license.rst;md5=b2c740efedc159745b9b31f88ff03dde \
"
require trusted-firmware-a.inc

SRC_URI:append = " file://0001-atf-renesas-build-Suppress-RWX-segment-warning-in-AT.patch"

# Configuration for rzv2h-evk-ver1 board
PLATFORM:rzv2h-evk-ver1 = "v2h"
EXTRA_FLAGS:rzv2h-evk-ver1 = "BOARD=evk_1 ENABLE_STACK_PROTECTOR=default"

FILES:${PN} = "/boot "
# Add the /boot directory to the target's sysroot
SYSROOT_DIRS += "/boot"

FILESEXTRAPATHS:append := "${THISDIR}/files"

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

# Make args with option ${EXTRA_OEMAKE}
EXTRA_OEMAKE="PLAT=${PLATFORM} ${EXTRA_FLAGS} bl2 bl31"

# Install bl2.bin and bl31.bin to boot folder and rename
do_install() {
    install -d ${D}/boot
    install -m 644 ${S}/build/${PLATFORM}/release/bl2.bin ${D}/boot/bl2-${MACHINE}.bin
    install -m 644 ${S}/build/${PLATFORM}/release/bl31.bin ${D}/boot/bl31-${MACHINE}.bin
}

do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}

    # Copy IPL to deploy folder
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2/bl2.elf ${DEPLOYDIR}/bl2-${MACHINE}.elf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl2.bin ${DEPLOYDIR}/bl2-${MACHINE}.bin
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31/bl31.elf ${DEPLOYDIR}/bl31-${MACHINE}.elf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}.bin

    install -d ${DEPLOYDIR}/target/images
    install -m 0644 ${D}/boot/bl2-${MACHINE}.bin ${DEPLOYDIR}/target/images/bl2-${MACHINE}.bin
}

addtask deploy after do_install

COMPATIBLE_MACHINE = "rzv2h-evk-ver1"