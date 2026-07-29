# U-Boot environment for RZ SBC board

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI = " \
    file://uEnv.txt \
"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

FILES:${PN} = "/boot"

do_install () {
    install -d ${D}/boot
    install -m 0644 ${S}/uEnv.txt ${D}/boot/uEnv.txt
}

inherit deploy
addtask deploy after do_install

do_deploy () {
    install -d ${DEPLOYDIR}/target/env
    install -m 0644 ${D}/boot/uEnv.txt ${DEPLOYDIR}/target/env
}

COMPATIBLE_MACHINE:rz-cmn = "(rz-cmn)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
