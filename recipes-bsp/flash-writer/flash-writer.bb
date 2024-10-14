require include/rzg2l-optee-config.inc
inherit python3native

LIC_FILES_CHKSUM = "file://LICENSE.md;md5=1fb5dca04b27614d6d04abca6f103d8d"
LICENSE="BSD-3-Clause"
PV = "1.06+git${SRCPV}"

FLASH_WRITER_URL:rzg2l-sbc = "git://github.com/Renesas-SST/flash-writer.git"
BRANCH:rzg2l-sbc = "dunfell/rz-sbc"
SRC_URI:rzg2l-sbc = "${FLASH_WRITER_URL};protocol=https;name=machine;branch=${BRANCH}"
SRCREV_machine:rzg2l-sbc = "${AUTOREV}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

S = "${WORKDIR}/git"
PMIC_BUILD_DIR = "${S}/build_pmic"

do_compile() {
    BOARD="RZG2L_SBC";
    PMIC_BOARD="RZG2L_SMARC_PMIC";

    oe_runmake BOARD=${BOARD}

    if [ "${PMIC_SUPPORT}" = "1" ]; then
        oe_runmake OUTPUT_DIR=${PMIC_BUILD_DIR} clean;
        oe_runmake BOARD=${PMIC_BOARD} OUTPUT_DIR=${PMIC_BUILD_DIR};
    fi

    mv ${S}/AArch64_output/Flash_Writer*${BOARD}*.mot \
        ${S}/AArch64_output/Flash_Writer_SCIF_${MACHINE}.mot
    mv ${S}/build_pmic/Flash_Writer*${PMIC_BOARD}*.mot \
        ${S}/build_pmic/Flash_Writer_SCIF_${MACHINE}_PMIC.mot
}

do_install[noexec] = "1"

do_deploy() {
    install -d ${DEPLOYDIR}
    install -m 0644 ${S}/AArch64_output/Flash_Writer_SCIF_${MACHINE}.mot ${DEPLOYDIR}
}

PARALLEL_MAKE = "-j 1"
addtask deploy after do_compile

COMPATIBLE_MACHINE = "rzg2l-sbc"
