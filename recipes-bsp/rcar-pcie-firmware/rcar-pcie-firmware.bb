DESCRIPTION = "R-Car Gen4 PCIe PHY firmware for V4H (Sparrow Hawk)"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

COMPATIBLE_MACHINE = "rz-cmn"
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rcar_gen4_pcie.bin;md5sum=293bdf19d8e16d3c4d8179e438db921b"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware
    install -m 0644 ${UNPACKDIR}/rcar_gen4_pcie.bin ${D}${nonarch_base_libdir}/firmware/
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/rcar_gen4_pcie.bin"
