DESCRIPTION = "Trusted Firmware-A for Renesas RZ, including BL31 for the Sparrow-Hawk (V4H) companion SoC built alongside rz-cmn"

require include/rz-optee-config.inc
inherit deploy

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "MIT & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
    file://${WORKDIR}/git/sparrowhawk/license.rst;md5=1dd070c98a281d18d9eefd938729b031 \
"
# Set S variable to folder that includes Makefile
S = "${WORKDIR}/git/cmn"
DEPENDS:append = " dtc-native xxd-native"

# Trusted Firmware-A source code repositories
SRC_URI:rz-cmn = " \
    git://github.com/Renesas-SST/rz-atf.git;name=machine;branch=${BRANCH};protocol=https;destsuffix=git/cmn \
    git://github.com/renesas-sst/rz-atf.git;name=sparrowhawk;branch=${SPARROWHAWK_BRANCH};protocol=https;destsuffix=git/sparrowhawk \
"
BRANCH:rz-cmn = "styhead/rz-cmn"
SPARROWHAWK_BRANCH = "styhead/rz-cmn-3.4-sparrowhawk"
SRCREV_machine:rz-cmn = "${AUTOREV}"
SRCREV_sparrowhawk = "7325a329a12228027d9a9d642e36751facd7aee2"
SRCREV_FORMAT = "machine_sparrowhawk"
PV = "v2.14+git"

SPARROWHAWK_S = "${WORKDIR}/git/sparrowhawk"

# Configuration for rz-cmn board
PLATFORM:rz-cmn = "cmn"
EXTRA_FLAGS:rz-cmn = "BOARD=rz_cmn"
BL2_METHODS:rz-cmn = "esd xspi emmc"

# Configuration for the Sparrow-Hawk (V4H) companion SoC
SPARROWHAWK_PLATFORM = "rcar_gen4"
SPARROWHAWK_OPT = "LSI=V4H CTX_INCLUDE_AARCH32_REGS=0 MBEDTLS_COMMON_MK=1 PTP_NONSECURE_ACCESS=1 LOG_LEVEL=20 DEBUG=0 ENABLE_ASSERTIONS=0 E=0"

FILES:${PN} = "/boot "
# Add the /boot directory to the target's sysroot
SYSROOT_DIRS += "/boot"

SEC_FLAGS = " \
    ${@oe.utils.conditional("ENABLE_SPD_OPTEE", "1", " SPD=opteed", "",d)} \
"

EXTRA_FLAGS:append = "${SEC_FLAGS}"

FILESEXTRAPATHS:append := "${THISDIR}/files"

ECC_FLAGS = " DDR_ECC_ENABLE=1 "
ECC_FLAGS += "${@oe.utils.conditional("ECC_MODE", "ERR_DETECT", "DDR_ECC_DETECT=1", "",d)}"
ECC_FLAGS += "${@oe.utils.conditional("ECC_MODE", "ERR_DETECT_CORRECT", "DDR_ECC_DETECT_CORRECT=1", "",d)}"
EXTRA_FLAGS:append = "${@oe.utils.conditional("USE_ECC", "1", " ${ECC_FLAGS} ", "",d)}"

# requires CROSS_COMPILE set by hand as there is no configure script
export CROSS_COMPILE="${TARGET_PREFIX}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

# Make args with option ${EXTRA_OEMAKE}
# Builds:
#   - all BL2 variants (xSPI, eMMC, eSD) via the `bl2-all` target
#   - BL31
#   - FCONF device trees (dtbs)
EXTRA_OEMAKE = "PLAT=${PLATFORM} ${EXTRA_FLAGS} LD=${TARGET_PREFIX}ld.bfd bl2-all bl31 dtbs"

do_compile() {
    # Build BL2/BL31 for the primary rz-cmn platform
    oe_runmake

    # Build BL31 for the Sparrow-Hawk (V4H) companion SoC
    cd ${SPARROWHAWK_S}
    oe_runmake distclean
    oe_runmake clean_srecord PLAT=${SPARROWHAWK_PLATFORM} SPD=none MBEDTLS_COMMON_MK=1 ${SPARROWHAWK_OPT}
    oe_runmake bl31 rcar_srecord PLAT=${SPARROWHAWK_PLATFORM} SPD=none MBEDTLS_COMMON_MK=1 ${SPARROWHAWK_OPT}
}

# Install bl2.bin and bl31.bin to boot folder and rename
do_install() {
    install -d ${D}/boot/fdts

    for method in ${BL2_METHODS}; do
        install -m 644 ${S}/build/${PLATFORM}/release/bl2-${method}.bin ${D}/boot/bl2-${method}-${MACHINE}.bin
    done
    install -m 644 ${S}/build/${PLATFORM}/release/bl31.bin ${D}/boot/bl31-${MACHINE}.bin
    install -m 644 ${S}/build/${PLATFORM}/release/fdts/*.dtb ${D}/boot/fdts
}

# Deploy bin file to deploy dir
do_deploy() {
    # Create deploy folder
    install -d ${DEPLOYDIR}/target/images/atf/fdts

    # Copy bl2, bl31 and fdts to deploy folder
    for method in ${BL2_METHODS}; do
        install -m 0644 ${D}/boot/bl2-${method}-${MACHINE}.bin ${DEPLOYDIR}/target/images/atf/bl2-${method}-${MACHINE}.bin
    done
    install -m 0644 ${D}/boot/bl31-${MACHINE}.bin ${DEPLOYDIR}/target/images/atf/bl31-${MACHINE}.bin
    install -m 0644 ${D}/boot/fdts/*.dtb ${DEPLOYDIR}/target/images/atf/fdts

    # Copy Sparrow-Hawk (V4H) BL31 images to deploy folder
    install -m 0644 ${SPARROWHAWK_S}/build/${SPARROWHAWK_PLATFORM}/release/bl31/bl31.elf ${DEPLOYDIR}/target/images/atf/bl31-sparrowhawk.elf
    install -m 0644 ${SPARROWHAWK_S}/build/${SPARROWHAWK_PLATFORM}/release/bl31.bin       ${DEPLOYDIR}/target/images/atf/bl31-sparrowhawk.bin
    install -m 0644 ${SPARROWHAWK_S}/build/${SPARROWHAWK_PLATFORM}/release/bl31.srec      ${DEPLOYDIR}/target/images/atf/bl31-sparrowhawk.srec
}

addtask deploy after do_install

COMPATIBLE_MACHINE = "rz-cmn"
