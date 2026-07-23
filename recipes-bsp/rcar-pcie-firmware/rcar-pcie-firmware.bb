DESCRIPTION = "R-Car Gen4 PCIe PHY firmware for V4H (Sparrow Hawk)"
LICENSE = "Proprietary"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://${UNPACKDIR}/LICENCE.r8a779g_pcie_phy;md5=0b20e76a9a004b83c4a1c87e2153bbad"

COMPATIBLE_MACHINE = "rz-cmn"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit deploy

# Keep the binary and its redistribution terms pinned to the same
# linux-firmware snapshot. The license must travel with the runtime package.
PCIE_FIRMWARE = "https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rcar_gen4_pcie.bin?h=20260519;name=pcie-firmware;downloadfilename=rcar_gen4_pcie.bin"
PCIE_FIRMWARE_LICENSE = "https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/LICENCE.r8a779g_pcie_phy?h=20260519;name=pcie-firmware-license;downloadfilename=LICENCE.r8a779g_pcie_phy"

SRC_URI = "${PCIE_FIRMWARE} ${PCIE_FIRMWARE_LICENSE}"
SRC_URI[pcie-firmware.md5sum] = "293bdf19d8e16d3c4d8179e438db921b"
SRC_URI[pcie-firmware-license.md5sum] = "0b20e76a9a004b83c4a1c87e2153bbad"

S = "${UNPACKDIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware
    install -m 0644 ${UNPACKDIR}/rcar_gen4_pcie.bin ${D}${nonarch_base_libdir}/firmware/

    install -d ${D}${datadir}/licenses/${PN}
    install -m 0644 ${UNPACKDIR}/LICENCE.r8a779g_pcie_phy ${D}${datadir}/licenses/${PN}/
}

do_deploy() {
    install -d ${DEPLOYDIR}/target/images
    install -m 0644 ${UNPACKDIR}/rcar_gen4_pcie.bin ${DEPLOYDIR}/target/images/rcar_gen4_pcie.bin
}

addtask deploy after do_install

FILES:${PN} = " \
    ${nonarch_base_libdir}/firmware/rcar_gen4_pcie.bin \
    ${datadir}/licenses/${PN}/LICENCE.r8a779g_pcie_phy \
"
