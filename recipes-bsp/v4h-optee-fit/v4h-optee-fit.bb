SUMMARY = "Sparrow Hawk FIT image with TF-A and OP-TEE"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy

DEPENDS = "u-boot-mkimage-native virtual/kernel trusted-firmware-a optee-os"
COMPATIBLE_MACHINE = "rz-cmn"

SRC_URI = "file://fit-image-sparrow-hawk.its"
S = "${UNPACKDIR}"

do_compile[noexec] = "1"
do_deploy[depends] += "virtual/kernel:do_deploy trusted-firmware-a:do_deploy optee-os:do_deploy"

do_deploy() {
    install -d ${WORKDIR}/fit ${DEPLOYDIR}/target/images/linux
    set -- ${DEPLOY_DIR_IMAGE}/target/images/linux/Image--*.bin
    test "$#" -eq 1
    install -m 0644 "$1" ${WORKDIR}/fit/Image
    install -m 0644 ${DEPLOY_DIR_IMAGE}/r8a779g3-sparrow-hawk.dtb ${WORKDIR}/fit/r8a779g3-sparrow-hawk.dtb
    install -m 0644 ${DEPLOY_DIR_IMAGE}/target/images/atf/bl31-sparrowhawk.bin ${WORKDIR}/fit/bl31-sparrowhawk.bin
    install -m 0644 ${DEPLOY_DIR_IMAGE}/target/images/atf/tee-rz-cmn-v4h.bin ${WORKDIR}/fit/tee-rz-cmn-v4h.bin
    install -m 0644 ${S}/fit-image-sparrow-hawk.its ${WORKDIR}/fit/
    cd ${WORKDIR}/fit
    mkimage -f fit-image-sparrow-hawk.its fitImage-sparrow-hawk
    install -m 0644 ${WORKDIR}/fit/fitImage-sparrow-hawk ${DEPLOYDIR}/target/images/linux/
}

addtask deploy after do_compile before do_build
