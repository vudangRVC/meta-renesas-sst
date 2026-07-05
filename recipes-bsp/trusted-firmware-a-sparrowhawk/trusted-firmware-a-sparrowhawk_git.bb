DESCRIPTION = "ARM Trusted Firmware BL31 for Sparrow-Hawk (V4H), built alongside rz-cmn"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.rst;md5=1dd070c98a281d18d9eefd938729b031"

COMPATIBLE_MACHINE = "rz-cmn"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

BRANCH = "styhead/rz-cmn-support-v4h-sparrow"
SRC_URI = " \
    git://github.com/vudangRVC/rz-atf-sst.git;branch=${BRANCH};protocol=https \
    file://0001-fix-rcar_gen4-V4H-CPU-topology-1-cluster-4-cores.patch \
"
SRCREV = "7325a329a12228027d9a9d642e36751facd7aee2"
PV = "v2.14.0+sparrowhawk+git${SRCPV}"

S = "${WORKDIR}/git"

PLATFORM = "rcar_gen4"
ATFW_OPT = "LSI=V4H CTX_INCLUDE_AARCH32_REGS=0 MBEDTLS_COMMON_MK=1 PTP_NONSECURE_ACCESS=1 LOG_LEVEL=20 DEBUG=0 ENABLE_ASSERTIONS=0 E=0"

export CROSS_COMPILE = "${TARGET_PREFIX}"

CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_install[noexec] = "1"

do_compile() {
    oe_runmake distclean
    oe_runmake clean_srecord PLAT=${PLATFORM} SPD=none MBEDTLS_COMMON_MK=1 ${ATFW_OPT}
    oe_runmake bl31 rcar_srecord PLAT=${PLATFORM} SPD=none MBEDTLS_COMMON_MK=1 ${ATFW_OPT}
}

do_deploy() {
    install -d ${DEPLOYDIR}/target/images/atf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31/bl31.elf  ${DEPLOYDIR}/target/images/atf/bl31-sparrowhawk.elf
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31.bin       ${DEPLOYDIR}/target/images/atf/bl31-sparrowhawk.bin
    install -m 0644 ${S}/build/${PLATFORM}/release/bl31.srec      ${DEPLOYDIR}/target/images/atf/bl31-sparrowhawk.srec
}

addtask deploy after do_compile
