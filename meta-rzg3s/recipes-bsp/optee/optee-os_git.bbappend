FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
	file://0001-g3s-optee-os-build.patch \
"

PLATFORM = "rz"
PLATFORM_FLAVOR_rzg3s-dev = "g3s_dev14_1"
PLATFORM_FLAVOR_smarc-rzg3s = "g3s_smarc_2"

# Let the Makefile handle setting up the flags as it is a standalone application
LD[unexport] = "1"
LDFLAGS[unexport] = "1"
export CCcore="${CC}"
export LDcore="${LD}"
libdir[unexport] = "1"

S = "${WORKDIR}/git"

CFLAGS_prepend = "--sysroot=${STAGING_DIR_HOST}"

RZ_SCE ?= "n"
EXTRA_OEMAKE = " \
        PLATFORM=${PLATFORM} PLATFORM_FLAVOR=${PLATFORM_FLAVOR} \
        CFG_ARM64_core=y CFG_REE_FS=y CFG_RPMB_FS=n CFG_CRYPTO_WITH_CE=n \
        CFG_RZ_SCE=${RZ_SCE} CFG_RZ_SCE_LIB_DIR=${SYMLINK_NATIVE_SEC_LIB_DIR} \
        CROSS_COMPILE64=${TARGET_PREFIX} \
"

do_install() {
        #install TA devkit
        install -d ${D}/usr/include/optee/export-user_ta/

        for f in  ${B}/out/arm-plat-${PLATFORM}/export-ta_arm64/* ; do
                cp -aR  $f      ${D}/usr/include/optee/export-user_ta/
        done

        # install firmware images
        install -d ${D}/boot

        # Copy TEE OS to install folder
        install -m 0644 ${S}/out/arm-plat-${PLATFORM}/core/tee.elf     ${D}/boot/tee-${MACHINE}.elf
        install -m 0644 ${S}/out/arm-plat-${PLATFORM}/core/tee-raw.bin ${D}/boot/tee-${MACHINE}.bin
}

FILES_${PN} = "/boot "
SYSROOT_DIRS += "/boot"

FILES_${PN}-dev = "/usr/include/optee"

INSANE_SKIP_${PN}-dev = "staticdev"
