# Flash writer recipe support

LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1fb5dca04b27614d6d04abca6f103d8d"
LICENSE="BSD-3-Clause"

FLASH_WRITER_URL = "git://github.com/Renesas-SST/flash-writer.git"
BRANCH = "dunfell/rz-sbc"
SRC_URI = "${FLASH_WRITER_URL};protocol=https;branch=${BRANCH}"
SRCREV = "${AUTOREV}"

inherit deploy
#require include/provisioning.inc

S = "${WORKDIR}/git"
PMIC_BUILD_DIR = "${S}/build_pmic"

do_compile() {
	cd ${S}

	oe_runmake BOARD=${BOARD}

        if [ "${PMIC_SUPPORT}" = "1" ]; then
		oe_runmake OUTPUT_DIR=${PMIC_BUILD_DIR} clean;
		oe_runmake BOARD=${PMIC_BOARD} OUTPUT_DIR=${PMIC_BUILD_DIR};
	fi
}

do_install[noexec] = "1"

do_deploy() {
    install -d ${DEPLOYDIR}/target/images
    install -m 0644 ${S}/AArch64_output/Flash_Writer_SCIF_${MACHINE}.mot ${DEPLOYDIR}/target/images
}

PARALLEL_MAKE = "-j 1"
addtask deploy after do_compile

PV = "1.06+git${SRCPV}"
PACKAGE_ARCH = "${MACHINE_ARCH}"
