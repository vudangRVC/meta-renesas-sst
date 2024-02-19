FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

require include/rzg2l-optee-config.inc
inherit deploy

SRC_URI += " \
	file://0001-hw_get_random_bytes-SUPPORT.patch \
"

PLATFORM = "rz"
PLATFORM_FLAVOR_smarc-rzg2l = "g2l_smarc_2"
PLATFORM_FLAVOR_rzg2l-dev = "g2l_dev15_4"
PLATFORM_FLAVOR_smarc-rzg2lc = "g2lc_smarc_1"
PLATFORM_FLAVOR_rzg2lc-dev = "g2l_dev13_1"
PLATFORM_FLAVOR_smarc-rzg2ul = "g2ul_smarc"
PLATFORM_FLAVOR_smarc-rzv2l = "g2l_smarc_4"
PLATFORM_FLAVOR_rzv2l-dev = "g2l_dev15_4"

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
		cp -aR	$f	${D}/usr/include/optee/export-user_ta/
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
