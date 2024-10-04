# For RZ/G2L Series
PLATFORM_smarc-rzg2l = "g2l"
EXTRA_FLAGS_smarc-rzg2l = "BOARD=smarc_2"
PMIC_EXTRA_FLAGS_smarc-rzg2l = "BOARD=smarc_pmic_2"

PLATFORM_rzg2l-dev = "g2l"
EXTRA_FLAGS_rzg2l-dev = "BOARD=dev15_4"

PLATFORM_smarc-rzg2lc = "g2l"
EXTRA_FLAGS_smarc-rzg2lc = "BOARD=smarc_1"

PLATFORM_rzg2lc-dev = "g2l"
EXTRA_FLAGS_rzg2lc-dev = "BOARD=dev13_1"

PLATFORM_smarc-rzg2ul = "g2ul"
EXTRA_FLAGS_smarc-rzg2ul = "BOARD=g2ul_smarc SOC_TYPE=1 SPI_FLASH=AT25QL128A"

PLATFORM_rzg2ul-dev = "g2ul"
EXTRA_FLAGS_rzg2ul-dev = "BOARD=g2ul_type1_ddr4_dev SOC_TYPE=1"

PLATFORM_smarc-rzv2l = "v2l"
EXTRA_FLAGS_smarc-rzv2l = "BOARD=smarc_4"
PMIC_EXTRA_FLAGS_smarc-rzv2l = "BOARD=smarc_pmic_2"

PLATFORM_rzv2l-dev = "v2l"
EXTRA_FLAGS_rzv2l-dev = "BOARD=dev15_4"

PMIC_BUILD_DIR = "${S}/build_pmic"

FILES_${PN} = "/boot "
SYSROOT_DIRS += "/boot"

FILESEXTRAPATHS_append := "${THISDIR}/files"
SRC_URI += " \
	file://0001-plat-renesas-rz-Disable-unused-CRYPTO_SUPPORT.patch \
"

ECC_FLAGS = " DDR_ECC_ENABLE=1 "
ECC_FLAGS += "${@oe.utils.conditional("ECC_MODE", "ERR_DETECT", "DDR_ECC_DETECT=1", "",d)}"
ECC_FLAGS += "${@oe.utils.conditional("ECC_MODE", "ERR_DETECT_CORRECT", "DDR_ECC_DETECT_CORRECT=1", "",d)}"
EXTRA_FLAGS_append = "${@oe.utils.conditional("USE_ECC", "1", " ${ECC_FLAGS} ", "",d)}"
PMIC_EXTRA_FLAGS_append = "${@oe.utils.conditional("USE_ECC", "1", " ${ECC_FLAGS} ", "",d)}"

do_compile() {
	oe_runmake PLAT=${PLATFORM} ${EXTRA_FLAGS} bl2 bl31

	if [ "${PMIC_SUPPORT}" = "1" ]; then
		oe_runmake PLAT=${PLATFORM} ${PMIC_EXTRA_FLAGS} BUILD_PLAT=${PMIC_BUILD_DIR} bl2 bl31
	fi
}

do_install() {
	install -d ${D}/boot
	install -m 644 ${S}/build/${PLATFORM}/release/bl2.bin ${D}/boot/bl2-${MACHINE}.bin
	install -m 644 ${S}/build/${PLATFORM}/release/bl31.bin ${D}/boot/bl31-${MACHINE}.bin

	if [ "${PMIC_SUPPORT}" = "1" ]; then
		install -m 0644 ${PMIC_BUILD_DIR}/bl2.bin ${D}/boot/bl2-${MACHINE}_pmic.bin
		install -m 0644 ${PMIC_BUILD_DIR}/bl31.bin ${D}/boot/bl31-${MACHINE}_pmic.bin
	fi
}

do_deploy_append() {
	install -d ${DEPLOYDIR}/target/images
	install -m 0644 ${D}/boot/bl2-${MACHINE}.bin ${DEPLOYDIR}/target/images/bl2-${MACHINE}.bin

	if [ "${PMIC_SUPPORT}" = "1" ]; then
		install -m 0644 ${PMIC_BUILD_DIR}/bl2.bin ${DEPLOYDIR}/bl2-${MACHINE}_pmic.bin
		install -m 0644 ${PMIC_BUILD_DIR}/bl31.bin ${DEPLOYDIR}/bl31-${MACHINE}_pmic.bin
	fi
}

# Support for RZ SBC board
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
        git://github.com/vudangRVC/rz-atf-sst.git;branch=${BRANCH};protocol=https \
"
BRANCH = "dunfell/rz-sbc-rebase-vlp-3.0.6"
SRCREV = "${AUTOREV}"
PV = "v2.9+git"

COMPATIBLE_MACHINE_rzg2l = "(smarc-rzg2l|rzg2l-dev|smarc-rzg2lc|rzg2lc-dev|smarc-rzg2ul|rzg2ul-dev|smarc-rzv2l|rzv2l-dev|rzpi)"

SRC_URI_remove = " \
	file://0001-plat-renesas-rz-Disable-unused-CRYPTO_SUPPORT.patch \
"

PLATFORM_rzpi = "g2l"
EXTRA_FLAGS_rzpi = "BOARD=sbc_1"
PMIC_EXTRA_FLAGS_rzpi = "BOARD=smarc_pmic_2"
FLASH_ADDRESS_BL2_BP_rzpi = "00000"
FLASH_ADDRESS_FIP_rzpi = "1D200"

